package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.core.event.*;

@XulorActionsTag
public class MercenaryDialogActions
{
    public static final String PACKAGE = "wakfu.mercenaries";
    
    public static void selectAchievement(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            UIMessage.send((short)19110, e.getValue());
        }
    }
    
    public static void selectFilter(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            UIMessage.send((short)19111, e.getValue());
        }
    }
    
    public static void activateAchievement(final Event e) {
        UIMessage.send((short)19112);
    }
}
