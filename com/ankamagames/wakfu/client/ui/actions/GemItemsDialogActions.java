package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class GemItemsDialogActions
{
    public static final String PACKAGE = "wakfu.gemItems";
    
    public static void drop(final DropEvent e, final GemSlotDisplayer displayer) {
        final Item gem = (Item)e.getSourceValue();
        final UISocketGemMessage msg = new UISocketGemMessage();
        msg.setGem(gem);
        msg.setItem(displayer.getHolder());
        msg.setByteValue(displayer.getIndex());
        Worker.getInstance().pushMessage(msg);
        EquipmentDialogActions.onDropItem();
    }
}
