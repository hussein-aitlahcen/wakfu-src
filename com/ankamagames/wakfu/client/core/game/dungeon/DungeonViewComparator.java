package com.ankamagames.wakfu.client.core.game.dungeon;

import java.util.*;

public class DungeonViewComparator implements Comparator<DungeonView>
{
    public static final DungeonViewComparator INSTANCE;
    
    @Override
    public int compare(final DungeonView o1, final DungeonView o2) {
        final int deltaLevel = o1.getMinLevel() - o2.getMinLevel();
        if (deltaLevel != 0) {
            return deltaLevel;
        }
        return o1.getInstanceId() - o2.getInstanceId();
    }
    
    static {
        INSTANCE = new DungeonViewComparator();
    }
}
