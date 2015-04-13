package com.ankamagames.wakfu.common.game.world.havenWorld.items;

import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;
import com.ankamagames.framework.kernel.core.maths.*;

public interface ModificationItem<T extends HavenWorldCatalogEntry>
{
    public static final ModificationItem[] EMPTY = new ModificationItem[0];
    
    ItemLayer getLayer();
    
    Point2i getCell();
    
    T getCatalogEntry();
    
    long getUid();
    
    public static class Helper
    {
        public static boolean equals(ModificationItem item, Object o) {
            ModificationItem that;
            if (item == o) {
                return true;
            }
            if (o == null || item.getClass() != o.getClass()) {
                return false;
            }
            that = (ModificationItem)o;
            return item.getUid() == that.getUid();
        }
        
        public static int hashCode(ModificationItem item) {
            return (int)item.getUid();
        }
    }
}
