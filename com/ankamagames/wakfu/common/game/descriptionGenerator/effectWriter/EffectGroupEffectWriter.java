package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import org.apache.log4j.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.regex.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter.*;
import com.ankamagames.wakfu.common.game.spell.*;
import java.util.*;

public class EffectGroupEffectWriter extends DefaultEffectWriter
{
    private static final Logger m_logger;
    
    @Override
    protected boolean effectTextIsValid(final String effectText, final int effectId) throws IllegalArgumentException {
        if (effectText == null) {
            throw new IllegalArgumentException("!!! ERROR !!! Pas de description sur l'effet id = " + effectId);
        }
        return true;
    }
    
    @Override
    protected String formatEffectParams(final WakfuEffect effect, String effectText, final Object[] params, final boolean customFilled, final ContainerWriter writer) {
        final short containerLevel = writer.getLevel();
        if (RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.contains(effect.getActionId())) {
            final Matcher matcher = CastableDescriptionGenerator.EFFECT_GROUP_PATTERN.matcher(effectText);
            final AbstractEffectGroup aeg = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(effect.getEffectId());
            if (aeg == null) {
                EffectGroupEffectWriter.m_logger.error((Object)("Groupe d'effet vide " + effect.getEffectId() + ", voir : RunningEffectGroupType.WITHOUT_EFFECT_GROUP_DESCRIPTION"));
                return null;
            }
            if (!matcher.find() && !customFilled) {
                final TextWidgetFormater effectGroupStringBuilder = CastableDescriptionGenerator.m_twfFactory.createNew();
                final Iterator<WakfuEffect> it = aeg.iterator();
                while (it.hasNext()) {
                    final WakfuEffect ef = it.next();
                    final EffectWriter effectWriter = CastableDescriptionGenerator.getEffectWriter(ef.getActionId());
                    final int result = effectWriter.writeEffect(effectGroupStringBuilder, ef, writer);
                    if (result == -1) {
                        continue;
                    }
                    if (!it.hasNext()) {
                        continue;
                    }
                    effectGroupStringBuilder.append("\n");
                }
                return effectGroupStringBuilder.finishAndToString();
            }
            effectText = this.describeEffectGroup(effectText, aeg, containerLevel, false, writer);
        }
        return StringFormatter.format(effectText, params);
    }
    
