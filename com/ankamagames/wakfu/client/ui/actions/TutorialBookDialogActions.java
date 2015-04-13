package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.tutorial.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;

@XulorActionsTag
public class TutorialBookDialogActions
{
    public static final String PACKAGE = "wakfu.tutorialBook";
    
    public static void launchTutorial(final ItemEvent e) {
        launchTutorial(e, (TutorialEntryView)e.getItemValue());
    }
    
    public static void launchTutorial(final Event e, final TutorialEntryView tutorialEntryView) {
        if (tutorialEntryView.isLaunched()) {
            final Message message = new UIMessage();
            message.setId(19149);
            Worker.getInstance().pushMessage(message);
        }
        else {
            tutorialEntryView.launch();
        }
    }
    
    public static void searchTutorial(final Event e) {
        final TextEditor target = e.getTarget();
        PropertiesProvider.getInstance().setPropertyValue("tutorialSearchDirty", target.getText().length() > 0);
        searchTutorial(e, target);
    }
    
    public static void searchTutorial(final Event e, final TextEditor ed) {
        final String value = ed.getText();
        if (value != null) {
            final UIMessage msg = new UIMessage();
            msg.setId(19370);
            msg.setStringValue(value);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void setPreviousTutorialPage(final Event e) {
        final UIMessage msg = new UIMessage();
        msg.setId(19371);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void setNextTutorialPage(final Event e) {
        final UIMessage msg = new UIMessage();
        msg.setId(19372);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void clearSearch(final Event e, final TextEditor ed) {
        final UIMessage msg = new UIMessage();
        msg.setId(19370);
        msg.setStringValue(null);
        Worker.getInstance().pushMessage(msg);
        ed.resetGhostText();
    }
}
