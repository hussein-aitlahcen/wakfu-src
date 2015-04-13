package com.ankamagames.framework.net.soap;

import java.util.*;
import com.ankamagames.framework.fileFormat.document.*;
import com.ankamagames.framework.fileFormat.xml.*;

public class SOAPEnvelope
{
    public static final String TAG = "SOAP-ENV:Envelope";
    public static final String XMLNS_SOAP_TAG = "xmlns:SOAP-ENV";
    public static final String XMLNS_SOAP_VALUE = "http://schemas.xmlsoap.org/soap/envelope/";
    public static final String XMLNS_XSD_TAG = "xmlns:xsd";
    public static final String XMLNS_XSD_VALUE = "http://www.w3.org/2001/XMLSchema";
    public static final String XMLNS_XSI_TAG = "xmlns:xsi";
    public static final String XMLNS_XSI_VALUE = "http://www.w3.org/2001/XMLSchema-instance";
    public static final String XMLNS_SOAP_ENC_TAG = "xmlns:SOAP-ENC";
    public static final String XMLNS_SOAP_ENC_VALUE = "http://schemas.xmlsoap.org/soap/encoding/";
    public static final String SOAP_ENCODING_TAG = "SOAP-ENV:encodingStyle";
    public static final String SOAP_ENCODING_VALUE = "http://schemas.xmlsoap.org/soap/encoding/";
    public static final String XMLNS_NS1_TAG = "xmlns:ns1";
    private final String m_serviceUri;
    private SOAPBody m_body;
    private final Map<String, List<String>> m_headerFields;
    
    public SOAPEnvelope(final String serviceUri) {
        super();
        this.m_headerFields = new HashMap<String, List<String>>();
        this.m_serviceUri = serviceUri;
    }
    
    public void setBody(final SOAPBody body) {
        this.m_body = body;
    }
    
    public SOAPBody getBody() {
        return this.m_body;
    }
    
    public Map<String, List<String>> getHeaderFields() {
        return this.m_headerFields;
    }
    
    public void putAllHeaderParams(final Map<String, List<String>> headerFields) {
        this.m_headerFields.putAll(headerFields);
    }
    
    public XMLDocumentNode toXMLDocumentNode() {
        final XMLDocumentNode node = new XMLDocumentNode("SOAP-ENV:Envelope", "");
        node.addParameter(new XMLNodeAttribute("xmlns:SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/"));
        node.addParameter(new XMLNodeAttribute("xmlns:ns1", this.m_serviceUri));
        node.addParameter(new XMLNodeAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema"));
        node.addParameter(new XMLNodeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance"));
        node.addParameter(new XMLNodeAttribute("xmlns:SOAP-ENC", "http://schemas.xmlsoap.org/soap/encoding/"));
        node.addParameter(new XMLNodeAttribute("SOAP-ENV:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/"));
        if (this.m_body != null) {
            node.addChild(this.m_body.toXMLDocumentNode());
        }
        return node;
    }
    
    public static SOAPEnvelope parseSOAPDocument(final XMLDocumentContainer container) throws IllegalArgumentException {
        final XMLDocumentNode node = container.getRootNode();
        final String name = node.getName();
        if (!name.equals("SOAP-ENV:Envelope")) {
            throw new IllegalArgumentException("Le nom de l'\u00e9l\u00e9ment ne respecte pas la norme : " + name);
        }
        final DocumentEntry uriParam = node.getParameterByName("xmlns:ns1");
        if (uriParam == null) {
            throw new IllegalArgumentException("Param\u00e8tre xmlns:ns1 manquant dans l'enveloppe.");
        }
        final DocumentEntry bodyNode = node.getChildByName("SOAP-ENV:Body");
        if (bodyNode == null) {
            throw new IllegalArgumentException("Enfant SOAP-ENV:Body manquant dans l'enveloppe.");
        }
        final SOAPEnvelope envelope = new SOAPEnvelope(uriParam.getStringValue());
        envelope.m_body = SOAPBody.parseSOAPXMLEntry(bodyNode);
        return envelope;
    }
    
    @Override
    public String toString() {
        return "SOAPEnvelope{m_serviceUri='" + this.m_serviceUri + '\'' + ", m_headerFields=" + this.m_headerFields + ", m_body=" + this.m_body + '}';
    }
}
