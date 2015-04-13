package com.ankamagames.wakfu.common.game.inventory.reborn.change.cosmetics;

import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;

public interface CosmeticsInventoryChange
{
    byte[] serialize();
    
    void unSerialize(ByteBuffer p0);
    
    void compute(CosmeticsInventoryController p0);
    
    CosmeticsInventoryChangeType getType();
}
