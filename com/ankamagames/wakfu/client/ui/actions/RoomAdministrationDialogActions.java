package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.wakfu.client.ui.protocol.message.dimensionalBag.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.rights.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.event.*;

@XulorActionsTag
public class RoomAdministrationDialogActions
{
    public static final String PACKAGE = "wakfu.roomAdministration";
    
    public static void changeGuildPerms(final Event event, final RoomView roomView) {
        final UIDimensionalBagChangeRoomPermissionMessage message = new UIDimensionalBagChangeRoomPermissionMessage();
        message.setId(17002);
        message.setIntValue(roomView.getTypeId());
        message.setBooleanValue(!roomView.getGroupPermission(GroupType.GUILD));
        message.setRoomView(roomView);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void changeAnonymousPerms(final Event event, final RoomView roomView) {
        final UIDimensionalBagChangeRoomPermissionMessage message = new UIDimensionalBagChangeRoomPermissionMessage();
        message.setId(17004);
        message.setIntValue(roomView.getTypeId());
        message.setBooleanValue(!roomView.getGroupPermission(GroupType.ALL));
        message.setRoomView(roomView);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void addPermission(final Event event, final TextEditor textEditor) {
        if (event.getType() == Events.MOUSE_CLICKED || (event.getType() == Events.KEY_PRESSED && ((KeyEvent)event).getKeyCode() == 10)) {
            final UIMessage message = new UIMessage();
            message.setId(17000);
            message.setStringValue(textEditor.getText());
            Worker.getInstance().pushMessage(message);
            textEditor.setText("");
        }
    }
    
    public static void removePermission(final Event event, final Long individualId) {
        final UIMessage message = new UIMessage();
        message.setId(17001);
        message.setLongValue(individualId);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void changeIndividualPerms(final SelectionChangedEvent event, final Long individualId, final RoomView roomView) {
        final UIDimensionalBagChangeRoomPermissionMessage message = new UIDimensionalBagChangeRoomPermissionMessage();
        message.setId(17005);
        message.setIntValue(roomView.getTypeId());
        message.setLongValue(individualId);
        message.setBooleanValue(event.isSelected());
        message.setRoomView(roomView);
        Worker.getInstance().pushMessage(message);
    }
}
