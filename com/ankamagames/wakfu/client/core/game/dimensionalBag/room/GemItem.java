package com.ankamagames.wakfu.client.core.game.dimensionalBag.room;

import com.ankamagames.wakfu.common.game.item.*;

public class GemItem
{
    private Item m_gem;
    private byte m_roomIndex;
    
    public GemItem(final Item gem, final byte roomIndex) {
        super();
        this.m_gem = gem;
        this.m_roomIndex = roomIndex;
    }
    
    public Item getGem() {
        return this.m_gem;
    }
    
    public byte getRoomIndex() {
        return this.m_roomIndex;
    }
}
