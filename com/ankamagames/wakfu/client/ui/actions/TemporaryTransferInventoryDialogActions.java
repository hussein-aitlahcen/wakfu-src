package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.*;

@XulorActionsTag
public class TemporaryTransferInventoryDialogActions
{
    protected static final Logger m_logger;
    public static final String PACKAGE = "wakfu.temporaryInventory";
    private static MessageHandler m_popupMessageHandler;
    private static long m_itemId;
    
    public static void selectHero(final MouseEvent e, final LocalPlayerCharacter localPlayer) {
        final UIMessage message = new UIMessage((short)19461);
        message.setLongValue(localPlayer.getId());
        Worker.getInstance().pushMessage(message);
    }
    
    public static void closeTemporaryInventory(final Event event) {
        if (event.getType() == Events.MOUSE_CLICKED) {
            UIMessage.send((short)16431);
        }
    }
    
    public static void minimizeMaximizeDialog(final MouseEvent event, final Window window) {
        if (event.getType() == Events.MOUSE_DOUBLE_CLICKED) {
            final Widget container = (Widget)window.getElementMap().getElement("contentWindow");
            if (!container.getVisible()) {
                window.setPrefSize(new Dimension(0, 0));
            }
            else {
                window.setPrefSize(new Dimension(0, container.getHeight()));
            }
            container.setVisible(!container.getVisible());
            final Widget button = (Widget)window.getElementMap().getElement("barCloseButton");
            button.setVisible(!button.getVisible());
        }
    }
    
    public static void showItemDetailPopup(final ItemEvent itemEvent, final Window window) {
        final Object item = itemEvent.getItemValue();
        PopupInfosActions.showPopup((FieldProvider)item, 500);
    }
    
    public static void dropOut(final DropOutEvent event) {
        if (event.getValue() instanceof Item) {
            final Item item = (Item)event.getValue();
            if (MasterRootContainer.getInstance().getWidget(event.getScreenX(), event.getScreenY()) == MasterRootContainer.getInstance()) {
                if (item.isRent() || (item.getReferenceItem().getCriterion(ActionsOnItem.DROP) != null && !item.getReferenceItem().getCriterion(ActionsOnItem.DROP).isValid(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer().getPosition(), item, WakfuGameEntity.getInstance().getLocalPlayer().getEffectContext()))) {
                    final ChatMessage chatMessage = new ChatMessage(WakfuTranslator.getInstance().getString("item.unDroppable", item.getQuantity(), item.getName()));
                    chatMessage.setPipeDestination(3);
                    ChatManager.getInstance().pushMessage(chatMessage);
                    return;
                }
                if (item.getUniqueId() == TemporaryTransferInventoryDialogActions.m_itemId) {
                    if (event.hasShift() && item.getQuantity() > 1) {
                        SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                        SplitStackDialogActions.setItem(item);
                        SplitStackDialogActions.setMessageType((short)16825);
                        final UIItemMessage message = new UIItemMessage();
                        message.setItem(item);
                        message.setX((short)event.getScreenX());
                        message.setY((short)event.getScreenY());
                        message.setId(16821);
                        Worker.getInstance().pushMessage(message);
                    }
                    else {
                        final UIItemMessage message = new UIItemMessage();
                        message.setItem(item);
                        message.setQuantity(item.getQuantity());
                        message.setId(16825);
                        message.setDestinationUniqueId(-1L);
                        Worker.getInstance().pushMessage(message);
                    }
                }
            }
        }
    }
    
    public static void dragItem(final DragEvent dragEvent) {
        if (Xulor.getInstance().isLoaded("splitStackDialog")) {
            Xulor.getInstance().unload("splitStackDialog");
        }
        if (dragEvent.getSourceValue() instanceof Item) {
            final Item dragItem = (Item)dragEvent.getSourceValue();
            TemporaryTransferInventoryDialogActions.m_itemId = dragItem.getUniqueId();
            PropertiesProvider.getInstance().setPropertyValue("temporaryInventory.currentDragItemId", dragItem.getUniqueId());
        }
    }
    
    public static void onItemDoubleClick(final ItemEvent event) {
        if (Xulor.getInstance().isLoaded("splitStackDialog")) {
            Xulor.getInstance().unload("splitStackDialog");
        }
        final Item item = (Item)event.getItemValue();
        final UIItemMessage message = new UIItemMessage();
        message.setId(16825);
        message.setItem(item);
        message.setQuantity(item.getQuantity());
        final ClientBagContainer bagContainer = UIEquipmentFrame.getCharacter().getBags();
        AbstractBag correctBag = bagContainer.getFirstBagWithStackablePlace(item);
        byte position = (byte)((correctBag == null) ? -1 : correctBag.getPosition(correctBag.getFirstWithReferenceId(item.getReferenceId())));
        if (correctBag == null) {
            correctBag = bagContainer.getFirstContainerWithFreePlaceFor(item);
            if (correctBag != null) {
                position = (byte)correctBag.getFirstFreeIndex();
            }
        }
        if (correctBag == null) {
            return;
        }
        TemporaryTransferInventoryDialogActions.m_itemId = item.getUniqueId();
        PropertiesProvider.getInstance().setPropertyValue("temporaryInventory.currentDragItemId", item.getUniqueId());
        message.setDestinationUniqueId(correctBag.getUid());
        message.setDestinationPosition(position);
        Worker.getInstance().pushMessage(message);
    }
    
    static {
        m_logger = Logger.getLogger((Class)TemporaryTransferInventoryDialogActions.class);
    }
}
