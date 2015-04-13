package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.personalSpace.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.room.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.*;

@XulorActionsTag
public class DimensionalBagRoomManagerDialogAction
{
    public static final String PACKAGE = "wakfu.roomManager";
    protected static final Logger m_logger;
    
    public static void addUser(final Event event, final DimensionalBagView dimensionalBag, final TextEditor textEditor) {
        DimensionalBagRoomManagerDialogAction.m_logger.error((Object)"DimensionalBagRoomManagerDialogAction.addUser()");
    }
    
    public static void removeUser(final Event event, final Form form, final DimensionalBagView dimensionalBag) {
        DimensionalBagRoomManagerDialogAction.m_logger.error((Object)"DimensionalBagRoomManagerDialogAction.removeUser()");
    }
    
    public void changeGroupPermission(final Event event, final Form form) throws Exception {
        final PSChangeGroupPermsRequestMessage netMsg = new PSChangeGroupPermsRequestMessage();
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
    }
    
    public static void changeUserPermission(final Event event, final Form form, final DimensionalBagView dimensionalBag) {
        DimensionalBagRoomManagerDialogAction.m_logger.error((Object)"DimensionalBagRoomManagerDialogAction.changeUserPermission()");
    }
    
    public void kickUser(final Event event, final Form form) throws Exception {
        final PSKickUserRequestMessage netMsg = new PSKickUserRequestMessage();
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMsg);
    }
    
    public static void onClicktRoom(final Event event, final DimensionalBagView dimensionalBag, final String roomId) {
        DimensionalBagRoomManagerDialogAction.m_logger.error((Object)"DimensionalBagRoomManagerDialog.onClicktRoom()");
    }
    
    public static void changeGroupePermission(final Event event, final Form form, final DimensionalBagView dimensionalBag) {
        DimensionalBagRoomManagerDialogAction.m_logger.error((Object)"DimensionalBagRoomManagerDialogAction.changeGroupePermission()");
    }
    
    public static void closeDimensionalBagRoomManagerDialog(final Event event) {
        if (Xulor.getInstance().isLoaded("dimensionalBagRoomManagerDialog")) {
            PropertiesProvider.getInstance().setPropertyValue("showRoomBagDetails", false);
            WakfuGameEntity.getInstance().removeFrame(UIDimensionalBagRoomManagerFrame.getInstance());
        }
    }
    
    public static void changeBagName(final Event event, final DimensionalBagView dimensionalBag, final TextEditor name) {
        DimensionalBagRoomManagerDialogAction.m_logger.error((Object)"DimensionalBagRoomManagerDialogActiob.changeBagRoom()");
    }
    
    public static void showInfos(final Event event, final Window window, final String infos) {
        if (event.getType() == Events.MOUSE_CLICKED) {
            final Widget texte = (Widget)window.getElementMap().getElement(infos + "Info");
            texte.setVisible(!texte.getVisible());
            final Widget bouton = (Widget)window.getElementMap().getElement(infos + "Button");
            if (bouton.getStyle().equals("add")) {
                bouton.setStyle("remove");
            }
            else {
                bouton.setStyle("add");
            }
        }
    }
    
    public static void closeRoomDetail(final MouseEvent event) {
        PropertiesProvider.getInstance().setPropertyValue("showRoomBagDetails", false);
    }
    
    public static void emptyTextEditor(final MouseEvent event, final Window window, final TextEditor textEditor) {
        if (event.getType() == Events.MOUSE_RELEASED && textEditor.getId().equals("tempAccessTextEditor") && PropertiesProvider.getInstance().getBooleanProperty("isTempAccessTexteditorDefault")) {
            textEditor.setText("");
            PropertiesProvider.getInstance().setPropertyValue("isTempAccessTexteditorDefault", true);
        }
    }
    
    public static void setTempAccessTexteditorProperty(final KeyEvent event, final DimensionalBagView dimensionalBag, final TextEditor textEditor) {
        DimensionalBagRoomManagerDialogAction.m_logger.error((Object)"DimensionalBagRoomManagerDialogAction.setTempAccessTexteditorProperty()");
    }
    
    public static void setPermissionsToDefault(final MouseEvent event) {
        DimensionalBagRoomManagerDialogAction.m_logger.warn((Object)"(pas fait : ) Remise des permissions aux valeurs par d\u00e9faut ");
    }
    
    public static void dropItem(final DropEvent e, final String indexValue) {
        final Object value = e.getSourceValue();
        if (value instanceof GemItem) {
            final GemItem gem = (GemItem)value;
            final UIItemMessage message = new UIItemMessage();
            message.setItem(gem.getGem());
            message.setBooleanValue(true);
            message.setByteValue(PrimitiveConverter.getByte(indexValue));
            message.setIntValue(gem.getRoomIndex());
            message.setId(17007);
            Worker.getInstance().pushMessage(message);
        }
        else if (value instanceof Item) {
            final UIItemMessage message2 = new UIItemMessage();
            message2.setSourceUniqueId(SplitStackDialogActions.getSourceUniqueId());
            message2.setSourcePosition(SplitStackDialogActions.getSourcePosition());
            message2.setSourceUniqueId(SplitStackDialogActions.getSourceUniqueId());
            message2.setItem((Item)value);
            message2.setBooleanValue(false);
            message2.setByteValue(PrimitiveConverter.getByte(indexValue));
            message2.setId(17007);
            Worker.getInstance().pushMessage(message2);
        }
    }
    
    public static void closeDialog(final Event e) {
        WakfuGameEntity.getInstance().removeFrame(UIDimensionalBagRoomManagerFrame.getInstance());
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagRoomManagerDialogAction.class);
    }
}
