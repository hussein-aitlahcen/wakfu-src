package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;

public enum ItemEffectType
{
    ALL_CHARACTERISTICS(true, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY) {
        @Override
        public EffectContainer<WakfuEffect> createEffectContainer(final AbstractReferenceItem item, final Item realItem) {
            if (realItem != null && realItem.hasGems()) {
                return new ItemAllEffectsIterator(realItem);
            }
            return new ItemEffectIterator(item);
        }
    }, 
    BASE_CHARACTERISTICS(true, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY) {
        @Override
        public EffectContainer<WakfuEffect> createEffectContainer(final AbstractReferenceItem item, final Item realItem) {
            return new ItemEffectIterator(item);
        }
    }, 
    EFFECTS(false, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY) {
        @Override
        public EffectContainer<WakfuEffect> createEffectContainer(final AbstractReferenceItem item, final Item realItem) {
            return new ItemEffectIterator(item);
        }
    }, 
    CRITICALS(false, CastableDescriptionGenerator.DescriptionMode.CRITICALS_ONLY) {
        @Override
        public EffectContainer<WakfuEffect> createEffectContainer(final AbstractReferenceItem item, final Item realItem) {
            return new ItemEffectIterator(item);
        }
    }, 
    GEMS(true, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY) {
        @Override
        public EffectContainer<WakfuEffect> createEffectContainer(final AbstractReferenceItem item, final Item realItem) {
            return new ItemGemEffectIterator(realItem);
        }
    };
    
    private final boolean m_isACharacteristic;
    private final CastableDescriptionGenerator.DescriptionMode m_mode;
    
    private ItemEffectType(final boolean characteristic, final CastableDescriptionGenerator.DescriptionMode mode) {
        this.m_isACharacteristic = characteristic;
        this.m_mode = mode;
    }
    
    public abstract EffectContainer<WakfuEffect> createEffectContainer(final AbstractReferenceItem p0, final Item p1);
    
    public boolean isACharacteristic() {
        return this.m_isACharacteristic;
    }
    
    public CastableDescriptionGenerator.DescriptionMode getMode() {
        return this.m_mode;
    }
}
