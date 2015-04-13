package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.ui.protocol.message.exchange.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.companion.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class UIExchangeFrame implements MessageFrame
{
    private static UIExchangeFrame m_instance;
    private static Logger m_logger;
    private DialogUnloadListener m_dialogUnloadListener;
    private boolean m_equipmentOpenWithExchange;
    
    public static UIExchangeFrame getInstance() {
        return UIExchangeFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        if (message instanceof ClockMessage) {
            PropertiesProvider.getInstance().setPropertyValue("exchange.valid", true);
            MessageScheduler.getInstance().removeAllClocks(this);
            return false;
        }
        switch (message.getId()) {
            case 16807: {
                final UIReadyForExchangeRequestMessage exchangeRequestMessage = (UIReadyForExchangeRequestMessage)message;
                final ExchangeSetReadyMessage netMessage = new ExchangeSetReadyMessage();
                netMessage.setExchangeId(exchangeRequestMessage.getExchangeId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGameEntity.getInstance().getLocalPlayer(), "states");
                return false;
            }
            case 16810: {
                if (PropertiesProvider.getInstance().getProperty("exchange.sourceBag") == null || PropertiesProvider.getInstance().getProperty("exchange.sourcePosition") == null) {
                    UIExchangeFrame.m_logger.error((Object)"Impossible d'ajouter l'objet : le bag / position de la source n'ont pas \u00e9t\u00e9 sp\u00e9cifi\u00e9s");
                    return false;
                }
                if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                    Xulor.getInstance().unload("splitStackDialog");
                }
                if (Xulor.getInstance().isLoaded("splitExchangeAmountOfCashDialog")) {
                    Xulor.getInstance().unload("splitExchangeAmountOfCashDialog");
                }
                final UIExchangeMoveItemMessage exchangeMoveItemMessage = (UIExchangeMoveItemMessage)message;
                final Item item = exchangeMoveItemMessage.getItem();
                if (!this.isExchangeable(item)) {
                    final String errorMsg = WakfuTranslator.getInstance().getString("exchange.itemUnexchangeable");
                    final ChatMessage chatErrorMsg = new ChatMessage(errorMsg);
                    chatErrorMsg.setPipeDestination(3);
                    ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg);
                    return false;
                }
                final long uniqueId = item.getUniqueId();
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (!localPlayer.getBags().contains(uniqueId) && !localPlayer.getEquipmentInventory().containsUniqueId(uniqueId)) {
                    for (final ShortCharacterView shortCharacterView : localPlayer.getCompanionViews()) {
                        if (!shortCharacterView.isPlayer()) {
                            final CharacteristicCompanionView characterSheetView = (CharacteristicCompanionView)UICompanionsEmbeddedFrame.getCharacterSheetView(shortCharacterView.getBreedId());
                            if (characterSheetView != null && characterSheetView.getItemEquipment().containsUniqueId(uniqueId)) {
                                final String errorMsg2 = WakfuTranslator.getInstance().getString("exchange.itemFromCompanion");
                                final ChatMessage chatErrorMsg2 = new ChatMessage(errorMsg2);
                                chatErrorMsg2.setPipeDestination(3);
                                ChatManager.getInstance().getChatPipe(3).pushMessage(chatErrorMsg2);
                                break;
                            }
                        }
                    }
                    return false;
                }
                final ExchangeAddItemMessage netMessage2 = new ExchangeAddItemMessage();
                netMessage2.setExchangeId(exchangeMoveItemMessage.getExchangeId());
                netMessage2.setItemId(uniqueId);
                netMessage2.setItemQuantity((short)exchangeMoveItemMessage.getItemQuantity());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage2);
                this.invalidateExchange();
                return false;
            }
            case 16811: {
                if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                    Xulor.getInstance().unload("splitStackDialog");
                }
                if (Xulor.getInstance().isLoaded("splitExchangeAmountOfCashDialog")) {
                    Xulor.getInstance().unload("splitExchangeAmountOfCashDialog");
                }
                final UIExchangeMoveItemMessage uiExchangeMoveItemMessage = (UIExchangeMoveItemMessage)message;
                final ExchangeRemoveItemMessage netMessage3 = new ExchangeRemoveItemMessage();
                netMessage3.setExchangeId(uiExchangeMoveItemMessage.getExchangeId());
                netMessage3.setItemId(uiExchangeMoveItemMessage.getItem().getUniqueId());
                netMessage3.setItemQuantity((short)uiExchangeMoveItemMessage.getItemQuantity());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage3);
                this.invalidateExchange();
                return false;
            }
            case 16823: {
                final UIExchangeSetCashMessage setCashMsg = (UIExchangeSetCashMessage)message;
                final LocalPlayerCharacter localPlayer2 = WakfuGameEntity.getInstance().getLocalPlayer();
                final ItemTrade trade = ClientTradeHelper.INSTANCE.getCurrentTrade();
                int cashToExchange = setCashMsg.getCashInExchange();
                final int cashInExchange = ((ItemExchanger<ContentType, WakfuExchangerUser>)trade).getUserById(localPlayer2.getId()).getMoneyExchanged();
                if (cashInExchange == cashToExchange) {
                    return false;
                }
                final int cappedCash = localPlayer2.getKamasCount() + cashInExchange;
                if (cappedCash < cashToExchange) {
                    cashToExchange = cappedCash;
                }
                final ExchangeSetCashRequestMessage netMessage4 = new ExchangeSetCashRequestMessage();
                netMessage4.setAmountOfCashInExchange(cashToExchange);
                netMessage4.setExchangeId(setCashMsg.getExchangeId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage4);
                this.invalidateExchange();
                return false;
            }
            case 16812: {
                final UIMessage msg = (UIMessage)message;
                final long exchangeId = msg.getLongValue();
                final ExchangeCancelMessage netMessage5 = new ExchangeCancelMessage();
                netMessage5.setExchangeId(exchangeId);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage5);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    public boolean isExchangeable(final Item item) {
        return !item.isBound() && !item.isRent() && (item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE) == null || item.getReferenceItem().getCriterion(ActionsOnItem.EXCHANGE).isValid(WakfuGameEntity.getInstance().getLocalPlayer(), WakfuGameEntity.getInstance().getLocalPlayer().getPosition(), item, WakfuGameEntity.getInstance().getLocalPlayer().getEffectContext()));
    }
    
    public void invalidateExchange() {
        if (!WakfuGameEntity.getInstance().hasFrame(this)) {
            return;
        }
        PropertiesProvider.getInstance().setPropertyValue("exchange.valid", false);
        MessageScheduler.getInstance().removeAllClocks(this);
        MessageScheduler.getInstance().addClock(this, 5000L, 0);
    }
    
    @Override
    public long getId() {
        return 1L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    private void sendCancelExchangeMessage() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer == null) {
            UIExchangeFrame.m_logger.error((Object)"on tente d'annuler l'\u00e9change mais le joueur n'existe d\u00e9j\u00e0 plus");
            return;
        }
        final ItemTrade trade = ClientTradeHelper.INSTANCE.getCurrentTrade();
        if (trade != null) {
            final ExchangeCancelMessage netMessage = new ExchangeCancelMessage();
            netMessage.setExchangeId(trade.getId());
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            PropertiesProvider.getInstance().setPropertyValue("exchange.valid", true);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("exchangeDialog")) {
                        UIExchangeFrame.this.sendCancelExchangeMessage();
                        Xulor.getInstance().removeDialogUnloadListener(UIExchangeFrame.this.m_dialogUnloadListener);
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            final Window w = (Window)Xulor.getInstance().load("exchangeDialog", Dialogs.getDialogPath("exchangeDialog"), 32785L, (short)10000);
            if (w == null) {
                UIExchangeFrame.m_logger.error((Object)("Impossible de r\u00e9cup\u00e9rer la fen\u00eatre d'\u00e9change !!! loaded=" + Xulor.getInstance().isLoaded("exchangeDialog")));
            }
            else {
                UIEquipmentFrame.getInstance().ensureInventoryLinkedWindowLayout(w);
            }
            if (!Xulor.getInstance().isLoaded("equipmentDialog")) {
                UIEquipmentFrame.getInstance().openEquipment();
                this.m_equipmentOpenWithExchange = true;
            }
            WakfuGameEntity.getInstance().removeFrame(UIWorldInteractionFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UIRecycleFrame.getInstance());
            WakfuGameEntity.getInstance().removeFrame(UICraftTableFrame.getInstance());
            UIMRUFrame.getInstance().closeCurrentMRU();
            Xulor.getInstance().putActionClass("wakfu.exchange", ExchangeDialogActions.class);
            final ItemTrade trade = ClientTradeHelper.INSTANCE.getCurrentTrade();
            final UIMessage msg = new UIMessage();
            msg.setId(19065);
            msg.setStringValue(trade.getRemoteUser().getName());
            Worker.getInstance().pushMessage(msg);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            UIChatFrameHelper.closeExchangeChatWindow();
            if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                Xulor.getInstance().unload("splitStackDialog");
            }
            MessageScheduler.getInstance().removeAllClocks(this);
            Xulor.getInstance().unload("exchangeDialog");
            if (this.m_equipmentOpenWithExchange) {
                WakfuGameEntity.getInstance().removeFrame(UIEquipmentFrame.getInstance());
            }
            this.m_equipmentOpenWithExchange = false;
            Xulor.getInstance().removeActionClass("wakfu.exchange");
            if (WakfuGameEntity.getInstance().getLocalPlayer() == null) {
                return;
            }
            WakfuGameEntity.getInstance().pushFrame(UIWorldInteractionFrame.getInstance());
        }
    }
    
    static {
        UIExchangeFrame.m_instance = new UIExchangeFrame();
        UIExchangeFrame.m_logger = Logger.getLogger((Class)UIExchangeFrame.class);
    }
}
