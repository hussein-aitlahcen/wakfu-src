package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.sound.*;

public class UIEcosystemEquilibriumFrame implements MessageFrame
{
    protected static final Logger m_logger;
    public static final int ECOSYSTEM_GAUGE_SIZE = 260;
    public static final int ECOSYSTEM_GAUGE_MARGIN = 45;
    private static UIEcosystemEquilibriumFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIEcosystemEquilibriumFrame getInstance() {
        return UIEcosystemEquilibriumFrame.m_instance;
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
            if (StaticProtectorView.INSTANCE.getProtectorId() == -1 && (ProtectorView.getInstance().getProtector() == null || !PropertiesProvider.getInstance().getBooleanProperty("wakfuEcosystemEnabled"))) {
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("ecosystemEquilibriumDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIEcosystemEquilibriumFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("wakfuMonsterZoneManager", WakfuMonsterZoneManager.getInstance());
            PropertiesProvider.getInstance().setPropertyValue("wakfuResourceZoneManager", WakfuResourceZoneManager.getInstance());
            PropertiesProvider.getInstance().setPropertyValue("wakfuGlobalZoneManager", WakfuGlobalZoneManager.getInstance());
            PropertiesProvider.getInstance().setPropertyValue("wakfuEcosystemGaugeSize", new Dimension(260, 30));
            Xulor.getInstance().load("ecosystemEquilibriumDialog", Dialogs.getDialogPath("ecosystemEquilibriumDialog"), 32769L, (short)10000);
            WakfuSoundManager.getInstance().playGUISound(600141L);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("wakfuMonsterZoneManager");
            PropertiesProvider.getInstance().removeProperty("wakfuResourceZoneManager");
            Xulor.getInstance().unload("ecosystemEquilibriumDialog");
            WakfuSoundManager.getInstance().windowFadeOut();
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIEcosystemEquilibriumFrame.class);
        UIEcosystemEquilibriumFrame.m_instance = new UIEcosystemEquilibriumFrame();
    }
}
