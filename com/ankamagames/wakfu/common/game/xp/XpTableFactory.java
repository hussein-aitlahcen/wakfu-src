package com.ankamagames.wakfu.common.game.xp;

import java.util.*;

public class XpTableFactory
{
    public static XpTable createXpTable(final XpTableSpec spec) {
        return new SortedSetXpTable(spec.getTable(), spec.getMinLevel(), spec.getMaxLevel(), spec.getMinValue(), spec.getMaxValue(), spec.getFactor());
    }
    
    public static XpTableSpec fromArray(final long[] table) {
        return new TableXpTableSpec(table);
    }
    
    public static XpTableSpec fromGainSteps(final Map<Integer, Long> gainSteps) {
        return new StepsXpTableSpec(gainSteps);
    }
    
    private static long[] tableFromSteps(final SortedMap<Integer, Long> gainSteps, final int minLevel, final int maxLevel) {
        final long[] table = new long[maxLevel - minLevel + 1];
        for (int i = 0; i < table.length - 1; ++i) {
            table[i + 1] = table[i] + gainAt(minLevel + i, gainSteps);
        }
        return table;
    }
    
    private static long gainAt(final int level, final SortedMap<Integer, Long> gainSteps) {
        final SortedMap<Integer, Long> headMap = gainSteps.headMap(level + 1);
        return headMap.isEmpty() ? 0L : gainSteps.get(headMap.lastKey());
    }
    
    public abstract static class XpTableSpec
    {
        protected Integer m_minLevel;
        protected Integer m_maxLevel;
        protected Long m_minValue;
        protected Long m_maxValue;
        protected Double m_factor;
        
        int getMinLevel() {
            return (this.m_minLevel == null) ? 1 : this.m_minLevel;
        }
        
        abstract int getMaxLevel();
        
        long getMinValue() {
            return (this.m_minValue == null) ? 0L : this.m_minValue;
        }
        
        long getMaxValue() {
            return (this.m_maxValue == null) ? Long.MAX_VALUE : this.m_maxValue;
        }
        
        double getFactor() {
            return (this.m_factor == null) ? 1.0 : this.m_factor;
        }
        
        abstract long[] getTable();
        
        public XpTableSpec withMinLevel(final int minLevel) {
            this.m_minLevel = minLevel;
            return this;
        }
        
        public XpTableSpec withMaxLevel(final int maxLevel) {
            this.m_maxLevel = maxLevel;
            return this;
        }
        
        public XpTableSpec withMinValue(final long minValue) {
            this.m_minValue = minValue;
            return this;
        }
        
        public XpTableSpec withMaxValue(final long maxValue) {
            this.m_maxValue = maxValue;
            return this;
        }
        
        public XpTableSpec withFactor(final double factor) {
            this.m_factor = factor;
            return this;
        }
    }
    
    private static class TableXpTableSpec extends XpTableSpec
    {
        private final long[] m_table;
        
        TableXpTableSpec(final long[] table) {
            super();
            this.m_table = table;
        }
        
        @Override
        int getMaxLevel() {
            return (this.m_maxLevel == null) ? (this.m_minLevel + this.m_table.length - 1) : this.m_maxLevel;
        }
        
        public long[] getTable() {
            return this.m_table;
        }
    }
    
    private static class StepsXpTableSpec extends XpTableSpec
    {
        private final SortedMap<Integer, Long> m_gainSteps;
        
        StepsXpTableSpec(final Map<Integer, Long> gainSteps) {
            super();
            this.m_gainSteps = new TreeMap<Integer, Long>(gainSteps);
        }
        
        @Override
        int getMaxLevel() {
            if (this.m_maxLevel != null) {
                return this.m_maxLevel;
            }
            final int span = this.m_gainSteps.isEmpty() ? 0 : (this.m_gainSteps.lastKey() - this.m_gainSteps.firstKey());
            return this.getMinLevel() + span;
        }
        
        @Override
        long[] getTable() {
            return tableFromSteps(this.m_gainSteps, this.getMinLevel(), this.getMaxLevel());
        }
    }
}
