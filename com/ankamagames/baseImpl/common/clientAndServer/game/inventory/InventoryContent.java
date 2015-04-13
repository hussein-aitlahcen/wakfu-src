package com.ankamagames.baseImpl.common.clientAndServer.game.inventory;

public interface InventoryContent
{
    void release();
    
    long getUniqueId();
    
    int getReferenceId();
    
    short getQuantity();
    
    void setQuantity(short p0);
    
    void updateQuantity(short p0);
    
    boolean canStackWith(InventoryContent p0);
    
    short getStackMaximumHeight();
    
    InventoryContent getCopy(boolean p0);
    
    InventoryContent getClone();
    
    boolean shouldBeSerialized();
}
