package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.util.*;

class ItemGemEffectIterator implements EffectContainer<WakfuEffect>
{
    private final Item m_item;
    
    ItemGemEffectIterator(final Item item) {
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
        return this.m_item.getGemsEffectsIterator();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ItemGemEffectIterator");
        sb.append("{m_item=").append(this.m_item);
        sb.append('}');
        return sb.toString();
    }
}
