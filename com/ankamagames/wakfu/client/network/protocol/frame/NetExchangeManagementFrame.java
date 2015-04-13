package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.exchange.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;

public class NetExchangeManagementFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static final NetExchangeManagementFrame m_instance;
    
    public static NetExchangeManagementFrame getInstance() {
        return NetExchangeManagementFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            WakfuGameEntity.getInstance().removeFrame(NetExchangeInvitationFrame.getInstance());
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WakfuGameEntity.getInstance().pushFrame(NetExchangeInvitationFrame.getInstance());
        }
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 6010: {
                final ExchangeItemAddedMessage msg = (ExchangeItemAddedMessage)message;
                final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
                final Item item = msg.getItem();
                if (exchanger != null && exchanger.getId() == msg.getExchangeId()) {
                    exchanger.setState(msg.getValidity());
                    ((ItemExchanger<Item, User>)exchanger).addItemToExchange(msg.getUserId(), item, msg.getItemQuantity());
                    UIExchangeFrame.getInstance().invalidateExchange();
                }
                else {
                    NetExchangeManagementFrame.m_logger.error((Object)"Message d'ajout d'un item dans un echange re\u00e7u alors que le player ne poss\u00e8de pas d'\u00e9changeur actif");
                }
                return false;
            }
            case 6012: {
                final ExchangeItemRemovedMessage msg2 = (ExchangeItemRemovedMessage)message;
                final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
                final Item item = msg2.getItem();
                if (exchanger != null && exchanger.getId() == msg2.getExchangeId()) {
                    exchanger.setState(msg2.getValidity());
                    ((ItemExchanger<Item, User>)exchanger).removeItemToExchange(msg2.getUserId(), item, msg2.getItemQuantity());
                    UIExchangeFrame.getInstance().invalidateExchange();
                }
                else {
                    NetExchangeManagementFrame.m_logger.error((Object)"Message de suppression d'un item dans un echange re\u00e7u alors que le player ne poss\u00e8de pas d'\u00e9changeur actif");
                }
                return false;
            }
            case 6014: {
                final ExchangeSetCashMessage msg3 = (ExchangeSetCashMessage)message;
                final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
                if (exchanger != null && exchanger.getId() == msg3.getExchangeId()) {
                    exchanger.setState(msg3.getValidity());
                    exchanger.setCashInExchange(msg3.getUserId(), msg3.getAmountOfCash());
                    UIExchangeFrame.getInstance().invalidateExchange();
                }
                else {
                    NetExchangeManagementFrame.m_logger.error((Object)"Message de mise a jour de kamas dans un echange re\u00e7u alors que le player ne poss\u00e8de pas d'\u00e9changeur actif");
                }
                return false;
            }
            case 6022: {
                final ExchangeUserReadyMessage msg4 = (ExchangeUserReadyMessage)message;
                final ItemExchanger exchanger2 = ClientTradeHelper.INSTANCE.getCurrentTrade();
                if (exchanger2 != null && exchanger2.getId() == msg4.getExchangeId()) {
                    exchanger2.setUserReady(msg4.getUserId(), msg4.isReady());
                }
                else {
                    NetExchangeManagementFrame.m_logger.error((Object)"Message de validation d'un echange re\u00e7u alors que le player ne poss\u00e8de pas d'\u00e9changeur actif");
                }
                return false;
            }
            case 6050: {
                final ExchangeEndMessage msg5 = (ExchangeEndMessage)message;
                final ItemTrade exchanger = ClientTradeHelper.INSTANCE.getCurrentTrade();
                if (exchanger == null || exchanger.getId() != msg5.getExchangeId()) {
                    NetExchangeManagementFrame.m_logger.error((Object)"Message terminant un echange re\u00e7u alors que le player ne poss\u00e8de pas d'\u00e9changeur actif");
                    return false;
                }
                switch (msg5.getReason()) {
                    case 0: {
                        ((ItemExchanger<ContentType, WakfuExchangerUser>)exchanger).acceptExchange(exchanger.getLocalUser());
                        break;
                    }
                    case 1: {
                        ((ItemExchanger<ContentType, WakfuExchangerUser>)exchanger).cancelExchange(exchanger.getLocalUser());
                        break;
                    }
                    case 2: {
                        ((ItemExchanger<ContentType, WakfuExchangerUser>)exchanger).cancelExchange(exchanger.getRemoteUser());
                        break;
                    }
                    case 3: {
                        exchanger.failExchange();
                        break;
                    }
                }
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetExchangeManagementFrame.class);
        m_instance = new NetExchangeManagementFrame();
    }
}
