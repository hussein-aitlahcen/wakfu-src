package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.background.gazette.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class GazetteDialogActions
{
    public static final String PACKAGE = "wakfu.gazette";
    
    public static void displayGazette(final ItemEvent e) {
        final GazetteView view = (GazetteView)e.getItemValue();
        if (view.isUnlocked()) {
            final UIMessage msg = new UIMessage();
            msg.setId(16880);
            msg.setIntValue(view.getId());
            Worker.getInstance().pushMessage(msg);
        }
    }
}
