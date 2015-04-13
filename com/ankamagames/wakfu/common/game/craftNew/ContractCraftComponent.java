package com.ankamagames.wakfu.common.game.craftNew;

public interface ContractCraftComponent
{
    int getItemReferenceId();
    
    byte getPosition();
    
    short getQuantity();
    
    short getRequesterCraftUserQuantity();
    
    short getRequestedCraftUserQuantity();
}
