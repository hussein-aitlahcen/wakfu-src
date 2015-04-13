package com.ankamagames.wakfu.common.game.xp.synthlevel;

import java.util.*;

public class MutableSyntheticLevelCalc extends AbstractSyntheticLevelCalc implements MutableSyntheticLevel
{
    private final SortedMap<Short, Integer> m_levels;
    private int m_levelsCount;
    
    public MutableSyntheticLevelCalc(final short... levels) {
        super();
        this.m_levels = new TreeMap<Short, Integer>();
        for (int i = 0; i < levels.length; ++i) {
            final short level = levels[i];
            this.addLevel(level);
        }
        if (levels.length > 0) {
            this.dirty();
        }
    }
    
    @Override
    protected short[] levels() {
        final short[] levels = new short[this.m_levelsCount];
        int index = 0;
        for (final Short level : this.m_levels.keySet()) {
            for (int i = 0; i < this.m_levels.get(level); ++i) {
                levels[index++] = level;
            }
        }
        return levels;
    }
    
    @Override
    public void addLevel(final short level) {
        if (!this.m_levels.containsKey(level)) {
            this.m_levels.put(level, 1);
        }
        else {
            this.m_levels.put(level, this.m_levels.get(level) + 1);
        }
        ++this.m_levelsCount;
        this.dirty();
    }
    
    @Override
    public void removeLevel(final short level) {
        if (!this.m_levels.containsKey(level)) {
            return;
        }
        final int countOfLevel = this.m_levels.get(level);
        if (countOfLevel == 1) {
            this.m_levels.remove(level);
        }
        else {
            this.m_levels.put(level, countOfLevel - 1);
        }
        --this.m_levelsCount;
        this.dirty();
    }
    
    @Override
    protected double getMaxLevel() {
        return this.m_levels.lastKey();
    }
    
    @Override
    public int getSize() {
        return this.m_levelsCount;
    }
    
    public void reset() {
        this.m_levelsCount = 0;
        this.m_levels.clear();
    }
}
