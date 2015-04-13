package com.ankamagames.wakfu.client.ui.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.game.interactiveElement.*;
import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.climate.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.interactiveElement.*;

public class UIWeatherInfoFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static UIWeatherInfoFrame m_instance;
    private WeatherBoard m_board;
    private DialogUnloadListener m_dialogUnloadListener;
    
    public static UIWeatherInfoFrame getInstance() {
        return UIWeatherInfoFrame.m_instance;
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
            if (!PropertiesProvider.getInstance().getBooleanProperty("isInExterior") || ProtectorView.getInstance().isStaticProtector()) {
                return;
            }
            this.m_dialogUnloadListener = new DialogUnloadListener() {
                @Override
                public void dialogUnloaded(final String id) {
                    if (id.equals("weatherInfoDialog")) {
                        WakfuGameEntity.getInstance().removeFrame(UIWeatherInfoFrame.getInstance());
                    }
                }
            };
            WeatherInfoManager.getInstance().resetResourceFilters();
            WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(new WeatherHistoryRequestMessage());
            Xulor.getInstance().addDialogUnloadListener(this.m_dialogUnloadListener);
            PropertiesProvider.getInstance().setPropertyValue("weather.manager", WeatherInfoManager.getInstance());
            Xulor.getInstance().load("weatherInfoDialog", Dialogs.getDialogPath("weatherInfoDialog"), 32769L, (short)10000);
            Xulor.getInstance().putActionClass("wakfu.weatherInformation", WeatherInformationDialogActions.class);
            if (this.m_board != null) {
                this.m_board.fireAction(InteractiveElementAction.ASK_INFORMATIONS, WakfuGameEntity.getInstance().getLocalPlayer());
            }
        }
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
        if (!isAboutToBeRemoved) {
            Xulor.getInstance().removeDialogUnloadListener(this.m_dialogUnloadListener);
            Xulor.getInstance().unload("weatherInfoDialog");
            Xulor.getInstance().removeActionClass("wakfu.weatherInformation");
        }
        this.m_board = null;
    }
    
    public void setBoard(final WeatherBoard weatherBoard) {
        this.m_board = weatherBoard;
    }
    
    static {
        m_logger = Logger.getLogger((Class)UIWeatherInfoFrame.class);
        UIWeatherInfoFrame.m_instance = new UIWeatherInfoFrame();
    }
}
