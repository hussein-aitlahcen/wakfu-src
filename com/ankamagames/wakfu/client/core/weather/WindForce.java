package com.ankamagames.wakfu.client.core.weather;

import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;

public enum WindForce
{
    NONE((byte)0), 
    MODERATE((byte)1), 
    STRONG((byte)2);
    
    private byte m_id;
    
    private WindForce(final byte id) {
        this.m_id = id;
    }
    
    public String getIconUrl() {
        try {
            return String.format(WakfuConfiguration.getInstance().getString("windForceIconsPath"), this.ordinal());
        }
        catch (PropertyException e) {
            return null;
        }
    }
    
    public byte getId() {
        return this.m_id;
    }
}
