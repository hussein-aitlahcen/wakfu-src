package com.ankamagames.wakfu.client.core.world;

import com.ankamagames.framework.reflect.*;
import org.apache.log4j.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import com.ankamagames.wakfu.client.sound.*;
import java.io.*;
import com.ankamagames.wakfu.client.alea.ambiance.*;
import com.ankamagames.wakfu.client.core.weather.*;
import com.ankamagames.wakfu.common.game.weather.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.sound.group.field.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.criteria.event.*;
import com.ankamagames.wakfu.client.core.world.river.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.client.sound.ambiance2D.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.wakfu.client.ui.systemMessage.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.protector.*;
import com.ankamagames.framework.graphics.engine.light.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;

public class WakfuAmbianceListener implements FieldProvider
{
    public static final int UNKNOW_GRAPHICAL_AMBIENCE_ID = -1;
    public static final int CHAOS_GRAPHICAL_AMBIENCE_ID = -2;
    public static final int HAVEN_WORLD_AMBIANCE_ID = 1009;
    private static final Logger m_logger;
    private short m_worldId;
    private AmbienceData m_ambianceData;
    private int m_oldGraphicAmbianceId;
    private boolean m_isInChaos;
    private ChaosAmbianceHandler m_chaosHandler;
    private WakfuClientEnvironmentMap m_currentMap;
    private String m_name;
    private int m_lastGroupId;
    private final AmbianceReader m_ambianceReader;
    private static final WakfuAmbianceListener m_instance;
    public final String ZONE_NAME = "zoneName";
    
    public WakfuAmbianceListener() {
        super();
        this.m_oldGraphicAmbianceId = -1;
        this.m_ambianceReader = new AmbianceReader();
    }
    
    public void setInChaos(final boolean inChaos) {
        this.setInChaos(inChaos, 5000);
    }
    
    private void setInChaos(final boolean inChaos, final int duration) {
        final boolean changed = this.m_isInChaos != inChaos;
        if (changed) {
            this.stopChaos(duration);
        }
        this.m_isInChaos = inChaos;
        if (changed) {
            this.m_ambianceData = this.getChaosAmbienceData(this.m_ambianceData);
            WakfuSoundManager.getInstance().onAmbianceZoneTypeChange();
        }
        if (this.m_ambianceData != null) {
            this.setGraphicalAmbience(this.m_ambianceData.m_graphicAmbienceId);
        }
    }
    
    public void initialize(final String ambianceFilename) throws IOException {
    }
    
    private void setGraphicalAmbience(int graphicalAmbienceId) {
        if (this.m_isInChaos) {
            graphicalAmbienceId = -2;
        }
        if (graphicalAmbienceId == this.m_oldGraphicAmbianceId) {
            return;
        }
        this.stopOldAmbiance();
        if (graphicalAmbienceId != -1) {
            if (graphicalAmbienceId == -2) {
                this.startChaos();
            }
            else {
                AmbianceManagerFake.INSTANCE.playAmbiance(graphicalAmbienceId);
            }
        }
        this.m_oldGraphicAmbianceId = graphicalAmbienceId;
    }
    
    private void startChaos() {
        final WeatherEffectManager weatherManager = WeatherEffectManager.INSTANCE;
        weatherManager.changeTo(Weather.CHAOS);
        weatherManager.changeWindStrength(0.9f);
        weatherManager.setLockChanging(true);
        (this.m_chaosHandler = new ChaosAmbianceHandler()).start();
    }
    
    private void stopChaos(final int duration) {
        if (!this.m_isInChaos) {
            return;
        }
        this.m_chaosHandler.stop(duration);
        final WeatherInfo weatherInfo = WeatherInfoManager.getInstance().getCurrentWeather().getWeatherInfo();
        final WeatherEffectManager weatherManager = WeatherEffectManager.INSTANCE;
        weatherManager.setLockChanging(false);
        if (weatherInfo != null) {
            weatherManager.changeTo(weatherInfo.getWeatherType(), duration);
            weatherManager.changeWindStrength(weatherInfo.getWindForce());
        }
    }
    
