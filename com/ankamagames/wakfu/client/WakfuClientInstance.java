package com.ankamagames.wakfu.client;

import com.ankamagames.baseImpl.graphics.*;
import org.apache.log4j.*;
import java.util.regex.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.*;
import com.ankamagames.wakfu.client.network.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.*;
import com.ankamagames.wakfu.client.network.event.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.sound.*;
import com.ankamagames.wakfu.client.service.*;
import com.ankama.wakfu.utils.injection.*;
import com.ankamagames.wakfu.client.alea.graphics.particle.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.baseImpl.graphics.opengl.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.wakfu.client.alea.graphics.tacticalView.*;
import com.ankamagames.framework.graphics.engine.particleSystem.lightColorHelper.*;
import com.ankamagames.framework.graphics.engine.profiling.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.*;
import com.ankamagames.wakfu.client.alea.ambiance.impl.*;
import com.ankamagames.baseImpl.graphics.alea.ambiance.blocks.*;
import com.ankamagames.wakfu.client.core.game.synchronizing.*;
import com.ankamagames.framework.kernel.core.common.message.synchronizing.*;
import com.ankamagames.framework.net.download.*;
import com.ankamagames.framework.fileFormat.news.*;
import com.ankamagames.wakfu.client.core.descriptionGenerator.*;
import com.ankamagames.baseImpl.common.clientAndServer.core.*;
import com.ankamagames.framework.text.*;
import com.ankamagames.wakfu.client.alea.graphics.anm.*;
import com.ankamagames.framework.graphics.engine.Anm2.actions.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.core.game.miniMap.*;
import com.ankamagames.baseImpl.graphics.game.worldPositionManager.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage.*;
import com.ankamagames.wakfu.client.core.weather.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.world.*;
import com.ankamagames.baseImpl.common.clientAndServer.world.topology.*;
import com.ankamagames.wakfu.client.alea.graphics.havenWorldMini.*;
import com.ankamagames.wakfu.client.alea.graphics.fightView.*;
import com.ankamagames.wakfu.client.alea.environment.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.graphics.isometric.particles.*;
import com.ankamagames.framework.sound.openAL.*;
import com.ankamagames.baseImpl.graphics.alea.environment.*;
import com.ankamagames.wakfu.client.core.world.*;
import com.ankamagames.wakfu.client.core.game.protector.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.client.core.game.travel.provider.*;
import com.ankamagames.wakfu.common.game.travel.*;
import com.ankamagames.wakfu.client.core.protector.*;
import com.ankamagames.wakfu.common.game.protector.event.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.game.item.listener.*;
import com.ankamagames.wakfu.client.core.game.aptitude.*;
import com.ankamagames.wakfu.common.game.aptitude.*;
import com.ankamagames.wakfu.client.core.game.breed.*;
import com.ankamagames.wakfu.client.core.criteria.*;
import com.ankamagames.wakfu.common.game.ai.*;
import com.ankamagames.framework.kernel.core.common.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.client.core.effectArea.*;
import com.ankamagames.wakfu.common.game.effectArea.*;
import com.ankamagames.wakfu.client.core.game.item.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.wakfu.client.core.game.specifics.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.client.core.game.eventsCalendar.serializer.*;
import com.ankamagames.wakfu.common.game.eventsCalendar.serialisation.*;
import com.ankamagames.wakfu.client.core.game.challenge.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.game.ressource.*;
import com.ankamagames.baseImpl.graphics.game.interactiveElement.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.wakfu.client.core.game.time.*;
import com.ankamagames.wakfu.client.core.webBrowser.*;
import com.ankamagames.wakfu.client.script.*;
import com.ankamagames.framework.script.*;
import java.io.*;
import com.ankamagames.baseImpl.graphics.script.*;
import com.ankamagames.framework.fileFormat.io.*;
import java.net.*;
import com.ankamagames.framework.kernel.core.controllers.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.alea.graphics.*;
import com.ankamagames.baseImpl.graphics.alea.display.lights.*;
import com.ankamagames.framework.sound.group.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.baseImpl.graphics.sound.*;
import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.graphics.engine.test.gl.*;
import com.ankamagames.framework.graphics.engine.test.al.*;
import com.ankamagames.framework.graphics.engine.benchmark.gl.*;
import com.ankamagames.framework.graphics.engine.benchmark.*;
import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import com.ankamagames.wakfu.client.core.dungeon.loader.*;
import com.ankamagames.wakfu.client.core.havenWorld.loader.*;
import com.ankamagames.wakfu.client.core.contentInitializer.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.client.binaryStorage.*;
import com.ankamagames.wakfu.client.core.game.item.data.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.client.console.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.preferences.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.helpers.*;
import com.ankamagames.wakfu.client.core.game.group.guild.*;
import com.ankamagames.wakfu.client.core.game.antiAddiction.*;
import com.ankamagames.wakfu.client.ui.mru.MRUActions.*;
import com.ankamagames.wakfu.client.ui.progress.*;
import com.ankamagames.xulor2.core.graphicalMouse.*;
import com.ankamagames.wakfu.client.chat.*;
import com.ankamagames.baseImpl.client.proxyclient.base.chat.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.occupation.*;
import com.ankamagames.wakfu.client.alea.*;
import com.ankamagames.xulor2.core.messagebox.*;
import com.ankamagames.framework.kernel.core.translator.*;
import com.ankamagames.baseImpl.graphics.ui.shortcuts.*;
import com.ankamagames.xulor2.core.dialogclose.*;
import com.ankamagames.wakfu.client.ui.theme.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.client.core.preferences.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.baseImpl.graphics.ui.*;
import com.ankamagames.baseImpl.graphics.game.*;
import com.ankamagames.baseImpl.graphics.alea.worldElements.*;
import com.ankamagames.baseImpl.graphics.alea.*;
import com.ankamagames.baseImpl.graphics.isometric.highlight.*;
import com.ankamagames.wakfu.client.ui.actions.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.kernel.utils.*;
import java.awt.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.debug.*;
import com.ankamagames.wakfu.client.debug.Components.*;
import com.ankamagames.wakfu.client.core.game.weather.*;
import com.ankamagames.wakfu.client.core.world.havenWorld.*;
import com.ankamagames.wakfu.client.chat.bubble.*;
import com.ankamagames.framework.script.events.*;
import com.ankamagames.wakfu.client.ui.script.*;
import com.ankamagames.wakfu.client.ui.script.function.bubbleText.*;
import com.ankamagames.wakfu.client.network.entity.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.baseImpl.graphics.game.DynamicElement.*;
import com.ankamagames.wakfu.client.core.world.dynamicElement.*;
import com.ankamagames.wakfu.common.game.nation.handlers.*;
import com.ankamagames.wakfu.client.core.nation.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.opengl.Cg.*;
import javax.swing.*;
import java.util.*;

public class WakfuClientInstance extends AbstractGameClientInstance
{
    private static final Logger m_logger;
    private static final WakfuClientInstance m_instance;
    private static final int DEFAULT_FONT_SIZE = 11;
    private static final int DEFAULT_NIO_TIMEOUT = 80;
    private static final long NEWS_FIRST_AWAIT_TIMEOUT = 1000L;
    private static final long NEWS_SECOND_AWAIT_TIMEOUT = 4000L;
    private static final Pattern SPLIT_PATTERN;
    private final GLApplicationUI m_applicationUI;
    private final ArrayList<LODChangeListener> m_lodChangeListeners;
    
