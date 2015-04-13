package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import org.jetbrains.annotations.*;
import java.util.*;

public class DummyEffectContainerWriter extends DefaultContainerWriter<EffectContainer<WakfuEffect>>
{
    public DummyEffectContainerWriter(final List<WakfuEffect> effects, final int id, final short level) {
        this(effects, id, level, true, CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY, 0);
    }
    
    public DummyEffectContainerWriter(final List<WakfuEffect> effects, final int id, final short level, final boolean useAutomaticDescription, final CastableDescriptionGenerator.DescriptionMode descriptionMode, final int freeDescriptionTranslationType) {
        this(effects, id, level, useAutomaticDescription, null, null, descriptionMode, freeDescriptionTranslationType);
    }
    
    public DummyEffectContainerWriter(final List<WakfuEffect> effects, final int id, final short level, final boolean useAutomaticDescription, @Nullable final ArrayList<String> validCriterion, @Nullable final ArrayList<String> invalidCriterion, final CastableDescriptionGenerator.DescriptionMode descriptionMode, final int freeDescriptionTranslationType) {
        super(new DummyEffectContainer(effects), id, level, useAutomaticDescription, validCriterion, invalidCriterion, descriptionMode, freeDescriptionTranslationType);
    }
    
    public static class DummyEffectContainer implements EffectContainer<WakfuEffect>
    {
        private List<WakfuEffect> m_list;
        
        public DummyEffectContainer(final List<WakfuEffect> list) {
            super();
            assert list != null && !list.contains(null);
            this.m_list = list;
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
            return this.m_list.iterator();
        }
    }
}
