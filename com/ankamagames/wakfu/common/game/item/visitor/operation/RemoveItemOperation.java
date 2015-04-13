package com.ankamagames.wakfu.common.game.item.visitor.operation;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class RemoveItemOperation extends BagOperation
{
    @Override
    public byte getOperationType() {
        return 1;
    }
    
    @Override
    public void serialize(final ByteArray buff) {
    }
    
    @Override
    public void unSerialize(final ByteBuffer buffer) {
    }
}
