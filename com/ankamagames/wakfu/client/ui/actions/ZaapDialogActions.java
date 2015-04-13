package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.travel.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class ZaapDialogActions
{
    public static final String PACKAGE = "wakfu.zaap";
    
    public static void selectZaap(final Event e, final TravelInfo info) {
        final UIMessage msg = new UIMessage();
        msg.setId(19312);
        msg.setLongValue(info.getId());
        msg.setIntValue(info.getCost());
        Worker.getInstance().pushMessage(msg);
    }
}
