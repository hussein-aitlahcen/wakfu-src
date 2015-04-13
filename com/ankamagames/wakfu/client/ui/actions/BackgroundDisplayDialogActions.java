package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

@XulorActionsTag
public class BackgroundDisplayDialogActions
{
    public static final String PACKAGE = "wakfu.background";
    
    public static void setNextPage(final Event e) {
        UIMessage.send((short)16142);
    }
    
    public static void setPreviousPage(final Event e) {
        UIMessage.send((short)16141);
    }
}
