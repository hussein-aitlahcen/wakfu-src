package com.ankamagames.wakfu.client.core.game.exchange;

import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.common.game.characterInfo.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.exchange.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.common.game.exchange.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.inventory.moderation.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.tween.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class WakfuExchangerUser extends AbstractExchangerUser<Item>
{
    private static final Logger m_logger;
    private final PlayerCharacter m_player;
    private ExchangeOccupation m_occupation;
    private ExchangeItemsPositionSaver m_exchangePositions;
    
    public WakfuExchangerUser(final PlayerCharacter player) {
        super();
        this.m_player = player;
    }
    
    public PlayerCharacter getPlayer() {
        return this.m_player;
    }
    
    public boolean isLocalPlayer() {
        return this.m_player.isLocalPlayer();
    }
    
    @Override
    public long getId() {
        return this.m_player.getId();
    }
    
    @Override
    public String getName() {
        return this.m_player.getName();
    }
    
    public ExchangeOccupation getOccupation() {
        return this.m_occupation;
    }
    
    public void setOccupation(final ExchangeOccupation occupation) {
        this.m_occupation = occupation;
        this.m_player.setCurrentOccupation(occupation);
    }
    
    @Override
    public void onItemExchangerEvent(final ItemExchangerEvent event) {
        if (!this.isLocalPlayer()) {
            return;
        }
        switch (event.getAction()) {
            case EXCHANGE_END: {
                final ItemExchangerEndEvent endEvent = (ItemExchangerEndEvent)event;
                PropertiesProvider.getInstance().setPropertyValue("exchange.itemTrade", null);
                PropertiesProvider.getInstance().removeProperty("exchange.remotePlayer");
                if (event.getItemExchanger().getCurrentState() != ItemExchangeState.AWAITING_INVITATION_ANSWER) {
                    if (Xulor.getInstance().isLoaded("splitExchangeAmountOfCashDialog")) {
                        Xulor.getInstance().unload("splitExchangeAmountOfCashDialog");
                    }
                    if (Xulor.getInstance().isLoaded("splitStackDialog")) {
                        Xulor.getInstance().unload("splitStackDialog");
                    }
                    ClientTradeHelper.INSTANCE.setCurrentTrade(null);
                }
                switch (endEvent.getReason()) {
                    case USER_IGNORED: {
                        final ItemExchanger trade = event.getItemExchanger();
                        final ExchangeInvitationAnswerMessage netMessage = new ExchangeInvitationAnswerMessage();
                        netMessage.setExchangeId(trade.getId());
                        netMessage.setInvitationResult((byte)4);
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                        break;
                    }
                    case INVITATION_LOCALLY_CANCELED: {
                        final ItemTrade trade2 = (ItemTrade)event.getItemExchanger();
                        final MessageBoxControler controler = trade2.getInvitationMessageBoxControler();
                        if (controler != null) {
                            Xulor.getInstance().unload(controler.getMessageBoxId());
                            break;
                        }
                        break;
                    }
                    case INVITATION_REMOTELY_CANCELED: {
                        final ItemTrade remoteTrade = (ItemTrade)event.getItemExchanger();
                        final MessageBoxControler remoteControler = remoteTrade.getInvitationMessageBoxControler();
                        if (remoteControler != null) {
                            Xulor.getInstance().unload(remoteControler.getMessageBoxId());
                        }
                        final long cancelerId = event.getUserId();
                        final WakfuExchangerUser canceler = ((ItemExchanger<ContentType, WakfuExchangerUser>)remoteTrade).getUserById(cancelerId);
                        if (canceler == ((ItemExchanger<ContentType, WakfuExchangerUser>)remoteTrade).getTarget()) {
                            final String message = WakfuTranslator.getInstance().getString("exchange.declinedBy", canceler.getName());
                            final ChatMessage chatMsg = new ChatMessage(message);
                            chatMsg.setPipeDestination(4);
                            ChatManager.getInstance().getChatPipe(4).pushMessage(chatMsg);
                            WakfuGameEntity.getInstance().removeFrame(UIExchangeFrame.getInstance());
                            WakfuGameEntity.getInstance().removeFrame(NetExchangeManagementFrame.getInstance());
                            break;
                        }
                        break;
                    }
                    case INVITATION_IMPOSSIBLE_USER_BUSY: {
                        final ItemTrade impossibleTrade = (ItemTrade)event.getItemExchanger();
                        final MessageBoxControler messageBox = impossibleTrade.getInvitationMessageBoxControler();
                        if (messageBox != null) {
                            Xulor.getInstance().unload(messageBox.getMessageBoxId());
                            break;
                        }
                        break;
                    }
                    case LOCALLY_CANCELED: {
                        this.cancelExchange(event);
                        final String cancelMsg = WakfuTranslator.getInstance().getString("exchange.locallyCanceled");
                        final ChatMessage chatCancelMsg = new ChatMessage(cancelMsg);
                        chatCancelMsg.setPipeDestination(4);
                        ChatManager.getInstance().getChatPipe(4).pushMessage(chatCancelMsg);
                        break;
                    }
                    case REMOTELY_CANCELED: {
                        this.cancelExchange(event);
                        final ItemTrade remotelyEndedTrade = (ItemTrade)event.getItemExchanger();
                        final WakfuExchangerUser remotePlayer = ((ItemExchanger<ContentType, WakfuExchangerUser>)remotelyEndedTrade).getOtherUser(this);
                        final String msg = WakfuTranslator.getInstance().getString("exchange.canceledBy", remotePlayer.getName());
                        final ChatMessage chatRemoteCancelMsg = new ChatMessage(msg);
                        chatRemoteCancelMsg.setPipeDestination(4);
                        ChatManager.getInstance().getChatPipe(4).pushMessage(chatRemoteCancelMsg);
                        break;
                    }
                    case EXCHANGE_DONE: {
                        final String successMsg = WakfuTranslator.getInstance().getString("exchange.done");
                        final ChatMessage chatSuccessMsg = new ChatMessage(successMsg);
                        chatSuccessMsg.setPipeDestination(4);
                        ChatManager.getInstance().getChatPipe(4).pushMessage(chatSuccessMsg);
                        WakfuGameEntity.getInstance().removeFrame(UIExchangeFrame.getInstance());
                        WakfuGameEntity.getInstance().removeFrame(NetExchangeManagementFrame.getInstance());
                        break;
                    }
                    case EXCHANGE_FAILED: {
                        this.cancelExchange(event);
                        final String cancelMsg = WakfuTranslator.getInstance().getString("exchange.failed");
                        final ChatMessage chatCancelMsg = new ChatMessage(cancelMsg);
                        chatCancelMsg.setPipeDestination(3);
                        ChatManager.getInstance().getChatPipe(3).pushMessage(chatCancelMsg);
                        break;
                    }
                }
                break;
            }
            case EXCHANGE_STARTED: {
                final ItemTrade trade3 = (ItemTrade)event.getItemExchanger();
                trade3.getInvitationMessageBoxControler().cleanUpAndRemoveQuick();
                WakfuGameEntity.getInstance().removeFrame(UIExchangeInvitationFrame.getInstance());
                PropertiesProvider.getInstance().setPropertyValue("exchange.itemTrade", trade3);
                final WakfuExchangerUser remoteUser = ((ItemExchanger<ContentType, WakfuExchangerUser>)trade3).getOtherUser(this);
                PropertiesProvider.getInstance().setPropertyValue("exchange.remotePlayer", remoteUser.m_player);
                this.m_exchangePositions = new ExchangeItemsPositionSaver(trade3);
                if (!WakfuGameEntity.getInstance().hasFrame(UIExchangeFrame.getInstance())) {
                    WakfuGameEntity.getInstance().pushFrame(UIExchangeFrame.getInstance());
                }
                WakfuGameEntity.getInstance().pushFrame(UIExchangeFrame.getInstance());
                WakfuGameEntity.getInstance().pushFrame(NetExchangeManagementFrame.getInstance());
                ClientGameEventManager.INSTANCE.fireEvent(new ClientEventStartExchange());
                WakfuExchangerUser.m_logger.info((Object)"Le joueur d?marre un ?change");
                break;
            }
            case EXCHANGE_PROPOSED: {
                WakfuGameEntity.getInstance().pushFrame(UIExchangeInvitationFrame.getInstance());
                final ItemTrade trade3 = (ItemTrade)event.getItemExchanger();
                final String message2 = WakfuTranslator.getInstance().getString("exchangeInvitation.messageIn", ((ItemExchanger<ContentType, WakfuExchangerUser>)trade3).getTarget().getName());
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(message2, WakfuMessageBoxConstants.getMessageBoxIconUrl(2), 18437L, 102, 2);
                if (messageBoxControler != null) {
                    trade3.setInvitationMessageBoxControler(messageBoxControler);
                    messageBoxControler.addEventListener(new MessageBoxEventListener() {
                        @Override
                        public void messageBoxClosed(final int type, final String userEntry) {
                            final UIExchangeInvitationRejectRequestMessage exchangeMessage = new UIExchangeInvitationRejectRequestMessage();
                            exchangeMessage.setInvitationId(trade3.getId());
                            Worker.getInstance().pushMessage(exchangeMessage);
                        }
                    });
                    break;
                }
                final UIExchangeInvitationRejectRequestMessage exchangeMessage = new UIExchangeInvitationRejectRequestMessage();
                exchangeMessage.setInvitationId(trade3.getId());
                Worker.getInstance().pushMessage(exchangeMessage);
                break;
            }
            case EXCHANGE_REQUESTED: {
                WakfuGameEntity.getInstance().pushFrame(UIExchangeInvitationFrame.getInstance());
                final ItemTrade trade3 = (ItemTrade)event.getItemExchanger();
                final WakfuExchangerUser itemExchangerUser = ((ItemExchanger<ContentType, WakfuExchangerUser>)trade3).getRequester();
                final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(itemExchangerUser.getId());
                String message3 = WakfuTranslator.getInstance().getString("exchangeInvitation.messageOut", itemExchangerUser.getName());
                AbstractMRUAction mruExchangeAction = new MRUExchangeAction();
                mruExchangeAction.initFromSource(character);
                final String addedText = mruExchangeAction.getComplementaryTooltip();
                if (addedText != null) {
                    message3 = message3 + '\n' + addedText;
                }
                mruExchangeAction = null;
                final MessageBoxControler messageBoxControler2 = Xulor.getInstance().msgBox(message3, WakfuMessageBoxConstants.getMessageBoxIconUrl(2), 2073L, 102, 1);
                trade3.setInvitationMessageBoxControler(messageBoxControler2);
                messageBoxControler2.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        if (type == 8) {
                            final UIExchangeInvitationAcceptRequestMessage exchangeMessage = new UIExchangeInvitationAcceptRequestMessage();
                            exchangeMessage.setInvitationId(trade3.getId());
                            Worker.getInstance().pushMessage(exchangeMessage);
                        }
                        else {
                            final UIExchangeInvitationRejectRequestMessage exchangeMessage2 = new UIExchangeInvitationRejectRequestMessage();
                            exchangeMessage2.setInvitationId(trade3.getId());
                            Worker.getInstance().pushMessage(exchangeMessage2);
                        }
                    }
                });
                break;
            }
            case EXCHANGE_CONTENT_MODIFIED: {
                final ItemExchangerModifiedEvent modifiedEvent = (ItemExchangerModifiedEvent)event;
                final ItemTrade trade2 = (ItemTrade)modifiedEvent.getItemExchanger();
                if (modifiedEvent.getUserId() == this.getId()) {
                    switch (modifiedEvent.getModification()) {
                        case CONTENT_ADDED: {
                            if (PropertiesProvider.getInstance().getProperty("exchange.sourceBag") == null || PropertiesProvider.getInstance().getProperty("exchange.sourcePosition") == null) {
                                WakfuExchangerUser.m_logger.error((Object)"Impossible d'ajouter l'objet : le bag / position de la source n'ont pas \u00e9t\u00e9 sp\u00e9cifi\u00e9s");
                                return;
                            }
                            final LocalPlayerCharacter player = (LocalPlayerCharacter)this.m_player;
                            final Object source = PropertiesProvider.getInstance().getProperty("exchange.sourceBag").getValue();
                            if (source instanceof Bag) {
                                final short updateQuantity = (short)(-modifiedEvent.getContentQuantity());
                                player.getBags().updateQuantity(modifiedEvent.getContent().getUniqueId(), updateQuantity);
                                final short sourcePosition = PropertiesProvider.getInstance().getProperty("exchange.sourcePosition").getShort();
                                this.m_exchangePositions.memorizeItemPosition(modifiedEvent.getContent(), source, sourcePosition);
                            }
                            if (source instanceof ItemEquipment) {
                                final ItemEquipment equipmentInventory = player.getEquipmentInventory();
                                if (modifiedEvent.getContentQuantity() != 1) {
                                    WakfuExchangerUser.m_logger.error((Object)"Impossible d'ajouter plusieurs items depuis l'\u00e9quipement");
                                }
                                else {
                                    final Item itemToRemove = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).getWithUniqueId(modifiedEvent.getContent().getUniqueId());
                                    final short sourcePosition2 = PropertiesProvider.getInstance().getProperty("exchange.sourcePosition").getShort();
                                    if (!((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).remove(itemToRemove)) {
                                        WakfuExchangerUser.m_logger.error((Object)"Impossible de retirer l'item de l'?quipement du joueur");
                                    }
                                    if (itemToRemove.getReferenceItem().getItemType().getLinkedPositions() != null) {
                                        for (final EquipmentPosition pos : itemToRemove.getReferenceItem().getItemType().getLinkedPositions()) {
                                            final Item temp = ((ArrayInventoryWithoutCheck<Item, R>)equipmentInventory).removeAt(pos.m_id);
                                            if (temp != null) {
                                                temp.release();
                                            }
                                        }
                                    }
                                    this.m_exchangePositions.memorizeItemPosition(modifiedEvent.getContent(), source, sourcePosition2);
                                }
                                UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
                            }
                            trade2.updateLocalItemProperties();
                            PropertiesProvider.getInstance().removeProperty("exchange.sourceBag");
                            PropertiesProvider.getInstance().removeProperty("exchange.sourcePosition");
                            break;
                        }
                        case CONTENT_REMOVED: {
                            final Object addedTarget = this.m_exchangePositions.takeBackItem((Item)modifiedEvent.getContent(), modifiedEvent.getContentQuantity());
                            if (addedTarget instanceof ItemEquipment) {
                                UIEquipmentFrame.getInstance().refreshPlayerEquimentSlots();
                            }
                            trade2.updateLocalItemProperties();
                            break;
                        }
                        case CASH_MODIFIED: {
                            if (modifiedEvent instanceof WakfuItemExchangerModifiedEvent) {
                                final LocalPlayerCharacter player = (LocalPlayerCharacter)this.m_player;
                                final int cashModification = ((WakfuItemExchangerModifiedEvent)modifiedEvent).getAmountOfCash();
                                if (cashModification >= 0) {
                                    if (!player.getOwnedDimensionalBag().getWallet().addAmount(cashModification)) {
                                        WakfuExchangerUser.m_logger.error((Object)"Erreur lors de la mise a jour du portefeuille");
                                    }
                                    else {
                                        ItemTracker.log(Level.DEBUG, player.getOwnerId(), player.getId(), player.getOwnerId(), player.getId(), "Exchange", this.m_occupation.getTrade().getId(), player.getInstanceId(), -1, -1L, -1L, cashModification);
                                    }
                                }
                                else if (!player.getOwnedDimensionalBag().getWallet().substractAmount(-cashModification)) {
                                    WakfuExchangerUser.m_logger.error((Object)"Erreur lors de la mise a jour du portefeuille");
                                }
                                else {
                                    ItemTracker.log(Level.DEBUG, player.getOwnerId(), player.getId(), player.getOwnerId(), player.getId(), "Exchange", this.m_occupation.getTrade().getId(), player.getInstanceId(), -1, -1L, -1L, -cashModification);
                                }
                                trade2.updateLocalCashProperties();
                                final Widget cashContainer = (Widget)Xulor.getInstance().getEnvironment().getElementMap("exchangeDialog").getElement("localMoney");
                                Color c2 = new Color(0.1f, 0.8f, 0.98f, 0.55f);
                                c2 = new Color(Color.WHITE.get());
                                cashContainer.getAppearance().addTween(new ModulationColorTween(c2, c2, cashContainer.getAppearance(), 0, 100, 11, TweenFunction.PROGRESSIVE));
                                break;
                            }
                            WakfuExchangerUser.m_logger.error((Object)"Impossible de mettre a jour le portefeuille du client : l'?v?nement re?u n'est pas de la classe WakfuItemExchangerModifiedEvent");
                            break;
                        }
                    }
                }
                else {
                    switch (modifiedEvent.getModification()) {
                        case CASH_MODIFIED: {
                            trade2.updateRemoteCashProperties();
                            final Widget container = (Widget)Xulor.getInstance().getEnvironment().getElementMap("exchangeDialog").getElement("remoteMoney");
                            final Color c3 = new Color(0.1f, 0.8f, 0.98f, 0.55f);
                            final Color c4 = new Color(Color.WHITE.get());
                            container.getAppearance().addTween(new ModulationColorTween(c4, c3, container.getAppearance(), 0, 50, 11, TweenFunction.PROGRESSIVE));
                            if (modifiedEvent instanceof WakfuItemExchangerModifiedEvent) {
                                final WakfuItemExchangerModifiedEvent event2 = (WakfuItemExchangerModifiedEvent)modifiedEvent;
                                this.logInChat(WakfuTranslator.getInstance().getString("exchange.cashChanged", this.m_occupation.getTrade().getRemoteUser().getName(), event2.getAmountOfCash()));
                                break;
                            }
                            break;
                        }
                        case CONTENT_ADDED:
                        case CONTENT_REMOVED: {
                            trade2.updateRemoteItemProperties();
                            final Item item = (Item)modifiedEvent.getContent();
                            final short quantity = modifiedEvent.getContentQuantity();
                            final String key = (modifiedEvent.getModification() == ItemExchangerModifiedEvent.Modification.CONTENT_ADDED) ? "exchange.itemAdded" : "exchange.itemRemoved";
                            final String name = WakfuTranslator.getInstance().getString(15, item.getReferenceId(), new Object[0]);
                            this.logInChat(WakfuTranslator.getInstance().getString(key, this.m_occupation.getTrade().getRemoteUser().getName(), quantity, name));
                            break;
                        }
                    }
                }
                trade2.updateRemoteReadyProperties();
                trade2.updateLocalReadyProperties();
                break;
            }
        }
    }
    
    private void logInChat(final String msg) {
        final TextWidgetFormater sb = new TextWidgetFormater();
        sb.append(msg).newLine();
        final ChatViewManager manager = (ChatViewManager)PropertiesProvider.getInstance().getObjectProperty("chat", "exchangeDialog");
        manager.getCurrentView().appendFieldValue("history", sb.finishAndToString());
        GlobalPropertiesProvider.getInstance().firePropertyValueChanged(manager.getCurrentView(), "history");
    }
    
    private void cancelExchange(final ItemExchangerEvent event) {
        final ItemTrade locallyEndedTrade = (ItemTrade)event.getItemExchanger();
        this.m_exchangePositions.removeAllItems(locallyEndedTrade);
        if (this.getMoneyExchanged() != 0) {
            final LocalPlayerCharacter player = (LocalPlayerCharacter)this.m_player;
            player.getOwnedDimensionalBag().getWallet().addAmount(this.getMoneyExchanged());
        }
        WakfuGameEntity.getInstance().removeFrame(UIExchangeFrame.getInstance());
        WakfuGameEntity.getInstance().removeFrame(NetExchangeManagementFrame.getInstance());
    }
    
    public boolean canStartNewExchange() {
        return false;
    }
    
    public void clearOccupation() {
        if (this.m_occupation != null && this.m_player.getCurrentOccupation() == this.m_occupation) {
            this.m_player.finishCurrentOccupation();
        }
        this.m_occupation = null;
    }
    
    @Override
    public void clear() {
        super.clear();
        this.clearOccupation();
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuExchangerUser.class);
    }
}
