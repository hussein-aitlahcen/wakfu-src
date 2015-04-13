package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class ModeratorChatDialogActions
{
    public static final String PACKAGE = "wakfu.moderatorChat";
    
    public static void close(final Event event) {
        Worker.getInstance().pushMessage(new UIMessage((short)19070));
    }
}
