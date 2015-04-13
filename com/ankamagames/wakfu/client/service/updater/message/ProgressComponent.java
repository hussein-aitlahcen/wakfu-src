package com.ankamagames.wakfu.client.service.updater.message;

import com.google.gson.annotations.*;

public final class ProgressComponent
{
    @SerializedName("id")
    private int m_priority;
    @SerializedName("label")
    private String m_name;
    @SerializedName("completed")
    private boolean m_completed;
    
    public int getPriority() {
        return this.m_priority;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean isCompleted() {
        return this.m_completed;
    }
}
