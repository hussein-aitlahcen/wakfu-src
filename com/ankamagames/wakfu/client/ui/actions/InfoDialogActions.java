package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class InfoDialogActions
{
    public static final String PACKAGE = "wakfu.info";
    
    public static void closeDialog(final Event event) {
        UIInfoDialogFrame.getInstance().closeDialog();
    }
}
