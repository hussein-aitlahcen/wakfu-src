package com.ankamagames.wakfu.common.game.inventory.reborn;

import java.util.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.exception.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.quest.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.temporary.*;
import com.ankamagames.wakfu.common.game.inventory.reborn.definition.cosmetics.*;

public class InventoryHandler
{
    private final EnumMap<InventoryType, Inventory> m_inventories;
    
    public InventoryHandler() {
        super();
        this.m_inventories = new EnumMap<InventoryType, Inventory>(InventoryType.class);
    }
    
    public void createInventory(final InventoryType type) throws InventoryException {
        if (this.m_inventories.containsKey(type)) {
            throw new InventoryException("Un inventaire de type " + type + " est d\u00e9j\u00e0 pr\u00e9sent");
        }
        this.m_inventories.put(type, type.createInventory());
    }
    
    public Inventory getInventory(final InventoryType type) throws InventoryException {
        final Inventory inventory = this.m_inventories.get(type);
        if (inventory == null) {
            throw new InventoryException("inventaire de type " + type + "inconnu");
        }
        return inventory;
    }
    
    public void toRaw(final RawInventoryHandler raw) {
        final QuestInventoryModel questInventory = this.m_inventories.get(InventoryType.QUEST);
        final QuestInventorySerializer questSerializer = new QuestInventorySerializer(questInventory, raw.questInventory, ReferenceItemManager.getInstance());
        questSerializer.toRaw();
        final TemporaryInventoryModel temporaryInventory = this.m_inventories.get(InventoryType.TEMPORARY_INVENTORY);
        final TemporaryInventorySerializer tempInvSerializer = new TemporaryInventorySerializer(temporaryInventory, raw.temporaryInventory);
        tempInvSerializer.toRaw();
        final CosmeticsInventoryModel cosmeticsInventory = this.m_inventories.get(InventoryType.COSMETICS);
        final CosmeticsInventorySerializer cosmeticsInventorySerializer = new CosmeticsInventorySerializer(cosmeticsInventory, raw.cosmeticsInventory, ReferenceItemManager.getInstance());
        cosmeticsInventorySerializer.toRaw();
        final CosmeticsInventoryModel petCosmeticsInventory = this.m_inventories.get(InventoryType.PET_COSMETICS);
        final CosmeticsInventorySerializer petCosmeticsInventorySerializer = new CosmeticsInventorySerializer(petCosmeticsInventory, raw.petCosmeticsInventory, ReferenceItemManager.getInstance());
        petCosmeticsInventorySerializer.toRaw();
    }
    
    public void fromRaw(final RawInventoryHandler raw) {
        final QuestInventoryModel questInventory = this.m_inventories.get(InventoryType.QUEST);
        final QuestInventorySerializer serializer = new QuestInventorySerializer(questInventory, raw.questInventory, ReferenceItemManager.getInstance());
        serializer.fromRaw();
        final TemporaryInventoryModel temporaryInventory = this.m_inventories.get(InventoryType.TEMPORARY_INVENTORY);
        final TemporaryInventorySerializer tempInvSerializer = new TemporaryInventorySerializer(temporaryInventory, raw.temporaryInventory);
        tempInvSerializer.fromRaw();
        final CosmeticsInventoryModel cosmeticsInventory = this.m_inventories.get(InventoryType.COSMETICS);
        final CosmeticsInventorySerializer cosmeticsInventorySerializer = new CosmeticsInventorySerializer(cosmeticsInventory, raw.cosmeticsInventory, ReferenceItemManager.getInstance());
        cosmeticsInventorySerializer.fromRaw();
        final CosmeticsInventoryModel petCosmeticsInventory = this.m_inventories.get(InventoryType.PET_COSMETICS);
        final CosmeticsInventorySerializer petCosmeticsInventorySerializer = new CosmeticsInventorySerializer(petCosmeticsInventory, raw.petCosmeticsInventory, ReferenceItemManager.getInstance());
        petCosmeticsInventorySerializer.fromRaw();
    }
    
    public void clear() {
        final QuestInventoryModel questInventory = this.m_inventories.get(InventoryType.QUEST);
        questInventory.clear();
        final TemporaryInventoryModel temporaryInventory = this.m_inventories.get(InventoryType.TEMPORARY_INVENTORY);
        temporaryInventory.clear();
        final CosmeticsInventoryModel cosmeticsInventory = this.m_inventories.get(InventoryType.COSMETICS);
        cosmeticsInventory.clear();
        final CosmeticsInventoryModel petCosmeticsInventory = this.m_inventories.get(InventoryType.PET_COSMETICS);
        petCosmeticsInventory.clear();
    }
    
    @Override
    public String toString() {
        return "InventoryHandler{m_inventories=" + this.m_inventories.size() + '}';
    }
}
