package com.ankamagames.wakfu.client.service.updater.message;

import com.google.gson.annotations.*;

public final class ErrorMessage
{
    @SerializedName("_msg_id")
    private String m_id;
    @SerializedName("message")
    private String m_message;
    @SerializedName("type")
    private int m_type;
    
    public String getId() {
        return this.m_id;
    }
    
    public String getMessage() {
        return this.m_message;
    }
    
    public int getType() {
        return this.m_type;
    }
}
