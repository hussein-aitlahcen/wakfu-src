package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.wakfu.client.core.*;

public class UIProtectorFrame implements MessageFrame
{
    private static UIProtectorFrame m_instance;
    private static final Logger m_logger;
    
    public static UIProtectorFrame getInstance() {
        return UIProtectorFrame.m_instance;
    }
    
    @Override
    public boolean onMessage(final Message message) {
        switch (message.getId()) {
            case 16002: {
                if (StaticProtectorView.INSTANCE.getProtectorId() != -1) {
                    UIProtectorFrame.m_logger.info((Object)"On a push\u00e9 un protecteur statique, on ne l'enl\u00e8ve pas \u00e0 l'entr\u00e9e du territoire (retir\u00e9 manuelllement)");
                    return false;
                }
                ProtectorView.getInstance().setCurrentProtector(null);
                StaticProtectorView.INSTANCE.setProtectorId(-1);
                return false;
            }
            default: {
                return true;
            }
        }
    }
    
    @Override
    public long getId() {
        return 3L;
    }
    
    @Override
    public void setId(final long id) {
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
        if (!isAboutToBeAdded) {
            PropertiesProvider.getInstance().setPropertyValue("weather.manager", WeatherInfoManager.getInstance());
            PropertiesProvider.getInstance().setPropertyValue("protector", (StaticProtectorView.INSTANCE.getProtectorId() != -1) ? StaticProtectorView.INSTANCE : ProtectorView.getInstance());
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            PropertiesProvider.getInstance().removeProperty("protector");
            ProtectorView.getInstance().setCurrentProtector(null);
            StaticProtectorView.INSTANCE.setProtectorId(-1);
        }
    }
    
    public void unloadLinkedDialogs() {
        if (WakfuGameEntity.getInstance().hasFrame(UIWeatherInfoFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIWeatherInfoFrame.getInstance());
        }
        if (WakfuGameEntity.getInstance().hasFrame(UIEcosystemEquilibriumFrame.getInstance())) {
            WakfuGameEntity.getInstance().removeFrame(UIEcosystemEquilibriumFrame.getInstance());
        }
        PropertiesProvider.getInstance().setPropertyValue("challengeDetailsVisible", false);
    }
    
    static {
        UIProtectorFrame.m_instance = new UIProtectorFrame();
        m_logger = Logger.getLogger((Class)UIProtectorFrame.class);
    }
}
