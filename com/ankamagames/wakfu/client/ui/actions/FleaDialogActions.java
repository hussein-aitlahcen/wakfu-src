package com.ankamagames.wakfu.client.ui.actions;

import org.apache.log4j.*;
import com.ankamagames.xulor2.*;
import java.text.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.actions.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.merchant.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;

@XulorActionsTag
public class FleaDialogActions
{
    private static final Logger m_logger;
    public static final String PACKAGE = "wakfu.flea";
    private static MerchantInventoryItem m_selectedMerchantItem;
    private static short m_packQuantity;
    private static int m_totalPrice;
    private static MessageHandler m_popupMessageHandler;
    public static String FLEA_CONTROL_GROUP_ID;
    
    public static void selectItem(final ItemEvent event, final Window window) {
        if (event.getItemValue() != null && event.getItemValue() instanceof MerchantInventoryItem && event.getButton() == 3) {
            Actions.openItemDetailWindow(event, window);
        }
    }
    
    public static void purchaseItem(final Event e, final MerchantInventoryItem merchantInventoryItem, final Window window) {
        if (!UIAbstractBrowseFleaFrame.getBrowsingDimentionalBag().getDimensionalBagFlea().isLocked()) {
            if (FleaDialogActions.m_selectedMerchantItem == merchantInventoryItem && Xulor.getInstance().isLoaded("confirmFleaPurchaseDialog")) {
                Xulor.getInstance().unload("confirmFleaPurchaseDialog");
            }
            else {
                FleaDialogActions.m_selectedMerchantItem = merchantInventoryItem;
                FleaDialogActions.m_packQuantity = 1;
                FleaDialogActions.m_totalPrice = FleaDialogActions.m_selectedMerchantItem.getPrice();
                PropertiesProvider.getInstance().setPropertyValue("fleaSelectedGood", FleaDialogActions.m_selectedMerchantItem);
                PropertiesProvider.getInstance().setPropertyValue("currentItemQuantity", FleaDialogActions.m_packQuantity);
                PropertiesProvider.getInstance().setPropertyValue("currentItemTotalPrice", FleaDialogActions.m_totalPrice);
                PropertiesProvider.getInstance().setPropertyValue("currentItemFormatedTotalPrice", NumberFormat.getIntegerInstance().format(FleaDialogActions.m_totalPrice) + " §");
                PropertiesProvider.getInstance().setPropertyValue("currentItemStackQtyPlusTotal", PackTypeFieldProvider.getPackTypeRepresentation(FleaDialogActions.m_selectedMerchantItem.getPackType()) + " = " + FleaDialogActions.m_packQuantity * FleaDialogActions.m_selectedMerchantItem.getPackType().qty);
                final String windowId = window.getElementMap().getId();
                final Window itemWindow = (Window)Xulor.getInstance().loadAsMultiple("confirmFleaPurchaseDialog", Dialogs.getDialogPath("confirmFleaPurchaseDialog"), windowId, windowId, FleaDialogActions.FLEA_CONTROL_GROUP_ID, 145L, (short)10000);
            }
        }
    }
    
    public static void cancelPurchase(final Event event) {
        Xulor.getInstance().unload("confirmFleaPurchaseDialog");
    }
    
    public static void confirmPurchase(final Event event) {
        final Property prop = PropertiesProvider.getInstance().getProperty("fleaSelectedGood");
        if (prop != null) {
            final MerchantInventoryItem merchantInventoryItem = (MerchantInventoryItem)prop.getValue();
            if (WakfuGameEntity.getInstance().hasFrame(UIBrowseDimensionalBagFleaFrame.getInstance())) {
                final DimensionalBagFleaBuyRequestMessage buyRequestMessage = new DimensionalBagFleaBuyRequestMessage();
                buyRequestMessage.setItemUid(merchantInventoryItem.getUniqueId());
                buyRequestMessage.setQuantity(FleaDialogActions.m_packQuantity);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(buyRequestMessage);
            }
            else {
                final FleaBuyRequestMessage buyRequestMessage2 = new FleaBuyRequestMessage();
                buyRequestMessage2.setItemUid(merchantInventoryItem.getUniqueId());
                buyRequestMessage2.setQuantity(FleaDialogActions.m_packQuantity);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(buyRequestMessage2);
            }
            Xulor.getInstance().unload("confirmFleaPurchaseDialog", false);
        }
    }
    
