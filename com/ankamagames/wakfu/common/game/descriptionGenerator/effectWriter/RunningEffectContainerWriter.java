package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;

public final class RunningEffectContainerWriter extends DefaultContainerWriter<EffectContainer<WakfuEffect>>
{
    public RunningEffectContainerWriter(final EffectContainer<WakfuEffect> container, final int id, final short level) {
        super(container, id, level);
    }
    
    public RunningEffectContainerWriter(final EffectContainer<WakfuEffect> container, final int id, final short level, final boolean useAutomaticDescription, final ArrayList<String> validCriterion, final ArrayList<String> invalidCriterion, final CastableDescriptionGenerator.DescriptionMode descriptionMode, final int freeDescriptionTranslationType) {
        super(container, id, level, useAutomaticDescription, validCriterion, invalidCriterion, descriptionMode, freeDescriptionTranslationType);
    }
    
    @Override
    public boolean isEffectDisplayable(final WakfuEffect effect) {
        return (effect.isDisplayInStateBar() || CastableDescriptionGenerator.m_utilityDelegate.forceDisplayState()) && effect.getContainerMinLevel() <= this.m_level && effect.getContainerMaxLevel() >= this.m_level;
    }
}
