package com.ankamagames.framework.net.soap;

public interface SOAPRequestAnswerListener
{
    void onAnswer(SOAPEnvelope p0);
    
    void onError(String p0);
}
