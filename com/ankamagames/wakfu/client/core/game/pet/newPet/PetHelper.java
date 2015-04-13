package com.ankamagames.wakfu.client.core.game.pet.newPet;

import com.ankamagames.baseImpl.common.clientAndServer.game.gameAction.*;
import com.ankamagames.wakfu.common.game.pet.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.pet.definition.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.framework.ai.criteria.antlrcriteria.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class PetHelper
{
    public static String getPetGfx(final ActionUser user, final Pet pet) {
        final PetDefinition definition = pet.getDefinition();
        final int equippedItemId = pet.getEquippedRefItemId();
        if (definition.containsReskinItem(equippedItemId)) {
            final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(equippedItemId);
            final SimpleCriterion crit = refItem.getCriterion(ActionsOnItem.EQUIP);
            if (crit == null || crit.isValid(user, pet, refItem, user.getAppropriateContext())) {
                return definition.getReskinItemGfxId(equippedItemId);
            }
        }
        return definition.getGfxId();
    }
    
    public static void applyEquipment(final ActionUser player, final Pet pet, final Actor mobile, final int equippedItem) {
        final PetDefinition definition = pet.getDefinition();
        if (equippedItem <= 0) {
            mobile.setGfx(getPetGfx(player, pet));
            mobile.unApplyAllPartsEquipment();
            mobile.forceReloadAnimation();
            return;
        }
        if (definition.containsReskinItem(equippedItem)) {
            mobile.setGfx(getPetGfx(player, pet));
            return;
        }
        mobile.unApplyAllPartsEquipment();
        final AbstractReferenceItem refItem = ReferenceItemManager.getInstance().getReferenceItem(equippedItem);
        if (refItem != null) {
            mobile.applyPartsEquipment(refItem.getGfxId());
        }
        mobile.forceReloadAnimation();
    }
}
