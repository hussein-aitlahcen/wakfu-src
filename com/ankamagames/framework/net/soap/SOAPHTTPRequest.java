package com.ankamagames.framework.net.soap;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.fileFormat.xml.*;
import java.net.*;
import java.util.zip.*;
import com.ankamagames.framework.fileFormat.document.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class SOAPHTTPRequest
{
    protected static final Logger m_logger;
    protected static final boolean DEBUG_REQUEST = false;
    protected static final String CONTENT_TYPE = "Content-Type";
    protected static final String CONTENT_TYPE_VALUE = "application/soap+xml; charset=UTF-8";
    protected static final String ACCEPT_ENCODING = "Accept-Encoding";
    protected static final String GZIP = "gzip";
    protected static final String CONTENT_LENGTH = "Content-Length";
    protected static final String CONTENT_ENCODING = "Content-Encoding";
    protected static final String SET_COOKIE = "Set-Cookie";
    protected static final String COOKIE = "Cookie";
    public static final String CACHE_CONTROL = "Cache-Control";
    public static final String PRAGMA = "Pragma";
    public static final String NO_CACHE = "no-cache";
    protected static final String SID = "SID";
    protected static final Pattern COOKIE_PATTERN;
    private final SOAPEnvelope m_envelope;
    private final URL m_url;
    private final SOAPRequestAnswerListener m_listener;
    private final Map<String, List<String>> m_headerValues;
    
    public SOAPHTTPRequest(@NotNull final SOAPEnvelope envelope, @NotNull final URL url, @NotNull final SOAPRequestAnswerListener listener, final Map<String, List<String>> headerValues) {
        super();
        this.m_headerValues = new HashMap<String, List<String>>();
        this.m_envelope = envelope;
        this.m_url = url;
        this.m_listener = listener;
        if (headerValues != null) {
            this.m_headerValues.putAll(headerValues);
        }
    }
    
    public void sendRequest() throws IOException {
        final XMLDocumentContainer container = new XMLDocumentContainer();
        container.setRootNode(this.m_envelope.toXMLDocumentNode());
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
        accessor.create(os);
        accessor.write(container);
        final byte[] bytes = os.toByteArray();
        final HttpURLConnection connection = (HttpURLConnection)this.m_url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8");
        connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Pragma", "no-cache");
        for (final Map.Entry<String, List<String>> entry : this.m_headerValues.entrySet()) {
            final String key = entry.getKey();
            final List<String> list = entry.getValue();
            for (int i = 0, size = list.size(); i < size; ++i) {
                connection.addRequestProperty(key, list.get(i));
            }
        }
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setDoOutput(true);
        connection.connect();
        connection.getOutputStream().write(bytes);
        connection.getOutputStream().flush();
        final ListeningThread listening = new ListeningThread();
        listening.setConnection(connection, this.m_listener);
        listening.start();
    }
    
    @Override
    public String toString() {
        return "SOAPHTTPRequest{m_envelope=" + this.m_envelope + ", m_url=" + this.m_url + ", m_listener=" + this.m_listener + ", m_headerValues=" + this.m_headerValues + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)SOAPHTTPRequest.class);
        COOKIE_PATTERN = Pattern.compile("([^=;]+)=([^=;]+)[;$]");
    }
    
    private static class ListeningThread extends Thread
    {
        private HttpURLConnection m_connection;
        private SOAPRequestAnswerListener m_listener;
        
        public void setConnection(final HttpURLConnection connection, final SOAPRequestAnswerListener listener) {
            this.m_connection = connection;
            this.m_listener = listener;
            this.setName("SOAP-AnswerListener");
        }
        
        @Override
        public void run() {
            SOAPEnvelope soapEnvelope = null;
            String errorMsg = null;
            try {
                final Map<String, List<String>> headerFields = this.extractHeaders();
                final String encoding = this.m_connection.getHeaderField("Content-Encoding");
                final InputStream is = "gzip".equals(encoding) ? new GZIPInputStream(this.m_connection.getInputStream()) : this.m_connection.getInputStream();
                final XMLDocumentAccessor accessor = new XMLDocumentAccessor();
                accessor.open(is);
                final XMLDocumentContainer container = new XMLDocumentContainer();
                accessor.read(container, new DocumentEntryParser[0]);
                this.m_connection.disconnect();
                soapEnvelope = SOAPEnvelope.parseSOAPDocument(container);
                soapEnvelope.putAllHeaderParams(headerFields);
            }
            catch (Exception e) {
                errorMsg = e.getMessage();
            }
            if (soapEnvelope != null) {
                this.m_listener.onAnswer(soapEnvelope);
            }
            else {
                this.m_listener.onError("Probl\u00e8me \u00e0 la r\u00e9ception du message SOAP : " + errorMsg);
            }
        }
        
        private Map<String, List<String>> extractHeaders() {
            final Map<String, List<String>> savedHeaders = new HashMap<String, List<String>>();
            final Map<String, List<String>> headerFields = this.m_connection.getHeaderFields();
            final List<String> cookieParams = headerFields.get("Set-Cookie");
            if (cookieParams == null) {
                return savedHeaders;
            }
            String sid = null;
        Label_0113:
            for (int i = 0, size = cookieParams.size(); i < size; ++i) {
                final Matcher matcher = SOAPHTTPRequest.COOKIE_PATTERN.matcher(cookieParams.get(i));
                while (matcher.find()) {
                    if ("SID".equals(matcher.group(1))) {
                        sid = matcher.group(2);
                        break Label_0113;
                    }
                }
            }
            final String cookie = "SID=" + sid;
            final List<String> cookieList = new ArrayList<String>();
            cookieList.add(cookie);
            savedHeaders.put("Cookie", cookieList);
            return savedHeaders;
        }
        
        @Override
        public String toString() {
            return "ListeningThread{m_connection=" + this.m_connection + ", m_listener=" + this.m_listener + '}';
        }
    }
}