    private String describeEffectGroup(final String effectText, final AbstractEffectGroup aeg, final short containerLevel, final boolean showSubContainerDescription, final ContainerWriter writer) {
        if (aeg == null) {
            return effectText;
        }
        short stateLevel = 0;
        final Matcher matcher = CastableDescriptionGenerator.EFFECT_GROUP_PATTERN.matcher(effectText);
        final TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
        int lastMatchingIndex = 0;
        while (matcher.find()) {
            final String group = matcher.group();
            if (group.length() == 0) {
                continue;
            }
            final Matcher matcher2 = CastableDescriptionGenerator.EFFECT_GROUP_NUM_REG_PATTERN.matcher(group);
            AbstractEffectGroup aeg2 = null;
            WakfuEffect subEffect = null;
            while (matcher2.find()) {
                final String group2 = matcher2.group();
                if (group2.length() > 0) {
                    final char type = group2.charAt(0);
                    switch (type) {
                        case '$': {
                            final byte effectNb = Byte.parseByte(group2.substring(1));
                            final WakfuEffect wakfuEffect = ((aeg2 != null) ? aeg2 : aeg).getEffect(effectNb - 1);
                            if (wakfuEffect == null) {
                                EffectGroupEffectWriter.m_logger.error((Object)("description d'effet foireuse, on demande l'effet inexistant " + effectNb + " du groupe d'effet " + aeg));
                                continue;
                            }
                            aeg2 = null;
                            subEffect = wakfuEffect;
                            if (RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.contains(wakfuEffect.getActionId())) {
                                aeg2 = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(wakfuEffect.getEffectId());
                                continue;
                            }
                            continue;
                        }
                        case '#': {
                            final byte paramNb = Byte.parseByte(group2.substring(1));
                            if (subEffect == null) {
                                EffectGroupEffectWriter.m_logger.error((Object)("description d'effet foireuse, on demande le param\u00e8tre " + paramNb + " d'un effet non sp\u00e9cifi\u00e9 !"));
                                continue;
                            }
                            String effectDesc = null;
                            try {
                                effectDesc = "";
                                if ((paramNb == 1 && (subEffect.getActionId() == CastableDescriptionGenerator.STATE_ACTION_ID || subEffect.getActionId() == CastableDescriptionGenerator.UNAPPLY_STATE_ACTION_ID)) || subEffect.getActionId() == CastableDescriptionGenerator.STATE_RESIST_ACTION_ID || subEffect.getActionId() == CastableDescriptionGenerator.APPLY_STATE_PERCENT_FUNCTION_AREA_HP_ID || subEffect.getActionId() == CastableDescriptionGenerator.APPLY_FECA_ARMOR_ACTION_ID) {
                                    final State currentState = StateManager.getInstance().getState(subEffect.getParam(paramNb - 1, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL));
                                    if (subEffect.getParamsCount() > paramNb) {
                                        stateLevel = (short)subEffect.getParam(paramNb, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                                    }
                                    if (currentState != null) {
                                        effectDesc = CastableDescriptionGenerator.getStateNameLink(currentState, stateLevel, (short)subEffect.getContainerMaxLevel(), true);
                                    }
                                }
                                else {
                                    effectDesc = String.valueOf(subEffect.getParam(paramNb - 1, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL));
                                }
                            }
                            catch (RuntimeException e) {
                                EffectGroupEffectWriter.m_logger.error((Object)("Exception while retriieving parameter of effect " + subEffect), (Throwable)e);
                                effectDesc = "TRANSLATION_ERROR";
                            }
                            sb.append(effectText, lastMatchingIndex, matcher.start());
                            sb.append(effectDesc);
                            lastMatchingIndex = matcher.end();
                            continue;
                        }
                        case 'p': {
                            final char charAt1 = group2.charAt(1);
                            switch (charAt1) {
                                case 'r': {
                                    if (subEffect == null) {
                                        EffectGroupEffectWriter.m_logger.error((Object)"description d'effet foireuse, on demande la probabilit\u00e9 d'un effet non sp\u00e9cifi\u00e9 !");
                                        continue;
                                    }
                                    sb.append(effectText, lastMatchingIndex, matcher.start());
                                    sb.append(String.valueOf(Math.round(subEffect.getExecutionProbability(containerLevel))) + "%");
                                    lastMatchingIndex = matcher.end();
                                    continue;
                                }
                            }
                            continue;
                        }
                        case 'e': {
                            final char charAt1 = group2.charAt(1);
                            switch (charAt1) {
                                case 'l': {
                                    if (subEffect == null) {
                                        EffectGroupEffectWriter.m_logger.error((Object)"description d'effet foireuse, on demande l'\u00e9l\u00e9ment d'un effet non sp\u00e9cifi\u00e9 !");
                                        continue;
                                    }
                                    sb.append(effectText, lastMatchingIndex, matcher.start());
                                    sb.append(CastableDescriptionGenerator.addElement(subEffect));
                                    lastMatchingIndex = matcher.end();
                                    continue;
                                }
                                case 'f': {
                                    if (subEffect == null) {
                                        EffectGroupEffectWriter.m_logger.error((Object)"description d'effet foireuse, on veut d\u00e9crire un effet non sp\u00e9cifi\u00e9 !");
                                        continue;
                                    }
                                    final TextWidgetFormater stringBuilder = CastableDescriptionGenerator.m_twfFactory.createNew();
                                    final EffectWriter effectWriter = CastableDescriptionGenerator.getEffectWriter(subEffect.getActionId());
                                    effectWriter.writeEffect(stringBuilder, subEffect, writer);
                                    sb.append(effectText, lastMatchingIndex, matcher.start());
                                    sb.append(stringBuilder.finishAndToString());
                                    lastMatchingIndex = matcher.end();
                                    continue;
                                }
                            }
                            continue;
                        }
                        case 'a': {
                            final char charAt1 = group2.charAt(1);
                            switch (charAt1) {
                                case 'e': {
                                    if (subEffect == null) {
                                        EffectGroupEffectWriter.m_logger.error((Object)"description d'effet foireuse, on demande l'aoe d'un effet non sp\u00e9cifi\u00e9 !");
                                        continue;
                                    }
                                    sb.append(effectText, lastMatchingIndex, matcher.start());
                                    sb.append(DefaultEffectWriter.getAreaOfEffect(subEffect));
                                    lastMatchingIndex = matcher.end();
                                    continue;
                                }
                            }
                            continue;
                        }
                        case 's': {
                            final char charAt1 = group2.charAt(1);
                            switch (charAt1) {
                                case 't': {
                                    if (subEffect == null) {
                                        EffectGroupEffectWriter.m_logger.error((Object)"description d'effet foireuse, on veut d\u00e9crire un effet (\u00e9tat) non sp\u00e9cifi\u00e9 !");
                                        continue;
                                    }
                                    if (subEffect.getActionId() != CastableDescriptionGenerator.STATE_ACTION_ID && subEffect.getActionId() != CastableDescriptionGenerator.UNAPPLY_STATE_ACTION_ID && subEffect.getActionId() != CastableDescriptionGenerator.APPLY_STATE_PERCENT_FUNCTION_AREA_HP_ID && subEffect.getActionId() != CastableDescriptionGenerator.STATE_RESIST_ACTION_ID) {
                                        continue;
                                    }
                                    final State currentState2 = StateManager.getInstance().getState(subEffect.getParam(0, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL));
                                    if (currentState2 == null) {
                                        continue;
                                    }
                                    stateLevel = (short)subEffect.getParam(1, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                                    final String subDesc = CastableDescriptionGenerator.describeSubContainerDescription(currentState2, stateLevel, false, writer.getFreeDescriptionTranslationType(), false, false);
                                    if (subDesc == null) {
                                        continue;
                                    }
                                    sb.append(effectText, lastMatchingIndex, matcher.start());
                                    sb.append(subDesc);
                                    lastMatchingIndex = matcher.end();
                                    continue;
                                }
                            }
                            continue;
                        }
                        case 'l': {
                            final char charAt1 = group2.charAt(1);
                            if (subEffect == null) {
                                EffectGroupEffectWriter.m_logger.error((Object)"description d'effet foireuse, on veut d\u00e9crire un effet (\u00e9tat) non sp\u00e9cifi\u00e9 !");
                                continue;
                            }
                            boolean withPlots = false;
                            switch (charAt1) {
                                case 'i': {
                                    withPlots = false;
                                    break;
                                }
                                case 'p': {
                                    withPlots = true;
                                    break;
                                }
                                default: {
                                    return "";
                                }
                            }
                            final boolean onlySubEffects = writer.displayOnlySubEffects();
                            if (subEffect.getActionId() == CastableDescriptionGenerator.SET_GLYPH) {
                                writer.setDisplayOnlySubEffects(true);
                            }
                            String subDesc2 = null;
                            if (RunningEffectGroupType.EFFECT_GROUP_TYPE_IDS.contains(subEffect.getActionId())) {
                                final ArrayList<String> strings = CastableDescriptionGenerator.generateDescription(new EffectGroupWriter(aeg2, subEffect.getEffectId(), containerLevel, writer.getFreeDescriptionTranslationType()));
                                subDesc2 = (showSubContainerDescription ? "\n" : ("" + CastableDescriptionGenerator.mergeSubDesc(strings, withPlots)));
                            }
                            else if (subEffect.getActionId() == CastableDescriptionGenerator.STATE_ACTION_ID || subEffect.getActionId() == CastableDescriptionGenerator.APPLY_FECA_ARMOR_ACTION_ID || subEffect.getActionId() == CastableDescriptionGenerator.APPLY_STATE_PERCENT_FUNCTION_AREA_HP_ID || subEffect.getActionId() == CastableDescriptionGenerator.UNAPPLY_STATE_ACTION_ID || subEffect.getActionId() == CastableDescriptionGenerator.STATE_RESIST_ACTION_ID) {
                                final State currentState3 = StateManager.getInstance().getState(subEffect.getParam(0, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL));
                                if (currentState3 == null) {
                                    continue;
                                }
                                stateLevel = (short)subEffect.getParam(1, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                                subDesc2 = CastableDescriptionGenerator.describeSubContainerDescription(currentState3, stateLevel, false, writer.getFreeDescriptionTranslationType(), false, withPlots);
                            }
                            else {
                                final int effectParamsCount = subEffect.getParamsCount();
                                final Object[] params = new Object[effectParamsCount];
                                for (int j = 0; j < effectParamsCount; ++j) {
                                    params[j] = subEffect.getParam(j, containerLevel, RoundingMethod.LIKE_PREVIOUS_LEVEL);
                                }
                                final TextWidgetFormater sb2 = new TextWidgetFormater();
                                final EffectWriter effectWriter2 = CastableDescriptionGenerator.getEffectWriter(subEffect.getActionId());
                                effectWriter2.writeEffect(sb2, subEffect, writer);
                                subDesc2 = sb2.finishAndToString();
                            }
                            if (subDesc2 != null) {
                                sb.append(effectText, lastMatchingIndex, matcher.start());
                                sb.append(subDesc2);
                                lastMatchingIndex = matcher.end();
                            }
                            writer.setDisplayOnlySubEffects(onlySubEffects);
                            continue;
                        }
                    }
                }
            }
        }
        sb.append(effectText, lastMatchingIndex, effectText.length());
        return sb.finishAndToString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectGroupEffectWriter.class);
    }
}
