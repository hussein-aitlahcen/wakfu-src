package com.ankamagames.wakfu.client.core.game.fight.history;

import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.wakfu.client.core.*;

public class SummonsXpGainFieldProvider implements FieldProvider
{
    public static final String CREATURE_XP_GAIN_FIELD = "creatureXpGain";
    public static final String CREATURE_LEVEL_GAIN_FIELD = "creatureLevelGain";
    public static final String PREVIOUS_XP_VALUE_FIELD = "previousXpValue";
    public static final String[] FIELDS;
    private final SymbiotInvocationCharacteristics m_invocationCharacteristics;
    private final long m_xpGained;
    private final int m_levelGained;
    
    public SummonsXpGainFieldProvider(final byte index, final long xpGained, final int levelGained) {
        super();
        final Symbiot symbiot = WakfuGameEntity.getInstance().getLocalPlayer().getSymbiot();
        this.m_invocationCharacteristics = (SymbiotInvocationCharacteristics)symbiot.getCreatureParametersFromIndex(index);
        this.m_xpGained = xpGained;
        this.m_levelGained = levelGained;
    }
    
    @Override
    public String[] getFields() {
        return SummonsXpGainFieldProvider.FIELDS;
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equals("creatureXpGain")) {
            return this.xpGainField();
        }
        if (fieldName.equals("creatureLevelGain")) {
            return this.levelGainField();
        }
        if (fieldName.equals("previousXpValue")) {
            return (this.m_levelGained > 0) ? null : this.m_invocationCharacteristics.getXpTable().getPercentageInLevel(this.m_invocationCharacteristics.getLevel(), this.m_invocationCharacteristics.getXp() - this.m_xpGained);
        }
        return this.m_invocationCharacteristics.getFieldValue(fieldName);
    }
    
    private Object levelGainField() {
        return (this.m_levelGained > 0) ? WakfuTranslator.getInstance().getString("levelGain", this.m_levelGained) : null;
    }
    
    private Object xpGainField() {
        if (this.m_xpGained >= 0L) {
            return '+' + WakfuTranslator.getInstance().getString("xpGain", this.m_xpGained);
        }
        return WakfuTranslator.getInstance().getString("xpGain", this.m_xpGained);
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public int getLevelGained() {
        return this.m_levelGained;
    }
    
    static {
        FIELDS = new String[] { "creatureXpGain", "creatureLevelGain", "previousXpValue" };
    }
}
