package com.ankamagames.wakfu.common.game.guild.storage;

public abstract class GuildStorageHistoryEntry
{
    protected String m_memberName;
    protected long m_date;
    
    protected GuildStorageHistoryEntry(final String memberName, final long date) {
        super();
        this.m_memberName = memberName;
        this.m_date = date;
    }
    
    protected GuildStorageHistoryEntry() {
        super();
    }
    
    public String getMemberName() {
        return this.m_memberName;
    }
    
    public long getDate() {
        return this.m_date;
    }
}
