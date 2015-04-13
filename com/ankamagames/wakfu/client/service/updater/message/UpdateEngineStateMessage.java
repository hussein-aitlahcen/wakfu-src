package com.ankamagames.wakfu.client.service.updater.message;

import com.google.gson.annotations.*;

public final class UpdateEngineStateMessage
{
    @SerializedName("_msg_id")
    private String m_id;
    @SerializedName("updating")
    private boolean m_updating;
    @SerializedName("upToDate")
    private boolean m_upToDate;
    @SerializedName("updateFailed")
    private boolean m_updateFailed;
    
    public boolean isUpdating() {
        return this.m_updating;
    }
    
    public boolean isUpToDate() {
        return this.m_upToDate;
    }
    
    public boolean isUpdateFailed() {
        return this.m_updateFailed;
    }
}
