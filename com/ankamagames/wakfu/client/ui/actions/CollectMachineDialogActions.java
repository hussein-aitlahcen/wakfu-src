package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.core.game.collector.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.message.collector.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

@XulorActionsTag
public class CollectMachineDialogActions
{
    public static final String PACKAGE = "wakfu.collectMachine";
    private static long m_draggedItemId;
    
    public static void valid(final Event e, final CollectorContentView collectorContentView) {
        final UICollectorMessage uiMessage = new UICollectorMessage(collectorContentView);
        uiMessage.setId(19323);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void setContentToMax(final Event e, final CollectorContentView lockedItemView) {
        final UICollectorMessage uiMessage = new UICollectorMessage(lockedItemView);
        uiMessage.setIntValue(-1);
        uiMessage.setId(19322);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void keyType(final Event e, final TextEditor textEditor, final CollectorContentView lockedItemView) {
        final String s = textEditor.getText();
        if (s.length() == 0) {
            return;
        }
        final int number = Integer.parseInt(s);
        final UICollectorMessage uiMessage = new UICollectorMessage(lockedItemView);
        uiMessage.setIntValue(number);
        uiMessage.setId(19322);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void dropItem(final DropEvent e) {
        if (!(e.getValue() instanceof Item)) {
            return;
        }
        final Item item = (Item)e.getValue();
        if (WakfuGameEntity.getInstance().getLocalPlayer().getBags().contains(item) == null) {
            return;
        }
        EquipmentDialogActions.onDropItem();
        final boolean shiftPressed = e.hasShift();
        final boolean isDefaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
        if (item.getQuantity() > 1 && ((shiftPressed && !isDefaultSplitMode) || (!shiftPressed && isDefaultSplitMode))) {
            SplitStackDialogActions.setMaxQuantity(item.getQuantity());
            SplitStackDialogActions.setItem(item);
            SplitStackDialogActions.setMessageType((short)19320);
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
            message.setShortValue(item.getQuantity());
            message.setId(19320);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void dragItem(final DragEvent dragEvent) {
        final Item item = (Item)dragEvent.getValue();
        CollectMachineDialogActions.m_draggedItemId = item.getUniqueId();
    }
    
    public static long getDraggedItemId() {
        return CollectMachineDialogActions.m_draggedItemId;
    }
    
    public static void setDraggedItemId(final long draggedItemId) {
        CollectMachineDialogActions.m_draggedItemId = draggedItemId;
    }
    
    public static void giveKamas(final MouseEvent e) {
        moveKamas(e, true);
    }
    
    public static void takeKamas(final MouseEvent e) {
        moveKamas(e, false);
    }
    
    public static void moveKamas(final MouseEvent e, final boolean give) {
        if (e.getButton() == 1) {
            final UIItemMessage message = new UIItemMessage();
            final Widget container = (Widget)Xulor.getInstance().getEnvironment().getElementMap("freeCollectMachineDialog").getElement("localMoney");
            final short widthLag = 65;
            final short heightLag = 20;
            SplitStackDialogActions.setMessageType((short)(give ? 19324 : 19325));
            SplitStackDialogActions.setMaxQuantity(give ? WakfuGameEntity.getInstance().getLocalPlayer().getKamasCount() : UICollectMachineFrame.getInstance().getAbstractCollectMachineView().getKamaQuantity());
            message.setX((short)(container.getScreenX() + widthLag));
            message.setY((short)(container.getScreenY() + heightLag));
            message.setId(16821);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void onItemDoubleClick(final ItemEvent event) {
        final Item item = (Item)event.getItemValue();
        CollectMachineDialogActions.m_draggedItemId = item.getUniqueId();
        final UIItemMessage message = new UIItemMessage();
        message.setItem(item);
        message.setLongValue(item.getUniqueId());
        message.setQuantity(item.getQuantity());
        message.setId(19321);
        message.setDestinationUniqueId(SplitStackDialogActions.getDestinationUniqueId());
        message.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
        Worker.getInstance().pushMessage(message);
    }
    
    public static void changeItemBackground(final MouseEvent event, final Item item, final Window window, final PopupElement popup) {
        String style = "";
        final Widget w = event.getTarget();
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (item != null) {
            boolean in = false;
            if (event.getType() == Events.MOUSE_ENTERED) {
                in = true;
                if (item.getReferenceItem().getSetId() != 0) {
                    style = "itemSetSelectedBackground";
                }
                else {
                    style = "itemSelectedBackground";
                }
            }
            else if (event.getType() == Events.MOUSE_EXITED) {
                style = ItemDisplayerImpl.getBackgroundStyle(item);
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
}
