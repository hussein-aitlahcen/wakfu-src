package com.ankamagames.wakfu.common.game.inventory.reborn.change.quest;

import com.ankamagames.framework.kernel.core.common.*;
import org.jetbrains.annotations.*;

public enum QuestInventoryChangeType
{
    ADD_ITEM((SimpleObjectFactory<? extends QuestInventoryChange>)new AddItemFactory()), 
    REMOVE_ITEM((SimpleObjectFactory<? extends QuestInventoryChange>)new RemoveItemFactory()), 
    ITEM_QUANTITY((SimpleObjectFactory<? extends QuestInventoryChange>)new ItemQuantityFactory());
    
    public final byte idx;
    private final SimpleObjectFactory<? extends QuestInventoryChange> factory;
    
    private QuestInventoryChangeType(final SimpleObjectFactory<? extends QuestInventoryChange> fact) {
        this.idx = (byte)this.ordinal();
        this.factory = fact;
    }
    
    public QuestInventoryChange createNew() {
        return (QuestInventoryChange)this.factory.createNew();
    }
    
    @Nullable
    public static QuestInventoryChangeType fromId(final byte id) {
        final QuestInventoryChangeType[] values = values();
        for (int i = 0, length = values.length; i < length; ++i) {
            final QuestInventoryChangeType type = values[i];
            if (type.idx == id) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "PetChangeType{idx=" + this.idx + ", factory=" + this.factory + '}';
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
}
