package com.ankamagames.wakfu.common.game.descriptionGenerator.containerWriter;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.common.game.descriptionGenerator.*;
import com.ankamagames.framework.java.util.*;
import java.text.*;
import com.ankamagames.framework.kernel.core.maths.*;
import java.util.regex.*;
import org.jetbrains.annotations.*;

public class DefaultContainerWriter<EC extends EffectContainer<WakfuEffect>> implements ContainerWriter
{
    private static final Logger m_logger;
    protected EC m_container;
    protected int m_id;
    protected short m_level;
    protected boolean m_useAutomaticDescription;
    protected ArrayList<String> m_validCriterion;
    protected ArrayList<String> m_invalidCriterion;
    protected CastableDescriptionGenerator.DescriptionMode m_descriptionMode;
    protected int m_freeDescriptionTranslationType;
    protected boolean m_isInMinimalDescriptionMode;
    protected boolean m_displayOnlySubEffects;
    
    public DefaultContainerWriter(final EC container, final int id, final short level) {
        super();
        this.m_container = container;
        this.m_id = id;
        this.m_level = level;
        this.m_useAutomaticDescription = true;
        final ArrayList<String> list = null;
        this.m_invalidCriterion = list;
        this.m_validCriterion = list;
        this.m_descriptionMode = CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY;
        this.m_freeDescriptionTranslationType = 0;
        this.m_isInMinimalDescriptionMode = false;
        this.m_displayOnlySubEffects = false;
    }
    
    public DefaultContainerWriter(final EC container, final int id, final short level, final boolean useAutomaticDescription, final ArrayList<String> validCriterion, final ArrayList<String> invalidCriterion, final CastableDescriptionGenerator.DescriptionMode descriptionMode, final int freeDescriptionTranslationType) {
        super();
        this.m_container = container;
        this.m_id = id;
        this.m_level = level;
        this.m_useAutomaticDescription = useAutomaticDescription;
        this.m_validCriterion = validCriterion;
        this.m_invalidCriterion = invalidCriterion;
        this.m_descriptionMode = descriptionMode;
        this.m_freeDescriptionTranslationType = freeDescriptionTranslationType;
        this.m_isInMinimalDescriptionMode = false;
        this.m_displayOnlySubEffects = false;
    }
    
