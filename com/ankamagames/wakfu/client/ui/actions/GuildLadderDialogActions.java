package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;

@XulorActionsTag
public class GuildLadderDialogActions
{
    public static final String PACKAGE = "wakfu.guildLadder";
    
    public static void previousPage(final Event e) {
        UIMessage.send((short)18220);
    }
    
    public static void nextPage(final Event e) {
        UIMessage.send((short)18221);
    }
}
