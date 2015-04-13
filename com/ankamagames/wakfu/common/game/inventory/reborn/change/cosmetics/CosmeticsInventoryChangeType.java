package com.ankamagames.wakfu.common.game.inventory.reborn.change.cosmetics;

import com.ankamagames.framework.kernel.core.common.*;
import org.jetbrains.annotations.*;

public enum CosmeticsInventoryChangeType
{
    ADD_ITEM((SimpleObjectFactory<? extends CosmeticsInventoryChange>)new AddItemFactory()), 
    REMOVE_ITEM((SimpleObjectFactory<? extends CosmeticsInventoryChange>)new RemoveItemFactory());
    
    public final byte idx;
    private final SimpleObjectFactory<? extends CosmeticsInventoryChange> factory;
    
    private CosmeticsInventoryChangeType(final SimpleObjectFactory<? extends CosmeticsInventoryChange> fact) {
        this.idx = (byte)this.ordinal();
        this.factory = fact;
    }
    
    public CosmeticsInventoryChange createNew() {
        return (CosmeticsInventoryChange)this.factory.createNew();
    }
    
    @Nullable
    public static CosmeticsInventoryChangeType fromId(final byte id) {
        final CosmeticsInventoryChangeType[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final CosmeticsInventoryChangeType type = values[i];
            if (type.idx == id) {
                return type;
            }
        }
        return null;
    }
    
    private static class AddItemFactory implements SimpleObjectFactory<AddItemChange>
    {
        @Override
        public AddItemChange createNew() {
            return new AddItemChange();
        }
    }
    
    private static class RemoveItemFactory implements SimpleObjectFactory<RemoveItemChange>
    {
        @Override
        public RemoveItemChange createNew() {
            return new RemoveItemChange();
        }
    }
}