    private WakfuClientInstance() {
        super(true);
        this.m_applicationUI = new WakfuApplicationUI();
        this.setNetworkEntityFactory(new NetworkEntityFactory());
        this.setClientMessageDecoder(new WakfuMessageDecoder());
        this.setNetworkEventHandler(new WakfuNetworkEventsHandler());
        this.createProxyClient();
        this.m_lodChangeListeners = new ArrayList<LODChangeListener>();
    }
    
    @Override
    public GLApplicationUI getAppUI() {
        return this.m_applicationUI;
    }
    
    public static WakfuClientInstance getInstance() {
        return WakfuClientInstance.m_instance;
    }
    
    public static Logger getLogger() {
        return WakfuClientInstance.m_logger;
    }
    
    public static WakfuGameEntity getGameEntity() {
        return WakfuGameEntity.getInstance();
    }
    
    @Override
    protected void initializePropertiesProvider() {
        GlobalPropertiesProvider.getInstance().setListener(PropertiesProvider.getInstance());
    }
    
    @Override
    public WakfuGamePreferences getGamePreferences() {
        return (WakfuGamePreferences)super.getGamePreferences();
    }
    
    @Override
    protected void createGamePreferences() {
        this.setGamePreferences(new WakfuGamePreferences());
    }
    
    @Override
    public void initializeUserPreferences() {
        super.initializeUserPreferences();
        GamePreferences.getDefaultPreferenceStore().setAutoSave(true);
    }
    
    @Nullable
    @Override
    protected String getGfxPath() {
        try {
            return WakfuConfiguration.getInstance().getString("gfxPath");
        }
        catch (PropertyException e) {
            WakfuClientInstance.m_logger.error((Object)"", (Throwable)e);
            return null;
        }
    }
    
    protected boolean initSound() throws Exception {
        if (WakfuSoundManager.getInstance().initialize()) {
            WakfuSoundManager.getInstance().start();
            return true;
        }
        WakfuClientInstance.m_logger.error((Object)"Probl\u00e8me d'initialisation du SoundManager.");
        return false;
    }
    
