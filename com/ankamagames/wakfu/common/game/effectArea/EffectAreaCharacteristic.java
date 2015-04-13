package com.ankamagames.wakfu.common.game.effectArea;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class EffectAreaCharacteristic extends AbstractCharacteristic
{
    protected static final Logger m_logger;
    private EffectAreaCharacteristicType m_type;
    public static final byte SIZE_IN_BYTE = 12;
    protected int m_currentValue;
    protected int m_maxValue;
    protected int m_minValue;
    protected int m_upperBound;
    protected int m_lowerBound;
    
    public EffectAreaCharacteristic(final byte typeId, final ByteBuffer serializedForm) {
        super();
        this.m_type = EffectAreaCharacteristicType.getCharacteristicTypeFromId(typeId);
        this.unserializeValues(serializedForm);
        if (this.m_type == null) {
            throw new IllegalArgumentException("Erreur dans la d\u00e9s\u00e9rialisation de la Charact\u00e9ristique, Type inconnu : " + typeId);
        }
    }
    
    public EffectAreaCharacteristic(final EffectAreaCharacteristicType type, final int lowerBound, final int upperBound) {
        super();
        this.m_type = type;
        this.setBounds(lowerBound, upperBound);
    }
    
    @Override
    public EffectAreaCharacteristicType getType() {
        return this.m_type;
    }
    
    @Override
    public int value() {
        if (this.m_currentValue > this.m_maxValue) {
            return this.max();
        }
        if (this.m_currentValue < this.m_minValue) {
            return this.min();
        }
        return this.m_currentValue;
    }
    
    @Override
    public int unboundedValue() {
        return this.m_currentValue;
    }
    
    @Override
    public int max() {
        return this.m_maxValue;
    }
    
    @Override
    public int plainMax() {
        return this.m_maxValue;
    }
    
    @Override
    public int min() {
        return this.m_minValue;
    }
    
    @Override
    public int updateMaxPercentModifier(final int value) {
        return 0;
    }
    
    @Override
    public void set(final int current) {
        this.set(current, true);
    }
    
    private void set(final int current, final boolean update) {
        final int min = (this.m_maxValue < current) ? this.m_maxValue : current;
        this.m_currentValue = ((this.m_minValue > min) ? this.m_minValue : min);
        if (update) {
            this.dispatchUpdate();
        }
    }
    
    @Override
    public int add(final int updateValue) {
        this.set(this.m_currentValue + updateValue);
        this.dispatchUpdate();
        return this.m_currentValue;
    }
    
    @Override
    public int substract(final int updateValue) {
        this.set(this.m_currentValue - updateValue);
        this.dispatchUpdate();
        return this.m_currentValue;
    }
    
    @Override
    public void setMax(final int max) {
        this.setMax(max, true);
    }
    
    private void setMax(final int max, final boolean update) {
        final int min = (this.m_upperBound < max) ? this.m_upperBound : max;
        this.m_maxValue = ((this.m_lowerBound > min) ? this.m_lowerBound : min);
        this.m_maxValue = ((this.m_maxValue > this.m_minValue) ? this.m_maxValue : this.m_minValue);
        if (this.m_currentValue > this.m_maxValue) {
            this.m_currentValue = this.m_maxValue;
        }
        if (update) {
            this.dispatchUpdate();
        }
    }
    
    @Override
    public void setMin(final int min) {
        this.setMin(min, true);
    }
    
    private void setMin(final int min, final boolean update) {
        final int max = (this.m_lowerBound > min) ? this.m_lowerBound : min;
        this.m_minValue = ((this.m_upperBound < max) ? this.m_upperBound : max);
        this.m_minValue = ((this.m_maxValue < this.m_minValue) ? this.m_maxValue : this.m_minValue);
        if (this.m_currentValue < this.m_minValue) {
            this.m_currentValue = this.m_minValue;
        }
        if (update) {
            this.dispatchUpdate();
        }
    }
    
    @Override
    public int updateMaxValue(final int modification) {
        if (this.m_maxValue == Integer.MAX_VALUE) {
            return this.m_maxValue;
        }
        this.setMax(this.m_maxValue + modification);
        return this.m_maxValue;
    }
    
    @Override
    public int updateMinValue(final int modification) {
        if (this.m_minValue == Integer.MIN_VALUE) {
            return this.m_minValue;
        }
        this.setMin(this.m_minValue + modification);
        return this.m_minValue;
    }
    
    private void setBounds(final int lowerBound, final int upperBound) {
        this.m_lowerBound = ((lowerBound < upperBound) ? lowerBound : upperBound);
        this.m_upperBound = ((lowerBound > upperBound) ? lowerBound : upperBound);
        if (this.m_maxValue > this.m_upperBound) {
            this.setMax(this.m_upperBound);
        }
        if (this.m_minValue < this.m_lowerBound) {
            this.setMin(this.m_lowerBound);
        }
    }
    
    public void serializeValues(final ByteBuffer bb) {
        bb.putInt(this.m_currentValue);
        bb.putInt(this.m_minValue);
        bb.putInt(this.m_maxValue);
    }
    
    public void unserializeValues(final ByteBuffer bb) {
        this.m_currentValue = bb.getInt();
        this.m_minValue = bb.getInt();
        this.m_maxValue = bb.getInt();
    }
    
    @Override
    public void makeDefault() {
        this.setMin(this.m_type.getDefaultMin(), false);
        this.setMax(this.m_type.getDefaultMax(), false);
        this.set(this.m_type.getDefaultValue(), false);
        this.dispatchUpdate();
    }
    
    static {
        m_logger = Logger.getLogger((Class)EffectAreaCharacteristic.class);
    }
}
