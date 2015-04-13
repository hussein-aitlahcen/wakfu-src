package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import java.awt.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.framework.java.util.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.market.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.message.Merchant.*;
import java.text.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.wakfu.common.game.market.constant.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.core.game.market.*;
import com.ankamagames.wakfu.client.core.game.pet.newPet.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;

@XulorActionsTag
public class MarketDialogActions
{
    public static final String PACKAGE = "wakfu.market";
    private static final Logger m_logger;
    private static short m_packQuantity;
    private static int m_totalPrice;
    private static UIMarketRequestMessage m_lastSearchRequest;
    private static ItemType m_lastSearchType;
    private static Color m_levelDefaultTextColor;
    private static Color m_priceDefaultTextColor;
    
    public static void clean() {
        MarketDialogActions.m_packQuantity = -1;
        MarketDialogActions.m_totalPrice = -1;
        MarketDialogActions.m_lastSearchRequest = new UIMarketRequestMessage();
        MarketDialogActions.m_lastSearchType = null;
        MarketDialogActions.m_levelDefaultTextColor = null;
        MarketDialogActions.m_priceDefaultTextColor = null;
    }
    
    public static void displayPage(final SelectionChangedEvent e) {
        if (e.isSelected()) {
            final RadioButton radioButton = e.getTarget();
            final int index = Integer.valueOf(radioButton.getValue());
            PropertiesProvider.getInstance().setLocalPropertyValue("currentPage", index, radioButton.getElementMap());
            final UIMessage msg = new UIMessage();
            msg.setId(19264);
            msg.setIntValue(index);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void clearSearch(final Event e, final ToggleButton tb, final TextEditor editor, final TextEditor minLevel, final TextEditor maxLevel, final TextEditor minPrice, final TextEditor maxPrice) {
        tb.setSelected(true);
        editor.resetGhostText();
        minLevel.setText("");
        maxLevel.setText("");
        minPrice.setText("");
        maxPrice.setText("");
        MarketView.INSTANCE.setCurrentSearchItemType1(null);
        MarketView.INSTANCE.setCurrentSearchItemType2(null);
        MarketView.INSTANCE.setCurrentSearchItemType3(null);
        MarketView.INSTANCE.clearSelectedElements();
        MarketDialogActions.m_lastSearchType = null;
        final UIMarketRequestMessage msg = new UIMarketRequestMessage();
        msg.setId(19253);
        Worker.getInstance().pushMessage(msg);
        MarketDialogActions.m_lastSearchRequest = msg;
    }
    
    public static void search(final Event e, final ToggleButton tb, final TextEditor editor, final TextEditor minLevel, final TextEditor maxLevel, final TextEditor minPrice, final TextEditor maxPrice) {
        final boolean searchLocked = !PropertiesProvider.getInstance().getBooleanProperty("marketSearchDirty") || PropertiesProvider.getInstance().getBooleanProperty("marketSearchInvalid");
        if (searchLocked || (e instanceof KeyEvent && ((KeyEvent)e).getKeyCode() != 10)) {
            return;
        }
        final UIMarketRequestMessage msg = new UIMarketRequestMessage();
        msg.setId(19253);
        short minLevelValue = -1;
        short maxLevelValue = -1;
        final String minLevelText = minLevel.getText();
        final String maxLevelText = maxLevel.getText();
        if (minLevelText != null && minLevelText.length() > 0) {
            minLevelValue = PrimitiveConverter.getShort(minLevelText);
        }
        if (maxLevelText != null && maxLevelText.length() > 0) {
            maxLevelValue = PrimitiveConverter.getShort(maxLevelText);
        }
        short minPriceValue = -1;
        short maxPriceValue = -1;
        final String minPriceText = minPrice.getText();
        final String maxPriceText = maxPrice.getText();
        if (minPriceText != null && minPriceText.length() > 0) {
            minPriceValue = PrimitiveConverter.getShort(minPriceText);
        }
        if (maxPriceText != null && maxPriceText.length() > 0) {
            maxPriceValue = PrimitiveConverter.getShort(maxPriceText);
        }
        msg.setMinLevel(minLevelValue);
        msg.setMaxLevel(maxLevelValue);
        msg.setMinPrice(minPriceValue);
        msg.setMaxPrice(maxPriceValue);
        msg.setName(editor.getText());
        msg.setLowestPrices(tb.getSelected());
        msg.setElementsMask(MarketView.INSTANCE.getElementMask());
        Worker.getInstance().pushMessage(msg);
        MarketDialogActions.m_lastSearchRequest = msg;
        MarketDialogActions.m_lastSearchType = MarketView.INSTANCE.getCurrentSearchItemType();
    }
    
    public static void selectElement(final Event e, final ElementFilterView marketElementFilterView) {
        final ToggleButton tb = e.getCurrentTarget();
        marketElementFilterView.setSelected(tb.getSelected());
    }
    
    public static void chooseCategory(final ListSelectionChangedEvent event, final String index, final ToggleButton tb, final TextEditor editor, final TextEditor minLevel, final TextEditor maxLevel, final TextEditor minPrice, final TextEditor maxPrice) {
        try {
            final ItemTypeFieldProvider itemTypeFieldProvider = (ItemTypeFieldProvider)event.getValue();
            final ItemType itemType = itemTypeFieldProvider.getItemType();
            PropertiesProvider.getInstance().setPropertyValue("marketSearchDirty", itemType != MarketDialogActions.m_lastSearchType);
            switch (Byte.parseByte(index)) {
                case 1: {
                    MarketView.INSTANCE.setCurrentSearchItemType1(itemType);
                    break;
                }
                case 2: {
                    MarketView.INSTANCE.setCurrentSearchItemType2(itemType);
                    break;
                }
                case 3: {
                    MarketView.INSTANCE.setCurrentSearchItemType3(itemType);
                    break;
                }
            }
            setSearchDirty(event, itemType, MarketView.INSTANCE.getElementMask(), tb, editor, minLevel, maxLevel, minPrice, maxPrice);
        }
        catch (Exception e) {
            MarketDialogActions.m_logger.error((Object)("impossible de conertir l'index=" + index + " dans le choix de la cat\u00e9gorie"));
        }
    }
    
    public static void setSearchDirty(final Event e, final ToggleButton tb, final TextEditor editor, final TextEditor minLevel, final TextEditor maxLevel, final TextEditor minPrice, final TextEditor maxPrice) {
        setSearchDirty(e, MarketView.INSTANCE.getCurrentSearchItemType(), MarketView.INSTANCE.getElementMask(), tb, editor, minLevel, maxLevel, minPrice, maxPrice);
    }
    
    public static void setSearchDirty(final Event e, final ItemType itemType, final byte elementMask, final ToggleButton tb, final TextEditor editor, final TextEditor minLevel, final TextEditor maxLevel, final TextEditor minPrice, final TextEditor maxPrice) {
        short minLevelValue = -1;
        short maxLevelValue = -1;
        final String minLevelText = minLevel.getText();
        final String maxLevelText = maxLevel.getText();
        boolean dirty = false;
        boolean invalid = false;
        if (itemType != MarketDialogActions.m_lastSearchType) {
            dirty = true;
        }
        if (MarketDialogActions.m_lastSearchRequest.getElementsMask() != elementMask) {
            dirty = true;
        }
        if (minLevelText != null && minLevelText.length() > 0) {
            minLevelValue = PrimitiveConverter.getShort(minLevelText);
        }
        if (MarketDialogActions.m_lastSearchRequest.getMinLevel() != minLevelValue) {
            dirty = true;
        }
        if (maxLevelText != null && maxLevelText.length() > 0) {
            maxLevelValue = PrimitiveConverter.getShort(maxLevelText);
        }
        if (MarketDialogActions.m_lastSearchRequest.getMaxLevel() != maxLevelValue) {
            dirty = true;
        }
        int minPriceValue = -1;
        int maxPriceValue = -1;
        final String minPriceText = minPrice.getText();
        final String maxPriceText = maxPrice.getText();
        if (minPriceText != null && minPriceText.length() > 0) {
            minPriceValue = PrimitiveConverter.getInteger(minPriceText);
        }
        if (MarketDialogActions.m_lastSearchRequest.getMinPrice() != minPriceValue) {
            dirty = true;
        }
        if (maxPriceText != null && maxPriceText.length() > 0) {
            maxPriceValue = PrimitiveConverter.getInteger(maxPriceText);
        }
        if (MarketDialogActions.m_lastSearchRequest.getMaxPrice() != maxPriceValue) {
            dirty = true;
        }
        if (!MarketDialogActions.m_lastSearchRequest.getName().equals(editor.getText())) {
            dirty = true;
        }
        if (MarketDialogActions.m_lastSearchRequest.isLowestPrices() != tb.getSelected()) {
            dirty = true;
        }
        if (((minLevelValue != -1 || maxLevelValue != -1) && minLevelValue != -1 && maxLevelValue != -1 && maxLevelValue <= minLevelValue) || maxLevelValue == 0 || minLevelValue == 0) {
            if (MarketDialogActions.m_levelDefaultTextColor == null) {
                MarketDialogActions.m_levelDefaultTextColor = minLevel.getTextBuilder().getDefaultColor();
            }
            invalid = true;
            if (minLevelValue != -1 && maxLevelValue != -1 && maxLevelValue <= minLevelValue) {
                minLevel.setColor(com.ankamagames.framework.graphics.image.Color.RED, "text");
                maxLevel.setColor(com.ankamagames.framework.graphics.image.Color.RED, "text");
            }
            else if (maxLevelValue == 0) {
                maxLevel.setColor(com.ankamagames.framework.graphics.image.Color.RED, "text");
                if (minLevelValue != 0) {
                    minLevel.getTextBuilder().setDefaultColor(MarketDialogActions.m_levelDefaultTextColor);
                }
            }
            else if (minLevelValue == 0) {
                minLevel.setColor(com.ankamagames.framework.graphics.image.Color.RED, "text");
                if (maxLevelValue != 0) {
                    maxLevel.getTextBuilder().setDefaultColor(MarketDialogActions.m_levelDefaultTextColor);
                }
            }
        }
        else if (MarketDialogActions.m_levelDefaultTextColor != null) {
            minLevel.getTextBuilder().setDefaultColor(MarketDialogActions.m_levelDefaultTextColor);
            maxLevel.getTextBuilder().setDefaultColor(MarketDialogActions.m_levelDefaultTextColor);
            MarketDialogActions.m_levelDefaultTextColor = null;
        }
        if ((minPriceValue != -1 || maxPriceValue != -1) && ((minPriceValue != -1 && maxPriceValue != -1 && maxPriceValue <= minPriceValue) || maxPriceValue == 0 || minPriceValue == 0)) {
            invalid = true;
            if (MarketDialogActions.m_priceDefaultTextColor == null) {
                MarketDialogActions.m_priceDefaultTextColor = minPrice.getTextBuilder().getDefaultColor();
            }
            if (minPriceValue != -1 && maxPriceValue != -1 && maxPriceValue <= minPriceValue) {
                minPrice.setColor(com.ankamagames.framework.graphics.image.Color.RED, "text");
                maxPrice.setColor(com.ankamagames.framework.graphics.image.Color.RED, "text");
            }
            else if (maxPriceValue == 0) {
                maxPrice.setColor(com.ankamagames.framework.graphics.image.Color.RED, "text");
                if (minPriceValue != 0) {
                    minPrice.getTextBuilder().setDefaultColor(MarketDialogActions.m_priceDefaultTextColor);
                }
            }
            else if (minPriceValue == 0) {
                minPrice.setColor(com.ankamagames.framework.graphics.image.Color.RED, "text");
                if (maxPriceValue != 0) {
                    maxPrice.getTextBuilder().setDefaultColor(MarketDialogActions.m_priceDefaultTextColor);
                }
            }
        }
        else if (MarketDialogActions.m_priceDefaultTextColor != null) {
            minPrice.getTextBuilder().setDefaultColor(MarketDialogActions.m_priceDefaultTextColor);
            maxPrice.getTextBuilder().setDefaultColor(MarketDialogActions.m_priceDefaultTextColor);
            MarketDialogActions.m_priceDefaultTextColor = null;
        }
        PropertiesProvider.getInstance().setPropertyValue("marketSearchDirty", dirty);
        PropertiesProvider.getInstance().setPropertyValue("marketSearchInvalid", invalid);
    }
    
    public static void nextResults(final Event e) {
        UIMessage.send((short)19242);
        final Button b = e.getTarget();
        b.getAppearance().exit();
    }
    
    public static void prevResults(final Event e) {
        UIMessage.send((short)19243);
        final Button b = e.getTarget();
        b.getAppearance().exit();
    }
    
    public static void startResults(final Event e) {
        UIMessage.send((short)19244);
        final Button b = e.getTarget();
        b.getAppearance().exit();
    }
    
    public static void endResults(final Event e) {
        UIMessage.send((short)19245);
        final Button b = e.getTarget();
        b.getAppearance().exit();
    }
    
    public static void applyAuction(final Event e) {
        if (e instanceof KeyEvent && ((KeyEvent)e).getKeyCode() != 10) {
            return;
        }
        UIMessage.send((short)19259);
    }
    
    public static void cancelAuction(final Event e) {
        UIMessage.send((short)19266);
    }
    
    public static void dropItem(final DropEvent e) {
        EquipmentDialogActions.onDropItem();
        UIMarketFrame.getInstance().stopDropZoneHighlight();
        if (!(e.getValue() instanceof Item)) {
            MarketDialogActions.m_logger.error((Object)"Impossible de traiter un drop \u00e0 cet endroit car il ne s'agit pas d'un item");
            return;
        }
        final Item item = (Item)e.getValue();
        if (WakfuGameEntity.getInstance().getLocalPlayer().getBags().getFirstContainerWith(item.getUniqueId()) == null) {
            MarketDialogActions.m_logger.error((Object)"Impossible de traiter un drop \u00e0 cet endroit car l'item en question n'est pas pr\u00e9sent dans les inventaires");
            return;
        }
        final boolean shiftPressed = e.hasShift();
        final boolean defaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
        if (item.getQuantity() > 1 && ((shiftPressed && !defaultSplitMode) || (!shiftPressed && defaultSplitMode))) {
            SplitStackDialogActions.setMaxQuantity(item.getQuantity());
            SplitStackDialogActions.setItem(item);
            SplitStackDialogActions.setMessageType((short)19258);
            final UIItemMessage uiMsg = new UIItemMessage();
            uiMsg.setItem(item);
            uiMsg.setX((short)e.getScreenX());
            uiMsg.setY((short)e.getScreenY());
            uiMsg.setId(16821);
            Worker.getInstance().pushMessage(uiMsg);
        }
        else {
            final UIMarketAddItem uiMarketItemMessage = new UIMarketAddItem();
            uiMarketItemMessage.setShortValue(item.getQuantity());
            uiMarketItemMessage.setItem(item);
            Worker.getInstance().pushMessage(uiMarketItemMessage);
        }
        EquipmentDialogActions.onDropItem();
    }
    
    public static void priceChanged(final Event event) {
        final String s = event.getTarget().getText();
        if (s.length() == 0) {
            return;
        }
        final int price = Integer.parseInt(s);
        final UIMessage msg = new UIMessage();
        msg.setId(19260);
        msg.setIntValue(price);
        Worker.getInstance().pushMessage(msg);
    }
    
    public static void packChanged(final ListSelectionChangedEvent event) {
        if (event.getSelected()) {
            final PackTypeFieldProvider packType = (PackTypeFieldProvider)event.getValue();
            final UIMerchantChangePackTypeMessage msg = new UIMerchantChangePackTypeMessage();
            msg.setPackType(packType.getPackType());
            msg.setId(19261);
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    public static void purchaseItem(final Event e, final Window window, final MarketEntryView marketEntryView) {
        MarketDialogActions.m_packQuantity = 1;
        MarketDialogActions.m_totalPrice = marketEntryView.getPrice();
        PropertiesProvider.getInstance().setPropertyValue("marketItemPurchase", marketEntryView);
        PropertiesProvider.getInstance().setPropertyValue("currentItemQuantity", MarketDialogActions.m_packQuantity);
        PropertiesProvider.getInstance().setPropertyValue("currentItemTotalPrice", MarketDialogActions.m_totalPrice);
        PropertiesProvider.getInstance().setPropertyValue("currentItemFormatedTotalPrice", NumberFormat.getIntegerInstance().format(MarketDialogActions.m_totalPrice) + " §");
        final PackType packType = marketEntryView.getPackType();
        PropertiesProvider.getInstance().setPropertyValue("currentItemStackQtyPlusTotal", PackTypeFieldProvider.getPackTypeRepresentation(packType) + " = " + MarketDialogActions.m_packQuantity * packType.qty);
        final String windowId = window.getElementMap().getId();
        final Window itemWindow = (Window)Xulor.getInstance().load("confirmMarketPurchaseDialog", Dialogs.getDialogPath("confirmMarketPurchaseDialog"), 385L, (short)10000);
        itemWindow.addWindowPostProcessedListener(new WindowPostProcessedListener() {
            @Override
            public void windowPostProcessed() {
                itemWindow.setPosition(MouseManager.getInstance().getX() - itemWindow.getWidth(), MouseManager.getInstance().getY());
                itemWindow.removeWindowPostProcessedListener(this);
            }
        });
    }
    
    public static void confirmPurchase(final Event event) {
        final Property prop = PropertiesProvider.getInstance().getProperty("marketItemPurchase");
        if (prop != null) {
            final MarketEntryView marketEntryView = (MarketEntryView)prop.getValue();
            final UIMessage uiMessage = new UIMessage();
            uiMessage.setLongValue(marketEntryView.getId());
            uiMessage.setShortValue(MarketDialogActions.m_packQuantity);
            uiMessage.setId(19262);
            Worker.getInstance().pushMessage(uiMessage);
            Xulor.getInstance().unload("confirmMarketPurchaseDialog", false);
        }
    }
    
    public static void decreaseQuantity(final Event event) {
        if (MarketDialogActions.m_packQuantity > 1) {
            --MarketDialogActions.m_packQuantity;
            applyQuantity();
        }
    }
    
    public static void increaseQuantity(final Event event) {
        final Property prop = PropertiesProvider.getInstance().getProperty("marketItemPurchase");
        if (prop == null) {
            return;
        }
        final MarketEntryView marketEntryView = (MarketEntryView)prop.getValue();
        if (MarketDialogActions.m_packQuantity < marketEntryView.getQuantity()) {
            final long test = (MarketDialogActions.m_packQuantity + 1) * marketEntryView.getPrice();
            if (test > 2147483647L) {
                return;
            }
            ++MarketDialogActions.m_packQuantity;
            applyQuantity();
        }
    }
    
    public static void validQuantity(final Event e, final TextEditor te) {
        final Property prop = PropertiesProvider.getInstance().getProperty("marketItemPurchase");
        if (prop == null) {
            return;
        }
        final MarketEntryView marketEntryView = (MarketEntryView)prop.getValue();
        short qty = PrimitiveConverter.getShort(te.getText());
        if (qty < 1) {
            qty = 1;
            te.setText(String.valueOf(qty));
        }
        else if (qty > marketEntryView.getQuantity()) {
            qty = marketEntryView.getQuantity();
        }
        MarketDialogActions.m_packQuantity = qty;
        applyQuantity();
    }
    
    public static void setQuantity(final Event event) {
        final Property prop = PropertiesProvider.getInstance().getProperty("marketItemPurchase");
        if (prop == null) {
            return;
        }
        final MarketEntryView marketEntryView = (MarketEntryView)prop.getValue();
        if (event instanceof SliderMovedEvent) {
            final short levelValue = (short)((SliderMovedEvent)event).getValue();
            final long test = levelValue * marketEntryView.getPrice();
            if (test > 2147483647L) {
                return;
            }
            MarketDialogActions.m_packQuantity = levelValue;
            applyQuantity();
        }
    }
    
    public static void applyQuantity() {
        final Property prop = PropertiesProvider.getInstance().getProperty("marketItemPurchase");
        if (prop == null) {
            return;
        }
        final MarketEntryView marketEntryView = (MarketEntryView)prop.getValue();
        MarketDialogActions.m_totalPrice = MarketDialogActions.m_packQuantity * marketEntryView.getPrice();
        PropertiesProvider.getInstance().setPropertyValue("currentItemQuantity", MarketDialogActions.m_packQuantity);
        PropertiesProvider.getInstance().setPropertyValue("currentItemTotalPrice", MarketDialogActions.m_totalPrice);
        PropertiesProvider.getInstance().setPropertyValue("currentItemFormatedTotalPrice", NumberFormat.getIntegerInstance().format(MarketDialogActions.m_totalPrice) + " §");
        PropertiesProvider.getInstance().setPropertyValue("currentItemStackQtyPlusTotal", PackTypeFieldProvider.getPackTypeRepresentation(marketEntryView.getPackType()) + " = " + MarketDialogActions.m_packQuantity * marketEntryView.getPackType().qty);
    }
    
    public static void removeItem(final Event e, final MarketHistoryEntryView marketEntryView) {
        removeItem(marketEntryView.getId());
    }
    
    public static void removeItem(final Event e, final MarketEntryView marketEntryView) {
        if (marketEntryView.isOverTimed()) {
            removeItem(marketEntryView.getId());
            return;
        }
        final MessageBoxControler controler = Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("market.removeItemConfirmation"), WakfuMessageBoxConstants.getMessageBoxIconUrl(0), 25L, 102, 1);
        controler.addEventListener(new MessageBoxEventListener() {
            @Override
            public void messageBoxClosed(final int type, final String userEntry) {
                if (type == 8) {
                    removeItem(marketEntryView.getId());
                }
            }
        });
    }
    
    private static void removeItem(final long id) {
        final UIMessage uiMessage = new UIMessage();
        uiMessage.setLongValue(id);
        uiMessage.setId(19263);
        Worker.getInstance().pushMessage(uiMessage);
    }
    
    public static void showItemDetails(final ItemEvent e) {
        if (e.getButton() != 3) {
            return;
        }
        final MerchantItemView merchantItemView = (MerchantItemView)e.getItemValue();
        if (merchantItemView == null) {
            return;
        }
        final Item item = merchantItemView.getItem();
        String dialogId = null;
        AbstractUIDetailMessage msg;
        if (item.hasPet()) {
            final PetMarketDetailDialogView view = new PetMarketDetailDialogView(merchantItemView);
            msg = new UIPetDetailMessage(view);
            msg.setId(16430);
            dialogId = UIItemManagementFrame.getInstance().getItemDetailDialogId(view.getUID());
        }
        else {
            msg = new UIItemDetailMessage();
            msg.setItem(item);
            msg.setId(16415);
            dialogId = UIItemManagementFrame.getInstance().getItemDetailDialogId(item.getUniqueId());
        }
        msg.setX(MouseManager.getInstance().getX());
        final Widget w = e.getTarget();
        msg.setY(e.getScreenY() + w.getHeight());
        Worker.getInstance().pushMessage(msg);
        UIMarketFrame.getInstance().addItemDetailedDialogId(item.getReferenceId(), dialogId);
    }
    
    public static void getMoneyBack(final Event e) {
        UIMessage.send((short)19265);
    }
    
    public static void getUnsoldBack(final Event e) {
        UIMessage.send((short)19267);
    }
    
    public static void openStuffPreviewWindow(final Event e, final MarketEntryView marketEntry) {
        ((ReferenceItem)marketEntry.getItem().getReferenceItem()).previewTradeEntry();
    }
    
    static {
        m_logger = Logger.getLogger((Class)MarketDialogActions.class);
        MarketDialogActions.m_lastSearchRequest = new UIMarketRequestMessage();
        MarketDialogActions.m_lastSearchType = null;
    }
}
