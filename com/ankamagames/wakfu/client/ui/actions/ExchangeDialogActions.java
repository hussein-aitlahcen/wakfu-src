package com.ankamagames.wakfu.client.ui.actions;

import com.ankamagames.xulor2.actions.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.item.*;
import com.ankamagames.xulor2.event.*;
import com.ankamagames.wakfu.client.ui.protocol.message.exchange.*;
import com.ankamagames.wakfu.client.core.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.event.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

@XulorActionsTag
public class ExchangeDialogActions
{
    public static final String PACKAGE = "wakfu.exchange";
    private static final Logger m_logger;
    private static MessageHandler m_popupMessageHandler;
    
    public static void setReadyForExchange(final Event event, final Long exchangeId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        final ItemTrade trade = ClientTradeHelper.INSTANCE.getCurrentTrade();
        final TextEditor te = (TextEditor)event.getTarget().getElementMap().getElement("exchangeKamasEditor");
        final String text = te.getText();
        final WakfuExchangerUser user = ((ItemExchanger<ContentType, WakfuExchangerUser>)trade).getUserById(localPlayer.getId());
        final int moneyToExchange = (text.length() == 0) ? 0 : Integer.parseInt(text);
        if (user.getMoneyExchanged() != moneyToExchange) {
            return;
        }
        if (trade.getState() == 0) {
            final UIReadyForExchangeRequestMessage message = new UIReadyForExchangeRequestMessage();
            message.setExchangeId(exchangeId);
            Worker.getInstance().pushMessage(message);
        }
        else {
            showErrorMessage(trade);
        }
    }
    
    public static void dragItem(final DragEvent event, final Long exchangeId) {
        if (Xulor.getInstance().isLoaded("splitStackDialog")) {
            Xulor.getInstance().unload("splitStackDialog");
        }
        final Object value = event.getValue();
        if (value != null && value instanceof Item) {
            final Item item = (Item)value;
            final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
            if (exchanger != null) {
                exchanger.setCurrentDragUniqueId(item.getUniqueId());
            }
        }
    }
    
