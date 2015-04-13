package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class ShukruteActions
{
    public static final String PACKAGE = "wakfu.shukrute";
    
    public static void closeDialog(final Event event) {
        UIShukruteRewardFrame.getInstance().closeDialog();
    }
}
