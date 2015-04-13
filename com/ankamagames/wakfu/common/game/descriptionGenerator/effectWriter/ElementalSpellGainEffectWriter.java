package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.text.*;

public class ElementalSpellGainEffectWriter extends DefaultEffectWriter
{
    @Override
    protected String formatEffectParams(final WakfuEffect effect, final String effectText, final Object[] params, final boolean customFilled, final ContainerWriter writer) {
        if (effect.getActionId() == RunningEffectConstants.ELEMENT_SPELL_GAIN.getId()) {
            final int elementId = ((Number)params[0]).intValue();
            final Elements element = Elements.getElementFromId((byte)elementId);
            final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
            CastableDescriptionGenerator.m_utilityDelegate.getElementIcon(sb, element);
            params[0] = sb.finishAndToString();
        }
        return StringFormatter.format(effectText, params);
    }
}
