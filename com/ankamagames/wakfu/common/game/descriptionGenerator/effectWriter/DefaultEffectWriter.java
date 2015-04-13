package com.ankamagames.wakfu.common.game.descriptionGenerator.effectWriter;

import org.apache.log4j.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.regex.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.framework.ai.targetfinder.aoe.*;

public class DefaultEffectWriter implements EffectWriter
{
    private static final Logger m_logger;
    protected boolean m_elementAdded;
    private final EffectParametersComputer m_effectParametersComputer;
    
    public DefaultEffectWriter() {
        super();
        this.m_elementAdded = false;
        this.m_effectParametersComputer = DefaultEffectParametersComputer.INSTANCE;
    }
    
    public DefaultEffectWriter(final EffectParametersComputer effectParametersComputer) {
        super();
        this.m_elementAdded = false;
        this.m_effectParametersComputer = effectParametersComputer;
    }
    
    protected boolean effectTextIsValid(final String effectText, final int effectId) throws IllegalArgumentException {
        return effectText != null && effectText.length() != 0;
    }
    
    @Override
    public final int writeEffect(final TextWidgetFormater sb, final WakfuEffect effect, final ContainerWriter writer) {
        if (!writer.isEffectDisplayable(effect)) {
            return -1;
        }
        String effectText = null;
        boolean customFilled = false;
        final boolean minimalDescriptionMode = writer.isInMinimalDescriptionMode();
        if (!minimalDescriptionMode && CastableDescriptionGenerator.m_translator.containsContentKey(CastableDescriptionGenerator.EFFECT_CUSTOM_DESCRIPTION_TRANSLATION_TYPE, effect.getEffectId())) {
            effectText = CastableDescriptionGenerator.m_translator.getStringWithoutFormat(CastableDescriptionGenerator.EFFECT_CUSTOM_DESCRIPTION_TRANSLATION_TYPE, effect.getEffectId());
        }
        if (effectText == null || effectText.length() == 0) {
            effectText = CastableDescriptionGenerator.m_translator.getStringWithoutFormat(CastableDescriptionGenerator.EFFECT_DESCRIPTION_TRANSLATION_TYPE, effect.getActionId());
        }
        else {
            customFilled = true;
        }
        if (!this.effectTextIsValid(effectText, effect.getEffectId())) {
            return -1;
        }
        final short containerLevel = writer.getLevel();
        final float probability = effect.getExecutionProbability(containerLevel);
        boolean probabilityAdded = false;
        boolean aoeAdded = false;
        TextWidgetFormater newSB = CastableDescriptionGenerator.m_twfFactory.createNew();
        int lastMatchingIndex = 0;
        final Matcher spellMatcher = CastableDescriptionGenerator.EFFECT_PATTERN.matcher(effectText);
        while (spellMatcher.find()) {
            final String value = spellMatcher.group(1);
            final char charAt0 = value.charAt(0);
            newSB.append(effectText, lastMatchingIndex, spellMatcher.start());
            switch (charAt0) {
                case 'p': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'r': {
                            newSB.append(String.valueOf(Math.round(probability)));
                            probabilityAdded = true;
                            break;
                        }
                        case 'l': {
                            newSB.append(CastableDescriptionGenerator.SUB_EFFECT_CARAC_PREFIX);
                            break;
                        }
                    }
                    break;
                }
                case 'a': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'e': {
                            newSB.append(getAreaOfEffect(effect));
                            aoeAdded = true;
                            break;
                        }
                    }
                    break;
                }
                case 's': {
                    final char charAt = value.charAt(1);
                    switch (charAt) {
                        case 'e': {
                            writer.setMinimalDescriptionMode(true);
                            final EffectWriter effectWriter = CastableDescriptionGenerator.getEffectWriter(effect.getActionId());
                            effectWriter.writeEffect(newSB, effect, writer);
                            writer.setMinimalDescriptionMode(minimalDescriptionMode);
                            if (!this.effectTextIsValid(effectText, effect.getEffectId())) {
                                return -1;
                            }
                            break;
                        }
                    }
                    break;
                }
            }
            lastMatchingIndex = spellMatcher.end();
        }
        newSB.append(effectText, lastMatchingIndex, effectText.length());
        effectText = newSB.toString();
        effectText = replaceStaticPatterns(effectText);
        this.m_elementAdded = false;
        newSB = CastableDescriptionGenerator.m_twfFactory.createNew();
        lastMatchingIndex = 0;
        final Matcher matcher = CastableDescriptionGenerator.ELEMENT_PATTERN.matcher(effectText);
        while (matcher.find()) {
            final String group3 = matcher.group(3);
            newSB.append(effectText, lastMatchingIndex, matcher.start());
            if (group3.length() > 0) {
                newSB.append(CastableDescriptionGenerator.addElement(Elements.getElementFromId(Byte.parseByte(group3))));
            }
            else {
                newSB.append(CastableDescriptionGenerator.addElement(effect));
                this.m_elementAdded = true;
            }
            lastMatchingIndex = matcher.end();
        }
        newSB.append(effectText, lastMatchingIndex, effectText.length());
        effectText = newSB.toString();
        if (!aoeAdded) {
            newSB = CastableDescriptionGenerator.m_twfFactory.createNew();
            lastMatchingIndex = 0;
            final Matcher aoeMatcher = CastableDescriptionGenerator.AOE_SHAPE_PATTERN.matcher(effectText);
            while (aoeMatcher.find()) {
                final String value2 = aoeMatcher.group(1);
                newSB.append(effectText, lastMatchingIndex, aoeMatcher.start());
                newSB.append(CastableDescriptionGenerator.m_utilityDelegate.getAreaOfEffectIcon(value2));
                lastMatchingIndex = aoeMatcher.end();
            }
            newSB.append(effectText, lastMatchingIndex, effectText.length());
            effectText = newSB.toString();
        }
        final Object[] params = this.m_effectParametersComputer.getAsObjects(effect, containerLevel);
        sb.append(this.formatEffectParams(effect, effectText, params, customFilled, writer));
        if (!minimalDescriptionMode) {
            if (!this.m_elementAdded) {
                final String elementStr = CastableDescriptionGenerator.addElement(effect);
                if (elementStr != null && elementStr.length() > 0) {
                    sb.append(" ").append(elementStr);
                }
            }
            if (!aoeAdded) {
                final String areaOfEffect = getAreaOfEffect(effect);
                if (areaOfEffect != null) {
                    sb.append(areaOfEffect);
                }
            }
        }
        if (!minimalDescriptionMode && probability < 100.0f && !probabilityAdded) {
            sb.append(" (").append(Math.round(probability)).append("%)");
        }
        return 0;
    }
    
    public static String replaceStaticPatterns(String effectText) {
        TextWidgetFormater sb = CastableDescriptionGenerator.m_twfFactory.createNew();
        int lastMatchingIndex = 0;
        final Matcher iconMatcher = CastableDescriptionGenerator.ICONS_PATTERN.matcher(effectText);
        while (iconMatcher.find()) {
            final String value = iconMatcher.group(1);
            sb.append(effectText, lastMatchingIndex, iconMatcher.start());
            CastableDescriptionGenerator.m_utilityDelegate.getSpellTargetIcon(sb, value);
            lastMatchingIndex = iconMatcher.end();
        }
        sb.append(effectText, lastMatchingIndex, effectText.length());
        effectText = sb.finishAndToString();
        sb = CastableDescriptionGenerator.m_twfFactory.createNew();
        lastMatchingIndex = 0;
        final Matcher stateMatcher = CastableDescriptionGenerator.STATE_PATTERN.matcher(effectText);
        while (stateMatcher.find()) {
            sb.append(effectText, lastMatchingIndex, stateMatcher.start());
            lastMatchingIndex = stateMatcher.end();
            final String value2 = stateMatcher.group(3);
            final Integer stateId = Integer.valueOf(value2);
            final State currentState = StateManager.getInstance().getState(stateId);
            if (currentState == null) {
                DefaultEffectWriter.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer l'\u00e9tat d'id=" + stateId));
            }
            else {
                sb.append(CastableDescriptionGenerator.getStateNameLink(currentState, (short)1, (short)1, false));
            }
        }
        sb.append(effectText, lastMatchingIndex, effectText.length());
        effectText = sb.finishAndToString();
        sb = CastableDescriptionGenerator.m_twfFactory.createNew();
        lastMatchingIndex = 0;
        final Matcher aoeMatcher = CastableDescriptionGenerator.AOE_PATTERN.matcher(effectText);
        while (aoeMatcher.find()) {
            sb.append(effectText, lastMatchingIndex, aoeMatcher.start());
            lastMatchingIndex = aoeMatcher.end();
            final String value3 = aoeMatcher.group(3);
            final Integer aoeId = Integer.valueOf(value3);
            final AbstractEffectArea areaFromId = StaticEffectAreaManager.getInstance().getAreaFromId(aoeId);
            if (areaFromId == null) {
                DefaultEffectWriter.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer l'\u00e9tat d'id=" + aoeId));
            }
            else {
                sb.append(CastableDescriptionGenerator.getEffectAreaNameLink(areaFromId, (short)0, false));
            }
        }
        sb.append(effectText, lastMatchingIndex, effectText.length());
        effectText = sb.finishAndToString();
        return effectText;
    }
    
    protected String formatEffectParams(final WakfuEffect effect, final String effectText, final Object[] params, final boolean customFilled, final ContainerWriter writer) {
        return StringFormatter.format(effectText, params);
    }
    
    public static String getAreaOfEffect(final WakfuEffect effect) {
        final String areaOfEffect = effect.getAreaOfEffect().getShape().toString();
        if (!areaOfEffect.equals(AreaOfEffectShape.SPECIAL.name()) && !areaOfEffect.equals(AreaOfEffectShape.POINT.name())) {
            return CastableDescriptionGenerator.m_utilityDelegate.getAreaOfEffectIcon(effect.getAreaOfEffect());
        }
        return "";
    }
    
    static {
        m_logger = Logger.getLogger((Class)DefaultEffectWriter.class);
    }
}
