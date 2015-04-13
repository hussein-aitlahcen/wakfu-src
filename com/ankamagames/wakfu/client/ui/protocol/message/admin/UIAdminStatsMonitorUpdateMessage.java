package com.ankamagames.wakfu.client.ui.protocol.message.admin;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public final class UIAdminStatsMonitorUpdateMessage extends UIMessage
{
    private boolean m_queryNewStats;
    
    public UIAdminStatsMonitorUpdateMessage() {
        super();
        this.m_queryNewStats = false;
    }
    
    public boolean isQueryNewStats() {
        return this.m_queryNewStats;
    }
    
    public void setQueryNewStats(final boolean queryNewStats) {
        this.m_queryNewStats = queryNewStats;
    }
}
