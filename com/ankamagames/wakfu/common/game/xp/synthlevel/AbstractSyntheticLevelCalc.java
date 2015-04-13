package com.ankamagames.wakfu.common.game.xp.synthlevel;

public abstract class AbstractSyntheticLevelCalc implements SyntheticLevel
{
    private double m_syntheticLevel;
    private double m_averageLevel;
    private double m_syntheticNumber;
    private int m_levelMax;
    private int m_totalLevel;
    private boolean m_dirty;
    private double m_valueSum;
    
    private static double toValue(final double level) {
        return Math.pow(20.0 + level, 7.0);
    }
    
    private static double toLevel(final double value) {
        return Math.pow(value, 0.14285714285714285) - 20.0;
    }
    
    protected final void dirty() {
        this.m_dirty = true;
    }
    
    private void calc() {
        if (!this.m_dirty) {
            return;
        }
        this.m_dirty = false;
        final short[] levels = this.levels();
        if (levels.length == 0) {
            this.reset();
            return;
        }
        if (levels.length == 1) {
            this.m_syntheticLevel = levels[0];
            this.m_averageLevel = levels[0];
            this.m_syntheticNumber = 1.0;
            this.m_valueSum = toValue(levels[0]);
            this.m_totalLevel = levels[0];
            this.m_levelMax = levels[0];
            return;
        }
        double valueSum = 0.0;
        this.m_totalLevel = 0;
        this.m_levelMax = 0;
        for (final short level : levels) {
            valueSum += toValue(level);
            this.m_totalLevel += level;
            if (level > this.m_levelMax) {
                this.m_levelMax = level;
            }
        }
        this.m_syntheticLevel = toLevel(valueSum);
        this.m_averageLevel = toLevel(valueSum / this.getSize());
        this.m_syntheticNumber = valueSum / toValue(this.getMaxLevel());
        this.m_valueSum = valueSum;
    }
    
    private void reset() {
        this.m_syntheticLevel = 0.0;
        this.m_syntheticNumber = 0.0;
        this.m_averageLevel = 0.0;
    }
    
    protected abstract short[] levels();
    
    protected abstract double getMaxLevel();
    
    @Override
    public abstract int getSize();
    
    @Override
    public double getSyntheticLevel() {
        this.calc();
        return this.m_syntheticLevel;
    }
    
    @Override
    public double getAverageLevel() {
        this.calc();
        return this.m_averageLevel;
    }
    
    @Override
    public double getSyntheticNumber() {
        this.calc();
        return this.m_syntheticNumber;
    }
    
    @Override
    public double getContribution(final short level) {
        this.calc();
        return level / this.m_levelMax;
    }
    
    @Override
    public int getTotalLevel() {
        this.calc();
        return this.m_totalLevel;
    }
    
    public static double quantityNeededForSyntheticLevel(final double individualLevel, final double syntheticLevel) {
        return toValue(syntheticLevel) / toValue(individualLevel);
    }
}
