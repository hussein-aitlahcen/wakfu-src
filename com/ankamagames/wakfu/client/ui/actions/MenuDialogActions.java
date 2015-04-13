package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

@XulorActionsTag
public class MenuDialogActions
{
    public static final String PACKAGE = "wakfu.menu";
    
    public static void loggoff(final Event event) {
        UIMessage.send((short)16386);
    }
    
    public static void contactModerator(final Event e) {
        UIMessage.send((short)16393);
    }
    
    public static void openOptionsDialog(final Event event) {
        UIMessage.send((short)16402);
    }
    
    public static void closeMenuDialog(final Event event) {
        UIMessage.send((short)16401);
    }
    
    public static void goToWorldChoiceDialog(final Event event) {
        UIMessage.send((short)16388);
    }
}
