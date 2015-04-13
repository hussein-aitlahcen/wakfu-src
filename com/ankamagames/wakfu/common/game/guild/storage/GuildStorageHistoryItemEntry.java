package com.ankamagames.wakfu.common.game.guild.storage;

import com.ankamagames.wakfu.common.rawData.*;

public class GuildStorageHistoryItemEntry extends GuildStorageHistoryEntry
{
    private final RawInventoryItem m_item;
    private final short m_qty;
    
    public GuildStorageHistoryItemEntry(final String memberName, final long date, final RawInventoryItem item, final short qty) {
        super(memberName, date);
        this.m_item = item;
        this.m_qty = qty;
    }
    
    public RawInventoryItem getItem() {
        return this.m_item;
    }
    
    public short getQty() {
        return this.m_qty;
    }
    
    @Override
    public String toString() {
        return "GuildStorageHistoryItemEntry{m_item=" + this.m_item + '}';
    }
}
