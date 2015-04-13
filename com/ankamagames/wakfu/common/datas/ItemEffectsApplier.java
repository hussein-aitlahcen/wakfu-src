package com.ankamagames.wakfu.common.datas;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import gnu.trove.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

final class ItemEffectsApplier
{
    private static final Logger m_logger;
    private final BasicCharacterInfo m_linkedCharacter;
    private final WakfuEffectContainerBuilder m_setEffectContainerBuilder;
    private final WakfuEffectContainerBuilder m_bagsEffectContainerBuilder;
    
    ItemEffectsApplier(final BasicCharacterInfo linkedCharacter) {
        super();
        this.m_setEffectContainerBuilder = new WakfuEffectContainerBuilder().setContainerType(14);
        this.m_bagsEffectContainerBuilder = new WakfuEffectContainerBuilder().setContainerType(31);
        this.m_linkedCharacter = linkedCharacter;
    }
    
    public void reloadItemEffects() {
        if (this.m_linkedCharacter.getEquipmentInventory() == null) {
            return;
        }
        this.m_linkedCharacter.beginUpdateItemEffects();
        this.lockPaPmPwHpCurrentValue();
        try {
            this.applyItemAndSetEffectsWithoutCriterionCheck();
            this.disableUnsatisfiedCriterionsItems();
            this.applyAllBagsEffects();
        }
        catch (Exception e) {
            ItemEffectsApplier.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        finally {
            this.unlockPaPmPwHpCurrentValue();
        }
        this.m_linkedCharacter.endUpdateItemEffects();
    }
    
    private void applyAllBagsEffects() {
        this.getRunningEffectManager().removeLinkedToContainerType(31);
        final AbstractBagContainer bags = this.m_linkedCharacter.getBags();
        if (bags == null) {
            return;
        }
        final TLongObjectIterator<AbstractBag> bagsIterator = bags.getBagsIterator();
        while (bagsIterator.hasNext()) {
            bagsIterator.advance();
            final AbstractBag bag = bagsIterator.value();
            this.applyBagEffects(bag);
        }
    }
    
    private void applyBagEffects(final AbstractBag bag) {
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(bag.getReferenceId());
        if (referenceItem == null) {
            return;
        }
        final Iterator it = referenceItem.getEffectsIterator();
        final EffectContext context = this.getAppropriateContext();
        final int cellX = this.m_linkedCharacter.getWorldCellX();
        final int cellY = this.m_linkedCharacter.getWorldCellY();
        final short cellZ = this.m_linkedCharacter.getWorldCellAltitude();
        while (it.hasNext()) {
            final WakfuEffect effect = it.next();
            if (effect.getEffectType() == 0 && !effect.isAnUsableEffect()) {
                effect.execute(this.m_bagsEffectContainerBuilder.build(), this.m_linkedCharacter, context, RunningEffectConstants.getInstance(), cellX, cellY, cellZ, this.m_linkedCharacter, null, false);
            }
        }
    }
    
    private void applyItemAndSetEffectsWithoutCriterionCheck() {
        final Set<AbstractItemSet> sets = new THashSet<AbstractItemSet>();
        for (final Item item : this.getEquipmentInventory()) {
            try {
                if (!item.isActive()) {
                    continue;
                }
                if (item.getReferenceItem().getSetId() != 0) {
                    final AbstractItemSet set = this.m_linkedCharacter.getItemSetManager().getItemSet(item.getReferenceItem().getSetId());
                    sets.add(set);
                }
                this.unapplyItemOnEquipEffect(item);
                this.applyItemOnEquipEffectWithoutCriterionCheck(item);
            }
            catch (ConcurrentModificationException e) {
                ItemEffectsApplier.m_logger.error((Object)("ConcurrentModificationException while applyingItemAndSetEffects for item " + item.toString() + " on breed " + this.m_linkedCharacter.getBreedId()));
                throw e;
            }
        }
        for (final AbstractItemSet abstractItemSet : sets) {
            this.updateSetBonus(abstractItemSet);
        }
    }
    
    private void disableUnsatisfiedCriterionsItems() {
        final EffectContext context = this.getAppropriateContext();
        final Set<Item> disabledItems = new HashSet<Item>();
        boolean itemEffectsModified;
        do {
            itemEffectsModified = false;
            for (final Item item : this.getEquipmentInventory()) {
                if (!item.isActive()) {
                    continue;
                }
                if (disabledItems.contains(item)) {
                    continue;
                }
                if (item.hasPet() && this.m_linkedCharacter.isOnFight()) {
                    continue;
                }
                if (!EquipmentInventoryChecker.getInstance().checkCriterion(item, (EffectUser)this.m_linkedCharacter, context)) {
                    itemEffectsModified = true;
                    disabledItems.add(item);
                    this.unapplyItemOnEquipEffect(item);
                    break;
                }
            }
        } while (itemEffectsModified);
    }
    
    void unapplyItemOnEquipEffect(final Item item) {
        this.getRunningEffectManager().removeLinkedToItem(item, true);
    }
    
    boolean applyItemOnEquipEffect(final Item item) {
        final EffectContext context = this.getAppropriateContext();
        return EquipmentInventoryChecker.getInstance().checkCriterion(item, (EffectUser)this.m_linkedCharacter, context) && this.applyItemOnEquipEffectWithoutCriterionCheck(item);
    }
    
    boolean applyItemOnEquipEffectWithoutCriterionCheck(final Item item) {
        if (!item.isActive()) {
            return false;
        }
        if (item.isRent() && item.getRentInfo().isExpired()) {
            return false;
        }
        final EffectContext context = this.getAppropriateContext();
        final int cellX = this.m_linkedCharacter.getWorldCellX();
        final int cellY = this.m_linkedCharacter.getWorldCellY();
        final short cellZ = this.m_linkedCharacter.getWorldCellAltitude();
        for (final WakfuEffect effect : item) {
            if (effect.getEffectType() == 0 && !effect.isAnUsableEffect()) {
                effect.execute(item, this.m_linkedCharacter, context, RunningEffectConstants.getInstance(), cellX, cellY, cellZ, this.m_linkedCharacter, null, false);
            }
        }
        return true;
    }
    
    void unapplyItemOnEquipEffect(final Item item, final AbstractItemSet set) {
        this.getRunningEffectManager().removeLinkedToItem(item, true);
        this.updateSetBonus(set);
    }
    
    private void updateSetBonus(final AbstractItemSet set) {
        final WakfuEffectContainer fakeSetLevelContainer = this.m_setEffectContainerBuilder.setContainerId(set.getId()).build();
        this.getRunningEffectManager().removeLinkedToContainer(fakeSetLevelContainer, true);
        final short count = this.getEquipedSetItemsCount(set);
        final ArrayList<ItemSetLevel> list = (ArrayList<ItemSetLevel>)set.getEffectsToApplyByNbElements(count);
        if (list.isEmpty()) {
            return;
        }
        for (int i = 0, size = list.size(); i < size; ++i) {
            this.executeSetEffects(list.get(i), this.m_linkedCharacter.getAppropriateContext());
        }
    }
    
    private short getEquipedSetItemsCount(final AbstractItemSet<? extends AbstractReferenceItem> set) {
        final ItemEquipment equipmentInventory = this.getEquipmentInventory();
        if (equipmentInventory == null) {
            return 0;
        }
        short count = 0;
        for (final AbstractReferenceItem setItem : set) {
            final Item item = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getFirstWithReferenceId(setItem.getId());
            if (item != null && EquipmentInventoryChecker.getInstance().checkCriterion(item, (EffectUser)this.m_linkedCharacter, this.getAppropriateContext())) {
                ++count;
            }
        }
        return count;
    }
    
    private void executeSetEffects(final ItemSetLevel setLevel, final EffectContext context) {
        final int cellX = this.m_linkedCharacter.getWorldCellX();
        final int cellY = this.m_linkedCharacter.getWorldCellY();
        final short cellZ = this.m_linkedCharacter.getWorldCellAltitude();
        for (final WakfuEffect effect : setLevel) {
            effect.execute(setLevel, this.m_linkedCharacter, context, RunningEffectConstants.getInstance(), cellX, cellY, cellZ, this.m_linkedCharacter, null, false);
        }
    }
    
    private void lockPaPmPwHpCurrentValue() {
        this.lockCharacCurrentValue(this.getCharacteristic(FighterCharacteristicType.AP));
        this.lockCharacCurrentValue(this.getCharacteristic(FighterCharacteristicType.MP));
        this.lockCharacCurrentValue(this.getCharacteristic(FighterCharacteristicType.WP));
        this.lockCharacCurrentValue(this.getCharacteristic(FighterCharacteristicType.HP));
    }
    
    private void lockCharacCurrentValue(final FighterCharacteristic charac) {
        if (charac != null) {
            charac.lockCurrentValue();
        }
    }
    
    private void unlockPaPmPwHpCurrentValue() {
        this.unlockCharacCurrentValue(this.getCharacteristic(FighterCharacteristicType.AP));
        this.unlockCharacCurrentValue(this.getCharacteristic(FighterCharacteristicType.MP));
        this.unlockCharacCurrentValue(this.getCharacteristic(FighterCharacteristicType.WP));
        this.unlockCharacCurrentValue(this.getCharacteristic(FighterCharacteristicType.HP));
    }
    
    private void unlockCharacCurrentValue(final FighterCharacteristic charac) {
        if (charac != null) {
            charac.unlockCurrentValue();
        }
    }
    
    private ItemEquipment getEquipmentInventory() {
        return this.m_linkedCharacter.getEquipmentInventory();
    }
    
    public TimedRunningEffectManager getRunningEffectManager() {
        return this.m_linkedCharacter.getRunningEffectManager();
    }
    
    public EffectContext getAppropriateContext() {
        return this.m_linkedCharacter.getAppropriateContext();
    }
    
    public FighterCharacteristic getCharacteristic(final CharacteristicType charac) {
        return this.m_linkedCharacter.getCharacteristic(charac);
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemEffectsApplier.class);
    }
}
