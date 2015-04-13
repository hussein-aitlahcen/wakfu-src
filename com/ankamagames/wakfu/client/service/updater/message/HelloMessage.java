package com.ankamagames.wakfu.client.service.updater.message;

import com.google.gson.annotations.*;

public final class HelloMessage
{
    @SerializedName("_msg_id")
    private String m_id;
    @SerializedName("id")
    private String m_type;
    
    public static HelloMessage get() {
        return new HelloMessage("HELLO", "GAME");
    }
    
    HelloMessage() {
        super();
    }
    
    public String getId() {
        return this.m_id;
    }
    
    public String getType() {
        return this.m_type;
    }
    
    private HelloMessage(final String id, final String type) {
        super();
        this.m_id = id;
        this.m_type = type;
    }
}
