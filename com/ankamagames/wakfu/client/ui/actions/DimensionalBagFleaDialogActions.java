package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.xulor2.core.form.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.*;
import com.ankamagames.baseImpl.common.clientAndServer.account.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.Merchant.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;

@XulorActionsTag
public class DimensionalBagFleaDialogActions
{
    public static final String PACKAGE = "wakfu.dimensionalBagFlea";
    private static final Logger m_logger;
    private static boolean m_isOverIntersection;
    private static MessageHandler m_popupMessageHandler;
    
    public static void closeDimensionalBagFleaDialog(final Event event) {
        DimensionalBagFleaDialogActions.m_logger.error((Object)"DimensionalBagFleaDialog.closeDimensionalBagFleaDialog()");
        if (Xulor.getInstance().isLoaded("dimensionalBagFleaDialog")) {
            closeDimensionalBagFleaDialog();
        }
    }
    
    private static final void closeDimensionalBagFleaDialog() {
        PropertiesProvider.getInstance().setPropertyValue("showRoomBagDetails", false);
        WakfuGameEntity.getInstance().removeFrame(UIManageFleaFrame.getInstance());
    }
    
    public static void setShortAd(final Event event, final Form form) {
        if (event instanceof FocusChangedEvent) {
            final FocusChangedEvent focusEvent = (FocusChangedEvent)event;
            if (focusEvent.getTarget() == focusEvent.getCurrentTarget() && !focusEvent.getFocused()) {
                if (!form.isValid()) {
                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.flea.invalidAd"), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
                }
            }
        }
    }
    
    public static void dropItem(final Event event) {
        CursorFactory.getInstance().unlock();
        if (event instanceof DropEvent) {
            EquipmentDialogActions.onDropItem();
            final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
            final WakfuAccountInformationHolder customHolder = new WakfuAccountInformationHolder() {
                @Override
                public short getInstanceId() {
                    return DimensionalBagFromInstanceManager.INSTANCE.getFromInstanceId();
                }
                
                @Override
                public WakfuAccountInformationHandler getAccountInformationHandler() {
                    return localPlayer.getAccountInformationHandler();
                }
            };
            if (!WakfuAccountPermissionContext.SUBSCRIBER_ZONE.hasPermission(customHolder)) {
                final String errorMessage = WakfuTranslator.getInstance().getString("error.playerNotSubscribed");
                final ChatMessage m = new ChatMessage(errorMessage);
                m.setPipeDestination(3);
                ChatManager.getInstance().pushMessage(m);
                return;
            }
            final DropEvent dropEvent = (DropEvent)event;
            final Object item = dropEvent.getValue();
            final RenderableContainer rc = dropEvent.getDroppedInto().getRenderableParent();
            byte destPos = (byte)rc.getCollection().getTableIndex(rc);
            if (DimensionalBagFleaDialogActions.m_isOverIntersection) {
                ++destPos;
            }
            SplitStackDialogActions.setDestinationPosition(destPos);
            if (item instanceof MerchantInventoryItem) {
                final UIMerchantMessage uiMsg = new UIMerchantMessage();
                uiMsg.setMerchantItem((MerchantInventoryItem)item);
                uiMsg.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                uiMsg.setId(17307);
                Worker.getInstance().pushMessage(uiMsg);
            }
            else if (item instanceof Item) {
                final boolean shiftPressed = dropEvent.hasShift();
                final boolean defaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
                if (((Item)item).getQuantity() > 1 && ((shiftPressed && !defaultSplitMode) || (!shiftPressed && defaultSplitMode))) {
                    SplitStackDialogActions.setMaxQuantity(((Item)item).getQuantity());
                    SplitStackDialogActions.setItem((Item)item);
                    SplitStackDialogActions.setMessageType((short)17304);
                    final UIItemMessage uiMsg2 = new UIItemMessage();
                    uiMsg2.setItem((Item)item);
                    uiMsg2.setX((short)dropEvent.getScreenX());
                    uiMsg2.setY((short)dropEvent.getScreenY());
                    uiMsg2.setId(16821);
                    Worker.getInstance().pushMessage(uiMsg2);
                }
                else {
                    final UIMerchantMessage uiMsg3 = new UIMerchantMessage();
                    uiMsg3.setItem((Item)item);
                    uiMsg3.setDestinationPosition(SplitStackDialogActions.getDestinationPosition());
                    uiMsg3.setId(17304);
                    Worker.getInstance().pushMessage(uiMsg3);
                }
            }
        }
    }
    
    public static void itemDropOut(final DropOutEvent event) {
        if (event.getValue() instanceof MerchantInventoryItem) {
            final MerchantInventoryItem item = (MerchantInventoryItem)event.getValue();
            if (!event.hasShift() || item.getQuantity() <= 1) {
                final UIMerchantMessage uiMsg = new UIMerchantMessage();
                uiMsg.setMerchantItem(item);
                uiMsg.setId(17305);
                Worker.getInstance().pushMessage(uiMsg);
            }
        }
    }
    
    public static void removeMarketItem(final Event event, final MerchantInventoryItem item) {
        final UIMerchantMessage uiMsg = new UIMerchantMessage();
        uiMsg.setMerchantItem(item);
        uiMsg.setId(17305);
        Worker.getInstance().pushMessage(uiMsg);
    }
    
    public static void onMouseEnterIntersection(final Event event, final PlainBackground plainBackground) {
        if (MasterRootContainer.getInstance().isDragging()) {
            plainBackground.setColor(Color.BLACK);
            DimensionalBagFleaDialogActions.m_isOverIntersection = true;
        }
    }
    
    public static void onMouseExitIntersection(final Event event, final PlainBackground plainBackground) {
        plainBackground.setColor(Color.WHITE_ALPHA);
        DimensionalBagFleaDialogActions.m_isOverIntersection = false;
    }
    
    public static void showItemDetailPopup(final ItemEvent itemEvent, final Window window) {
        final Object item = itemEvent.getItemValue();
        if (!(item instanceof MerchantInventoryItem)) {
            return;
        }
        if (itemEvent.getType() == Events.ITEM_OVER) {
            final PopupElement popup = (PopupElement)window.getElementMap().getElement("itemDetailPopup");
            PropertiesProvider.getInstance().setPropertyValue("itemPopupDetail", itemEvent.getItemValue());
            XulorActions.popup(itemEvent, popup);
        }
        else if (itemEvent.getType() == Events.ITEM_OUT) {
            XulorActions.closePopup(itemEvent);
        }
    }
    
    public static void openStuffPreviewWindow(final Event e, final MerchantInventoryItem item) {
        ((ReferenceItem)item.getItem().getReferenceItem()).previewTradeEntry();
    }
    
    static {
        m_logger = Logger.getLogger((Class)DimensionalBagFleaDialogActions.class);
        DimensionalBagFleaDialogActions.m_isOverIntersection = false;
    }
}
