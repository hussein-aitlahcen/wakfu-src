package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient.*;
import com.ankamagames.wakfu.client.core.monitoring.statistics.*;
import com.ankamagames.framework.kernel.gameStats.*;
import com.ankamagames.wakfu.client.ui.protocol.message.admin.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import java.util.*;

public final class NetAdminFrame implements MessageFrame
{
    private static final boolean DEBUG_MODE = false;
    private final Logger m_logger;
    private static final NetAdminFrame m_instance;
    
    public NetAdminFrame() {
        super();
        this.m_logger = Logger.getLogger((Class)NetAdminFrame.class);
    }
    
    public static NetAdminFrame getInstance() {
        return NetAdminFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 2058: {
                final StatisticsUpdateResultMessage msg = (StatisticsUpdateResultMessage)message;
                boolean queryNewStats = false;
                if (StatisticsView.getInstance().getLastTimestamp() > msg.getTimestamp()) {
                    return false;
                }
                if (StatisticsView.getInstance().getLastTimestamp() < msg.getTimestamp()) {
                    Statistics.getInstance().clear();
                    StatisticsView.getInstance().setLastTimestamp(msg.getTimestamp());
                    queryNewStats = true;
                }
                for (final byte[] data : msg.getSerializedNodeSets()) {
                    Statistics.getInstance().unserializeNodeSet(data);
                }
                final UIAdminStatsMonitorUpdateMessage uimsg = new UIAdminStatsMonitorUpdateMessage();
                uimsg.setId(17723);
                uimsg.setQueryNewStats(queryNewStats);
                Worker.getInstance().pushMessage(uimsg);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    static {
        m_instance = new NetAdminFrame();
    }
}
