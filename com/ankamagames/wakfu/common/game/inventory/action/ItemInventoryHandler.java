package com.ankamagames.wakfu.common.game.inventory.action;

import com.ankamagames.wakfu.common.game.item.*;
import org.jetbrains.annotations.*;

public interface ItemInventoryHandler
{
    boolean canAdd(Item p0);
    
    boolean add(Item p0);
    
    boolean canAdd(Item p0, byte p1);
    
    boolean add(Item p0, byte p1);
    
    @Nullable
    Item getItem(long p0);
    
    @Nullable
    Item getItemFromPosition(byte p0);
    
    byte getPosition(long p0);
    
    boolean canRemove(long p0, short p1);
    
    boolean canRemove(Item p0);
    
    boolean remove(long p0, short p1);
    
    boolean remove(Item p0);
    
    boolean isRemote();
}
