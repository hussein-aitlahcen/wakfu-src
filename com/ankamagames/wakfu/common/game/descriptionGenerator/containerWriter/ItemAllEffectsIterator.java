package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;

class ItemAllEffectsIterator implements EffectContainer<WakfuEffect>
{
    private final Item m_item;
    
    ItemAllEffectsIterator(final Item item) {
        super();
        this.m_item = item;
    }
    
    @Override
    public int getContainerType() {
        return 0;
    }
    
    @Override
    public long getEffectContainerId() {
        return 0L;
    }
    
    @Override
    public Iterator<WakfuEffect> iterator() {
        return new MergedCharacEffectsIterator(this.m_item.getBaseAndGemsEffectsIterator());
    }
    
    @Override
    public String toString() {
        return "ItemAllEffectsIterator{m_item=" + this.m_item + '}';
    }
}
