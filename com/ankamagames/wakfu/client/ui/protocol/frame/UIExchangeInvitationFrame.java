package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.exchange.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.exchange.*;
import com.ankamagames.framework.kernel.*;

public class UIExchangeInvitationFrame implements MessageFrame
{
    private static UIExchangeInvitationFrame m_instance;
    
    public static UIExchangeInvitationFrame getInstance() {
        return UIExchangeInvitationFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16808: {
                final UIExchangeInvitationAcceptRequestMessage msg = (UIExchangeInvitationAcceptRequestMessage)message;
                final ExchangeInvitationAnswerMessage netMessage = new ExchangeInvitationAnswerMessage();
                netMessage.setExchangeId(msg.getInvitationId());
                netMessage.setInvitationResult((byte)0);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
                return false;
            }
            case 16809: {
                final UIExchangeInvitationRejectRequestMessage msg2 = (UIExchangeInvitationRejectRequestMessage)message;
                final ExchangeInvitationAnswerMessage netMessage = new ExchangeInvitationAnswerMessage();
                netMessage.setExchangeId(msg2.getInvitationId());
                netMessage.setInvitationResult((byte)1);
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(netMessage);
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
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    static {
        UIExchangeInvitationFrame.m_instance = new UIExchangeInvitationFrame();
    }
}
