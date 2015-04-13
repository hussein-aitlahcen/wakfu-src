package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.hero.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

@XulorActionsTag
public class VaultDialogActions
{
    public static final String PACKAGE = "wakfu.vault";
    private static long m_draggedItemId;
    private static Image m_image;
    
    public static void dropItem(final DropEvent e) {
        if (!(e.getValue() instanceof Item)) {
            return;
        }
        EquipmentDialogActions.onDropItem();
        final Item item = (Item)e.getValue();
        final boolean isInVault = UIVaultFrame.getInstance().getVaultView().contains(item);
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final boolean isInInventories = HeroUtils.getItemFromHero(localPlayer.getOwnerId(), item.getUniqueId()) != null;
        if (!isInInventories && !isInVault) {
            return;
        }
        final boolean shiftPressed = e.hasShift();
        final boolean isDefaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
        final RenderableContainer rc = e.getDroppedInto().getRenderableParent();
        final byte destPos = (byte)rc.getCollection().getTableIndex(rc);
        final boolean move = item.getUniqueId() == VaultDialogActions.m_draggedItemId;
        short messageId;
        if (move) {
            messageId = 19335;
        }
        else {
            messageId = 19333;
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
        VaultDialogActions.m_draggedItemId = item.getUniqueId();
    }
    
    public static long getDraggedItemId() {
        return VaultDialogActions.m_draggedItemId;
    }
    
    public static void setDraggedItemId(final long draggedItemId) {
        VaultDialogActions.m_draggedItemId = draggedItemId;
    }
    
    public static void onMouseOverFilter(final ItemEvent event) {
        if (event.getItemValue().equals(UIStorageBoxFrame.getInstance().getStorageBoxBoxView().getSelectedCompartment())) {
            return;
        }
        VaultDialogActions.m_image = (Image)event.getCurrentTarget().getInnerElementMap().getElement("image");
        ((StaticLayoutData)VaultDialogActions.m_image.getLayoutData()).setYOffset(-1);
        VaultDialogActions.m_image.getContainer().invalidateMinSize();
    }
    
    public static void onMouseOutFilter(final ItemEvent event) {
        if (VaultDialogActions.m_image == null) {
            return;
        }
        ((StaticLayoutData)VaultDialogActions.m_image.getLayoutData()).setYOffset(-3);
        VaultDialogActions.m_image.getContainer().invalidateMinSize();
        VaultDialogActions.m_image = null;
    }
    
    public static void onItemDoubleClick(final ItemEvent event) {
        final Item item = (Item)event.getItemValue();
        final UIItemMessage message = new UIItemMessage();
        message.setId(19334);
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
        Actions.sendOpenCloseItemDetailMessage("vaultDialog", (Item)event.getItemValue());
    }
    
    public static void depositMoney(final Event e, final TextWidget moneyAmountTE) {
        final int value = PrimitiveConverter.getInteger(moneyAmountTE.getText());
        moneyAmountTE.setText("");
        final AbstractUIMessage msg = new UIMessage();
        msg.setId(19336);
        msg.setIntValue(value);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void withdrawMoney(final Event e, final TextWidget moneyAmountTE) {
        final int value = PrimitiveConverter.getInteger(moneyAmountTE.getText());
        moneyAmountTE.setText("");
        final AbstractUIMessage msg = new UIMessage();
        msg.setId(19337);
        msg.setIntValue(value);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void buyUpgrade(final Event e, final Article article) {
        UIWebShopFrame.getInstance().openArticleDialog(article);
    }
}