    @Override
    public ArrayList<String> writeContainer() {
        final ArrayList<String> description = new ArrayList<String>();
        final short containerLevel = this.getLevel();
        final CastableDescriptionGenerator.DescriptionMode descriptionMode = this.getDescriptionMode();
        final int freeDescriptionTranslationType = this.getFreeDescriptionTranslationType();
        final int id = this.getId();
        this.onContainerBegin(description);
        if (this.isUseAutomaticDescription()) {
            final EC container = this.getContainer();
            if (container != null) {
                boolean emptyEffectString = true;
                boolean firstCriticalEffect = true;
                for (final WakfuEffect effect : container) {
                    if (effect == null) {
                        DefaultContainerWriter.m_logger.error((Object)"Effet null", (Throwable)new Exception());
                    }
                    else {
                        final float probability = effect.getExecutionProbability(containerLevel);
                        if (probability < 0.0f) {
                            continue;
                        }
                        if (containerLevel > effect.getContainerMaxLevel()) {
                            continue;
                        }
                        if (containerLevel < effect.getContainerMinLevel()) {
                            continue;
                        }
                        if (effect.checkFlags(1L) && descriptionMode.equals(CastableDescriptionGenerator.DescriptionMode.EFFECTS_ONLY)) {
                            continue;
                        }
                        if (!effect.checkFlags(1L) && descriptionMode.equals(CastableDescriptionGenerator.DescriptionMode.CRITICALS_ONLY)) {
                            continue;
                        }
                        if (effect.checkFlags(1L) && firstCriticalEffect) {
                            firstCriticalEffect = false;
                            if (descriptionMode.equals(CastableDescriptionGenerator.DescriptionMode.ALL)) {
                                description.add(CastableDescriptionGenerator.m_twfFactory.createNew().b().append(CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.CRITICAL))._b().finishAndToString());
                            }
                        }
                        final TextWidgetFormater currentStringBuilder = CastableDescriptionGenerator.m_twfFactory.createNew();
                        final EffectWriter effectWriter = CastableDescriptionGenerator.getEffectWriter(effect.getActionId());
                        final int descriptorResult = effectWriter.writeEffect(currentStringBuilder, effect, this);
                        if (descriptorResult == -1) {
                            continue;
                        }
                        emptyEffectString = false;
                        String desc = this.onEffectAdded(currentStringBuilder.finishAndToString(), effect);
                        desc = parseArithmetic(desc);
                        if (desc.length() <= 0) {
                            continue;
                        }
                        final String[] strings = desc.split("\n");
                        Collections.addAll(description, strings);
                    }
                }
                if (emptyEffectString) {
                    return description;
                }
                if (CastableDescriptionGenerator.m_translator.containsContentKey(freeDescriptionTranslationType, id)) {
                    final String freeText = CastableDescriptionGenerator.m_translator.getString(freeDescriptionTranslationType, id, new Object[0]);
                    if (freeText.length() > 0) {
                        description.add(freeText);
                    }
                }
            }
        }
        else if (CastableDescriptionGenerator.m_translator.containsContentKey(freeDescriptionTranslationType, id)) {
            description.add(CastableDescriptionGenerator.m_translator.getString(freeDescriptionTranslationType, id, new Object[0]));
        }
        if ((this.m_validCriterion != null || this.m_invalidCriterion != null) && description.size() != 0 && descriptionMode.equals(CastableDescriptionGenerator.DescriptionMode.ALL)) {
            description.add(CastableDescriptionGenerator.m_twfFactory.createNew().b().append(CastableDescriptionGenerator.m_translator.getString(CastableDescriptionGenerator.REQUIREMENTS))._b().finishAndToString());
            if (this.m_validCriterion != null) {
                description.addAll(this.m_validCriterion);
            }
            if (this.m_invalidCriterion != null) {
                for (final String crit : this.m_invalidCriterion) {
                    description.add(CastableDescriptionGenerator.m_utilityDelegate.formatInvalidCriterion(CastableDescriptionGenerator.m_twfFactory.createNew(), crit).finishAndToString());
                }
            }
        }
        this.onContainerEnd(description);
        return description;
    }
    
    public static String parseArithmetic(final String desc) {
        final TextWidgetFormater newSB = CastableDescriptionGenerator.m_twfFactory.createNew();
        int lastMatchingIndex = 0;
        final Matcher arithmeticMatcher = CastableDescriptionGenerator.ARITHMETIC_REG_PATTERN.matcher(desc);
        try {
            while (arithmeticMatcher.find()) {
                int decimals = 0;
                final String group1 = arithmeticMatcher.group(1);
                if (arithmeticMatcher.groupCount() == 4 && group1 != null) {
                    decimals = PrimitiveConverter.getInteger(group1.charAt(0));
                }
                final Number formatedNumber = NumberFormat.getInstance(CastableDescriptionGenerator.m_translator.getLanguage().getLocale()).parse(arithmeticMatcher.group(2));
                float op1 = formatedNumber.floatValue();
                final String operand = arithmeticMatcher.group(4);
                final Matcher operandMatcher = CastableDescriptionGenerator.OPERAND_REG_PATTERN.matcher(operand);
                while (operandMatcher.find()) {
                    final char operator = operandMatcher.group(1).charAt(0);
                    final float op2 = Float.parseFloat(operandMatcher.group(2));
                    switch (operator) {
                        case '+': {
                            op1 += op2;
                            continue;
                        }
                        case '-': {
                            op1 -= op2;
                            continue;
                        }
                        case '*': {
                            op1 *= op2;
                            continue;
                        }
                        case '/': {
                            op1 /= op2;
                            continue;
                        }
                    }
                }
                newSB.append(desc, lastMatchingIndex, arithmeticMatcher.start());
                if (decimals == 0) {
                    newSB.append(MathHelper.fastRound(op1));
                }
                else if (decimals > 0) {
                    newSB.append(MathHelper.round(op1, decimals));
                }
                lastMatchingIndex = arithmeticMatcher.end();
            }
        }
        catch (Exception e) {
            return "Error";
        }
        newSB.append(desc, lastMatchingIndex, desc.length());
        return newSB.toString();
    }
    
    @Override
    public void onContainerBegin(@NotNull final ArrayList<String> descriptions) {
    }
    
    @Override
    public void onContainerEnd(@NotNull final ArrayList<String> descriptions) {
    }
    
    @Override
    public String onEffectAdded(@NotNull final String effectDescription, @NotNull final WakfuEffect effect) {
        return effectDescription;
    }
    
    @Override
    public int getId() {
        return this.m_id;
    }
    
    @Override
    public EC getContainer() {
        return this.m_container;
    }
    
    @Override
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public boolean isUseAutomaticDescription() {
        return this.m_useAutomaticDescription;
    }
    
    @Override
    public boolean isEffectDisplayable(final WakfuEffect effect) {
        return (effect.isDisplayInSpellDescription() || CastableDescriptionGenerator.m_utilityDelegate.forceDisplayState()) && effect.getContainerMinLevel() <= this.m_level && effect.getContainerMaxLevel() >= this.m_level;
    }
    
    @Override
    public ArrayList<String> getValidCriterion() {
        return this.m_validCriterion;
    }
    
    @Override
    public ArrayList<String> getInvalidCriterion() {
        return this.m_invalidCriterion;
    }
    
    @Override
    public CastableDescriptionGenerator.DescriptionMode getDescriptionMode() {
        return this.m_descriptionMode;
    }
    
    @Override
    public int getFreeDescriptionTranslationType() {
        return this.m_freeDescriptionTranslationType;
    }
    
    @Override
    public void setMinimalDescriptionMode(final boolean minimal) {
        this.m_isInMinimalDescriptionMode = minimal;
    }
    
    @Override
    public boolean isInMinimalDescriptionMode() {
        return this.m_isInMinimalDescriptionMode;
    }
    
    @Override
    public boolean displayOnlySubEffects() {
        return this.m_displayOnlySubEffects;
    }
    
    @Override
    public void setDisplayOnlySubEffects(final boolean display) {
        this.m_displayOnlySubEffects = display;
    }
    
    static {
        m_logger = Logger.getLogger((Class)DefaultContainerWriter.class);
    }
}
