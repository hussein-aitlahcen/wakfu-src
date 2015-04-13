package com.ankamagames.wakfu.client.core.game.spell;

import com.ankamagames.wakfu.common.game.spell.*;

public class EffectGroup extends AbstractEffectGroup
{
    public EffectGroup(final int basedId) {
        super(basedId);
    }
    
    private EffectGroup() {
        super();
    }
    
    @Override
    public EffectGroup instanceAnother(final short level) {
        final EffectGroup effectGroup = new EffectGroup();
        effectGroup.m_effectGroupBaseId = this.m_effectGroupBaseId;
        effectGroup.m_effects = this.m_effects;
        effectGroup.m_level = level;
        return effectGroup;
    }
}
