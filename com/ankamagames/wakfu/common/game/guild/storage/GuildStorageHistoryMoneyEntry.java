package com.ankamagames.wakfu.common.game.guild.storage;

public class GuildStorageHistoryMoneyEntry extends GuildStorageHistoryEntry
{
    private int m_amount;
    
    public GuildStorageHistoryMoneyEntry(final String memberName, final long date, final int amount) {
        super(memberName, date);
        this.m_amount = amount;
    }
    
    public int getAmount() {
        return this.m_amount;
    }
    
    @Override
    public String toString() {
        return "GuildStorageHistoryMoneyEntry{m_memberName='" + this.m_memberName + '\'' + ", m_date=" + this.m_date + ", m_amount=" + this.m_amount + '}';
    }
}
