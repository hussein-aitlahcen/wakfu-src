package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.wakfu.client.core.game.storageBox.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.storageBox.guild.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;

@XulorActionsTag
public class StorageBoxDialogActions
{
    public static final String PACKAGE = "wakfu.storageBox";
    private static long m_draggedItemId;
    private static Image m_image;
    
    public static void selectCompartment(final Event e, final CompartmentView compartmentView) {
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setId(19328);
        uiMessage.setByteValue(compartmentView.getIndex());
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void unlockCompartment(final Event e) {
        UIMessage.send((short)19330);
    }
    
    public static void dropItem(final DropEvent e) {
        if (!(e.getValue() instanceof Item)) {
            return;
        }
        EquipmentDialogActions.onDropItem();
        final Item item = (Item)e.getValue();
        final boolean isInStorageBox = UIStorageBoxFrame.getInstance().getStorageBoxBoxView().getSelectedCompartment().contains(item);
        final boolean isInInventories = WakfuGameEntity.getInstance().getLocalPlayer().getBags().contains(item) != null;
        if (!isInInventories && !isInStorageBox) {
            return;
        }
        final boolean shiftPressed = e.hasShift();
        final boolean isDefaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
        final RenderableContainer rc = e.getDroppedInto().getRenderableParent();
        final byte destPos = (byte)rc.getCollection().getTableIndex(rc);
        final boolean move = item.getUniqueId() == StorageBoxDialogActions.m_draggedItemId;
        short messageId;
        if (move) {
            messageId = 19329;
        }
        else {
            messageId = 19326;
        }
        if (!move && item.getQuantity() > 1 && ((shiftPressed && !isDefaultSplitMode) || (!shiftPressed && isDefaultSplitMode))) {
            SplitStackDialogActions.setMaxQuantity(item.getQuantity());
            SplitStackDialogActions.setItem(item);
            SplitStackDialogActions.setMessageType(messageId);
            SplitStackDialogActions.setDestinationPosition(destPos);
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
            message.setDestinationPosition(destPos);
            message.setId(messageId);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void dragItem(final DragEvent dragEvent) {
        final Item item = (Item)dragEvent.getValue();
        StorageBoxDialogActions.m_draggedItemId = item.getUniqueId();
    }
    
    public static long getDraggedItemId() {
        return StorageBoxDialogActions.m_draggedItemId;
    }
    
    public static void setDraggedItemId(final long draggedItemId) {
        StorageBoxDialogActions.m_draggedItemId = draggedItemId;
    }
    
    public static void onMouseOverFilter(final ItemEvent event) {
        if (event.getItemValue().equals(UIStorageBoxFrame.getInstance().getStorageBoxBoxView().getSelectedCompartment())) {
            return;
        }
        StorageBoxDialogActions.m_image = (Image)event.getCurrentTarget().getInnerElementMap().getElement("image");
        ((StaticLayoutData)StorageBoxDialogActions.m_image.getLayoutData()).setYOffset(-1);
        StorageBoxDialogActions.m_image.getContainer().invalidateMinSize();
    }
    
    public static void onMouseOutFilter(final ItemEvent event) {
        if (StorageBoxDialogActions.m_image == null) {
            return;
        }
        ((StaticLayoutData)StorageBoxDialogActions.m_image.getLayoutData()).setYOffset(-3);
        StorageBoxDialogActions.m_image.getContainer().invalidateMinSize();
        StorageBoxDialogActions.m_image = null;
    }
    
    public static void seeItemDetails(final Event e) {
        final ReferenceItem itemToUnlock = ((CompartmentViewImpl)UIStorageBoxFrame.getInstance().getStorageBoxBoxView().getSelectedCompartment()).getItemToUnlock();
        Actions.sendOpenCloseItemDetailMessage("storageBoxDialog", itemToUnlock);
    }
    
    public static void onItemDoubleClick(final ItemEvent event) {
        final Item item = (Item)event.getItemValue();
        final UIItemMessage message = new UIItemMessage();
        message.setId(19327);
        message.setItem(item);
        message.setQuantity(item.getQuantity());
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
    
    public static void showItemDetails(final ItemEvent event) {
        if (event.getButton() != 3) {
            return;
        }
        if (event.getItemValue() instanceof GuildStorageHistoryItemEntryView) {
            final Item item = ((GuildStorageHistoryItemEntryView)event.getItemValue()).getItem();
            Actions.sendOpenCloseItemDetailMessage("storageBoxDialog", item);
        }
        else {
            Actions.sendOpenCloseItemDetailMessage("storageBoxDialog", (Item)event.getItemValue());
        }
    }
    
    public static void depositMoney(final Event e, final TextWidget moneyAmountTE) {
        final int value = PrimitiveConverter.getInteger(moneyAmountTE.getText());
        moneyAmountTE.setText("");
        final AbstractUIMessage msg = new UIMessage();
        msg.setId(19331);
        msg.setIntValue(value);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void withdrawMoney(final Event e, final TextWidget moneyAmountTE) {
        final int value = PrimitiveConverter.getInteger(moneyAmountTE.getText());
        moneyAmountTE.setText("");
        final AbstractUIMessage msg = new UIMessage();
        msg.setId(19332);
        msg.setIntValue(value);
        Worker.getInstance().pushMessage(msg);
    }
}
