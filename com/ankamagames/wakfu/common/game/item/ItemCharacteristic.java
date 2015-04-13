package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class ItemCharacteristic extends AbstractCharacteristic
{
    protected static final Logger m_logger;
    private ItemCharacteristicType m_type;
    public static final byte SIZE_IN_BYTE = 12;
    protected int m_currentValue;
    protected int m_maxValue;
    protected int m_minValue;
    protected int m_upperBound;
    protected int m_lowerBound;
    
    public ItemCharacteristic(final byte typeId, final ByteBuffer serializedForm) {
        super();
        this.m_type = ItemCharacteristicType.getCharacteristicTypeFromId(typeId);
        this.unserializeValues(serializedForm);
        if (this.m_type == null) {
            throw new IllegalArgumentException("Erreur dans la d\u00e9s\u00e9rialisation de la Charact\u00e9ristique, Type inconnu : " + typeId);
        }
    }
    
    public ItemCharacteristic(final ItemCharacteristicType type, final int lowerBound, final int upperBound) {
        super();
        this.m_type = type;
        this.setBounds(lowerBound, upperBound);
        if (type != null) {
            this.m_currentValue = type.getDefaultValue();
        }
    }
    
    @Override
    public ItemCharacteristicType getType() {
        return this.m_type;
    }
    
    @Override
    public int unboundedValue() {
        return this.m_currentValue;
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
        this.m_currentValue = Math.max(this.m_minValue, Math.min(this.m_maxValue, current));
        if (update) {
            this.dispatchUpdate();
        }
    }
    
    @Override
    public int add(final int updateValue) {
        this.set(this.m_currentValue + updateValue);
        return this.m_currentValue;
    }
    
    @Override
    public int substract(final int updateValue) {
        this.set(this.m_currentValue - updateValue);
        return this.m_currentValue;
    }
    
    @Override
    public void setMax(final int max) {
        this.setMax(max, true);
    }
    
    private void setMax(final int max, final boolean update) {
        this.m_maxValue = Math.max(this.m_lowerBound, Math.min(this.m_upperBound, max));
        this.m_maxValue = Math.max(this.m_maxValue, this.m_minValue);
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
        this.m_minValue = Math.min(this.m_upperBound, Math.max(this.m_lowerBound, min));
        this.m_minValue = Math.min(this.m_maxValue, this.m_minValue);
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
        this.m_lowerBound = Math.min(lowerBound, upperBound);
        this.m_upperBound = Math.max(lowerBound, upperBound);
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
        m_logger = Logger.getLogger((Class)ItemCharacteristic.class);
    }
}