    private void stopOldAmbiance() {
        if (this.m_oldGraphicAmbianceId != -1) {
            AmbianceManager.INSTANCE.stopAmbiance(this.m_oldGraphicAmbianceId);
            AmbianceManagerFake.INSTANCE.stopAmbiance(this.m_oldGraphicAmbianceId);
            this.m_oldGraphicAmbianceId = -1;
        }
    }
    
    public void setWorldId(final short worldId) {
        AmbianceManager.INSTANCE.clear();
        this.stopOldAmbiance();
        this.m_worldId = worldId;
    }
    
    private void generateSeaPositionEvent() {
        if (this.m_currentMap == null) {
            return;
        }
        final WorldInfoManager.WorldInfo info = WorldInfoManager.getInstance().getInfo((short)TopologyMapManager.getWorldId());
        float altitude;
        if (info == null) {
            altitude = 0.0f;
        }
        else {
            altitude = info.m_altitude;
        }
        final float localPlayerAltitude = WakfuGameEntity.getInstance().getLocalPlayer().getAltitude();
        final int mapX = this.m_currentMap.getX();
        final int mapY = this.m_currentMap.getY();
        int strength = 0;
        int vectX = 0;
        int vectY = 0;
        for (short x = -1; x < 2; ++x) {
            for (int y = -1; y < 2; ++y) {
                final WakfuClientEnvironmentMap map = (WakfuClientEnvironmentMap)EnvironmentMapManager.getInstance().getMap((short)(mapX + x), (short)(mapY + y));
                if (map != null) {
                    final float percent = map.getSeaPercent() * 100.0f;
                    vectX += (int)(x * percent);
                    vectY += (int)(y * percent);
                    strength += (int)percent;
                }
            }
        }
        strength /= (int)9.0f;
        final Vector3 pos = new Vector3(vectX, vectY, 0.0f).normalize();
        WakfuSoundManager.getInstance().onEvent(new GeographySoundEvent(new StaticPositionProvider(pos.getX() * 10.0f, pos.getY() * 10.0f, localPlayerAltitude - altitude, true, 0), GeographyEventType.SEA, strength));
    }
    
    public void onCreateScene(final int partitionWorldX, final int partitionWorldY) {
        this.m_currentMap = (WakfuClientEnvironmentMap)EnvironmentMapManager.getInstance().getMapFromCell(partitionWorldX, partitionWorldY);
        this.generateSeaPositionEvent();
        SoundPartitionManager.INSTANCE.setMapXY((short)(partitionWorldX / 18), (short)(partitionWorldY / 18));
        AmbianceManager.INSTANCE.reset();
        AmbianceManagerFake.INSTANCE.reset();
    }
    
    public static WakfuAmbianceListener getInstance() {
        return WakfuAmbianceListener.m_instance;
    }
    
    private AmbienceData getChaosAmbienceData(AmbienceData data) {
        if (data == null) {
            return null;
        }
        if (!this.m_isInChaos) {
            return AmbienceZoneBank.getInstance().get(data.m_zoneId);
        }
        data = new AmbienceData(data.m_zoneId, data.m_name, data.m_musicPlayListId, data.m_useReverb, data.m_soundPreset, AmbianceZoneType.CHAOS.getId(), -2, data.m_entryGfxId);
        return data;
    }
    
    public AmbienceData getAmbianceData() {
        return this.m_ambianceData;
    }
    
