package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.global.group.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.global.clientToServer.group.*;
import com.ankamagames.wakfu.client.ui.protocol.message.group.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.messagebox.*;

public class UIGroupFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final UIGroupFrame m_instance;
    
    public static UIGroupFrame getInstance() {
        return UIGroupFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17600: {
                final UIGroupInvitationMessage msg = (UIGroupInvitationMessage)message;
                String invitMessage = "";
                String messageBoxIconUrl = WakfuMessageBoxConstants.getMessageBoxIconUrl(7);
                switch (msg.getGroupType()) {
                    case PARTY: {
                        final boolean isFromPartySearch = msg.getBooleanValue();
                        if (isFromPartySearch) {
                            invitMessage = WakfuTranslator.getInstance().getString("partySearch.receivedInvitationByOutDoor", msg.getInviterName());
                            messageBoxIconUrl = WakfuMessageBoxConstants.getMessageBoxIconUrl(3);
                        }
                        else {
                            invitMessage = WakfuTranslator.getInstance().getString("group.party.invitation", msg.getInviterName());
                            messageBoxIconUrl = WakfuMessageBoxConstants.getMessageBoxIconUrl(3);
                        }
                        if (UIPartySearchFrame.isPartyRequesterExists()) {
                            invitMessage = invitMessage + "\n\n" + WakfuTranslator.getInstance().getString("partySearch.joinWillUnregister");
                        }
                        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                        if (localPlayer != null) {
                            final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(localPlayer.getInstanceId());
                            if (worldInfo != null && worldInfo.getGrouptype() == GroupType.PARTY) {
                                invitMessage = invitMessage + '\n' + WakfuTranslator.getInstance().getString("group.party.leave.warn.dungeon");
                            }
                            break;
                        }
                        break;
                    }
                }
                final MessageBoxControler messageBoxControler = Xulor.getInstance().msgBox(invitMessage, messageBoxIconUrl, 2073L, 102, 1);
                messageBoxControler.addEventListener(new MessageBoxEventListener() {
                    @Override
                    public void messageBoxClosed(final int type, final String userEntry) {
                        final GroupInvitationAnswerMessage answerMsg = new GroupInvitationAnswerMessage();
                        answerMsg.setInvitationAccepted(type == 8);
                        answerMsg.setInviterName(msg.getInviterName());
                        answerMsg.setInviterId(msg.getInviterId());
                        answerMsg.setGroupType(msg.getGroupType().getId());
                        WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(answerMsg);
                    }
                });
                return false;
            }
            case 17601: {
                final UIGroupSendInvitationMessage msg2 = (UIGroupSendInvitationMessage)message;
                final LocalPlayerCharacter localPlayer2 = WakfuGameEntity.getInstance().getLocalPlayer();
                if (msg2.getGroupType() == GroupType.PARTY) {
                    localPlayer2.getPartyComportment().inviteSomeone(msg2.getName());
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
        m_logger = Logger.getLogger((Class)UIGroupFrame.class);
        m_instance = new UIGroupFrame();
    }
}
