package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.merchant.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.*;

public class NetNotInFightManagementFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static NetNotInFightManagementFrame m_instance;
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 5240: {
                final WalletUpdateMessage updateMessage = (WalletUpdateMessage)message;
                WakfuGameEntity.getInstance().getLocalPlayer().getOwnedDimensionalBag().getWallet().setAmountOfCash(updateMessage.getAmountOfCash());
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
    
    public static NetNotInFightManagementFrame getInstance() {
        return NetNotInFightManagementFrame.m_instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)NetNotInFightManagementFrame.class);
        NetNotInFightManagementFrame.m_instance = new NetNotInFightManagementFrame();
    }
}
