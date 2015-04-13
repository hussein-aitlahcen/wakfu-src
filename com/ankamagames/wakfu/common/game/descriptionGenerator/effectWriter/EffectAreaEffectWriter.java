package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effectArea.*;
import com.ankamagames.framework.text.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.effectArea.*;

public class EffectAreaEffectWriter extends DefaultEffectWriter
{
    @Override
    protected String formatEffectParams(final WakfuEffect effect, final String effectText, final Object[] params, final boolean customFilled, final ContainerWriter writer) {
        final BasicEffectArea<WakfuEffect, EffectAreaParameters> aura = getEffectArea(effect.getActionId(), params);
        final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
        final short containerLevel = writer.getLevel();
        if (aura != null) {
            final boolean displayName = !writer.displayOnlySubEffects() && effect.getActionId() != CastableDescriptionGenerator.SET_FECA_GLYPH && effect.getActionId() != CastableDescriptionGenerator.SET_CADRAN;
            if (displayName) {
                final String name = CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.AREA_NAME_TRANSLATION_TYPE, (short)aura.getBaseId(), new Object[0]);
                sb.append(name);
                if (!this.m_elementAdded) {
                    final String elementStr = CastableDescriptionGenerator.addElement(effect);
                    if (elementStr != null && elementStr.length() > 0) {
                        sb.append(" ").append(elementStr);
                    }
                }
                sb.append(DefaultEffectWriter.getAreaOfEffect(effect));
                if (effect.getActionId() == RunningEffectConstants.SET_BEACON.getId() || effect.getActionId() == RunningEffectConstants.SET_BARREL.getId()) {
                    final int hp = (int)(aura.getParams(0) + aura.getParams(1) * containerLevel);
                    sb.append("\n").append(CastableDescriptionGenerator.SUB_EFFECT_CARAC_PREFIX);
                    sb.append(CastableDescriptionGenerator.m_translator.getString((effect.getActionId() == RunningEffectConstants.SET_BARREL.getId()) ? CastableDescriptionGenerator.HP_VAR_BARREL : CastableDescriptionGenerator.HP_VAR_BEACON, hp));
                }
            }
            if (effect.getActionId() == CastableDescriptionGenerator.SET_FECA_GLYPH) {
                sb.append(CastableDescriptionGenerator.m_utilityDelegate.getSpellTargetIcon(CastableDescriptionGenerator.m_twfFactory.createNew(), "glyph").finishAndToString()).append(" ");
                sb.append(CastableDescriptionGenerator.getEffectAreaNameLink((AbstractEffectArea)aura, ((Number)params[1]).shortValue(), true));
            }
            else if (effect.getActionId() != CastableDescriptionGenerator.SET_BOMB_ID) {
                final ArrayList<String> auraEffectsDescription = CastableDescriptionGenerator.generateDescription(new EffectAreaWriter(aura, containerLevel, writer.getFreeDescriptionTranslationType()));
                if (auraEffectsDescription.size() != 0) {
                    if (displayName) {
                        sb.append("\n").append(CastableDescriptionGenerator.mergeSubDesc(auraEffectsDescription, !effectText.contains("\n")));
                    }
                    else {
                        sb.append(CastableDescriptionGenerator.mergeSubDesc(auraEffectsDescription, false));
                    }
                }
            }
        }
        if (params == null || params.length == 0) {
            return StringFormatter.format(effectText, sb.finishAndToString());
        }
        final Object param0Saved = params[0];
        params[0] = sb.finishAndToString();
        final String result = StringFormatter.format(effectText, params);
        params[0] = param0Saved;
        return result;
    }
    
    public static BasicEffectArea<WakfuEffect, EffectAreaParameters> getEffectArea(final int actionId, final Object[] params) {
        if (actionId == RunningEffectConstants.SET_TRAP.getId()) {
            return StaticEffectAreaManager.getInstance().getTrapEffectArea(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_GLYPH.getId()) {
            return StaticEffectAreaManager.getInstance().getGlyph(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_AURA.getId() || actionId == RunningEffectConstants.SET_AURA_ON_TARGET.getId()) {
            return StaticEffectAreaManager.getInstance().getAura(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_LOOT_EFFECT_AREA.getId()) {
            return StaticEffectAreaManager.getInstance().getAreaFromId(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_BARREL.getId()) {
            return StaticEffectAreaManager.getInstance().getAreaFromId(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_BEACON.getId()) {
            return StaticEffectAreaManager.getInstance().getBeacon(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_HOUR.getId()) {
            return StaticEffectAreaManager.getInstance().getHourAreas(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_CADRAN.getId()) {
            return StaticEffectAreaManager.getInstance().getHourAreas(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_EFFECT_AREA.getId()) {
            return StaticEffectAreaManager.getInstance().getAreaFromId(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_FECA_GLYPH.getId()) {
            return StaticEffectAreaManager.getInstance().getAreaFromId(((Number)params[0]).intValue());
        }
        if (actionId == RunningEffectConstants.SET_BOMB.getId()) {
            return StaticEffectAreaManager.getInstance().getBombArea(((Number)params[0]).intValue());
        }
        return null;
    }
}
