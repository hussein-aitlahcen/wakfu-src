package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.event.*;

@XulorActionsTag
public class HavenWorldResourcesCollectorDialogActions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu.havenWorldResourcesCollector";
    private static long m_draggedItemId;
    
    public static void closeWindow(final Event e) {
        if (!UIHavenWorldResourcesCollectorFrame.getInstance().getHavenWorldResourcesCollectorView().isEmpty()) {
            final String msgText = WakfuTranslator.getInstance().getString("question.havenWorldResourcesCollectorClose");
            final MessageBoxData data = new MessageBoxData(102, 0, msgText, null, WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 24L);
            final MessageBoxControler controler = Xulor.getInstance().msgBox(data);
            controler.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    if (type == 8) {
                        unloadDialog();
                    }
                }
            });
        }
        else {
            unloadDialog();
        }
    }
    
    private static void unloadDialog() {
        Xulor.getInstance().unload("havenWorldResourcesCollectorDialog");
    }
    
    public static void validCollect(final Event e) {
        UIMessage.send((short)19363);
    }
    
    public static void removeItem(final Event e) {
        if (e instanceof DropOutEvent && ((DropOutEvent)e).getValue() instanceof FakeItem) {
            removeItem((FakeItem)((DropOutEvent)e).getValue());
        }
    }
    
    public static void onItemDoubleClick(final ItemEvent event) {
        if (!(event.getItemValue() instanceof FakeItem)) {
            return;
        }
        removeItem((FakeItem)event.getItemValue());
    }
    
    public static void removeItem(final FakeItem item) {
        final UIFakeItemMessage uiFakeItemMessage = new UIFakeItemMessage(item);
        uiFakeItemMessage.setId(19365);
        Worker.getInstance().pushMessage(uiFakeItemMessage);
    }
    
    public static void dropItem(final DropEvent e) {
        if (!(e.getValue() instanceof Item)) {
            return;
        }
        final Item item = (Item)e.getValue();
        final boolean isInInventories = WakfuGameEntity.getInstance().getLocalPlayer().getBags().contains(item) != null;
        if (!isInInventories) {
            return;
        }
        EquipmentDialogActions.onDropItem();
        final boolean shiftPressed = e.hasShift();
        final boolean isDefaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
        final short messageId = 19364;
        if (item.getQuantity() > 1 && ((shiftPressed && !isDefaultSplitMode) || (!shiftPressed && isDefaultSplitMode))) {
            SplitStackDialogActions.setMaxQuantity(item.getQuantity());
            SplitStackDialogActions.setItem(item);
            SplitStackDialogActions.setMessageType((short)19364);
            final UIItemMessage message = new UIItemMessage();
            message.setItem(item);
            message.setX((short)e.getScreenX());
            message.setY((short)e.getScreenY());
            message.setId(16821);
            Worker.getInstance().pushMessage(message);
        }
        else {
            final UIItemMessage message = new UIItemMessage();
            message.setLongValue(item.getUniqueId());
            message.setQuantity(item.getQuantity());
            message.setId(19364);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void changeItemBackground(final MouseEvent event, final Item item) {
        changeItemBackground(event, item, null);
    }
    
    public static void changeItemBackground(final MouseEvent event) {
        String style = "";
        final Widget w = event.getTarget();
        if (event.getType() == Events.MOUSE_ENTERED) {
            style = "itemSelectedBackground";
        }
        else if (event.getType() == Events.MOUSE_EXITED) {
            style = "itemBackground";
        }
        w.setStyle(style);
    }
    
    public static void changeItemBackground(final MouseEvent event, final Item item, final PopupElement popup) {
        String style = "";
        final Widget w = event.getTarget();
        if (item != null) {
            if (event.getType() == Events.MOUSE_ENTERED) {
                style = "itemSelectedBackground";
            }
            else if (event.getType() == Events.MOUSE_EXITED) {
                style = "itemBackground";
            }
            w.setStyle(style);
        }
        if (popup != null) {
            if (event.getType() == Events.MOUSE_ENTERED) {
                PopupInfosActions.showPopup(event, item, popup, w);
            }
            else {
                PopupInfosActions.closePopup(event);
            }
        }
    }
    
    public static void dragItem(final DragEvent dragEvent) {
        final Item item = (Item)dragEvent.getValue();
        HavenWorldResourcesCollectorDialogActions.m_draggedItemId = item.getUniqueId();
    }
    
    public static long getDraggedItemId() {
        return HavenWorldResourcesCollectorDialogActions.m_draggedItemId;
    }
    
    public static void setDraggedItemId(final long draggedItemId) {
        HavenWorldResourcesCollectorDialogActions.m_draggedItemId = draggedItemId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldResourcesCollectorDialogActions.class);
        HavenWorldResourcesCollectorDialogActions.m_draggedItemId = -1L;
    }
}
