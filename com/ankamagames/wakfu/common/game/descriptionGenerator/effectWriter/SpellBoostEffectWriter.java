package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.text.*;

public class SpellBoostEffectWriter extends DefaultEffectWriter
{
    private static final Logger m_logger;
    
    @Override
    protected String formatEffectParams(final WakfuEffect effect, final String effectText, final Object[] params, final boolean customFilled, final ContainerWriter writer) {
        AbstractSpellLevel<AbstractSpell> spellLevel = null;
        if (effect.getActionId() == RunningEffectConstants.SPELL_BOOST_LEVEL.getId()) {
            final int spellId = ((Number)params[0]).intValue();
            spellLevel = (AbstractSpellLevel<AbstractSpell>)CastableDescriptionGenerator.m_utilityDelegate.getSpell((short)spellId);
            spellLevel.setLevel(writer.getLevel(), true);
            final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
            CastableDescriptionGenerator.m_utilityDelegate.formatSpellName(sb, spellLevel.getSpell());
            params[0] = sb.finishAndToString();
        }
        return StringFormatter.format(effectText, params);
    }
    
    static {
        m_logger = Logger.getLogger((Class)SpellBoostEffectWriter.class);
    }
}
