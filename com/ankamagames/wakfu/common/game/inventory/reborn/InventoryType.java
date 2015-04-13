package com.ankamagames.wakfu.common.game.inventory.reborn;

import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;

public enum InventoryType
{
    BAG((SimpleObjectFactory<? extends Inventory>)new BagFactory()), 
    QUEST((SimpleObjectFactory<? extends Inventory>)new QuestItemFactory()), 
    EQUIPMENT((SimpleObjectFactory<? extends Inventory>)new EquipmentFactory()), 
    TEMPORARY_INVENTORY((SimpleObjectFactory<? extends Inventory>)new TemporaryInventoryFactory()), 
    COSMETICS((SimpleObjectFactory<? extends Inventory>)new CosmeticsInventoryFactory()), 
    PET_COSMETICS((SimpleObjectFactory<? extends Inventory>)new CosmeticsInventoryFactory());
    
    private final SimpleObjectFactory<? extends Inventory> factory;
    
    private InventoryType(final SimpleObjectFactory<? extends Inventory> f) {
        this.factory = f;
    }
    
    public Inventory createInventory() {
        return (Inventory)this.factory.createNew();
    }
    
    private static class TemporaryInventoryFactory implements SimpleObjectFactory<TemporaryInventoryModel>
    {
        @Override
        public TemporaryInventoryModel createNew() {
            return new TemporaryInventoryModel();
        }
    }
    
    private static class CosmeticsInventoryFactory implements SimpleObjectFactory<CosmeticsInventoryModel>
    {
        @Override
        public CosmeticsInventoryModel createNew() {
            return new CosmeticsInventoryModel();
        }
    }
    
    private static class BagFactory implements SimpleObjectFactory<BagInventoryModel>
    {
        @Override
        public BagInventoryModel createNew() {
            return new BagInventoryModel();
        }
    }
    
    private static class QuestItemFactory implements SimpleObjectFactory<QuestInventoryModel>
    {
        @Override
        public QuestInventoryModel createNew() {
            return new QuestInventoryModel();
        }
    }
    
    private static class EquipmentFactory implements SimpleObjectFactory<EquipmentModel>
    {
        @Override
        public EquipmentModel createNew() {
            return new EquipmentModel();
        }
    }
}