    public void changeCell(final int x, final int y) {
        if (this.m_currentMap == null) {
            return;
        }
        if (!this.m_currentMap.isInMap(x, y)) {
            this.m_currentMap = (WakfuClientEnvironmentMap)EnvironmentMapManager.getInstance().getMapFromCell(x, y);
            if (this.m_currentMap != null) {
                this.generateSeaPositionEvent();
                SoundPartitionManager.INSTANCE.setMapXY(this.m_currentMap.getX(), this.m_currentMap.getY());
            }
        }
        SoundPartitionManager.INSTANCE.setCameraTargetPosition(x, y);
        final int ambianceId = this.getAmbianceId(x, y);
        if (this.m_ambianceData != null && ambianceId == this.m_ambianceData.m_zoneId) {
            return;
        }
        final AmbienceZoneBank ambienceZoneBank = AmbienceZoneBank.getInstance();
        AmbienceData data = ambienceZoneBank.get(ambianceId);
        if (data != null) {
            final WakfuSoundManager soundManager = WakfuSoundManager.getInstance();
            soundManager.playPlayList((short)data.m_musicPlayListId);
            try {
                if (data.m_useReverb) {
                    soundManager.setReverbFromParams(data.m_soundPreset);
                }
                else {
                    soundManager.setReverbFromParams(-1);
                }
            }
            catch (Exception e) {
                WakfuAmbianceListener.m_logger.error((Object)"Exception :", (Throwable)e);
            }
            final String newName = WakfuTranslator.getInstance().getString(54, ambianceId, new Object[0]);
            if (!newName.equals(this.m_name)) {
                this.m_name = newName;
                this.displayZoneName(x, y, ambianceId);
            }
        }
        else {
            this.m_name = "";
        }
        PropertiesProvider.getInstance().firePropertyValueChanged(this, "zoneName");
        data = this.getChaosAmbienceData(data);
        if ((data == null ^ this.m_ambianceData == null) || (data != null && data.m_zoneTypeId != this.m_ambianceData.m_zoneTypeId)) {
            WakfuSoundManager.getInstance().onAmbianceZoneTypeChange();
        }
        this.setGraphicalAmbience((data == null) ? -1 : data.m_graphicAmbienceId);
        this.m_ambianceData = data;
    }
    
    private int getAmbianceId(final int x, final int y) {
        if (this.m_currentMap == null) {
            WakfuAmbianceListener.m_logger.error((Object)("La map de coord cell(" + x + "," + y + ") n'est pas charg\u00e9e "));
            return -1;
        }
        if (HavenWorldManager.INSTANCE.hasHavenWorld()) {
            return 1009;
        }
        return this.m_currentMap.getAmbianceZone(x, y);
    }
    
    public final void displayGroupName(final boolean inGroup) {
        if (this.m_ambianceData == null) {
            return;
        }
        final AleaIsoCamera isoCamera = WakfuClientInstance.getInstance().getWorldScene().getIsoCamera();
        final int groupId = inGroup ? isoCamera.getGroupId() : 0;
        if (this.m_lastGroupId == groupId) {
            return;
        }
        this.m_lastGroupId = groupId;
        if (!WakfuTranslator.getInstance().containsContentKey(88, groupId)) {
            return;
        }
        final TextWidgetFormater formater = new TextWidgetFormater();
        formater.append(this.m_name);
        if (this.m_lastGroupId != 0) {
            formater.newLine();
            formater.b().addSize(14);
            formater.append(WakfuTranslator.getInstance().getString(88, groupId, new Object[0]));
            formater._b();
        }
        this.showBanner(formater, 100, this.m_ambianceData.m_zoneId);
    }
    
    private boolean showImageBanner(final TextWidgetFormater formater, final int fadeDuration, final int ambianceId) {
        final AmbienceData ambienceData = AmbienceZoneBank.getInstance().get(ambianceId);
        final short entryGfxId = ambienceData.m_entryGfxId;
        if (entryGfxId != -1) {
            WakfuSystemMessageManager.getInstance().showMessage(new ImageSystemMessageData(WakfuSystemMessageManager.SystemMessageType.AMBIENCE_ZONE_INFO_2, formater.finishAndToString(), fadeDuration, 2500, entryGfxId));
            return true;
        }
        return false;
    }
    
    private void showBanner(final TextWidgetFormater formater, final int fadeDuration, final int ambianceId) {
        final String bannerAnimationName = this.getBannerAnimationName(ambianceId);
        if (bannerAnimationName == null) {
            return;
        }
        WakfuSystemMessageManager.getInstance().showMessage(new BannerSystemMessageData(WakfuSystemMessageManager.SystemMessageType.AMBIENCE_ZONE_INFO, formater.finishAndToString(), fadeDuration, 2500, bannerAnimationName));
    }
    
