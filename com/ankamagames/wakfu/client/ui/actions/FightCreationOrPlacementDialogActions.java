package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class FightCreationOrPlacementDialogActions
{
    public static final String PACKAGE = "wakfu.fightCreationOrPlacement";
    
    public static void lockFight() {
        final Window window = UIControlCenterContainerFrame.getInstance().getWindow();
        if (window != null) {
            final ToggleButton tb = (ToggleButton)window.getElementMap().getElement("lockFightBtn");
            tb.setSelected(!tb.getSelected());
            lockFight(null, tb);
        }
    }
    
    public static void lockFight(final Event event, final ToggleButton tb) {
        final UIMessage message = new UIMessage();
        message.setBooleanValue(tb.getSelected());
        message.setId(18014);
        Worker.getInstance().pushMessage(message);
    }
}
