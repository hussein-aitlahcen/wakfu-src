package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;
import org.jetbrains.annotations.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.text.*;

public class ItemSetWriter extends DummyEffectContainerWriter
{
    public ItemSetWriter(final List<WakfuEffect> effects) {
        super(effects, 0, (short)0);
    }
    
    @Override
    public void onContainerEnd(@NotNull final ArrayList<String> descriptions) {
        final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
        if (!descriptions.isEmpty()) {
            boolean first = true;
            for (int i = 0, size = descriptions.size(); i < size; ++i) {
                if (!first) {
                    sb.newLine();
                }
                else {
                    first = false;
                }
                sb.append(descriptions.get(i));
            }
        }
        descriptions.clear();
        descriptions.add(sb.finishAndToString());
    }
    
    @Override
    public String onEffectAdded(@NotNull final String effectDescription, @NotNull final WakfuEffect effect) {
        if (effect.isAnUsableEffect()) {
            return "";
        }
        final String effectString = super.onEffectAdded(effectDescription, effect);
        final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
        sb.append(effectString).newLine();
        return sb.finishAndToString();
    }
}
