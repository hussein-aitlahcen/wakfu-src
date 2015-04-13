package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.fight.time.*;
import com.ankamagames.wakfu.client.ui.protocol.message.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class TimePointSelectionDialogActions
{
    public static final String PACKAGE = "wakfu.timePointSelection";
    
    public static void selectEffect(final Event e, final TimePointEffect effect) {
        final UITimePointBonusMessage msg = new UITimePointBonusMessage();
        msg.setId(18105);
        msg.setEffect(effect);
        Worker.getInstance().pushMessage(msg);
    }
}
