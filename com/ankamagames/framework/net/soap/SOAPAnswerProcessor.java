package com.ankamagames.framework.net.soap;

public interface SOAPAnswerProcessor<T extends SOAPRequestProvider>
{
    void process(SOAPEnvelope p0, T p1);
    
    void onError(T p0);
}
