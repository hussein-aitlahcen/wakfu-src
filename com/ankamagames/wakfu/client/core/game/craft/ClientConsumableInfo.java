package com.ankamagames.wakfu.client.core.game.craft;

import com.ankamagames.wakfu.common.game.craft.collect.*;

public class ClientConsumableInfo extends ConsumableInfo
{
    private final int m_consumableGfxId;
    
    public ClientConsumableInfo(final int consumableId, final int consumableGfxId) {
        super(consumableId);
        this.m_consumableGfxId = consumableGfxId;
    }
    
    public boolean hasConsumableGfx() {
        return this.m_consumableGfxId != 0;
    }
    
    public int getConsumableGfxId() {
        return this.m_consumableGfxId;
    }
}