    @Override
    public void initialize() throws Exception {
        super.initialize();
        Injection.getInstance().getInstance(IServiceManager.class).startServices();
        final WakfuGamePreferences gameOptions = this.getGamePreferences();
        final String configFileName = WakfuConfiguration.getContentPath("gfxConfigFile");
        MemoryObjectPoolManager.getInstance().loadConfiguration(configFileName);
        Engine.getInstance().initializePools(configFileName);
        WakfuSoundManager.getInstance();
        IsoParticleSystemFactory.setInstance(new WakfuIsoParticleSystemFactory());
        BaseGameDateProvider.INSTANCE.setProvider(WakfuGameCalendar.getInstance());
        final ApplicationResolution resolution = ApplicationResolution.unserialize(gameOptions.getStringValue(KeyPreferenceStoreEnum.APPLICATION_RESOLUTION_KEY));
        final boolean bVSync = gameOptions.getBooleanValue(WakfuKeyPreferenceStoreEnum.VSYNC_ACTIVATED_KEY);
        final GLCaps caps = new GLCaps();
        caps.m_numDepthBufferBits = 0;
        caps.m_numStencilBits = 8;
        this.initApplicationUI(resolution, bVSync, caps);
        this.m_applicationUI.startGLRendering();
        final float minZoom = WakfuConfiguration.getInstance().getFloat("cameraMinZoom", IsoWorldScene.DEFAULT_MIN_ZOOM_FACTOR);
        final float maxZoom = WakfuConfiguration.getInstance().getFloat("cameraMaxZoom", IsoWorldScene.DEFAULT_MAX_ZOOM_FACTOR);
        this.initScenes(this.getRenderer(), minZoom, maxZoom);
        this.getXulorScene().addEventListener(new SceneEventListener());
        ColorHelperProvider.addValidator(new ParticleColorTacticalFightValidator());
        Profiler.initialize("wci", 4, 11);
        AmbianceManager.INSTANCE.init(WakfuEffectBlockFactory.getInstance(), this.m_worldScene);
        this.displayDebugBar(WakfuClientInstance.m_instance.getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.DISPLAY_DEBUG_BAR));
    }
    
    public void delayedInitialization() throws Exception {
        BarrierManager.INSTANCE.setBarrier(SynchroBarriers.LOCAL_PLAYER_LOADED);
        WakfuTranslator.getInstance();
        final WakfuConfiguration config = WakfuConfiguration.getInstance();
        HTTPFileCacheManager.INSTANCE.setFileName(config.getCachePath(".cache"));
        HTTPFileCacheManager.INSTANCE.load();
        final URL loginNewsUrl = WakfuConfiguration.getInstance().getLoginNewsUrl();
        final String loginNewsCacheDirectory = WakfuConfiguration.getInstance().getCachePath("news");
        NewsManager.INSTANCE.load(loginNewsUrl, loginNewsCacheDirectory);
        new ClientCastableDescriptionGenerator().initialize(WakfuTranslator.getInstance(), TextWidgetFormatterFactoryImpl.INSTANCE);
        BinaryDocumentManager.getInstance().setPath(config.getString("binaryDataFile"));
        final String shaderPath = config.getString("shadersPath");
        initEffectManager(shaderPath);
        final WakfuGamePreferences gameOptions = this.getGamePreferences();
        AnmActionFactoryProvider.INSTANCE.setFactory(WakfuAnmActionFactory.INSTANCE);
        Xulor.getInstance().m_animationPath = WakfuConfiguration.getInstance().getString("ANMGUIPath");
        Xulor.getInstance().m_particlePath = WakfuConfiguration.getInstance().getString("particlePath");
        Xulor.getInstance().m_shaderPath = WakfuConfiguration.getInstance().getString("shadersPath");
        AbstractWorldPositionMarkerManager.APS_IDS = MiniMapConstants.WORLD_POSITION_MARKER_APS;
        XulorWorldPositionMarkerManager.setParticlePath(WakfuConfiguration.getInstance().getString("worldPositionMarkerApsPath"));
        SimpleBinaryStorage.getInstance().setWorkspace(WakfuConfiguration.getInstance().getString("contentStaticDataStorageDirectory"));
        SimpleBinaryStorage.getInstance().init();
        IsoSceneLightManager.INSTANCE.initialize();
        this.getRenderer().requestVSync(gameOptions.getBooleanValue(WakfuKeyPreferenceStoreEnum.VSYNC_ACTIVATED_KEY));
        WeatherEffectManager.INSTANCE.setActivated(gameOptions.getBooleanValue(WakfuKeyPreferenceStoreEnum.METEO_EFFECT_ACTIVATED_KEY));
        AnimatedElement.setEnableRunningRadius(gameOptions.getBooleanValue(WakfuKeyPreferenceStoreEnum.ENABLE_RUNNING_RADIUS));
        final AleaWorldScene scene = WakfuClientInstance.m_instance.getWorldScene();
        WorldInfoManager.getInstance().load(WakfuConfiguration.getContentPath("worldInfoFile"));
        final String gfxMapsCoordFile = WakfuConfiguration.getContentPath("mapsGfxCoord");
        MapManagerHelper.setValidMapsCoordFile(WakfuConfiguration.getContentPath("mapsTplgCoord"), gfxMapsCoordFile);
        LightningMapManager.getInstance().setPath(WakfuConfiguration.getContentPath("mapsLightPath"));
        TopologyMapManager.enableConstantWorld();
        TopologyMapManager.enableAsyncLoading();
        TopologyMapManager.setPath(WakfuConfiguration.getContentPath("mapsTopologyPath"));
        final EnvironmentMapManager environmentMapManager = EnvironmentMapManager.getInstance();
        environmentMapManager.setPath(WakfuConfiguration.getContentPath("mapsEnvironmentPath"));
        final String displayedScreenWorldPath = WakfuConfiguration.getContentPath("mapsGfxPath");
        DisplayedScreenWorld.getInstance().setPath(displayedScreenWorldPath);
        MiniSpriteFactory.setGfxPath(WakfuConfiguration.getInstance().getString("patchMiniImagePath"), WakfuConfiguration.getInstance().getString("buildingMiniImagePath"));
        this.removeAllLODChangeListener();
        this.setLODLevel(gameOptions.getIntValue(KeyPreferenceStoreEnum.LOD_LEVEL_KEY));
        FightVisibilityManager.getInstance().setFightLODLevel(gameOptions.getIntValue(KeyPreferenceStoreEnum.FIGHT_LOD_LEVEL_KEY));
        for (int i = 0, length = this.m_backgroundScenes.length; i < length; ++i) {
            this.m_backgroundScenes[i].getDisplayedScreenWorld().setPath(displayedScreenWorldPath);
        }
        environmentMapManager.setMapFactory(new EnvironmentMapManager.MapFactory() {
            @NotNull
            @Override
            public ClientEnvironmentMap createMap() {
                return new WakfuClientEnvironmentMap();
            }
        });
        final String anmDynamicPath = config.getString("ANMDynamicElementPath");
        DynamicElementTypeProviderFactory.setInstance(new DynamicElementTypeProvider(anmDynamicPath));
        environmentMapManager.setParticleManager(WakfuConfiguration.getInstance().getBoolean("activateMapParticles") ? IsoParticleSystemManager.getInstance() : null);
        environmentMapManager.setSoundManager(WakfuSoundManager.getInstance());
        PaperMapManager.getInstance().setPath(config.getString("mapsAmbienceDataPath"));
        AmbienceZoneBank.getInstance().setFile(config.getString("ambienceBankFile"));
        try {
            WakfuAmbianceListener.getInstance().initialize(WakfuConfiguration.getContentPath("graphicalAmbienceFile"));
        }
        catch (IOException e) {
            WakfuClientInstance.m_logger.error((Object)"Probl\u00e8me de chargement des ambiance graphqiue", (Throwable)e);
        }
        catch (PropertyException e2) {
            WakfuClientInstance.m_logger.error((Object)"Probl\u00e8me de chargement des ambiance graphqiue", (Throwable)e2);
        }
        catch (RuntimeException e3) {
            WakfuClientInstance.m_logger.error((Object)"Probl\u00e8me de chargement des ambiance graphqiue", (Throwable)e3);
        }
        WeatherEffectManager.INSTANCE.intialize();
        WeatherEffectManager.INSTANCE.start();
        ProtectorSerializer.INSTANCE.setProtectorFactory(new ProtectorFactory());
        Nation.setHandlersFactory(new ClientNationHandlersFactory());
        TravelHelper.registerProvider(new ZaapTravelProvider(), new DragoTravelProvider(), new BoatTravelProvider(), new CannonTravelProvider(), new ZaapOutOnlyTravelProvider());
        ProtectorView.getInstance().initialize();
        ProtectorEventDispatcher.INSTANCE.addListener(ProtectorAssaultSoldierNotifier.INSTANCE);
        prepareLightSceneManager();
        this.loadSoundConfig(config, gameOptions, scene);
        this.loadShortcuts();
        final String dayLightFile = WakfuConfiguration.getContentPath("dayLightFile");
        final String defaultDayLightFile = WakfuConfiguration.getContentPath("defaultDayLightFile");
        WakfuClientInstance.m_logger.info((Object)"Loading embedded DayLight file.");
        try {
            try {
                SunLightModifier.INSTANCE.readFromXML(dayLightFile);
            }
            catch (Exception e4) {
                WakfuClientInstance.m_logger.error((Object)"Le dayLight file n'est pas trouvable, on utilise celui par d\u00e9faut");
                SunLightModifier.INSTANCE.readFromXML(defaultDayLightFile);
            }
        }
        catch (Exception e4) {
            WakfuClientInstance.m_logger.error((Object)"Exception : ", (Throwable)e4);
            throw new Exception("Impossible de charger les d\u00e9finitions de couleur de Jour/Nuit embarqu\u00e9s depuis le fichier " + dayLightFile + " !");
        }
        this.prepareLuaManager();
        Item.getItemComposer().init(ItemUIDsManager.getInstance(), false, new ItemDisplayerImpl(), ItemQuantityChangeListener.getInstance());
        AptitudeComposer.init(AptitudeDisplayerImpl.getInstance());
        AvatarBreedInfoManager.getInstance().initialize();
        AbstractWakfuCriteriaInformationProvider.registerInstance(new WakfuCriteriaInformationProvider());
        GUIDGenerator.init(0);
        StateManager.getInstance().setStateLoader(new StateLoaderClient());
        final BinaryLoaderFromFile<AreaEffectBinaryData> areaEffectLoader = new BinaryLoaderFromFile<AreaEffectBinaryData>(new AreaEffectBinaryData());
        StaticEffectAreaManager.setInstance(new StaticEffectAreaManagerImpl(areaEffectLoader));
        ReferenceItemManager.setUniqueInstance((ReferenceItemManager<AbstractReferenceItem>)new ItemManagerImpl());
        DoubleInvocationCharacteristics.setDefaultInstance(new DoubleInvocationCharacteristicsImpl());
        ImageCharacteristics.setDefaultInstance(new ImageCharacteristicsImpl());
        BellaphoneDoubleCharacteristics.setDefaultInstance(new BellaphoneDoubleCharacteristicsImpl());
        IceStatueDoubleCharacteristics.setDefaultInstance(new IceStatueDoubleCharacteristicsImpl());
        WakfuCalendarEventSerializer.setEventProvider(WakfuClientCalendarEventProvider.getInstance());
        MobileManager.getInstance().addPermanentCreationListener(ChallengeObjectListener.INSTANCE);
        MobileManager.getInstance().addPermanentDestructionListener(ChallengeObjectListener.INSTANCE);
        ResourceManager.getInstance().addPermanentCreationListener(ChallengeObjectListener.INSTANCE);
        ResourceManager.getInstance().addPermanentDestructionListener(ChallengeObjectListener.INSTANCE);
        AnimatedElementSceneViewManager.getInstance().addPermanentCreationListener(ChallengeObjectListener.INSTANCE);
        AnimatedElementSceneViewManager.getInstance().addPermanentDestructionListener(ChallengeObjectListener.INSTANCE);
        RunningEffect.setUIDGenerator(new WakfuRunningEffectUIDGenerator());
        AnmManager.getInstance().loadAllCommonAnimations(WakfuConfiguration.getContentPath("ANMIndexFile"));
        UIUnlockEventHelper.loadEvents();
        final boolean failed = !NewsManager.INSTANCE.awaitTermination(1000L);
        if (failed) {
            WakfuClientInstance.m_logger.info((Object)"News pas chargees dans le temps imparti. On continue.");
        }
        TimeManager.INSTANCE.start();
        NewsManager.INSTANCE.awaitTermination(4000L);
        NewsManager.INSTANCE.getState();
        PropertiesProvider.getInstance().setPropertyValue("isBrowserAvailable", WakfuSWT.isInit());
        WakfuSWT.runAsync(new SWFRunnable());
    }
    
    private void prepareLuaManager() throws PropertyException, IOException {
        JavaFunctionLoader.INSTANCE.setDefaultLibraries(WakfuLuaGeneratedLibraries.getLibraries());
        boolean useCompiledLua = true;
        try {
            useCompiledLua &= WakfuConfiguration.getInstance().getBoolean("useCompiledLua");
        }
        catch (PropertyException ex) {
            WakfuClientInstance.m_logger.error((Object)"Exception : ", (Throwable)ex);
        }
        if (useCompiledLua) {
            LuaManager.getInstance().useCompiledScript();
        }
        JavaFunctionLoader.INSTANCE.load(WakfuConfiguration.getContentPath("scriptFunctionLibraryFile"));
        LuaManager.getInstance().setPath(WakfuConfiguration.getInstance().getString("scriptPath"));
        CameraFunctionsLibrary.getInstance().setWorldScene(this.m_worldScene);
        EffectFunctionsLibrary.getInstance().setWorldSence(this.m_worldScene);
        final Properties scriptConf = new Properties();
        try {
            final File file = new File("script.properties");
            if (file.exists()) {
                scriptConf.load(new FileInputStream(file));
            }
        }
        catch (FileNotFoundException e) {
            WakfuClientInstance.m_logger.error((Object)e.getMessage());
        }
        for (final Object o : ((Hashtable<Object, V>)scriptConf).keySet()) {
            final String key = (String)o;
            int eventId;
            try {
                eventId = Integer.parseInt(key);
            }
            catch (NumberFormatException e2) {
                WakfuClientInstance.m_logger.error((Object)"Exception : ", (Throwable)e2);
                continue;
            }
            final String[] scriptFunctionName = WakfuClientInstance.SPLIT_PATTERN.split(scriptConf.getProperty(key));
            if (scriptFunctionName.length < 1) {
                continue;
            }
            ScriptManager.getInstance().addEvent(eventId, scriptFunctionName[0].trim(), scriptFunctionName[1].trim());
        }
    }
    
    private void loadShortcuts() throws Exception {
        final ShortcutManager shortcutManager = ShortcutManager.getInstance();
        final String shortcutsFile = WakfuConfiguration.getContentPath("shortcutsFile");
        final String defaultShortcutsFile = WakfuConfiguration.getContentPath("defaultShortcutsFile");
        try {
            try {
                shortcutManager.loadFromXMLFile(defaultShortcutsFile, false);
                if (FileHelper.isExistingFile(new URL(WakfuConfiguration.getContentPath("shortcutsFile")).getFile())) {
                    shortcutManager.loadFromXMLFile(shortcutsFile, true);
                }
            }
            catch (MalformedURLException e) {
                WakfuClientInstance.m_logger.debug((Object)"Exception \u00e0 la lecture des shortcuts : ", (Throwable)e);
            }
            catch (PropertyException e2) {
                WakfuClientInstance.m_logger.debug((Object)"Exception \u00e0 la lecture des shortcuts : ", (Throwable)e2);
            }
            catch (Exception e3) {
                WakfuClientInstance.m_logger.debug((Object)"Exception \u00e0 la lecture des shortcuts : ", (Throwable)e3);
            }
            shortcutManager.enableGroup("debug", false);
            shortcutManager.enableGroup("admin", false);
            shortcutManager.enableGroup("default", true);
            this.m_applicationUI.getRenderer().pushKeyboardController(shortcutManager, false);
        }
        catch (RuntimeException e4) {
            WakfuClientInstance.m_logger.error((Object)"Exception : ", (Throwable)e4);
            throw new Exception("Impossible de charger les raccourcis clavier depuis le fichier " + shortcutsFile + " !");
        }
    }
    
    private static void prepareLightSceneManager() {
        IsoSceneLightManager.INSTANCE.addScene(DisplayedScreenWorld.getInstance());
        IsoSceneLightManager.INSTANCE.addScene(MobileManager.getInstance());
        IsoSceneLightManager.INSTANCE.addScene(ResourceManager.getInstance());
        IsoSceneLightManager.INSTANCE.addScene(AnimatedElementSceneViewManager.getInstance());
        IsoSceneLightManager.INSTANCE.addScene(SimpleAnimatedElementManager.getInstance());
        IsoSceneLightManager.INSTANCE.addScene(IsoParticleSystemManager.getInstance());
        IsoSceneLightManager.INSTANCE.addLightingModifier(SunLightModifier.INSTANCE);
        IsoSceneLightManager.INSTANCE.addLightingModifier(ResourceShadowCast.INSTANCE);
        IsoSceneLightManager.INSTANCE.addGlobalLightingListener(ResourceShadowCast.INSTANCE);
        SunLightModifier.INSTANCE.setEnable(true);
    }
    
    private void loadSoundConfig(final PropertiesReaderWriter config, final GamePreferences gameOptions, final IsoWorldScene scene) throws Exception {
        final boolean soundEnable = config.getBoolean("soundEnable");
        try {
            WakfuSoundManager.getInstance().setSoundSourceFlavor(WakfuConfiguration.getInstance().getString("soundSourceFlavor"));
        }
        catch (PropertyException e) {
            WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.SOUND_SOURCE_FLAVOR", (Throwable)e);
        }
        catch (RuntimeException ex) {
            WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.SOUND_SOURCE_FLAVOR", (Throwable)ex);
        }
        if (soundEnable && this.initSound()) {
            WakfuSoundManager.getInstance().setListener(scene.getIsoCamera());
            WakfuSoundManager.getInstance().setAmbianceSoundsVolume(gameOptions.getFloatValue(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_VOLUME_PREFERENCE_KEY));
            WakfuSoundManager.getInstance().setAmbianceSoundsMute(gameOptions.getBooleanValue(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_MUTE_PREFERENCE_KEY));
            WakfuSoundManager.getInstance().setUiSoundsVolume(gameOptions.getFloatValue(KeyPreferenceStoreEnum.UI_SOUNDS_VOLUME_PREFERENCE_KEY));
            WakfuSoundManager.getInstance().setUiSoundsMute(gameOptions.getBooleanValue(KeyPreferenceStoreEnum.UI_SOUNDS_MUTE_PREFERENCE_KEY));
            WakfuSoundManager.getInstance().setMusicVolume(gameOptions.getFloatValue(KeyPreferenceStoreEnum.MUSIC_VOLUME_PREFERENCE_KEY));
            WakfuSoundManager.getInstance().setMusicMute(gameOptions.getBooleanValue(KeyPreferenceStoreEnum.MUSIC_MUTE_PREFERENCE_KEY));
            WakfuSoundManager.getInstance().setMusicContinuousMode(gameOptions.getBooleanValue(KeyPreferenceStoreEnum.MUSIC_CONTINUOUS_MODE_PREFERENCE_KEY));
            try {
                final String sndPath = WakfuConfiguration.getContentPath("lowPassPresetFile");
                WakfuSoundManager.getInstance().initLowPassPreset(sndPath);
            }
            catch (PropertyException e2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.LOWPASS_PRESET_FILE", (Throwable)e2);
            }
            catch (Exception ex2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.LOWPASS_PRESET_FILE", (Throwable)ex2);
            }
            try {
                final String sndPath = WakfuConfiguration.getContentPath("reverbPresetFile");
                WakfuSoundManager.getInstance().initReverbPreset(sndPath);
            }
            catch (PropertyException e2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.REVERB_PRESET", (Throwable)e2);
            }
            catch (Exception ex2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.REVERB_PRESET", (Throwable)ex2);
            }
            try {
                final String sndPath = WakfuConfiguration.getContentPath("rollOffPresetFile");
                WakfuSoundManager.getInstance().initRollOffPreset(sndPath);
            }
            catch (PropertyException e2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.ROLLOFF_PRESET_FILE", (Throwable)e2);
            }
            catch (Exception ex2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.ROLLOFF_PRESET_FILE", (Throwable)ex2);
            }
            try {
                final String sndPath = WakfuConfiguration.getContentPath("barksFile");
                WakfuSoundManager.getInstance().initBarks(sndPath);
            }
            catch (PropertyException e2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.BARKS_FILE", (Throwable)e2);
            }
            catch (Exception ex2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.BARKS_FILE", (Throwable)ex2);
            }
            try {
                final String sndPath = WakfuConfiguration.getContentPath("groundsFile");
                WakfuSoundManager.getInstance().initGrounds(sndPath);
            }
            catch (PropertyException e2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.GROUNDS_FILE", (Throwable)e2);
            }
            catch (Exception ex2) {
                WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser WakfuSoundManager.GROUNDS_FILE", (Throwable)ex2);
            }
            if (!this.useLuaScriptForAudio()) {
                try {
                    final String sndPath = WakfuConfiguration.getContentPath("particlesAudioFile");
                    final BinaryParticleSoundManager bpsm = (BinaryParticleSoundManager)ParticleSoundManager.getInstance();
                    bpsm.loadBinary(sndPath);
                }
                catch (IOException e3) {
                    WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser ParticleSoundManager", (Throwable)e3);
                }
                catch (PropertyException e2) {
                    WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser ParticleSoundManager", (Throwable)e2);
                }
                catch (RuntimeException ex3) {
                    WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser ParticleSoundManager", (Throwable)ex3);
                }
                try {
                    final String sndPath = WakfuConfiguration.getContentPath("animatedElementsAudioFile");
                    final BinaryAnmActionRunScriptManager manager = (BinaryAnmActionRunScriptManager)AnmActionRunScriptManager.getInstance();
                    manager.loadBinary(sndPath);
                }
                catch (IOException e3) {
                    WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser AnmActionRunScriptManager", (Throwable)e3);
                }
                catch (PropertyException e2) {
                    WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser AnmActionRunScriptManager", (Throwable)e2);
                }
                catch (RuntimeException ex3) {
                    WakfuClientInstance.m_logger.error((Object)"impossible d'initialiser AnmActionRunScriptManager", (Throwable)ex3);
                }
            }
        }
    }
    
    protected void onHardwareTestsComplete() {
        final WakfuGamePreferences gameOptions = this.getGamePreferences();
        EffectManager.getInstance().enablePostProcess(HardwareFeatureManager.INSTANCE.isFeatureSupported(HardwareFeature.GL_FRAGMENT_SHADERS) && gameOptions.getBooleanValue(WakfuKeyPreferenceStoreEnum.SHADERS_ACTIVATED_KEY));
        final ViewPort viewPort = this.m_worldScene.getViewPort();
        EffectManager.getInstance().createRenderTargets(viewPort.getWidth(), viewPort.getHeight());
    }
    
    @Override
    protected void runHardwareTests() {
        HardwareFeatureTester.INSTANCE.addTest(new GLRenderTargetTest());
        HardwareFeatureTester.INSTANCE.addTest(new GLTextureCompressionTest());
        HardwareFeatureTester.INSTANCE.addTest(new GLTextureNonPowerOfTwoTest());
        HardwareFeatureTester.INSTANCE.addTest(new GLMultiSamplingTest());
        HardwareFeatureTester.INSTANCE.addTest(new GLMultiTexturingTest());
        HardwareFeatureTester.INSTANCE.addTest(new GLFragmentShadersTest());
        HardwareFeatureTester.INSTANCE.addTest(new GLVertexShadersTest());
        HardwareFeatureTester.INSTANCE.addTest(new ALEffectTest());
        HardwareFeatureTester.INSTANCE.addTest(new ALFilterTest());
        HardwareFeatureTester.INSTANCE.initialize();
        HardwareFeatureTester.INSTANCE.runTest();
        HardwareFeatureTester.INSTANCE.cleanUp();
        this.onHardwareTestsComplete();
    }
    
    @Override
    protected void runBenchmarks() {
        BenchmarkRunner.INSTANCE.addBenchmark(new GLBandwidthAndTextureFormatBenchmark());
        BenchmarkRunner.INSTANCE.addBenchmark(new GLPixelFillRateBenchmark());
        BenchmarkRunner.INSTANCE.addBenchmark(new GLTextureFillRateBenchmark());
        final BenchmarkResult benchmarkResult = new BenchmarkResult();
        BenchmarkRunner.INSTANCE.initialize();
        BenchmarkRunner.INSTANCE.run(benchmarkResult);
        BenchmarkRunner.INSTANCE.cleanUp();
        benchmarkResult.log();
        final WakfuGraphicalPreferences graphicalPreferences = new WakfuGraphicalPreferences();
        graphicalPreferences.build(benchmarkResult);
        graphicalPreferences.log();
    }
    
    @Override
    protected void registerContentInitializers() {
        registerBinaryLoaders();
        this.registerContentInitializer(new AvatarBreedLoader());
        this.registerContentInitializer(ConsoleLoader.getInstance());
        this.registerContentInitializer(ChatInitializer.getInstance());
        this.registerContentInitializer(new EmoteLoader());
        this.registerContentInitializer(new DialogLoader());
        this.registerContentInitializer(new SpellLoader());
        this.registerContentInitializer(EffectGroupLoader.getInstance());
        this.registerContentInitializer(new SkillLoader());
        this.registerContentInitializer(AptitudeLoader.getInstance());
        this.registerContentInitializer(AptitudeBonusLoader.INSTANCE);
        this.registerContentInitializer(AptitudeCategoryModelLoader.INSTANCE);
        this.registerContentInitializer(NationRankLoader.getInstance());
        this.registerContentInitializer(new CraftLoader());
        this.registerContentInitializer(new NewCraftLoader());
        this.registerContentInitializer(ItemLoader.getInstance());
        this.registerContentInitializer(new ResourceLoader());
        this.registerContentInitializer(TravelsLoader.getInstance());
        this.registerContentInitializer(new TreasureLoader());
        this.registerContentInitializer(new GemAndPowderLoader());
        this.registerContentInitializer(new MagicraftLoader());
        this.registerContentInitializer(new InteractiveElementLoader());
        this.registerContentInitializer(IEParametersLoader.getInstance());
        this.registerContentInitializer(InteractiveElementTemplatesLoader.getInstance());
        this.registerContentInitializer(new CharacGainPerLevelLoader());
        this.registerContentInitializer(BonusDistributionTableLoader.getInstance());
        this.registerContentInitializer(new PlayListLoader());
        this.registerContentInitializer(new LockLoader());
        this.registerContentInitializer(new DungeonLoader());
        this.registerContentInitializer(new MonsterFamilyLoader());
        this.registerContentInitializer(new MonsterFamilyRelationShipLoader());
        this.registerContentInitializer(new MonsterFamilyPestLoader());
        this.registerContentInitializer(new TimelineBuffListLoader());
        this.registerContentInitializer(CitizenRankLoader.getInstance());
        this.registerContentInitializer(NationLawsLoader.getInstance());
        this.registerContentInitializer(new ProtectorBuffLoader());
        this.registerContentInitializer(new ClimateBonusLoader());
        this.registerContentInitializer(new ProtectorEcosystemProtectionLoader());
        this.registerContentInitializer(PetLoader.INSTANCE);
        this.registerContentInitializer(ClientGameEventLoader.INSTANCE);
        this.registerContentInitializer(TutorialLoader.INSTANCE);
        this.registerContentInitializer(AchievementsLoader.INSTANCE);
        this.registerContentInitializer(AlmanachEntryLoader.INSTANCE);
        this.registerContentInitializer(AlmanachDateLoader.INSTANCE);
        this.registerContentInitializer(BackgroundDisplayLoader.INSTANCE);
        this.registerContentInitializer(CensorLoader.INSTANCE);
        this.registerContentInitializer(new NationColorLoader());
        this.registerContentInitializer(new GroundTypeLoader());
        this.registerContentInitializer(new GuildBonusLoader());
        this.registerContentInitializer(new GuildLevelLoader());
        this.registerContentInitializer(new HavenWorldAuctionDefinitionLoader());
        this.registerContentInitializer(new HavenBagModelViewLoader());
        this.registerContentInitializer(new ActionVisualLoader());
        this.registerContentInitializer(ArcadeDungeonLoader.INSTANCE);
        this.registerContentInitializer(HavenWorldLoader.INSTANCE);
        this.registerContentInitializer(new FightChallengeLoader());
        this.registerContentInitializer(new FightChallengeMonsterLoader());
        this.registerContentInitializer(new SecretLoader());
        this.registerContentInitializer(new KrosmozFigureLoader());
        this.registerContentInitializer(new CompanionItemLoader());
        this.registerContentInitializer(new InstanceInteractionLevelLoader());
    }
    
    private static void registerBinaryLoaders() {
        MetaItemManager.INSTANCE.setBinaryLoader(new BinaryLoaderFromFile((T)new ItemBinaryData()));
        MetaItemManager.INSTANCE.setBinaryTransformer(new ReferenceItemUniqueInstanceLoader());
    }
    
    public void start() {
        final PropertiesProvider propertiesProvider = PropertiesProvider.getInstance();
        final String accountName = this.getGamePreferences().getStringValue(WakfuKeyPreferenceStoreEnum.LAST_LOGIN_PREFERENCE_KEY);
        propertiesProvider.setPropertyValue("account.name", (accountName == null) ? "" : accountName);
        propertiesProvider.setPropertyValue("account.remember", this.getGamePreferences().getBooleanValue(WakfuKeyPreferenceStoreEnum.REMEMBER_LAST_LOGIN_PREFERENCE_KEY));
        ShortcutsFieldProvider.getInstance().initialize();
        PropertiesProvider.getInstance().setPropertyValue("shortcutsList", ShortcutsFieldProvider.getInstance());
        PropertiesProvider.getInstance().setPropertyValue("isAdmin", false);
        PropertiesProvider.getInstance().setPropertyValue("isInGame", false);
        WakfuGameEntity.getInstance().pushFrame(UIMessageCatcherFrame.INSTANCE);
        WakfuGameEntity.getInstance().pushFrame(UIAuthentificationFrame.getInstance());
        AutoLogin.startAutoLogin();
    }
    
    @Override
    public void partialCleanUp() {
        WakfuGameEntity.getInstance().partialCleanUp();
        super.partialCleanUp();
        this.doCleanUp();
        WakfuClientConfigurationManager.getInstance().setInstanceId((short)(-1));
        this.getGamePreferences().setCharacterPreferenceStore(null);
        WakfuClientConfigurationManager.getInstance().setCharacterName(null);
    }
    
    @Override
    public void cleanUp() {
        try {
            WakfuGameEntity.getInstance().cleanUp();
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du cleanUp de la GameEntity", (Throwable)e);
        }
        super.cleanUp();
        this.doCleanUp();
        try {
            WakfuClientConfigurationManager.getInstance().setWorldName(null);
            this.getGamePreferences().setAccountPreferenceStore(null);
            WakfuClientConfigurationManager.getInstance().setAccountName(null);
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors de la sauvegarde des pr\u00e9f\u00e9rences du compte", (Throwable)e);
        }
    }
    
    private void doCleanUp() {
        EmoteIconHelper.cleanAllSmileys();
        NationDisplayer.getInstance().clean();
        WakfuGuildView.getInstance().clean();
        ChatConfigurator.setChatLoaded(false);
        ProtectorNationBuffEventHandler.INSTANCE.clean();
        AntiAddictionClientHelper.RemoveRunnable();
        try {
            ShortcutManager.getInstance().enableAllShortcuts();
            ShortcutManager.getInstance().enableGroup("world", false);
            ShortcutManager.getInstance().enableGroup("fight", false);
            ShortcutManager.getInstance().enableGroup("common", false);
            ShortcutManager.getInstance().enableGroup("binding", false);
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du nettoyage des racourcis", (Throwable)e);
        }
        try {
            MRUActions.setAllUsable(true);
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du nettoyage du MRU", (Throwable)e);
        }
        WakfuProgressMonitorManager.getInstance().done();
        try {
            Xulor.getInstance().unloadAll();
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du nettoyage de Xulor", (Throwable)e);
        }
        try {
            Xulor.getInstance().removeAllActionClass();
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du retrait des ActionClass", (Throwable)e);
        }
        try {
            CursorFactory.getInstance().unlock();
            GraphicalMouseManager.getInstance().hide();
            Xulor.getInstance().hidePopupMenu();
            loadDefaultXulorActionClasses();
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du nettoyage des \u00e9l\u00e9ments flottants Xulor", (Throwable)e);
        }
        try {
            ChatWindowManager.getInstance().cleanWindowContent();
            ChatWindowManager.getInstance().cleanPrivateChats();
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du nettoyage du chat.", (Throwable)e);
        }
        this.cleanOnExitWorld();
        try {
            ChatManager.getInstance().clean();
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du nettoyage des listes de contacts priv\u00e9s", (Throwable)e);
        }
        QueueCollectManager.getInstance().clear();
        MapManagerHelper.clear();
        final WakfuWorldScene scene = (WakfuWorldScene)this.getWorldScene();
        scene.setDispatchMouseMovedMessage(false);
        scene.setDispatchMouseMovedExtendedMessage(false);
        this.removeAllLODChangeListener();
    }
    
    @Override
    protected void initXulor() throws Exception {
        this.setUiManager(Xulor.getInstance());
        ScriptUIEventManager.getInstance().setUiManager(Xulor.getInstance());
        super.initXulor();
        MessageBoxData.setDefaultIconUrl(WakfuMessageBoxConstants.getMessageBoxIconUrl(7));
        Xulor.getInstance().addBigDialog("craftDialog");
        Xulor.getInstance().addBigDialog("craftTableDialog");
        Xulor.getInstance().addBigDialog("characterSheetDialog");
        Xulor.getInstance().addBigDialog("spellsDialog");
        Xulor.getInstance().addBigDialog("equipmentDialog");
        XulorSoundManager.getInstance().setDelegate(WakfuSoundManager.getInstance().getXulorSoundManagementDelegate());
        Xulor.getInstance().setTranslator(WakfuTranslator.getInstance());
        Xulor.getInstance().setShortcutManager(ShortcutManager.getInstance());
        Xulor.getInstance().setDialogClosesManager(DialogClosesManager.getInstance());
        loadDefaultXulorActionClasses();
        boolean useXml = false;
        try {
            useXml = WakfuConfiguration.getInstance().getBoolean("useXmlTheme");
        }
        catch (PropertyException ex) {
            WakfuClientInstance.m_logger.info((Object)"useXmlTheme n'est pas d\u00e9fini, on utilise le theme compil\u00e9 par d\u00e9faut.");
        }
        if (useXml) {
            Xulor.getInstance().loadTheme(WakfuConfiguration.getContentPath("themeFile"), WakfuConfiguration.getInstance().getString("themeDirectory"));
        }
        else {
            Xulor.getInstance().loadTheme(new WakfuStyleProvider(), new WakfuThemeLoader(), WakfuConfiguration.getInstance().getString("themeDirectory"));
        }
        final TIntObjectHashMap<String> messageBoxPathes = new TIntObjectHashMap<String>();
        messageBoxPathes.put(0, Dialogs.getDialogPath("messageBoxDialog"));
        messageBoxPathes.put(1, Dialogs.getDialogPath("deathMessageBoxDialog"));
        Xulor.getInstance().setMessageBoxPathes(messageBoxPathes);
        Xulor.getInstance().setPopupMenuPath(Dialogs.getDialogPath("popupDialog"));
        Xulor.getInstance().setMRUPath(Dialogs.getDialogPath("mruDialog"));
        PropertiesProvider.getInstance().setPropertyValue("buildVersion", Version.READABLE_VERSION);
        PropertiesProvider.getInstance().setPropertyValue("tutorialProperty.display", true);
        PropertiesProvider.getInstance().setPropertyValue("wakfu.config", WakfuConfiguration.getInstance());
        Xulor.getInstance().setUserDefinedAdapter(new WakfuUserDefinedAdapter());
        Xulor.getInstance().setModalBackgroundColor(new Color(0.02f, 0.08f, 0.1f, 0.75f));
    }
    
    @Override
    public void registerCustomWidgets() {
        super.registerCustomWidgets();
        WakfuCustomWidgetRegister.registerCustom();
    }
    
    @Override
    protected XulorScene createXulorScene() throws Exception {
        return new XulorScene();
    }
    
    @Override
    protected void initXulorScene(final UIScene xulorScene) throws Exception {
    }
    
    @Override
    protected GameWorldScene createWorldScene(final float minZoom, final float maxZoom) throws Exception {
        return new WakfuWorldScene(this, minZoom, maxZoom);
    }
    
    @Override
    protected void initWorldScene(final AleaWorldScene worldScene) throws Exception {
        super.initWorldScene(worldScene);
        final WakfuConfiguration configuration = WakfuConfiguration.getInstance();
        try {
            ElementPropertiesLibrary.load(WakfuConfiguration.getContentPath("elementsFile"));
            WorldGroupManager.getInstance().setPath(configuration.getString("groupsFile"));
            worldScene.setGfxPath(configuration.getString("gfxPath"));
            worldScene.initSoundBank(configuration.getString("soundBankFile"));
            HavenWorldSound.INSTANCE.initialize();
            final String s = configuration.getString("particlePath");
            IsoParticleSystemFactory.getInstance().setPath(s);
        }
        catch (PropertyException e) {
            WakfuClientInstance.m_logger.error((Object)"Erreur \u00e0 l'initialisation de la worldScene", (Throwable)e);
        }
    }
    
    @Override
    public void onWorldSceneInitialized() {
        try {
            final String textureFilePath = WakfuConfiguration.getContentPath("highLightGfxDefaultFile");
            HighLightManager.getInstance().setTexture(textureFilePath);
        }
        catch (PropertyException e) {
            WakfuClientInstance.m_logger.error((Object)"Erreur \u00e0 l'initialisation du HighLightManager", (Throwable)e);
        }
        catch (RuntimeException e2) {
            WakfuClientInstance.m_logger.error((Object)"Erreur \u00e0 l'initialisation du HighLightManager", (Throwable)e2);
        }
    }
    
    private static void loadDefaultXulorActionClasses() {
        Xulor.getInstance().putActionClass("wakfu", Actions.class);
        Xulor.getInstance().putActionClass("console", ConsoleDialogActions.class);
    }
    
    @Override
    protected void onContentInitializeStart(final int initializedContentInitializer) {
        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).beginTask(WakfuTranslator.getInstance().getString("loading"), initializedContentInitializer);
    }
    
    @Override
    protected void onContentInitializeFinished(final int initializedContentInitializer) {
        WakfuProgressMonitorManager.getInstance().done();
    }
    
    @Override
    protected void onContentInitializerStart(final ContentInitializer contentInitializer) {
        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).subTask(contentInitializer.getName());
    }
    
    @Override
    protected void onContentInitializerrError(final ContentInitializer contentInitializer, final Exception exception) {
        Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.loading") + contentInitializer.getName(), WakfuMessageBoxConstants.getMessageBoxIconUrl(1), 1026L, 4, 2);
        WakfuClientInstance.m_logger.error((Object)("Erreur au chargement sur le loader " + contentInitializer.getClass().getSimpleName()), (Throwable)exception);
    }
    
    @Override
    protected void onContentInitializerDone(final ContentInitializer contentInitializer, final int initializedContentInitializer) {
        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).worked(initializedContentInitializer);
        WakfuProgressMonitorManager.getInstance().getProgressMonitor(true).subTask(" ");
    }
    
    @Override
    public void onUIInitializationError(final Object obj, final String message) {
        final GL gl = RendererType.OpenGL.getRenderer().getDevice();
        final String text = WakfuTranslator.getInstance().getString((OS.getCurrentOS() == OS.MAC) ? "error.unsupportedMaterialMac" : "error.unsupportedMaterial", gl.glGetString(7936), gl.glGetString(7937), gl.glGetString(7938));
        WakfuClientInstance.m_logger.error((Object)("Erreur au lancement de l'application : mat\u00e9riel non support\u00e9 : " + gl.glGetString(7936) + " - " + gl.glGetString(7937) + " : " + gl.glGetString(7938)));
        JOptionPane.showMessageDialog(this.m_applicationUI.getAppFrame(), text, "Error", 0);
    }
    
    @Override
    public void onUIClosed() {
        super.onUIClosed();
        try {
            ChatConfigurator.save();
        }
        catch (Exception e) {
            WakfuClientInstance.m_logger.error((Object)"Echec de la tentative de sauvegarde des param\u00e8tres de chat: ", (Throwable)e);
        }
        WakfuSoundManager.getInstance().stopAndClean();
        GlobalUserDefinedManager.getInstance().saveAll();
        WakfuGameEntity.getInstance().disconnectFromServer("UI Closed");
        Injection.getInstance().getInstance(IServiceManager.class).stopServices();
    }
    
    public void setLODLevel(final int value) {
        DisplayedScreenWorld.getInstance().setLODLevel(value);
        for (int i = 0, length = this.m_backgroundScenes.length; i < length; ++i) {
            final ParallaxWorldScene scene = this.m_backgroundScenes[i];
            scene.getDisplayedScreenWorld().setLODLevel(value);
            if (scene.isValidWorld()) {
                this.m_worldScene.removeParallax(scene);
                this.m_worldScene.addParallax(scene);
            }
        }
        for (int i = 0, size = this.m_lodChangeListeners.size(); i < size; ++i) {
            this.m_lodChangeListeners.get(i).onLODChange(value);
        }
    }
    
    public void addLODChangeListener(final LODChangeListener listener) {
        if (!this.m_lodChangeListeners.contains(listener)) {
            this.m_lodChangeListeners.add(listener);
        }
    }
    
    public void removeAllLODChangeListener() {
        this.m_lodChangeListeners.clear();
        WakfuClientInstance.m_instance.addLODChangeListener(new ParticleSystemLODListener());
    }
    
    public static void initEffectManager(final String shadersPath) {
        EffectManager.getInstance().setFactory(new GLEffectFactory());
        Engine.getInstance().initializeEffects(shadersPath);
    }
    
    @Override
    protected void addDebugBarComponents(final DebugBar bar) {
        super.addDebugBarComponents(bar);
        bar.addDebugComponent(ParticlesSwitch.class);
        bar.addDebugComponent(WeatherSwitch.class);
        bar.addDebugComponent(DayPercentSwitch.class);
        bar.addDebugComponent(PositionViewer.class);
    }
    
    @Override
    protected boolean useLuaScriptForAudio() {
        try {
            return WakfuConfiguration.getInstance().getBoolean("useLuaAudio");
        }
        catch (PropertyException e) {
            WakfuClientInstance.m_logger.info((Object)"useLuaAudio n'est pas d\u00e9fini, on utilise les scripts compil\u00e9s par d\u00e9faut");
            return false;
        }
    }
    
    public void cleanOnExitWorld() {
        WeatherEffectManager.INSTANCE.stopAllEffects();
        WeatherInfoManager.getInstance().updateCurrentWeather(null);
        HavenWorldManager.INSTANCE.onLeaveWorld();
        ChatBubbleManager.cleanAllBubbles();
        try {
            this.getWorldScene().clean(false);
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du nettoyage de la world scene", (Throwable)e);
        }
        for (int i = 0, length = this.m_backgroundScenes.length; i < length; ++i) {
            this.m_backgroundScenes[i].reset();
        }
        try {
            LuaManager.getInstance().closeAllScripts();
            ScriptEventManager.getInstance().clean();
            ScriptManager.getInstance().clear();
            ScriptUIEventManager.getInstance().clean();
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du nettoyage du moteur de scripts", (Throwable)e);
        }
        try {
            UIFunctionsLibrary.getInstance().clear();
            BubbleText.getInstance().clear();
        }
        catch (RuntimeException e) {
            WakfuClientInstance.m_logger.error((Object)"Exception lors du nettoyage des bulles de texte", (Throwable)e);
        }
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuClientInstance.class);
        m_instance = new WakfuClientInstance();
        SPLIT_PATTERN = Pattern.compile(":");
    }
    
    private static class NetworkEntityFactory implements SimpleObjectFactory<NetworkEntity>
    {
        @Override
        public NetworkEntity createNew() {
            return new WakfuNetworkEntity(WakfuGameEntity.getInstance());
        }
    }
    
    private static class DynamicElementTypeProvider extends DynamicElementTypeProviderFactory
    {
        private final String m_anmDynamicPath;
        
        DynamicElementTypeProvider(final String anmDynamicPath) {
            super();
            this.m_anmDynamicPath = anmDynamicPath;
        }
        
        @Override
        public DynamicElementType getFromId(final int type) {
            return WakfuDynamicElementType.getFromId(type);
        }
        
        @Override
        public String getAnmPath() {
            return this.m_anmDynamicPath;
        }
    }
    
    private static class ClientNationHandlersFactory implements NationHandlersFactory
    {
        @Override
        public NationMembersHandler createMembersHandler(final Nation nation) {
            return new CNationMembersHandler(nation);
        }
        
        @Override
        public NationPoliticHandler createPoliticHandler(final Nation nation) {
            return new CNationPoliticHandler(nation);
        }
        
        @Override
        public NationBuffsHandler createBuffsHandler(final Nation nation) {
            return new CNationBuffsHandler(nation);
        }
        
        @Override
        public NationJusticeHandler createJusticeHandler(final Nation nation) {
            return new CNationJusticeHandler(nation);
        }
    }
    
    private static class SWFRunnable implements Runnable
    {
        @Override
        public void run() {
            SWFWrapper.INSTANCE.init();
        }
    }
    
    private static class ParticleSystemLODListener implements LODChangeListener
    {
        @Override
        public void onLODChange(final int newLevel) {
            IsoParticleSystemManager.getInstance().setCurrentLod((byte)newLevel);
        }
    }
    
    private static class GLEffectFactory implements EffectManager.Factory
    {
        @Override
        public Effect createEffect(final String className) {
            return new GLEffect();
        }
    }
    
    private class SceneEventListener implements UISceneEventListener
    {
        @Override
        public void onSceneInitializationComplete(final UIScene scene) {
            WakfuClientInstance.this.getXulorScene().removeEventListener(this);
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        WakfuClientInstance.this.delayedInitialization();
                    }
                    catch (Exception e) {
                        WakfuClientInstance.m_logger.fatal((Object)"Erreur au lancement", (Throwable)e);
                        JOptionPane.showMessageDialog(null, e, "Error", 0);
                        System.exit(0);
                    }
                }
            });
        }
        
        @Override
        public void onProcess(final UIScene scene, final int deltaTime) {
        }
        
        @Override
        public void onResize(final UIScene scene, final int deltaWidth, final int deltaHeight) {
        }
    }
}
