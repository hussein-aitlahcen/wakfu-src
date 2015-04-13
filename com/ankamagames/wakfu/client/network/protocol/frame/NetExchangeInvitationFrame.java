package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.game.exchange.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.event.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.exchange.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class NetExchangeInvitationFrame implements MessageFrame
{
    private static final NetExchangeInvitationFrame m_instance;
    protected static final Logger m_logger;
    
    public static NetExchangeInvitationFrame getInstance() {
        return NetExchangeInvitationFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        switch (message.getId()) {
            case 6002: {
                final ExchangeInvitationMessage msg = (ExchangeInvitationMessage)message;
                final ItemTrade currentTrade = ClientTradeHelper.INSTANCE.getCurrentTrade();
                if (currentTrade != null && currentTrade.getId() != msg.getExchangeId()) {
                    return false;
                }
                final PlayerCharacter requester = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg.getRequesterId());
                if (requester == null) {
                    return false;
                }
                final WakfuExchangerUser requesterUser = new WakfuExchangerUser(requester);
                final WakfuExchangerUser targetUser = new WakfuExchangerUser(localPlayer);
                final ItemTrade exchanger = new ItemTrade(msg.getExchangeId(), requesterUser, targetUser);
                final ExchangeUserUpdater updater = new ExchangeUserUpdater(exchanger);
                requesterUser.addListener(updater);
                targetUser.addListener(updater);
                if (WakfuUserGroupManager.getInstance().getIgnoreGroup().getUserById(requester.getOwnerId()) != null) {
                    ((ItemExchanger<ContentType, WakfuExchangerUser>)exchanger).declineInvitation(requesterUser, ItemExchangerEndEvent.Reason.USER_IGNORED);
                    return false;
                }
                exchanger.start();
                if (exchanger.getCurrentState() != ItemExchangeState.FINISHED) {
                    ClientTradeHelper.INSTANCE.setCurrentTrade(exchanger);
                }
                return false;
            }
            case 6004: {
                final ExchangeInvitationConfirmationMessage msg2 = (ExchangeInvitationConfirmationMessage)message;
                final ItemTrade currentTrade = ClientTradeHelper.INSTANCE.getCurrentTrade();
                if (currentTrade != null && currentTrade.getId() != msg2.getExchangeId()) {
                    return false;
                }
                switch (msg2.getResult()) {
                    case 0: {
                        final PlayerCharacter target = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg2.getRequestedId());
                        final WakfuExchangerUser requesterUser = new WakfuExchangerUser(localPlayer);
                        final WakfuExchangerUser targetUser = new WakfuExchangerUser(target);
                        final ItemTrade exchanger = new ItemTrade(msg2.getExchangeId(), requesterUser, targetUser);
                        final ExchangeUserUpdater updater = new ExchangeUserUpdater(exchanger);
                        requesterUser.addListener(updater);
                        targetUser.addListener(updater);
                        exchanger.start();
                        if (exchanger.getCurrentState() != ItemExchangeState.FINISHED) {
                            ClientTradeHelper.INSTANCE.setCurrentTrade(exchanger);
                            break;
                        }
                        break;
                    }
                    case 3: {
                        if (currentTrade != null) {
                            final PlayerCharacter requested = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg2.getRequestedId());
                            currentTrade.acceptInvitation(requested.getId());
                            break;
                        }
                        break;
                    }
                    case 1: {
                        final PlayerCharacter requested = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg2.getRequestedId());
                        final String msgUserBusy = WakfuTranslator.getInstance().getString("exchange.canceledUserBusy", (requested == null) ? "" : requested.getName());
                        Xulor.getInstance().msgBox(msgUserBusy, WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 4099L, 102, 1);
                        if (currentTrade == null) {
                            break;
                        }
                        if (requested != null) {
                            currentTrade.cancelExchangeUserBusy(requested.getId());
                            break;
                        }
                        currentTrade.getInvitationMessageBoxControler().cleanUpAndRemoveQuick();
                        WakfuGameEntity.getInstance().removeFrame(UIExchangeInvitationFrame.getInstance());
                        ItemExchangerManager.getInstance().removeExchanger(currentTrade);
                        break;
                    }
                    case 2: {
                        if (currentTrade != null) {
                            ((ItemExchanger<ContentType, WakfuExchangerUser>)currentTrade).declineInvitation(((ItemExchanger<ContentType, WakfuExchangerUser>)currentTrade).getUserById(msg2.getAnswererId()), ItemExchangerEndEvent.Reason.INVITATION_REMOTELY_CANCELED);
                            break;
                        }
                        break;
                    }
                    case 4: {
                        final PlayerCharacter requested = (PlayerCharacter)CharacterInfoManager.getInstance().getCharacter(msg2.getRequestedId());
                        final String errorMessage = WakfuTranslator.getInstance().getString("error.chat.userIgnoreYou", requested.getName());
                        final ChatMessage chatMessage = new ChatMessage(errorMessage);
                        chatMessage.setPipeDestination(3);
                        final ChatManager chatManager = ChatManager.getInstance();
                        chatManager.pushMessage(chatMessage);
                        if (currentTrade != null) {
                            currentTrade.cancel(localPlayer.getId());
                            break;
                        }
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
        m_instance = new NetExchangeInvitationFrame();
        m_logger = Logger.getLogger((Class)NetExchangeInvitationFrame.class);
    }
}
