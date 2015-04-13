package com.ankamagames.wakfu.common.game.item.visitor.operation;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public abstract class BagOperation
{
    public static final byte ADD = 0;
    public static final byte REM = 1;
    public static final byte UP = 2;
    
    public static BagOperation createFromType(final byte opType) {
        switch (opType) {
            case 0: {
                return new AddItemOperation();
            }
            case 1: {
                return new RemoveItemOperation();
            }
            case 2: {
                return new UpdateItemOperation();
            }
            default: {
                return null;
            }
        }
    }
    
    public abstract byte getOperationType();
    
    public abstract void serialize(final ByteArray p0);
    
    public abstract void unSerialize(final ByteBuffer p0);
}
