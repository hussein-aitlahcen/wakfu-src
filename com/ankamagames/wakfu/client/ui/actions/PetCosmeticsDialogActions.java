package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.client.core.game.item.cosmetic.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;

@XulorActionsTag
public class PetCosmeticsDialogActions
{
    public static final String PACKAGE = "wakfu.petCosmetics";
    
    public static void selectCostume(final ItemEvent e, final PetDetailDialogView pet) {
        final PetCosmeticsItemView view = (PetCosmeticsItemView)e.getItemValue();
        final AbstractUIMessage msg = new UIMessage((short)19401);
        msg.setIntValue(view.getRefId());
        msg.setLongValue(pet.getPetItem().getUniqueId());
        Worker.getInstance().pushMessage(msg);
    }
}
