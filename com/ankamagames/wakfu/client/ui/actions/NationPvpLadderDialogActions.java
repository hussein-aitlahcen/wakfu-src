package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.pvp.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;

@XulorActionsTag
public class NationPvpLadderDialogActions
{
    public static final String PACKAGE = "wakfu.nationPvpLadder";
    
    public static void first(final Event e) {
        UIMessage.send((short)19425);
    }
    
    public static void previous(final Event e) {
        UIMessage.send((short)19426);
    }
    
    public static void next(final Event e) {
        UIMessage.send((short)19427);
    }
    
    public static void last(final Event e) {
        UIMessage.send((short)19428);
    }
    
    public static void selectFilter(final SelectionChangedEvent e, final PvpLadderPageView.FilterType filter) {
        if (e.isSelected()) {
            final AbstractUIMessage msg = new UIMessage((short)19432);
            msg.setObjectValue(filter);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void selectBreed(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            final AbstractUIMessage msg = new UIMessage((short)19430);
            msg.setObjectValue(e.getValue());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void selectNation(final ListSelectionChangedEvent e) {
        if (e.getSelected()) {
            final AbstractUIMessage msg = new UIMessage((short)19431);
            msg.setObjectValue(e.getValue());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void openPvpLadderEntry(final Event e, final PvpLadderEntryView view) {
        final AbstractUIMessage msg = new UIMessage((short)19433);
        msg.setObjectValue(view);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void search(final KeyEvent e) {
        if (e.getType() == Events.KEY_PRESSED && e.getKeyCode() == 10) {
            final TextWidget widget = e.getTarget();
            search(widget);
        }
    }
    
    public static void search(final Event e, final TextEditor te) {
        search(te);
    }
    
    private static void search(final TextWidget tw) {
        final AbstractUIMessage msg = new UIMessage((short)19434);
        msg.setStringValue(tw.getText());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void resetSearch(final Event e) {
        UIMessage.send((short)19435);
    }
}