    public static void confirmOutsidePurchase(final Event event) {
        final Property prop = PropertiesProvider.getInstance().getProperty("fleaSelectedGood");
        if (prop != null) {
            final MerchantInventoryItem merchantInventoryItem = (MerchantInventoryItem)prop.getValue();
            Xulor.getInstance().unload("confirmFleaPurchaseDialog", false);
        }
    }
    
    public static void decreaseQuantity(final Event event) {
        if (FleaDialogActions.m_packQuantity > 1) {
            --FleaDialogActions.m_packQuantity;
            applyQuantity();
        }
    }
    
    public static void increaseQuantity(final Event event) {
        if (FleaDialogActions.m_packQuantity * FleaDialogActions.m_selectedMerchantItem.getPackType().qty < FleaDialogActions.m_selectedMerchantItem.getQuantity()) {
            final long test = (FleaDialogActions.m_packQuantity + 1) * FleaDialogActions.m_selectedMerchantItem.getPrice();
            if (test > 2147483647L) {
                return;
            }
            ++FleaDialogActions.m_packQuantity;
            applyQuantity();
        }
    }
    
    public static void validQuantity(final Event e, final TextEditor te) {
        short qty = PrimitiveConverter.getShort(te.getText());
        final int totalQty = qty * FleaDialogActions.m_selectedMerchantItem.getPackType().qty;
        if (qty < 1) {
            qty = 1;
        }
        else if (totalQty > FleaDialogActions.m_selectedMerchantItem.getQuantity()) {
            qty = (short)(FleaDialogActions.m_selectedMerchantItem.getQuantity() / FleaDialogActions.m_selectedMerchantItem.getPackType().qty);
        }
        FleaDialogActions.m_packQuantity = qty;
        applyQuantity();
    }
    
    public static void setQuantity(final Event event) {
        if (event instanceof SliderMovedEvent) {
            final short levelValue = (short)((SliderMovedEvent)event).getValue();
            final long test = levelValue * FleaDialogActions.m_selectedMerchantItem.getPrice();
            if (test > 2147483647L) {
                return;
            }
            FleaDialogActions.m_packQuantity = levelValue;
            applyQuantity();
        }
    }
    
    public static void applyQuantity() {
        FleaDialogActions.m_totalPrice = FleaDialogActions.m_packQuantity * FleaDialogActions.m_selectedMerchantItem.getPrice();
        PropertiesProvider.getInstance().setPropertyValue("currentItemQuantity", FleaDialogActions.m_packQuantity);
        PropertiesProvider.getInstance().setPropertyValue("currentItemTotalPrice", FleaDialogActions.m_totalPrice);
        PropertiesProvider.getInstance().setPropertyValue("currentItemFormatedTotalPrice", NumberFormat.getIntegerInstance().format(FleaDialogActions.m_totalPrice) + " §");
        PropertiesProvider.getInstance().setPropertyValue("currentItemStackQtyPlusTotal", PackTypeFieldProvider.getPackTypeRepresentation(FleaDialogActions.m_selectedMerchantItem.getPackType()) + " = " + FleaDialogActions.m_packQuantity * FleaDialogActions.m_selectedMerchantItem.getPackType().qty);
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
    
    public static void selectFleaToBrowse(final ItemEvent e) {
        final MerchantInventory inventory = (MerchantInventory)e.getItemValue();
        UIBrowseDimensionalBagFleaFrame.getInstance().reflowFleaListOffset();
        final ObjectPair obj = UIBrowseDimensionalBagFleaFrame.getInstance().getFleaRefItem(inventory.getUid());
        if (obj == null) {
            return;
        }
        if (inventory.isLocked() || obj.getFirst() <= 0) {
            return;
        }
        final DimensionalBagFleaContentBrowseRequestMessage fleaContentBrowseRequestMessage = new DimensionalBagFleaContentBrowseRequestMessage();
        fleaContentBrowseRequestMessage.setMerchantInventoryUid(inventory.getUid());
        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(fleaContentBrowseRequestMessage);
        UIBrowseDimensionalBagFleaFrame.getInstance().loadFleaDialog(inventory);
    }
    
    public static void openStuffPreviewWindow(final Event e, final MerchantInventoryItem item) {
        ((ReferenceItem)item.getItem().getReferenceItem()).previewTradeEntry();
    }
    
    static {
        m_logger = Logger.getLogger((Class)FleaDialogActions.class);
        FleaDialogActions.FLEA_CONTROL_GROUP_ID = "fleaControlGroup";
    }
}
