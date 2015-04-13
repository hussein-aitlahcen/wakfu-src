package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.group.partySearch.*;
import com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.common.game.group.partySearch.*;
import com.ankamagames.xulor2.core.messagebox.*;
import java.util.*;

public class NetPartySearchFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static final NetPartySearchFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 20415: {
                NetPartySearchFrame.m_logger.info((Object)"[PartySearch] R\u00e9ception d'un r\u00e9sultat de recherche");
                final PartySearchPlayerSearchResultMessage msg = (PartySearchPlayerSearchResultMessage)message;
                final List<PartyRequester> result = msg.getResult();
                final AbstractUIMessage resultMsg = new UIMessage((short)19445);
                resultMsg.setObjectValue(result);
                Worker.getInstance().pushMessage(resultMsg);
                return false;
            }
            case 20417: {
                final PartySearchPlayerInviteTransferMessage msg2 = (PartySearchPlayerInviteTransferMessage)message;
                final PartyRequester requester = msg2.getResult();
                NetPartySearchFrame.m_logger.info((Object)("[PartySearch] R\u00e9ception d'une invitation de groupe via la recherche du requester : " + requester.getId()));
                final StringBuilder sb = new StringBuilder();
                sb.append(WakfuTranslator.getInstance().getString("partySearch.receivedInvitation"));
                final PartyMoodView mood = PartyMoodView.getView(requester.getMood());
                final String description = requester.getDescription();
                final PartyOccupation occupation = PartyOccupationManager.INSTANCE.getPartyOccupation(msg2.getOccupationId());
                final PartyRequesterView view = new PartyRequesterView(requester);
                sb.append("\n\n").append(mood.getName());
                if (!description.isEmpty()) {
                    sb.append(" - ").append(description);
                }
                if (occupation != null) {
                    sb.append(" - ").append(PartyOccupationView.name(occupation));
                }
                sb.append('\n').append(view.getPlayersText());
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(sb.toString(), WakfuMessageBoxConstants.getMessageBoxIconUrl(3), 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        final GroupInvitationByIdAnswerMessage answerMsg = new GroupInvitationByIdAnswerMessage(type == 8, requester.getId(), GroupType.PARTY.getId());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(answerMsg);
                    }
                });
                return false;
            }
            case 20418: {
                final PartySearchPlayerDefinitionMessage msg3 = (PartySearchPlayerDefinitionMessage)message;
                for (final PartyPlayerDefinition def : msg3.getDefinitions()) {
                    UIPartySearchFrame.addPartyPlayer(def);
                }
                NetPartySearchFrame.m_logger.info((Object)("[PartySearch] R\u00e9ception de PartyPlayerDefinitions : " + msg3.getDefinitions()));
                return false;
            }
            case 20419: {
                final PartySearchPlayerFeedbackMessage msg4 = (PartySearchPlayerFeedbackMessage)message;
                PartySearchFeedbackManagement.computeFeedback(PartySearchFeedbackEnum.getFromId(msg4.getFeedbackId()));
                return false;
            }
            case 20420: {
                final PartyRequesterUpdateMessage msg5 = (PartyRequesterUpdateMessage)message;
                final PartyRequester partyRequester = msg5.getPartyRequester();
                UIPartySearchFrame.updatePartyRequester(partyRequester);
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
        m_logger = Logger.getLogger((Class)NetPartySearchFrame.class);
        INSTANCE = new NetPartySearchFrame();
    }
}
