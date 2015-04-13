package com.ankamagames.framework.net.soap;

import org.apache.log4j.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class SoapEntryLoader<T extends SOAPRequestProvider, U extends SOAPAnswerProcessor<T>> implements SOAPRequestAnswerListener
{
    private static final Logger m_logger;
    private final Object m_mutex;
    private final String m_soapServiceUrl;
    private final U m_answerProcessor;
    private final ArrayList<T> m_runningRequests;
    private Map<String, List<String>> m_sessionID;
    
    public SoapEntryLoader(final String soapServiceUrl, final U answerProcessor) {
        super();
        this.m_mutex = new Object();
        this.m_runningRequests = new ArrayList<T>();
        this.m_soapServiceUrl = soapServiceUrl;
        this.m_answerProcessor = answerProcessor;
    }
    
    protected void setSessionID(final Map<String, List<String>> sessionID) {
        this.m_sessionID = sessionID;
    }
    
    public void sendRequest(final T provider) {
        synchronized (this.m_mutex) {
            SoapEntryLoader.m_logger.info((Object)("[" + this.getClass().getSimpleName() + "] Envoi de requ\u00eate : " + provider.toString()));
            if (this.m_runningRequests.contains(provider)) {
                SoapEntryLoader.m_logger.info((Object)("[" + this.getClass().getSimpleName() + "] Requ\u00eate d\u00e9j\u00e0 en cours, on arr\u00eate :" + provider.toString()));
                this.m_answerProcessor.onError(provider);
                return;
            }
            final boolean empty = this.m_runningRequests.isEmpty();
            this.m_runningRequests.add(provider);
            if (empty) {
                this.doSendRequest(provider);
            }
        }
    }
    
    protected void doSendRequest(final SOAPRequestProvider provider) {
        final SOAPEnvelope envelope = new SOAPEnvelope(this.m_soapServiceUrl);
        envelope.setBody(provider.createRequest());
        try {
            final SOAPHTTPRequest request = new SOAPHTTPRequest(envelope, new URL(this.m_soapServiceUrl), this, this.m_sessionID);
            request.sendRequest();
        }
        catch (MalformedURLException e) {
            SoapEntryLoader.m_logger.warn((Object)("Erreur : " + e.getMessage()));
            this.removeAndNextRequest();
        }
        catch (IOException e2) {
            SoapEntryLoader.m_logger.warn((Object)("Erreur : " + e2.getMessage()));
            this.removeAndNextRequest();
        }
    }
    
    private void removeAndNextRequest() {
        if (this.m_runningRequests.isEmpty()) {
            return;
        }
        this.m_runningRequests.remove(0);
        if (!this.m_runningRequests.isEmpty()) {
            this.doSendRequest(this.m_runningRequests.get(0));
        }
    }
    
    @Override
    public void onAnswer(final SOAPEnvelope envelope) {
        synchronized (this.m_mutex) {
            this.m_answerProcessor.process(envelope, this.m_runningRequests.get(0));
            this.removeAndNextRequest();
        }
    }
    
    @Override
    public void onError(final String errorMsg) {
        SoapEntryLoader.m_logger.warn((Object)errorMsg);
        synchronized (this.m_mutex) {
            this.m_answerProcessor.onError(this.m_runningRequests.get(0));
            this.removeAndNextRequest();
        }
    }
    
    @Override
    public String toString() {
        return "SoapEntryLoader{m_soapServiceUrl='" + this.m_soapServiceUrl + '\'' + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)SoapEntryLoader.class);
    }
}
