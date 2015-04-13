package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public final class NetConnectionQueueFrame extends MessageRunnerFrame
{
    public static final NetConnectionQueueFrame INSTANCE;
    private static MessageBoxControler m_queueBoxController;
    
    private NetConnectionQueueFrame() {
        super(new MessageRunner[] { new ConnectionQueueRunner() });
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (NetConnectionQueueFrame.m_queueBoxController == null) {
            return;
        }
        NetConnectionQueueFrame.m_queueBoxController.cleanUpAndRemoveQuick();
        NetConnectionQueueFrame.m_queueBoxController = null;
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new NetConnectionQueueFrame();
    }
    
    private static class ConnectionQueueRunner implements MessageRunner<ConnectionQueuedMessage>
    {
        @Override
        public boolean run(final ConnectionQueuedMessage msg) {
            final int position = msg.getQueuePosition();
            final String title = WakfuTranslator.getInstance().getString("connection.queued.title");
            final String message = WakfuTranslator.getInstance().getString("connection.queued.message", position);
            if (NetConnectionQueueFrame.m_queueBoxController != null) {
                NetConnectionQueueFrame.m_queueBoxController.getData().setMessage(message);
                return false;
            }
            final MessageBoxData data = new MessageBoxData(102, 0, message, title, null, 16901L);
            NetConnectionQueueFrame.m_queueBoxController = Xulor.getInstance().msgBox(data);
            NetConnectionQueueFrame.m_queueBoxController.addEventListener(new MessageBoxEventListener() {
                @Override
                public void messageBoxClosed(final int type, final String userEntry) {
                    WakfuGameEntity.getInstance().setLogin(null);
                    WakfuGameEntity.getInstance().setPassword(null);
                    WakfuGameEntity.getInstance().setSecurityCardQuestion(null);
                    WakfuGameEntity.getInstance().setSecurityCardAnswer(null);
                    WakfuGameEntity.getInstance().disconnectFromServer("Player cancel waiting queue");
                }
            });
            return false;
        }
        
        @Override
        public int getProtocolId() {
            return 1208;
        }
    }
}
