package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.wakfu.client.ui.*;

public class UIWeatherInfoSmallFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIWeatherInfoSmallFrame m_instance;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIWeatherInfoSmallFrame getInstance() {
        return UIWeatherInfoSmallFrame.m_instance;
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
            if (!PropertiesProvider.getInstance().getBooleanProperty("isInExterior")) {
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("weatherInfoSmallDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIWeatherInfoSmallFrame.getInstance());
                    }
                }
            };
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("weather.manager", WeatherInfoManager.getInstance());
            Xulor.getInstance().load("weatherInfoSmallDialog", Dialogs.getDialogPath("weatherInfoSmallDialog"), 32769L, (short)10000);
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().removeProperty("weather.manager");
            Xulor.getInstance().unload("weatherInfoSmallDialog");
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIWeatherInfoSmallFrame.class);
        UIWeatherInfoSmallFrame.m_instance = new UIWeatherInfoSmallFrame();
    }
}
