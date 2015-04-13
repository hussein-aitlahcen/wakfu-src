package com.ankamagames.wakfu.common.game.item;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import java.util.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.item.validator.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class ItemSetMergeHelper
{
    private static final Logger m_logger;
    
    public static boolean isItemSetMergeable(final AbstractItemSet<? extends AbstractReferenceItem> itemSet) {
        final AbstractReferenceItem linkedItem = ReferenceItemManager.getInstance().getReferenceItem(itemSet.getLinkedReferenceItemId());
        if (linkedItem == null) {
            return false;
        }
        for (final AbstractReferenceItem referenceItem : itemSet) {
            if (referenceItem.getMetaType() == ItemMetaType.SUB_META_ITEM) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean checkInventoryForSplit(final BasicCharacterInfo concernedPlayer, final AbstractItemSet<? extends AbstractReferenceItem> set, final Item sourceItem) {
        final int piecesNumber = set.getPiecesNumber();
        int availableSpotsInBag = concernedPlayer.getBags().getNbFreePlaces();
        if (sourceItem.getQuantity() == 1) {
            ++availableSpotsInBag;
        }
        if (piecesNumber <= availableSpotsInBag) {
            return true;
        }
        for (final AbstractReferenceItem itemInSet : set) {
            final Item itemInBag = concernedPlayer.getBags().getFirstItemFromInventoryFromRefId(itemInSet.getId());
            final boolean canStack = itemInBag != null && itemInBag.getStackFreePlace() != 0 && (!itemInSet.hasRandomElementEffect() || itemInBag.randomElementsInit());
            if (canStack) {
                continue;
            }
            --availableSpotsInBag;
        }
        return availableSpotsInBag >= 0;
    }
    
    public static boolean checkInventoryForMerge(final BasicCharacterInfo character, final AbstractItemSet<? extends AbstractReferenceItem> itemSet) {
        return checkInventoryForMerge(character, itemSet, false);
    }
    
    public static boolean checkInventoryForMerge(final BasicCharacterInfo character, final AbstractItemSet<? extends AbstractReferenceItem> itemSet, final boolean withLogs) {
        boolean ok = true;
        final Iterator<? extends AbstractReferenceItem> it = itemSet.iterator();
        while (it.hasNext() && ok) {
            final AbstractReferenceItem referenceItem = (AbstractReferenceItem)it.next();
            final Item item = character.getBags().getFirstItemFromInventoryFromRefId(referenceItem.getId(), IntoSetMergeableItemValidator.INSTANCE);
            if (item == null) {
                ok = false;
                if (!withLogs) {
                    continue;
                }
                ItemSetMergeHelper.m_logger.error((Object)("Fusion de panoplie : le joueur id=" + character.getId() + " ne poss\u00e8de pas l'item de refId=" + referenceItem.getId()));
            }
        }
        return ok;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ItemSetMergeHelper.class);
    }
}
