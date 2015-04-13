package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class DisplayButtonDialogActions
{
    public static final String PACKAGE = "wakfu.displayButton";
    
    public static void nextStep(final Event e) {
        UIDisplayButtonFrame.INSTANCE.callFunction();
    }
}
