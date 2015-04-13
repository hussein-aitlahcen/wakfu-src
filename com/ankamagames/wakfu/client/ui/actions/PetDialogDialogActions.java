package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class PetDialogDialogActions
{
    public static final String PACKAGE = "wakfu.petDialog";
    
    public static void unloadTutorialDialog(final Event e) {
        if (WakfuGameEntity.getInstance().getLocalPlayer().isActiveProperty(WorldPropertyType.CANT_UNLOAD_TUTORIAL_DIALOG)) {
            return;
        }
        final Message message = new UIMessage();
        message.setId(19149);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void validOrSetNextMessage(final Event e) {
        UITutorialFrame.getInstance().setNextPetMessage();
    }
    
    public static void mutePet(final Event e) {
    }
}
