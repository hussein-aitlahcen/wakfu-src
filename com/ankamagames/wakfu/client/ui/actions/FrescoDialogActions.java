package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;

@XulorActionsTag
public class FrescoDialogActions
{
    public static final String PACKAGE = "wakfu.fresco";
    
    public static void closeDialog(final Event event) {
        WakfuGameEntity.getInstance().removeFrame(UIFrescoFrame.getInstance());
    }
}
