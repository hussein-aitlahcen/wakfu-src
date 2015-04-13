package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.monitoring.statistics.*;
import com.ankamagames.wakfu.client.ui.protocol.message.admin.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.framework.kernel.gameStats.*;
import com.ankamagames.wakfu.client.network.protocol.frame.*;

public class UIAdminMonitorFrame implements MessageFrame
{
    private static final UIAdminMonitorFrame m_instance;
    private static final Runnable RUNNABLE;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIAdminMonitorFrame getInstance() {
        return UIAdminMonitorFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 17720: {
                WakfuGameEntity.getInstance().removeFrame(this);
                return false;
            }
            case 17722: {
                final UIMessage msg = (UIMessage)message;
                return false;
            }
            case 17723: {
                final UIMessage msg = (UIMessage)message;
                StatisticsView.getInstance().createView();
                final UIAdminStatsMonitorUpdateMessage uimsg = (UIAdminStatsMonitorUpdateMessage)msg;
                if (uimsg.isQueryNewStats()) {
                    ProcessScheduler.getInstance().schedule(UIAdminMonitorFrame.RUNNABLE, 10000L, 1);
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
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            PropertiesProvider.getInstance().setPropertyValue("statistics", StatisticsView.getInstance());
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("adminMonitorDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIAdminMonitorFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("adminMonitorDialog", Dialogs.getDialogPath("adminMonitorDialog"), 1L, (short)10000);
            Statistics.getInstance().clear();
            StatisticsView.getInstance().clear();
            if (!WakfuGameEntity.getInstance().hasFrame(NetAdminFrame.getInstance())) {
                WakfuGameEntity.getInstance().pushFrame(NetAdminFrame.getInstance());
            }
            StatisticsView.getInstance().sendUpdateRequest();
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            ProcessScheduler.getInstance().remove(UIAdminMonitorFrame.RUNNABLE);
            PropertiesProvider.getInstance().removeProperty("statistics");
            Xulor.getInstance().unload("adminMonitorDialog");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Statistics.getInstance().clear();
            StatisticsView.getInstance().clear();
        }
    }
    
    static {
        m_instance = new UIAdminMonitorFrame();
        RUNNABLE = new SendUpdateRunnable();
    }
    
    private static class SendUpdateRunnable implements Runnable
    {
        @Override
        public void run() {
            if (WakfuGameEntity.getInstance().hasFrame(UIAdminMonitorFrame.getInstance())) {
                StatisticsView.getInstance().sendUpdateRequest();
            }
        }
    }
}
