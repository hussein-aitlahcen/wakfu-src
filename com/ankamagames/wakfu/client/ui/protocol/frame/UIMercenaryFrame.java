package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.achievements.mercenary.*;
import com.ankamagames.wakfu.client.core.game.achievements.ui.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;

public class UIMercenaryFrame implements MessageFrame
{
    public static final UIMercenaryFrame INSTANCE;
    protected static final Logger m_logger;
    private MercenariesView m_mercenariesView;
    private DialogUnloadListener m_dialogUnloadListener;
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 19111: {
                final UIMessage msg = (UIMessage)message;
                this.m_mercenariesView.setSelectedFilter(msg.getObjectValue());
                return false;
            }
            case 19110: {
                final UIMessage msg = (UIMessage)message;
                this.m_mercenariesView.setSelectedAchievement(msg.getObjectValue());
                return false;
            }
            case 19112: {
                this.m_mercenariesView.activateSelectedAchievement();
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("mercenaryDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIMercenaryFrame.INSTANCE);
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("mercenaries", this.m_mercenariesView);
            Xulor.getInstance().load("mercenaryDialog", Dialogs.getDialogPath("mercenaryDialog"), 32768L, (short)10000);
            PropertiesProvider.getInstance().setLocalPropertyValue("displayedAchievement", this.m_mercenariesView.getSelectedAchievement(), "mercenaryDialog");
            Xulor.getInstance().putActionClass("wakfu.mercenaries", MercenaryDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            PropertiesProvider.getInstance().removeProperty("mercenaries");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("mercenaryDialog");
            Xulor.getInstance().removeActionClass("wakfu.mercenaries");
        }
    }
    
    public void initFrame(final int categoryId) {
        this.m_mercenariesView = new MercenariesView(categoryId);
    }
    
    @Override
    public long getId() {
        return 0L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    static {
        INSTANCE = new UIMercenaryFrame();
        m_logger = Logger.getLogger((Class)UIMercenaryFrame.class);
    }
}
