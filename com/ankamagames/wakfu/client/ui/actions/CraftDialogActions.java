package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.craft.*;
import com.ankamagames.wakfu.client.ui.protocol.message.craft.*;
import com.ankamagames.framework.kernel.core.common.message.*;

@XulorActionsTag
public class CraftDialogActions
{
    public static final String PACKAGE = "wakfu.craft";
    
    public static void selectCraft(final ItemEvent itemEvent) {
        final UISelectCraftMessage uiSelectCraftMessage = new UISelectCraftMessage((CraftView)itemEvent.getItemValue());
        Worker.getInstance().pushMessage(uiSelectCraftMessage);
    }
}
