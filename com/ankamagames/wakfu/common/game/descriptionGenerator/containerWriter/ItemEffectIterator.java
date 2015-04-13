package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import java.util.*;

class ItemEffectIterator implements EffectContainer<WakfuEffect>
{
    private AbstractReferenceItem m_item;
    
    ItemEffectIterator(final AbstractReferenceItem item) {
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
        return (Iterator<WakfuEffect>)this.m_item.getEffectsIterator();
    }
}
