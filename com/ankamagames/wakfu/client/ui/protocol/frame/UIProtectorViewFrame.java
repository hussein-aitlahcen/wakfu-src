package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.xulor2.core.*;

public class UIProtectorViewFrame implements MessageFrame
{
    private static UIProtectorViewFrame m_instance;
    public static final int ALL_VIEW_MODE = 0;
    public static final int CHALLENGES_VIEW_MODE = 1;
    public static final int SECRETS_VIEW_MODE = 2;
    private static final Logger m_logger;
    private Protector m_protector;
    private DialogUnloadListener m_dialogUnloadListener;
    private MobileStartPathListener m_listener;
    
    public static UIProtectorViewFrame getInstance() {
        return UIProtectorViewFrame.m_instance;
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
            final LocalPlayerCharacter character = WakfuGameEntity.getInstance().getLocalPlayer();
            this.m_listener = new MobileStartPathListener() {
                @Override
                public void pathStarted(final PathMobile mobile, final PathFindResult path) {
                    WakfuGameEntity.getInstance().removeFrame(UIProtectorViewFrame.getInstance());
                }
            };
            character.getActor().addStartPathListener(this.m_listener);
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("protectorViewDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIProtectorViewFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("protectorViewMode", 0);
            final EventDispatcher ed = Xulor.getInstance().load("protectorViewDialog", Dialogs.getDialogPath("protectorViewDialog"), 32768L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.protectorView", ProtectorViewDialogActions.class);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            WakfuGameEntity.getInstance().getLocalPlayer().getActor().removeStartListener(this.m_listener);
            this.m_listener = null;
            PropertiesProvider.getInstance().removeProperty("protectorViewMode");
            Xulor.getInstance().removeActionClass("wakfu.protectorView");
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("protectorViewDialog");
        }
    }
    
    static {
        UIProtectorViewFrame.m_instance = new UIProtectorViewFrame();
        m_logger = Logger.getLogger((Class)UIProtectorViewFrame.class);
    }
}
