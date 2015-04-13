package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.dialog.*;

public class NetInteractiveDialogFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static NetInteractiveDialogFrame INSTANCE;
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 15704: {
                final DisplayDialogRequestMessage ddrm = (DisplayDialogRequestMessage)message;
                UIInteractiveDialogFrame.getInstance().startDialog(ddrm.getDialogId(), ddrm.getSourceType(), ddrm.getSourceId());
                return false;
            }
            case 15702: {
                final ValidateDialogResultMessage validateDialogResultMessage = (ValidateDialogResultMessage)message;
                UIInteractiveDialogFrame.getInstance().setNextDialog(validateDialogResultMessage.getDialogId());
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
        m_logger = Logger.getLogger((Class)NetInteractiveDialogFrame.class);
        NetInteractiveDialogFrame.INSTANCE = new NetInteractiveDialogFrame();
    }
}