    private void displayZoneName(final int x, final int y, final int ambianceId) {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (localPlayer.getCurrentFight() != null) {
            return;
        }
        while (true) {
            if (this.m_name == null || this.m_name.length() == 0) {
                final TextWidgetFormater formater = new TextWidgetFormater();
                formater.append(this.m_name);
                final Territory enteredTerritory = TerritoriesView.INSTANCE.getFromWorldPosition(x, y);
                formater.newLine();
                if (enteredTerritory != null) {
                    final ProtectorBase protector = enteredTerritory.getProtector();
                    if (protector != null) {
                        formater.b().addSize(14);
                        final int nationId = protector.getCurrentNationId();
                        final ClientCitizenComportment citizen = (ClientCitizenComportment)localPlayer.getCitizenComportment();
                        final Nation nationById = NationManager.INSTANCE.getNationById(nationId);
                        if (nationById != null) {
                            formater.addColor(DiplomacyColorHelper.getColor(citizen, nationById).getRGBtoHex());
                        }
                        else {
                            formater.addColor(DiplomacyColorHelper.getColor(citizen, Nation.VOID_NATION).getRGBtoHex());
                        }
                        formater.append(WakfuTranslator.getInstance().getString(39, nationId, new Object[0]));
                        formater._b();
                    }
                }
                if (!this.showImageBanner(formater, 500, ambianceId)) {
                    this.showBanner(formater, 500, ambianceId);
                }
                return;
            }
            continue;
        }
    }
    
    private String getBannerAnimationName(final int ambianceZoneId) {
        final AmbienceData data = AmbienceZoneBank.getInstance().get(ambianceZoneId);
        final AmbianceZoneType zoneType = AmbianceZoneType.getFromId(data.m_zoneTypeId);
        if (zoneType.hasMessageBanner()) {
            return "AnimAmbiance-" + zoneType.getId();
        }
        final WorldInfoManager.WorldInfo worldInfo = WorldInfoManager.getInstance().getInfo(this.m_worldId);
        final int bannerId = worldInfo.m_bannerTypeId;
        if (bannerId == 0) {
            return null;
        }
        return "AnimInstance-" + bannerId;
    }
    
    public void clear() {
        this.m_currentMap = null;
        this.m_name = "";
        this.m_ambianceData = null;
        this.setInChaos(false, 0);
        this.stopOldAmbiance();
    }
    
    @Override
    public String[] getFields() {
        return new String[] { "zoneName" };
    }
    
    @Override
    public Object getFieldValue(final String fieldName) {
        if (fieldName.equalsIgnoreCase("zoneName")) {
            return (this.m_name != null && this.m_name.length() > 0) ? this.m_name : null;
        }
        return null;
    }
    
    @Override
    public void setFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void prependFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public void appendFieldValue(final String fieldName, final Object value) {
    }
    
    @Override
    public boolean isFieldSynchronisable(final String fieldName) {
        return false;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuAmbianceListener.class);
        m_instance = new WakfuAmbianceListener();
    }
    
    private static class ChaosAmbianceHandler
    {
        private LitSceneModifier m_chaosLightModifier;
        private final LightColor m_light;
        
        private ChaosAmbianceHandler() {
            super();
            this.m_light = new LightColor(LightColor.ONE);
        }
        
        public void start() {
            this.m_light.fadeTo(0.9f, 0.87f, 0.8f, 1000);
            this.m_chaosLightModifier = new LitSceneModifier() {
                private final LightColor lightColor = ChaosAmbianceHandler.this.m_light;
                
                @Override
                public void update(final int deltaTime) {
                    this.lightColor.update(deltaTime);
                }
                
                @Override
                public int getPriority() {
                    return 0;
                }
                
                @Override
                public boolean useless() {
                    return false;
                }
                
                @Override
                public void apply(final int x, final int y, final int z, final int layerId, final float[] colors) {
                    final int n = 0;
                    colors[n] *= this.lightColor.getRed();
                    final int n2 = 1;
                    colors[n2] *= this.lightColor.getGreen();
                    final int n3 = 2;
                    colors[n3] *= this.lightColor.getBlue();
                }
            };
            IsoSceneLightManager.INSTANCE.addLightingModifier(this.m_chaosLightModifier);
        }
        
        public void stop(final int duration) {
            this.m_light.fadeTo(1.0f, 1.0f, 1.0f, duration);
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    IsoSceneLightManager.INSTANCE.removeLightingModifier(ChaosAmbianceHandler.this.m_chaosLightModifier);
                }
            }, duration);
        }
    }
}
