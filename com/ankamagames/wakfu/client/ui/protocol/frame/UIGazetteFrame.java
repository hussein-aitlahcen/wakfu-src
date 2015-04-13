package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.graphics.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.background.gazette.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIGazetteFrame implements MessageFrame
{
    private static UIGazetteFrame m_instance;
    protected static Logger m_logger;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIGazetteFrame getInstance() {
        return UIGazetteFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16880: {
                final AbstractUIMessage msg = (AbstractUIMessage)message;
                final int gazetteId = msg.getIntValue();
                GazetteManager.INSTANCE.readGazette(gazetteId);
                final UIBackgroundDisplayFrame displayFrame = UIBackgroundDisplayFrame.getInstance();
                displayFrame.loadBackgroundDisplay(gazetteId, false);
                WakfuGameEntity.getInstance().pushFrame(displayFrame);
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
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("gazetteDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIGazetteFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().load("gazetteDialog", Dialogs.getDialogPath("gazetteDialog"), 0L, (short)30000);
            Xulor.getInstance().putActionClass("wakfu.gazette", GazetteDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("gazetteDialog");
            Xulor.getInstance().removeActionClass("wakfu.gazette");
        }
    }
    
    static {
        UIGazetteFrame.m_instance = new UIGazetteFrame();
        UIGazetteFrame.m_logger = Logger.getLogger((Class)UIGazetteFrame.class);
    }
}
