package com.ankamagames.wakfu.common.game.fighter;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public class FighterCharacteristic extends AbstractCharacteristic
{
    protected static final Logger m_logger;
    private final FighterCharacteristicType m_type;
    private List<FighterCharacteristicProcedure> m_procedures;
    private boolean m_currentValueLocked;
    public static final byte SIZE_IN_BYTE = 12;
    protected int m_currentValue;
    protected int m_maxValue;
    protected int m_minValue;
    protected int m_upperBound;
    protected int m_lowerBound;
    protected int m_maxPercentModifier;
    
    public FighterCharacteristic(final FighterCharacteristicType type, final int lowerBound, final int upperBound) {
        super();
        this.m_type = type;
        this.setBounds(lowerBound, upperBound);
    }
    
    @Override
    public FighterCharacteristicType getType() {
        return this.m_type;
    }
    
    public void addProcedure(final FighterCharacteristicProcedure procedure) {
        if (procedure == null) {
            return;
        }
        if (this.m_procedures == null) {
            (this.m_procedures = new ArrayList<FighterCharacteristicProcedure>()).add(procedure);
        }
        else if (!this.m_procedures.contains(procedure)) {
            this.m_procedures.add(procedure);
        }
    }
    
    public boolean removeProcedure(final FighterCharacteristicProcedure procedure) {
        return this.m_procedures != null && this.m_procedures.remove(procedure);
    }
    
    public boolean containsProcedure(final FighterCharacteristicProcedure procedure) {
        return this.m_procedures != null && this.m_procedures.contains(procedure);
    }
    
    private void executeProcedures(final FighterCharacteristicEvent event, final int value) {
        if (this.m_procedures != null && !this.m_procedures.isEmpty()) {
            for (final FighterCharacteristicProcedure procedure : this.m_procedures) {
                procedure.execute(event, value);
            }
        }
    }
    
    @Override
    public int unboundedValue() {
        return this.m_currentValue;
    }
    
    @Override
    public int value() {
        if (this.m_currentValue > this.max()) {
            return this.max();
        }
        if (this.m_currentValue < this.min()) {
            return this.min();
        }
        return this.m_currentValue;
    }
    
    public int unboundedmax() {
        return this.m_maxValue;
    }
    
    public int unboundedmin() {
        return this.m_minValue;
    }
    
    @Override
    public int max() {
        final int max = this.m_maxValue + this.m_maxValue * this.m_maxPercentModifier / 100;
        return MathHelper.clamp(max, this.m_lowerBound, this.m_upperBound);
    }
    
    @Override
    public int plainMax() {
        return this.m_maxValue;
    }
    
    @Override
    public int min() {
        return MathHelper.clamp(this.m_minValue, this.m_lowerBound, this.m_upperBound);
    }
    
    @Override
    public int updateMaxPercentModifier(final int value) {
        final int oldValue = this.m_maxPercentModifier;
        this.m_maxPercentModifier += value;
        this.dispatchUpdate();
        return this.m_maxPercentModifier - oldValue;
    }
    
    public int getLowerBound() {
        return this.m_lowerBound;
    }
    
    public int getUpperBound() {
        return this.m_upperBound;
    }
    
    @Override
    public void set(final int current) {
        this.set(current, true);
    }
    
    private void set(final int current, final boolean update) {
        if (this.m_currentValueLocked) {
            return;
        }
        final int oldValue = this.value();
        if (this.m_type != null && this.m_type.isCurrentCanExceedMax()) {
            this.m_currentValue = MathHelper.clamp(current, this.min(), this.m_upperBound);
        }
        else {
            this.m_currentValue = MathHelper.clamp(current, this.min(), this.max());
        }
        if (oldValue != this.value()) {
            if (update) {
                this.dispatchUpdate();
            }
            if (this.value() > oldValue) {
                this.executeProcedures(FighterCharacteristicEvent.VALUE_ADDED, this.value() - oldValue);
            }
            else {
                this.executeProcedures(FighterCharacteristicEvent.VALUE_SUBSTRACTED, oldValue - this.value());
            }
        }
    }
    
    public void set(final FighterCharacteristic characToCopy) {
        if (characToCopy.m_type != this.m_type) {
            FighterCharacteristic.m_logger.error((Object)("Trying to assign a " + characToCopy.m_type + " charac to a " + this.m_type + " one."));
            return;
        }
        this.setLowerBound(characToCopy.getLowerBound());
        this.setUpperBound(characToCopy.getUpperBound());
        this.setMax(characToCopy.m_maxValue);
        this.m_maxPercentModifier = characToCopy.m_maxPercentModifier;
        this.setMin(characToCopy.min());
        this.set(characToCopy.value());
    }
    
    @Override
    public int add(final int updateValue) {
        if (this.m_currentValueLocked) {
            return 0;
        }
        final int oldValue = this.m_currentValue;
        final long maxToAdd = this.max() - this.value();
        final int previonnalValue = this.m_currentValue + updateValue;
        if (updateValue > maxToAdd) {
            this.set(this.max());
        }
        else {
            this.set(previonnalValue);
        }
        return this.m_currentValue - oldValue;
    }
    
    @Override
    public int substract(final int updateValue) {
        if (this.m_currentValueLocked) {
            return 0;
        }
        final int oldValue = this.m_currentValue;
        final long maxToSubstract = this.value() - this.min();
        if (updateValue > maxToSubstract) {
            this.set(this.min());
        }
        else {
            this.set(this.m_currentValue - updateValue);
        }
        return oldValue - this.m_currentValue;
    }
    
    @Override
    public void setMax(final int max) {
        this.setMax(max, true);
    }
    
    private void setMax(final int max, final boolean update) {
        this.m_maxValue = max;
        this.m_maxValue = ((this.m_maxValue > this.m_minValue) ? this.m_maxValue : this.m_minValue);
        if (!this.m_currentValueLocked && this.m_currentValue > this.max() && (this.m_type == null || !this.m_type.isCurrentCanExceedMax())) {
            this.m_currentValue = this.max();
        }
        this.executeProcedures(FighterCharacteristicEvent.MAX_SET, this.max());
        if (update) {
            this.dispatchUpdate();
        }
    }
    
    @Override
    public void setMin(final int min) {
        this.setMin(min, true);
    }
    
    private void setMin(final int min, final boolean update) {
        this.m_minValue = min;
        this.m_minValue = ((this.m_maxValue < this.m_minValue) ? this.m_maxValue : this.m_minValue);
        if (!this.m_currentValueLocked && this.m_currentValue < this.min()) {
            this.m_currentValue = this.min();
        }
        if (update) {
            this.dispatchUpdate();
        }
        this.executeProcedures(FighterCharacteristicEvent.MIN_SET, this.min());
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
    }
    
    public void setUpperBound(final int upperBound) {
        this.m_upperBound = Math.max(upperBound, this.m_lowerBound);
    }
    
    public void setLowerBound(final int lowerBound) {
        this.m_lowerBound = Math.min(lowerBound, this.m_upperBound);
    }
    
    @Override
    public void makeDefault() {
        this.setMin(this.m_type.getDefaultMin(), false);
        this.setMax(this.m_type.getDefaultMax(), false);
        this.m_currentValue = 0;
        this.m_maxPercentModifier = 0;
        this.set(this.m_type.getDefaultValue(), false);
        if (this.m_procedures != null) {
            this.m_procedures.clear();
        }
        this.dispatchUpdate();
    }
    
    public void copyMinMaxAndValue(final FighterCharacteristic srcCharac) {
        this.setMax(srcCharac.m_maxValue);
        this.setMin(srcCharac.m_minValue);
        this.set(srcCharac.m_currentValue);
    }
    
    public void copyMinMaxValueAndMaxPercentModificator(final FighterCharacteristic srcCharac) {
        this.setMax(srcCharac.m_maxValue);
        this.setMin(srcCharac.m_minValue);
        this.set(srcCharac.m_currentValue);
        this.m_maxPercentModifier = srcCharac.m_maxPercentModifier;
    }
    
    public void copyMinMaxValueAndMaxPercentModificator(final FighterCharacteristic srcCharac, final int percentToCopy) {
        this.setMax(srcCharac.m_maxValue * percentToCopy / 100);
        this.setMin(srcCharac.m_minValue * percentToCopy / 100);
        this.set(srcCharac.m_currentValue * percentToCopy / 100);
        this.m_maxPercentModifier = srcCharac.m_maxPercentModifier * percentToCopy / 100;
    }
    
    public void lockCurrentValue() {
        this.m_currentValueLocked = true;
    }
    
    public void unlockCurrentValue() {
        this.m_currentValueLocked = false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)FighterCharacteristic.class);
    }
}
