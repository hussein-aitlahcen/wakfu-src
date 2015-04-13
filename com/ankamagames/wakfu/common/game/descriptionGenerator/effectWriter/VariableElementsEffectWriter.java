package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.elements.*;
import com.ankamagames.framework.text.*;
import java.util.*;

public class VariableElementsEffectWriter extends DefaultEffectWriter
{
    @Override
    protected String formatEffectParams(final WakfuEffect effect, final String effectText, final Object[] params, final boolean customFilled, final ContainerWriter writer) {
        final ItemWriter itemWriter = (ItemWriter)writer;
        final Item item = itemWriter.getRealItem();
        final MultiElementsInfo multiElementsEffects = (item == null) ? null : item.getMultiElementsInfo();
        if (multiElementsEffects == null || multiElementsEffects.isEmpty()) {
            return StringFormatter.format(effectText, params);
        }
        final HashSet<Elements> set = multiElementsEffects.get(effect.getActionId());
        final TextWidgetFormater twf = CastableDescriptionGenerator.m_twfFactory.createNew();
        for (final Elements e : set) {
            twf.append(CastableDescriptionGenerator.m_utilityDelegate.getElementIcon(CastableDescriptionGenerator.m_twfFactory.createNew(), e).finishAndToString());
            if (e != set.toArray()[set.size() - 1]) {
                twf.append(", ");
            }
        }
        final Object[] localParams = new Object[params.length + 1];
        System.arraycopy(params, 0, localParams, 0, params.length);
        localParams[localParams.length - 1] = twf.finishAndToString();
        return StringFormatter.format(effectText, localParams);
    }
}
