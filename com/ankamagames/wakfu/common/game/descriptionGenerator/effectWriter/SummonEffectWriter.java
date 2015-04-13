package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.kernel.utils.*;

public class SummonEffectWriter extends DefaultEffectWriter
{
    @Override
    protected String formatEffectParams(final WakfuEffect effect, final String effectText, final Object[] params, final boolean customFilled, final ContainerWriter writer) {
        final short id1 = Short.valueOf(params[0].toString());
        params[0] = CastableDescriptionGenerator.m_utilityDelegate.getMonsterName(id1);
        return StringFormatter.format(effectText, params);
    }
}
