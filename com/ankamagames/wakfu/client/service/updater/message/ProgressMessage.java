package com.ankamagames.wakfu.client.service.updater.message;

import com.google.gson.annotations.*;
import java.util.*;

public final class ProgressMessage
{
    @SerializedName("_msg_id")
    private String m_id;
    @SerializedName("progress")
    private double m_progress;
    @SerializedName("eta")
    private double m_estimatedTime;
    @SerializedName("groups")
    private ProgressComponent[] m_parts;
    
    public String getId() {
        return this.m_id;
    }
    
    public double getProgress() {
        return this.m_progress;
    }
    
    public double getEstimatedTime() {
        return this.m_estimatedTime;
    }
    
    public List<ProgressComponent> getParts() {
        return Arrays.asList(this.m_parts);
    }
}
