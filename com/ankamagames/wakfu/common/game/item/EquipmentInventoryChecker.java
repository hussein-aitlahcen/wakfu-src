package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class EquipmentInventoryChecker implements InventoryContentChecker<Item>
{
    private static final EquipmentInventoryChecker m_instance;
    
    public static EquipmentInventoryChecker getInstance() {
        return EquipmentInventoryChecker.m_instance;
    }
    
    @Override
    public int canAddItem(final Inventory inventory, final Item item) {
        return -8;
    }
    
    @Override
    public int canAddItem(final Inventory inventory, final Item item, final short position) {
        if (!(inventory instanceof ItemEquipment)) {
            return -6;
        }
        final ItemEquipment equip = (ItemEquipment)inventory;
        if (!equip.isPositionFree(position)) {
            return -9;
        }
        if (!item.getReferenceItem().getItemType().isEquipmentPositionValid(EquipmentPosition.fromId((byte)position))) {
            return -5;
        }
        if (equip.isPositionLocked((byte)position)) {
            return -8;
        }
        if (!petCanBeEquipped(item)) {
            return -8;
        }
        if (item.getReferenceItem().hasRandomElementEffect() && item.needRollRandomElementsEffect()) {
            return -11;
        }
        if (!checkExclusiveEquipmentProperty(equip, null, item)) {
            return -7;
        }
        final EquipmentPosition[] disabledPosition = item.getReferenceItem().getItemType().getLinkedPositions();
        if (disabledPosition != null) {
            for (final EquipmentPosition pos : disabledPosition) {
                if (!equip.isPositionFree(pos.getId())) {
                    final Item curItem = ((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos.getId());
                    if (curItem != null) {
                        return -9;
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public int canReplaceItem(final Inventory inventory, final Item oldItem, final Item newItem) {
        if (!(inventory instanceof ItemEquipment)) {
            return -6;
        }
        final ItemEquipment equip = (ItemEquipment)inventory;
        final short position = ((ArrayInventoryWithoutCheck<Item, R>)equip).getPosition(oldItem);
        if (!newItem.getReferenceItem().getItemType().isEquipmentPositionValid(EquipmentPosition.fromId((byte)position))) {
            return -5;
        }
        if (equip.isPositionLocked((byte)position)) {
            return -8;
        }
        if (!petCanBeEquipped(newItem)) {
            return -8;
        }
        if (newItem.getReferenceItem().hasRandomElementEffect() && newItem.needRollRandomElementsEffect()) {
            return -11;
        }
        if (!checkExclusiveEquipmentProperty(equip, oldItem, newItem)) {
            return -7;
        }
        final EquipmentPosition[] disabledPosition = newItem.getReferenceItem().getItemType().getLinkedPositions();
        if (disabledPosition != null) {
            for (final EquipmentPosition pos : disabledPosition) {
                if (!equip.isPositionFree(pos.getId())) {
                    final Item item = ((ArrayInventoryWithoutCheck<Item, R>)equip).getFromPosition(pos.getId());
                    if (item != null && item.isActive() && this.canRemoveItem(equip, item) < 0) {
                        return -9;
                    }
                }
            }
        }
        return 0;
    }
    
    @Override
    public int canRemoveItem(final Inventory inventory, final Item item) {
        if (!(inventory instanceof ItemEquipment)) {
            return -6;
        }
        final ItemEquipment equip = (ItemEquipment)inventory;
        final short position = ((ArrayInventoryWithoutCheck<Item, R>)equip).getPosition(item);
        if (equip.isPositionLocked((byte)position)) {
            return -8;
        }
        return item.isActive() ? 0 : -8;
    }
    
    @Override
    public boolean checkCriterion(final Item item, final EffectUser player, final EffectContext context) {
        return checkItemLevelValidity(item, player) && petCanBeEquipped(item) && this.checkCompanionCriterion(item, player) && (item.getReferenceItem().getCriterion(ActionsOnItem.EQUIP) == null || item.getReferenceItem().getCriterion(ActionsOnItem.EQUIP).isValid(player, player, item, context));
    }
    
    private boolean checkCompanionCriterion(final Item item, final EffectUser player) {
        return player.getId() != 0L || !item.hasBind() || !item.getBind().getType().isCharacter();
    }
    
    @Override
    public boolean checkCriterion(final Inventory<Item> inventory, final EffectUser player, final EffectContext context) {
        for (final Item item : inventory) {
            if (item.isActive()) {
                if (!checkItemLevelValidity(item, player)) {
                    return false;
                }
                if (!petCanBeEquipped(item)) {
                    return false;
                }
                if (item.getReferenceItem().getCriterion(ActionsOnItem.EQUIP) != null && !item.getReferenceItem().getCriterion(ActionsOnItem.EQUIP).isValid(player, player, item, context)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    public static boolean checkItemLevelValidity(final AbstractReferenceItem item, final EffectUser player) {
        if (item == null) {
            return false;
        }
        if (item.hasPet() || item.hasItemProperty(ItemProperty.ADMIN_XP)) {
            return true;
        }
        int equipmentKnowledge = 0;
        if (player.hasCharacteristic(FighterCharacteristicType.EQUIPMENT_KNOWLEDGE)) {
            equipmentKnowledge = player.getCharacteristicValue(FighterCharacteristicType.EQUIPMENT_KNOWLEDGE);
        }
        int equipmentKnowledgeBonus = 0;
        final Iterator<WakfuEffect> effectsIterator = (Iterator<WakfuEffect>)item.getEffectsIterator();
        while (effectsIterator.hasNext()) {
            final WakfuEffect next = effectsIterator.next();
            if (next.getActionId() == RunningEffectConstants.EQUIPMENT_KNOWLEDGE_GAIN.getId()) {
                equipmentKnowledgeBonus = next.getParam(0, item.getLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                equipmentKnowledge = ((equipmentKnowledge + equipmentKnowledgeBonus > FighterCharacteristicType.EQUIPMENT_KNOWLEDGE.getDefaultMax()) ? FighterCharacteristicType.EQUIPMENT_KNOWLEDGE.getDefaultMax() : (equipmentKnowledge + equipmentKnowledgeBonus));
                break;
            }
        }
        return player instanceof BasicCharacterInfo && item.getLevel() <= ((Citizen)player).getLevel() + equipmentKnowledge;
    }
    
    public static boolean checkItemLevelValidity(final Item item, final EffectUser player) {
        final AbstractReferenceItem referenceItem = item.getReferenceItem();
        if (referenceItem.hasPet() || referenceItem.hasItemProperty(ItemProperty.ADMIN_XP)) {
            return true;
        }
        int equipmentKnowledge = 0;
        if (player.hasCharacteristic(FighterCharacteristicType.EQUIPMENT_KNOWLEDGE)) {
            equipmentKnowledge = player.getCharacteristicValue(FighterCharacteristicType.EQUIPMENT_KNOWLEDGE);
        }
        if (!((BasicCharacterInfo)player).getEquipmentInventory().containsUniqueId(item.getUniqueId())) {
            int equipmentKnowledgeBonus = 0;
            final Iterator<WakfuEffect> effectsIterator = (Iterator<WakfuEffect>)item.getReferenceItem().getEffectsIterator();
            while (effectsIterator.hasNext()) {
                final WakfuEffect next = effectsIterator.next();
                if (next.getActionId() == RunningEffectConstants.EQUIPMENT_KNOWLEDGE_GAIN.getId()) {
                    equipmentKnowledgeBonus = next.getParam(0, item.getLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
                    equipmentKnowledge = ((equipmentKnowledge + equipmentKnowledgeBonus > FighterCharacteristicType.EQUIPMENT_KNOWLEDGE.getDefaultMax()) ? FighterCharacteristicType.EQUIPMENT_KNOWLEDGE.getDefaultMax() : (equipmentKnowledge + equipmentKnowledgeBonus));
                    break;
                }
            }
        }
        return player instanceof BasicCharacterInfo && item.getLevel() <= ((Citizen)player).getLevel() + equipmentKnowledge;
    }
    
    public static boolean petCanBeEquipped(final Item petHolder) {
        if (!petHolder.hasPet()) {
            return true;
        }
        final Pet pet = petHolder.getPet();
        return pet.isActive() && pet.getHealth() > 0;
    }
    
    public static boolean checkExclusiveEquipmentProperty(final ItemEquipment equipment, final Item oldItem, final Item newItem) {
        return !checkForDuplicateProperty(equipment, oldItem, newItem, ItemProperty.EXCLUSIVE_EQUIPMENT_ITEM) && !checkForDuplicateProperty(equipment, oldItem, newItem, ItemProperty.EXCLUSIVE_EQUIPMENT_ITEM_2);
    }
    
    private static boolean checkForDuplicateProperty(final ItemEquipment equipment, final Item oldItem, final Item newItem, final ItemProperty property) {
        if (newItem.getReferenceItem().hasItemProperty(property)) {
            for (final Item next : equipment) {
                if (newItem != next && oldItem != next && next.getReferenceItem().hasItemProperty(property)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    static {
        m_instance = new EquipmentInventoryChecker();
    }
}
