package com.ankamagames.wakfu.common.game.inventory.action;

import java.nio.*;

public interface InventoryAction
{
    int serializedSize();
    
    void serializeIn(ByteBuffer p0);
    
    void unSerializeFrom(ByteBuffer p0);
    
    InventoryActionType getType();
}
