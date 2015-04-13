package com.ankamagames.wakfu.common.game.item.companion;

import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;

public final class CompanionItemInfo
{
    private long m_xp;
    
    public CompanionItemInfo() {
        super();
    }
    
    public CompanionItemInfo(final long xp) {
        super();
        this.m_xp = xp;
    }
    
    public long getXp() {
        return this.m_xp;
    }
    
    public void setXp(final long xp) {
        this.m_xp = xp;
    }
    
    public void toRaw(final RawCompanionInfo rawCompanionInfo) {
        rawCompanionInfo.xp = this.m_xp;
    }
    
    @Override
    public String toString() {
        return "CompanionItemInfo{m_xp=" + this.m_xp + '}';
    }
}
