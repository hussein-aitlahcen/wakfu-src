package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.havenWorld.view.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIHavenWorldPanelFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIHavenWorldPanelFrame m_instance;
    private HavenWorldView m_worldView;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIHavenWorldPanelFrame getInstance() {
        return UIHavenWorldPanelFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        message.getId();
        return true;
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
            if (this.m_worldView == null) {
                UIHavenWorldPanelFrame.m_logger.error((Object)"World null on ne peut pas afficher le panneau !");
                return;
            }
            PropertiesProvider.getInstance().setPropertyValue("havenWorld", this.m_worldView);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("havenWorldPanelDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIHavenWorldPanelFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("havenWorldPanelDialog", Dialogs.getDialogPath("havenWorldPanelDialog"), 256L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.havenWorldPanel", HavenWorldPanelDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            this.m_worldView.removeFromTimeManager();
            PropertiesProvider.getInstance().removeProperty("havenWorld");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("havenWorldPanelDialog");
            Xulor.getInstance().removeActionClass("wakfu.havenWorldPanel");
        }
    }
    
    public void setWorldView(final HavenWorldView worldView) {
        this.m_worldView = worldView;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIHavenWorldPanelFrame.class);
        UIHavenWorldPanelFrame.m_instance = new UIHavenWorldPanelFrame();
    }
}
