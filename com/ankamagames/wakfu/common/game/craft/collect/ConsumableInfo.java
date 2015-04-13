package com.ankamagames.wakfu.common.game.craft.collect;

public class ConsumableInfo
{
    private final int m_consumableId;
    
    public ConsumableInfo(final int consumableId) {
        super();
        this.m_consumableId = consumableId;
    }
    
    public boolean hasConsumable() {
        return this.m_consumableId != 0;
    }
    
    public int getConsumableId() {
        return this.m_consumableId;
    }
}
