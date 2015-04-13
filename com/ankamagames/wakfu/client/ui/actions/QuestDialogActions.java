package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

@XulorActionsTag
public class QuestDialogActions
{
    public static final String PACKAGE = "wakfu.quests";
    
    public static void accept(final Event e) {
        QuestActivationManager.INSTANCE.acceptCurrentQuest();
    }
    
    public static void refuse(final Event e) {
        QuestActivationManager.INSTANCE.refuseCurrentQuest();
    }
}
