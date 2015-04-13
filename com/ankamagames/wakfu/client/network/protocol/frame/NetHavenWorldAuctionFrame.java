package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.havenworld.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.common.game.havenWorld.action.*;
import com.ankamagames.framework.kernel.*;

public class NetHavenWorldAuctionFrame implements MessageFrame
{
    private static final Logger m_logger;
    public static final NetHavenWorldAuctionFrame INSTANCE;
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 5526: {
                final HavenWorldAuctionAnswerMessage msg = (HavenWorldAuctionAnswerMessage)message;
                UIHavenWorldBidFrame.getInstance().updateAuctionInfo(msg.getGuildId(), msg.getGuildName(), msg.getBidValue());
                return false;
            }
            case 20095: {
                final HavenWorldBidResultMessage msg2 = (HavenWorldBidResultMessage)message;
                final HavenWorldError error = msg2.getError();
                String errorString = null;
                switch (error) {
                    case MONEY_NEEDED: {
                        errorString = "havenWorldMoneyNeeded";
                        break;
                    }
                    case HAS_ANOTHER_BID: {
                        errorString = "havenWorldHasAnotherBid";
                        break;
                    }
                    default: {
                        NetHavenWorldAuctionFrame.m_logger.error((Object)"Code d'erreur d'ench\u00e8re d'havre monde non g\u00e9r\u00e9");
                        break;
                    }
                }
                if (errorString != null) {
                    Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString(errorString), WakfuMessageBoxConstants.getMessageBoxIconUrl(8), 1027L, 7, 1);
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
        return 1L;
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
        m_logger = Logger.getLogger((Class)NetHavenWorldAuctionFrame.class);
        INSTANCE = new NetHavenWorldAuctionFrame();
    }
}
