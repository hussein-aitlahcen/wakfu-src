package com.ankamagames.wakfu.common.game.xp;

import java.util.*;

public class LevelableComparator implements Comparator<Levelable>
{
    public static final LevelableComparator m_instance;
    
    @Override
    public int compare(final Levelable left, final Levelable right) {
        if (left.getLevel() < right.getLevel()) {
            return 1;
        }
        if (left.getLevel() > right.getLevel()) {
            return -1;
        }
        return 0;
    }
    
    public static LevelableComparator getInstance() {
        return LevelableComparator.m_instance;
    }
    
    static {
        m_instance = new LevelableComparator();
    }
}