    public static void dropItem(final DropEvent event, final Long exchangeId) {
        CursorFactory.getInstance().unlock();
        final Object value = event.getValue();
        if (value != null && value instanceof Item) {
            final Item item = (Item)event.getValue();
            final ItemTrade currentTrade = ClientTradeHelper.INSTANCE.getCurrentTrade();
            if (currentTrade.getCurrentDragUniqueId() == item.getUniqueId()) {
                return;
            }
            final boolean shiftPressed = event.hasShift();
            final boolean defaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
            if (!UIExchangeFrame.getInstance().isExchangeable(item)) {
                final String errorMsg = WakfuTranslator.getInstance().getString("exchange.itemUnexchangeable");
                final ChatMessage chatErrorMsg = new ChatMessage(errorMsg);
                chatErrorMsg.setPipeDestination(3);
                ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg);
                return;
            }
            if (item.getQuantity() > 1 && ((shiftPressed && !defaultSplitMode) || (!shiftPressed && defaultSplitMode))) {
                SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                SplitStackDialogActions.setItem(item);
                SplitStackDialogActions.setExchangeId(exchangeId);
                SplitStackDialogActions.setMessageType((short)16810);
                final UIItemMessage message = new UIItemMessage();
                message.setItem(item);
                message.setX((short)event.getScreenX());
                message.setY((short)event.getScreenY());
                message.setId(16821);
                Worker.getInstance().pushMessage(message);
            }
            else {
                final UIExchangeMoveItemMessage message2 = new UIExchangeMoveItemMessage();
                message2.setId(16810);
                message2.setExchangeId(exchangeId);
                message2.setItem((Item)value);
                message2.setItemQuantity(item.getQuantity());
                Worker.getInstance().pushMessage(message2);
            }
        }
    }
    
    public static void dropOut(final Event event, final Long exchangeId) {
        if (event instanceof DropOutEvent) {
            final DropOutEvent dropOutEvent = (DropOutEvent)event;
            final Object value = dropOutEvent.getValue();
            if (value != null && value instanceof Item) {
                final Item item = (Item)value;
                final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
                if (exchanger != null) {
                    exchanger.setCurrentDragUniqueId(-1L);
                }
                final boolean shiftPressed = dropOutEvent.hasShift();
                final boolean defaultSplitMode = WakfuClientInstance.getInstance().getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DEFAULT_SPLIT_MODE_KEY);
                if ((item.getQuantity() > 1 && shiftPressed && !defaultSplitMode) || (!shiftPressed && defaultSplitMode)) {
                    SplitStackDialogActions.setMaxQuantity(item.getQuantity());
                    SplitStackDialogActions.setItem(item);
                    SplitStackDialogActions.setExchangeId(exchangeId);
                    SplitStackDialogActions.setMessageType((short)16811);
                    final UIItemMessage message = new UIItemMessage();
                    message.setItem(item);
                    message.setX((short)dropOutEvent.getScreenX());
                    message.setY((short)dropOutEvent.getScreenY());
                    message.setId(16821);
                    Worker.getInstance().pushMessage(message);
                }
                else {
                    final UIExchangeMoveItemMessage message2 = new UIExchangeMoveItemMessage();
                    message2.setId(16811);
                    message2.setExchangeId(exchangeId);
                    message2.setItem(item);
                    message2.setItemQuantity(item.getQuantity());
                    Worker.getInstance().pushMessage(message2);
                }
            }
        }
    }
    
    public static void removeItem(final ItemEvent event, final Long exchangeId) {
        final Object value = event.getItemValue();
        if (value != null && value instanceof Item) {
            final Item item = (Item)value;
            final UIExchangeMoveItemMessage message = new UIExchangeMoveItemMessage();
            message.setId(16811);
            message.setExchangeId(exchangeId);
            message.setItem(item);
            message.setItemQuantity(item.getQuantity());
            Worker.getInstance().pushMessage(message);
            final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
            if (exchanger != null) {
                exchanger.setCurrentDragUniqueId(-1L);
            }
        }
    }
    
    public static void exchangeMoney(final FocusChangedEvent event, final Long exchangeId) {
        if (!event.getFocused()) {
            exchangeMoney(event.getCurrentTarget(), exchangeId);
        }
    }
    
    public static void exchangeMoney(final KeyEvent event, final Long exchangeId) {
        if (event.getKeyCode() == 10) {
            FocusManager.getInstance().setFocused(null);
        }
    }
    
    public static void exchangeMoney(final TextEditor textEditor, final Long exchangeId) {
        final UIExchangeSetCashMessage message = new UIExchangeSetCashMessage();
        final String text = textEditor.getText();
        message.setCashInExchange((text.length() == 0) ? 0 : Integer.parseInt(text));
        message.setExchangeId(exchangeId);
        message.setId(16823);
        Worker.getInstance().pushMessage(message);
    }
    
    public static void openItemDetailWindow(final ItemEvent itemClickEvent, final Window window) {
        if (itemClickEvent.getButton() == 3) {
            final Object value = itemClickEvent.getItemValue();
            if (value instanceof Item) {
                Actions.sendOpenCloseItemDetailMessage(window.getElementMap().getId(), (Item)value);
            }
            else if (value instanceof ReferenceItem) {
                Actions.sendOpenCloseItemDetailMessage(window.getElementMap().getId(), (ReferenceItem)value);
            }
        }
    }
    
    public static void showErrorMessage(final ItemTrade itemTrade) {
        if (itemTrade.getState() == 0) {
            ExchangeDialogActions.m_logger.error((Object)"On veut afficher un message d'erreur pour un \u00e9change valide");
            return;
        }
        ExchangerUser<Item> problematicExchanger;
        String state;
        if (itemTrade.getState() == 1) {
            problematicExchanger = ((ItemExchanger<ContentType, ExchangerUser<Item>>)itemTrade).getRequester();
            state = "inventoryFull";
        }
        else if (itemTrade.getState() == 2) {
            problematicExchanger = ((ItemExchanger<ContentType, ExchangerUser<Item>>)itemTrade).getTarget();
            state = "inventoryFull";
        }
        else if (itemTrade.getState() == 3) {
            problematicExchanger = ((ItemExchanger<ContentType, ExchangerUser<Item>>)itemTrade).getRequester();
            state = "objectDoesntExist";
        }
        else {
            problematicExchanger = ((ItemExchanger<ContentType, ExchangerUser<Item>>)itemTrade).getTarget();
            state = "objectDoesntExist";
        }
        final String message = WakfuTranslator.getInstance().getString("exchange.error." + state, problematicExchanger.getName());
        Xulor.getInstance().msgBox(message, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1027L, 102, 1);
    }
    
    public static void closeExchangeDialog(final Event event, final Long exchangeId) {
        boolean close = false;
        switch (event.getType()) {
            case KEY_RELEASED: {
                final KeyEvent e = (KeyEvent)event;
                close = (e.getKeyCode() == 27);
                break;
            }
            case MOUSE_CLICKED: {
                close = true;
                break;
            }
        }
        if (close) {
            final UIMessage message = new UIMessage();
            message.setId(16812);
            message.setLongValue(exchangeId);
            Worker.getInstance().pushMessage(message);
        }
    }
    
    public static void onMouseOverExhange(final Event event) {
        if (MasterRootContainer.getInstance().isDragging()) {
            final EventDispatcher draggedParent = MasterRootContainer.getInstance().getDragged().getParent();
            if (draggedParent instanceof RenderableContainer) {
                final RenderableContainer renderableContainer = (RenderableContainer)draggedParent;
                if (renderableContainer.getItemValue() instanceof Item) {
                    final Item item = (Item)renderableContainer.getItemValue();
                    if (!UIExchangeFrame.getInstance().isExchangeable(item)) {
                        CursorFactory.getInstance().show(CursorFactory.CursorType.FORBIDDEN, true);
                    }
                }
            }
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)ExchangeDialogActions.class);
    }
}
