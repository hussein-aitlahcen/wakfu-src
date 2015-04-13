package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.item.cosmetic.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.costume.*;
import com.ankamagames.wakfu.client.core.*;

@XulorActionsTag
public class CosmeticsDialogActions
{
    public static final String PACKAGE = "wakfu.cosmetics";
    
    public static void selectCostume(final ItemEvent e) {
        final CosmeticsItemView view = (CosmeticsItemView)e.getItemValue();
        final AbstractUIMessage msg = new UIMessage((short)19400);
        msg.setIntValue(view.getRefId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void unlinkCostume(final Event e, final List l) {
        final CosmeticsItemView selectedValue = (CosmeticsItemView)l.getSelectedValue();
        if (selectedValue != null && selectedValue.getRefId() > 0) {
            final Message netMsg = new UnlinkCostumeMessage(selectedValue.getRefId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
        }
    }
}
