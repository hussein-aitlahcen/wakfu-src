package com.ankamagames.wakfu.client.network.protocol.frame;

import com.ankamagames.framework.kernel.events.*;
import org.apache.log4j.*;
import com.ankamagames.framework.kernel.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.ecosystem.*;
import com.ankamagames.wakfu.client.core.game.wakfu.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.weather.*;
import java.nio.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.protector.ecosystem.*;

public class NetEcosystemFrame implements MessageFrame
{
    protected static final Logger m_logger;
    private static NetEcosystemFrame m_instance;
    
    public static NetEcosystemFrame getInstance() {
        return NetEcosystemFrame.m_instance;
    }
    
    @Override
    public void onFrameAdd(final FrameHandler frameHandler, final boolean isAboutToBeAdded) {
    }
    
    @Override
    public void onFrameRemove(final FrameHandler frameHandler, final boolean isAboutToBeRemoved) {
    }
    
    @Override
    public boolean onMessage(final Message message) {
        final Territory currentTerritory = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentTerritory();
        switch (message.getId()) {
            case 12600: {
                final WakfuZoneInformationMessage msg = (WakfuZoneInformationMessage)message;
                final int ambianceZoneId = msg.getAmbianceZoneId();
                final byte[] wakfuMonsterInformations = msg.getWakfuMonsterInformations();
                final byte[] wakfuResourceInformations = msg.getWakfuResourceInformations();
                final WakfuGlobalZoneManager globalZoneManager = WakfuGlobalZoneManager.getInstance();
                final WakfuResourceZoneManager resourceZoneManager = WakfuResourceZoneManager.getInstance();
                final WakfuMonsterZoneManager monsterZoneManager = WakfuMonsterZoneManager.getInstance();
                globalZoneManager.setAmbianceZoneId(ambianceZoneId);
                resourceZoneManager.unserialize(wakfuResourceInformations);
                monsterZoneManager.unserialize(wakfuMonsterInformations);
                globalZoneManager.updateZoneEquilibrium();
                if (currentTerritory != null) {
                    final Protector protector = (Protector)currentTerritory.getProtector();
                    if (protector != null) {
                        final ProtectorEcosystemHandler handler = protector.getEcosystemHandler();
                        final ProtectorEcosystemView ecosystem = handler.getView();
                        final ArrayList<WakfuEcosystemFamilyInfo> families = monsterZoneManager.getMonsterFamilyInfos();
                        for (int i = 0, size = families.size(); i < size; ++i) {
                            final int id = families.get(i).getFamilyId();
                            final ProtectorEcosystemElement ecosystemElement = ecosystem.getElement(id, true);
                            if (ecosystemElement == null) {
                                NetEcosystemFrame.m_logger.error((Object)("On tente d'update le famille d'id=" + id + " alors qu'elle n'existe pas dans l'\u00e9cosyst\u00e8me !!!"));
                            }
                            else {
                                ecosystemElement.updateExtinctField();
                            }
                        }
                    }
                }
                WakfuSoundManager.getInstance().onWakfuStasisUpdate();
                final boolean ecoZoneEnabled = resourceZoneManager.getResourceFamilyInfos().size() > 0 && monsterZoneManager.getMonsterFamilyInfos().size() > 0;
                PropertiesProvider.getInstance().setPropertyValue("wakfuEcosystemEnabled", ecoZoneEnabled);
                PropertiesProvider.getInstance().setPropertyValue("wakfuGlobalZoneManager", WakfuGlobalZoneManager.getInstance());
                PropertiesProvider.getInstance().firePropertyValueChanged(WakfuGlobalZoneManager.getInstance(), "protectorSatisfaction", "protectorSatisfactionColor");
                return false;
            }
            case 9504: {
                final WeatherZoneInformationMessage weatherMessage = (WeatherZoneInformationMessage)message;
                final WeatherInfo weather = new WeatherInfo();
                weather.setDayWeather(weatherMessage.getDayTMin(), weatherMessage.getDayTMax(), weatherMessage.getDayTMod(), weatherMessage.getDayWindForce(), weatherMessage.getDayWindMod(), weatherMessage.getDayPrecipitations(), weatherMessage.getDayPrecipitationsMod());
                weather.setWeather(weatherMessage.getTemperature() + weatherMessage.getDayTMod(), 0.0f, 0.0f, 0.0f, null, weatherMessage.getRainIntensity(), weatherMessage.getRainDuration(), 0.0f, weatherMessage.getWind(), 0.0f);
                if (currentTerritory != null) {
                    final Protector protector2 = (Protector)currentTerritory.getProtector();
                    weather.updateViews(protector2);
                }
                else {
                    NetEcosystemFrame.m_logger.error((Object)"R\u00e9ception d'informations de meteo wakfu hors d'un territoire : \u00e9trange");
                }
                return false;
            }
            case 9602: {
                final WeatherHistoryAnswerMessage msg2 = (WeatherHistoryAnswerMessage)message;
                final byte[] serializedHistory = msg2.getHistory();
                if (serializedHistory != null && serializedHistory.length > 0) {
                    final WeatherHistory weatherHistory = new WeatherHistory();
                    weatherHistory.fromBuild(ByteBuffer.wrap(serializedHistory));
                    WeatherInfoManager.getInstance().updateFromWeatherHistory(weatherHistory);
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
    
    static {
        m_logger = Logger.getLogger((Class)NetEcosystemFrame.class);
        NetEcosystemFrame.m_instance = new NetEcosystemFrame();
    }
}
