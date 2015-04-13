package com.ankamagames.wakfu.client.core.game.item.xp;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.common.game.item.xp.*;

public class ItemXpControllerLevelModifier extends ItemXpController
{
    private long m_initialXp;
    
    public ItemXpControllerLevelModifier(final ItemXpHolder holder) {
        super(holder);
        this.m_initialXp = holder.getXp().getXp();
    }
    
    public void restore() {
        this.setXp(this.m_initialXp);
    }
    
    public void setLevel(final short level) {
        if (!this.getXpHolder().hasXp()) {
            return;
        }
        final ItemXp xp = this.getXpHolder().getXp();
        this.setXp(xp.getXpTable().getXpByLevel(level));
    }
    
    @Override
    public void setXp(final long xp) throws ItemXpException {
        super.setXp(xp);
        final Item item = (Item)this.getXpHolder();
        item.resetCache();
        PropertiesProvider.getInstance().firePropertyValueChanged((FieldProvider)this.getXpHolder(), "ap", "level", "effect", "effectAndCaracteristic", "caracteristic", "criticalEffectDetails");
    }
}
