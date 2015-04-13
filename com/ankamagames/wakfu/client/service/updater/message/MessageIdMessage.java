package com.ankamagames.wakfu.client.service.updater.message;

import com.google.gson.annotations.*;

public final class MessageIdMessage
{
    @SerializedName("_msg_id")
    private String m_id;
    
    public String getId() {
        return this.m_id;
    }
    
    public MessageId getMessageId() {
        return MessageId.from(this.m_id);
    }
}
