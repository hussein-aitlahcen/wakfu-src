package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.console.command.debug.anim.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class DebugAnmActions
{
    public static final String PACKAGE = "wakfu.debugAnm";
    
    public static void updateLinkageList(final Event e) {
        AnmDebuggerFieldProvider.getInstance().updateLinkages();
    }
    
    public static void selectionChanged(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            AnmDebuggerFieldProvider.getInstance().setSelectedLinkage((String)e.getValue());
        }
    }
    
    public static void play(final Event e) {
        AnmDebuggerFieldProvider.getInstance().play();
    }
    
    public static void setSkin(final Event e, final TextEditor te) {
        AnmDebuggerFieldProvider.getInstance().setSkin(te.getText());
    }
    
    public static void setType(final Event e, final String type) {
        AnmDebuggerFieldProvider.getInstance().setType(type);
    }
}
