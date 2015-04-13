package com.ankamagames.wakfu.client.service.updater.message;

import com.google.gson.annotations.*;

public final class NewGameClientConnectedMessage
{
    @SerializedName("_msg_id")
    private String m_id;
    @SerializedName("numberOfClients")
    private int m_numberOfClients;
    @SerializedName("newClientPid")
    private long m_newClientPid;
    
    public String getId() {
        return this.m_id;
    }
    
    public int getNumberOfClients() {
        return this.m_numberOfClients;
    }
    
    public long getNewClientPid() {
        return this.m_newClientPid;
    }
}
