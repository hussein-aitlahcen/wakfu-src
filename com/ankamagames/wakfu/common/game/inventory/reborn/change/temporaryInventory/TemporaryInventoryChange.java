package com.ankamagames.wakfu.common.game.inventory.reborn.change.temporaryInventory;

import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;

public interface TemporaryInventoryChange
{
    byte[] serialize();
    
    void unSerialize(ByteBuffer p0);
    
    void compute(TemporaryInventoryController p0);
    
    TemporaryInventoryChangeType getType();
}
