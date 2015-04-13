package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.dialog.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class InteractiveDialogActions
{
    public static final String PACKAGE = "wakfu.interactiveDialog";
    
    public static void selectChoice(final Event event, final WakfuClientDialogChoiceView wakfuClientDialogChoiceView) {
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setIntValue(wakfuClientDialogChoiceView.getId());
        uiMessage.setId(16147);
        Worker.getInstance().pushMessage(uiMessage);
    }
}
