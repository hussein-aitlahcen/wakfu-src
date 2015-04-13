package com.ankamagames.wakfu.client.core.game.item.operation;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.visitor.operation.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.exception.*;
import com.ankamagames.wakfu.client.core.game.item.ui.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import gnu.trove.*;

public class BagOperationComputer
{
    private static final TLongObjectHashMap<AddItemOperation> ADD_OPERATIONS;
    
    public static void applyOperations(final TLongObjectHashMap<BagOperation> operations) throws InventoryCapacityReachedException, ContentAlreadyPresentException, PositionAlreadyUsedException {
        BagOperationComputer.ADD_OPERATIONS.clear();
        final TLongObjectIterator<BagOperation> it = operations.iterator();
        while (it.hasNext()) {
            it.advance();
            final long itemUid = it.key();
            final BagOperation operation = it.value();
            final LocalPlayerCharacter player = HeroUtils.getHeroWithItemUidInBags(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), itemUid);
            switch (operation.getOperationType()) {
                case 0: {
                    final AddItemOperation op = (AddItemOperation)operation;
                    BagOperationComputer.ADD_OPERATIONS.put(itemUid, op);
                    continue;
                }
                case 1: {
                    final RemoveItemOperation op2 = (RemoveItemOperation)operation;
                    removeItem(player, itemUid, op2);
                    continue;
                }
                case 2: {
                    final UpdateItemOperation op3 = (UpdateItemOperation)operation;
                    updateItem(player, itemUid, op3);
                    continue;
                }
                default: {
                    continue;
                }
            }
        }
        final TLongObjectIterator<AddItemOperation> it2 = BagOperationComputer.ADD_OPERATIONS.iterator();
        while (it2.hasNext()) {
            it2.advance();
            final LocalPlayerCharacter player2 = HeroUtils.getHeroWithBagUid(WakfuGameEntity.getInstance().getLocalPlayer().getOwnerId(), it2.value().getBagId());
            if (player2 != null) {
                addItem(player2, it2.key(), it2.value());
            }
        }
        BagOperationComputer.ADD_OPERATIONS.clear();
    }
    
    private static void updateItem(final BasicCharacterInfo player, final long itemUid, final UpdateItemOperation op) {
        final Item item = player.getBags().getItemFromInventories(itemUid);
        final short updateValue = (short)(op.getQty() - item.getQuantity());
        player.getBags().updateQuantity(itemUid, updateValue);
        if (updateValue > 0 && !op.isInsideMove()) {
            ItemFeedbackHelper.sendChatItemAddedMessage(item, updateValue);
        }
    }
    
    private static void removeItem(final BasicCharacterInfo player, final long itemUid, final RemoveItemOperation op) {
        player.getBags().removeItemFromBags(itemUid);
    }
    
    private static void addItem(final BasicCharacterInfo player, final long itemUid, final AddItemOperation op) throws InventoryCapacityReachedException, ContentAlreadyPresentException, PositionAlreadyUsedException {
        final ReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(op.getRefId());
        final Item it = new Item(itemUid);
        it.initializeWithReferenceItem(refItem);
        it.setQuantity(op.getQty());
        it.restorePet(op.getPet());
        it.restoreXp(op.getXp());
        it.restoreGems(op.getGems());
        it.restoreCompanion(op.getCompanion());
        it.setRentInfo(op.getRentInfo());
        it.setBind(op.getBind());
        it.setMultiElementsInfo(op.getMultiElementsEffects());
        it.setMergedSetItems(op.getMergedSetInfo());
        player.getBags().get(op.getBagId()).addAt(it, op.getPosInBag());
        if (!op.isInsideMove()) {
            ItemFeedbackHelper.sendChatItemAddedMessage(it, it.getQuantity());
        }
    }
    
    static {
        ADD_OPERATIONS = new TLongObjectHashMap<AddItemOperation>();
    }
}
