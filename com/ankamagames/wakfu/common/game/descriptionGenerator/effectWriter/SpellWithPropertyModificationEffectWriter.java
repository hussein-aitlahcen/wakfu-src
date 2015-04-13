package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.text.*;

public final class SpellWithPropertyModificationEffectWriter extends DefaultEffectWriter
{
    @Override
    protected String formatEffectParams(final WakfuEffect effect, final String effectText, final Object[] params, final boolean customFilled, final ContainerWriter writer) {
        if (effect.getActionId() == RunningEffectConstants.SPELL_WITH_PROPERTY_AP_COST_REDUCTION.getId() || effect.getActionId() == RunningEffectConstants.SPELL_WITH_PROPERTY_RANGE_GAIN.getId()) {
            final int propertyId = ((Number)params[1]).intValue();
            final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
            CastableDescriptionGenerator.m_utilityDelegate.formatSpellPropertyName(sb, propertyId);
            params[0] = sb.finishAndToString();
        }
        return StringFormatter.format(effectText, params);
    }
}
