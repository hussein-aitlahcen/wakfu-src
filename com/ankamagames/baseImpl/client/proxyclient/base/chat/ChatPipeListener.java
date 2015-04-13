package com.ankamagames.baseImpl.client.proxyclient.base.chat;

public interface ChatPipeListener
{
    void onMessage(ChatMessage p0);
    
    void onSubPipeCreated(ChatPipe p0, ChannelMode p1);
}
