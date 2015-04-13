package com.ankamagames.wakfu.client.core.game.storageBox;

import com.ankamagames.wakfu.common.game.inventory.action.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.game.storageBox.guild.*;

public interface CompartmentView
{
    int getId();
    
    boolean isUnlocked();
    
    boolean isSerialized();
    
    void setInventory(ItemInventoryHandler p0);
    
    void clearInventory();
    
    void updateFields();
    
    void tryToUnlockCompartment();
    
    boolean contains(Item p0);
    
    byte getIndex();
    
    void select();
    
    GuildStorageOperationStatus executeMove(long p0, byte p1);
    
    GuildStorageOperationStatus executeAdd(Item p0, short p1, byte p2);
    
    GuildStorageOperationStatus executeRemove(long p0, short p1, long p2, byte p3);
}
