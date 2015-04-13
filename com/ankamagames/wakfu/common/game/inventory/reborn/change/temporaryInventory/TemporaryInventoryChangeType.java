package com.ankamagames.wakfu.common.game.inventory.reborn.change.temporaryInventory;

import com.ankamagames.framework.kernel.core.common.*;
import org.jetbrains.annotations.*;

public enum TemporaryInventoryChangeType
{
    ADD_ITEM((SimpleObjectFactory<? extends TemporaryInventoryChange>)new AddItemFactory()), 
    REMOVE_ITEM((SimpleObjectFactory<? extends TemporaryInventoryChange>)new RemoveItemFactory()), 
    ITEM_QUANTITY((SimpleObjectFactory<? extends TemporaryInventoryChange>)new ItemQuantityFactory()), 
    CLEAR((SimpleObjectFactory<? extends TemporaryInventoryChange>)new ClearChangeFactory());
    
    public final byte idx;
    private final SimpleObjectFactory<? extends TemporaryInventoryChange> factory;
    
    private TemporaryInventoryChangeType(final SimpleObjectFactory<? extends TemporaryInventoryChange> fact) {
        this.idx = (byte)this.ordinal();
        this.factory = fact;
    }
    
    public TemporaryInventoryChange createNew() {
        return (TemporaryInventoryChange)this.factory.createNew();
    }
    
    @Nullable
    public static TemporaryInventoryChangeType fromId(final byte id) {
        final TemporaryInventoryChangeType[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final TemporaryInventoryChangeType type = values[i];
            if (type.idx == id) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "TemporaryInventoryChangeType{idx=" + this.idx + ", factory=" + this.factory + '}';
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
    
    private static class ItemQuantityFactory implements SimpleObjectFactory<ItemQuantityChange>
    {
        @Override
        public ItemQuantityChange createNew() {
            return new ItemQuantityChange();
        }
    }
    
    private static class ClearChangeFactory implements SimpleObjectFactory<ClearChange>
    {
        @Override
        public ClearChange createNew() {
            return ClearChange.INSTANCE;
        }
    }
}
