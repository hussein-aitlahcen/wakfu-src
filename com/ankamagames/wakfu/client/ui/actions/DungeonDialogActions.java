package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.dungeon.*;

@XulorActionsTag
public class DungeonDialogActions
{
    public static final String PACKAGE = "wakfu.dungeon";
    
    public static void selectDungeon(final ItemEvent e) {
        final DungeonView view = (DungeonView)e.getItemValue();
        final UIMessage msg = new UIMessage();
        msg.setId(16540);
        msg.setIntValue(view.getId());
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void setFilter(final Event e, final DungeonFilterTypeView view) {
        final UIMessage msg = new UIMessage();
        msg.setId(16541);
        msg.setByteValue(view.getType().getId());
        Worker.getInstance().pushMessage(msg);
    }
}
