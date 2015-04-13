package com.ankamagames.wakfu.common.game.inventory.reborn.change.temporaryInventory;

import java.nio.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.*;

class ClearChange implements TemporaryInventoryChange
{
    public static final ClearChange INSTANCE;
    private static final byte[] SERIALIZED;
    
    @Override
    public byte[] serialize() {
        return ClearChange.SERIALIZED;
    }
    
    @Override
    public void unSerialize(final ByteBuffer bb) {
    }
    
    @Override
    public void compute(final TemporaryInventoryController controller) {
        controller.clear();
    }
    
    @Override
    public TemporaryInventoryChangeType getType() {
        return TemporaryInventoryChangeType.CLEAR;
    }
    
    static {
        INSTANCE = new ClearChange();
        SERIALIZED = new byte[0];
    }
}
