package com.ankamagames.baseImpl.graphics;

import com.ankamagames.baseImpl.client.proxyclient.base.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.core.contentLoader.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.graphics.opengl.*;
import com.ankamagames.framework.preferences.*;
import java.io.*;
import com.ankamagames.framework.reflect.*;
import com.ankamagames.framework.graphics.engine.test.*;
import com.ankamagames.framework.sound.*;
import com.ankamagames.baseImpl.graphics.sound.*;
import com.ankamagames.baseImpl.graphics.game.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.baseImpl.graphics.opengl.*;
import javax.swing.*;
import java.awt.*;
import com.ankamagames.baseImpl.graphics.ui.*;
import com.ankamagames.framework.graphics.engine.fadeManager.*;
import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.framework.kernel.core.controllers.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.baseImpl.graphics.debug.*;
import com.ankamagames.baseImpl.graphics.debug.Components.*;

public abstract class AbstractGameClientInstance extends BasicProxyClientInstance implements GLApplicationUIEventListener
{
    private static final Logger m_logger;
    protected static final String DEFAULT_USER_PREFERENCES_FILE = "userPreferences.properties";
    public static final String GAME_PREFERENCES_PROPERTY_NAME = "gamePreferences";
    public static final String HARDWARE_FEATURE_MANAGER_PROPERTY_NAME = "hardwareManager";
    protected UIScene m_xulorScene;
    protected UIManager m_uiManager;
    private static final byte BACKGROUND_SCENE_COUNT = 3;
    protected final ParallaxWorldScene[] m_backgroundScenes;
    protected AleaWorldSceneWithParallax m_worldScene;
    private final List<ContentInitializer> m_contentInitializers;
    private int m_currentContentInitializerIndex;
    protected GamePreferences m_gamePreferences;
    private DebugBar m_debugBar;
    
    protected AbstractGameClientInstance(final boolean startInOpenGLThread) {
        super();
        this.m_backgroundScenes = new ParallaxWorldScene[3];
        this.m_contentInitializers = new ArrayList<ContentInitializer>();
        this.initializePropertiesProvider();
        this.createGamePreferences();
        this.initializeUserPreferences();
        if (startInOpenGLThread) {
            Worker.getInstance().startInOpenGLThread();
        }
        else {
            Worker.getInstance().start();
        }
        MessageScheduler.getInstance().start();
    }
    
    public abstract GLApplicationUI getAppUI();
    
    protected void setUiManager(final UIManager uiManager) {
        this.m_uiManager = uiManager;
    }
    
    public UIScene getXulorScene() {
        return this.m_xulorScene;
    }
    
    public AleaWorldSceneWithParallax getWorldScene() {
        return this.m_worldScene;
    }
    
    public ParallaxWorldScene[] getBackgroundWorldScene() {
        return this.m_backgroundScenes;
    }
    
    public final Renderer getRenderer() {
        return this.getAppUI().getRenderer();
    }
    
    protected abstract void initializePropertiesProvider();
    
    protected void createPreferenceStore() {
        this.getGamePreferences().setDefaultPreferenceStore(new PreferenceStore("userPreferences.properties"));
    }
    
    public GamePreferences getGamePreferences() {
        if (this.m_gamePreferences == null) {
            this.createGamePreferences();
        }
        return this.m_gamePreferences;
    }
    
    protected void setGamePreferences(final GamePreferences gamePreferences) {
        this.m_gamePreferences = gamePreferences;
    }
    
    protected void createGamePreferences() {
        this.m_gamePreferences = new GamePreferences();
    }
    
    public void initializeUserPreferences() {
        this.createPreferenceStore();
        try {
            GamePreferences.getDefaultPreferenceStore().load();
        }
        catch (IOException ex) {}
        final GamePreferences gamePreferences = this.getGamePreferences();
        if (gamePreferences != null) {
            GlobalPropertiesProvider.getInstance().setPropertyValue("gamePreferences", gamePreferences);
            GlobalPropertiesProvider.getInstance().setPropertyValue("hardwareManager", HardwareFeatureManager.INSTANCE);
        }
    }
    
    public void initialize() throws Exception {
        if (this.useLuaScriptForAudio()) {
            ParticleSoundManager.setInstance(new LuaParticleSoundManager());
            AnmActionRunScriptManager.setInstance(new LuaAnmActionRunScriptManager());
        }
        else {
            ParticleSoundManager.setInstance(new BinaryParticleSoundManager());
            AnmActionRunScriptManager.setInstance(new BinaryAnmActionRunScriptManager());
        }
        this.getAppUI().setEventListener(this);
    }
    
    protected abstract boolean useLuaScriptForAudio();
    
    protected void initXulor() throws Exception {
        this.m_uiManager.setAppUI(this.getAppUI());
        this.registerCustomWidgets();
    }
    
    public void registerCustomWidgets() {
    }
    
    protected abstract UIScene createXulorScene() throws Exception;
    
    protected abstract void initXulorScene(final UIScene p0) throws Exception;
    
    protected abstract GameWorldScene createWorldScene(final float p0, final float p1) throws Exception;
    
    protected void initWorldScene(final AleaWorldScene worldScene) throws Exception {
    }
    
    protected boolean initApplicationUI(final ApplicationResolution res, final boolean bVSync, final GLCaps caps) throws Exception {
        this.getAppUI().initialize(caps);
        this.getRenderer().setSyncWait(bVSync);
        this.getAppUI().applyResolution(res);
        return true;
    }
    
    protected abstract void runHardwareTests();
    
    protected abstract void runBenchmarks();
    
    public void resumePostDiagnosticLoading() {
        try {
            this.registerContentInitializers();
            this.runInitializers();
        }
        catch (Exception e) {
            AbstractGameClientInstance.m_logger.error((Object)"Erreur lors du resomePostDiagnosticLoading : ", (Throwable)e);
            JOptionPane.showMessageDialog(this.getAppUI().getAppFrame(), e.getMessage() + " (" + e.getClass().getName() + ')');
        }
    }
    
    protected final void initBackgroundScene(final String gfxPath, final float minZoom, final float maxZoom) throws Exception {
        for (int i = 0; i < this.m_backgroundScenes.length; ++i) {
            (this.m_backgroundScenes[i] = new ParallaxWorldScene(minZoom, maxZoom)).setGfxPath(gfxPath);
        }
    }
    
    protected void initializeHardwareTest() {
        this.m_xulorScene.addEventListener(new UISceneEventListener() {
            @Override
            public void onProcess(final UIScene scene, final int deltaTime) {
                AbstractGameClientInstance.this.runHardwareTests();
                AbstractGameClientInstance.this.runBenchmarks();
                scene.removeEventListener(this);
                AbstractGameClientInstance.this.resumePostDiagnosticLoading();
            }
            
            @Override
            public void onResize(final UIScene scene, final int deltaWidth, final int deltaHeight) {
            }
            
            @Override
            public void onSceneInitializationComplete(final UIScene scene) {
            }
        });
    }
    
    protected void initScenes(final Renderer renderer, final float minZoom, final float maxZoom) throws Exception {
        renderer.pushScene(FadeManager.getInstance(), true);
        this.m_worldScene = this.createWorldScene(minZoom, maxZoom);
        this.initBackgroundScene(this.getGfxPath(), minZoom, maxZoom);
        this.initXulor();
        this.m_xulorScene = this.createXulorScene();
        this.initializeHardwareTest();
        this.initXulorScene(this.m_xulorScene);
        if (this.m_xulorScene != null) {
            renderer.pushScene(this.m_xulorScene, true);
            renderer.pushMouseController(this.m_xulorScene, true);
            renderer.pushKeyboardController(this.m_xulorScene, false);
            renderer.pushFocusController(this.m_xulorScene, false);
        }
        this.initWorldScene(this.m_worldScene);
        if (this.m_worldScene != null) {
            renderer.pushScene(this.m_worldScene, false);
            renderer.pushMouseController(this.m_worldScene, false);
            renderer.pushKeyboardController(this.m_worldScene, false);
            renderer.pushFocusController(this.m_worldScene, false);
            for (int i = 0; i < this.m_backgroundScenes.length; ++i) {
                this.m_backgroundScenes[i].setReferenceScene(this.m_worldScene);
            }
        }
    }
    
    protected abstract String getGfxPath();
    
    protected void registerContentInitializer(final ContentInitializer loader) {
        this.m_contentInitializers.add(loader);
    }
    
    protected abstract void registerContentInitializers();
    
    protected void runInitializers() throws Exception {
        this.m_currentContentInitializerIndex = -1;
        this.onContentInitializeStart(this.m_contentInitializers.size() - 1);
        this.processNextContentInitializer();
    }
    
    private void processNextContentInitializer() {
        if (++this.m_currentContentInitializerIndex < this.m_contentInitializers.size()) {
            final ContentInitializer contentLoader = this.m_contentInitializers.get(this.m_currentContentInitializerIndex);
            if (contentLoader != null) {
                try {
                    contentLoader.init(this);
                }
                catch (Exception e) {
                    this.onContentInitializerrError(contentLoader, e);
                }
            }
        }
        else {
            this.onContentInitializeFinished(this.m_currentContentInitializerIndex);
            this.start();
        }
    }
    
    public void fireContentInitializerDone(final ContentInitializer contentInitializer) {
        this.onContentInitializerDone(contentInitializer, this.m_currentContentInitializerIndex);
        try {
            final ContentInitializer contentLoader = this.m_contentInitializers.get(this.m_currentContentInitializerIndex);
            this.onContentInitializerStart(contentLoader);
        }
        catch (RuntimeException e) {
            AbstractGameClientInstance.m_logger.error((Object)"exception sur onContentInitializerStart", (Throwable)e);
        }
        this.m_xulorScene.addEventListener(new UISceneEventListener() {
            @Override
            public void onProcess(final UIScene scene, final int deltaTime) {
                AbstractGameClientInstance.this.processNextContentInitializer();
                scene.removeEventListener(this);
            }
            
            @Override
            public void onResize(final UIScene scene, final int deltaWidth, final int deltaHeight) {
            }
            
            @Override
            public void onSceneInitializationComplete(final UIScene scene) {
            }
        });
    }
    
    protected abstract void onContentInitializeStart(final int p0);
    
    protected abstract void onContentInitializeFinished(final int p0);
    
    protected abstract void onContentInitializerStart(final ContentInitializer p0);
    
    protected abstract void onContentInitializerrError(final ContentInitializer p0, final Exception p1);
    
    protected abstract void onContentInitializerDone(final ContentInitializer p0, final int p1);
    
    protected abstract void start();
    
    public void partialCleanUp() {
        this.doCleanUp();
    }
    
    public void cleanUp() {
        this.doCleanUp();
    }
    
    private void doCleanUp() {
        try {
            MobileManager.getInstance().removeAllMobiles();
        }
        catch (RuntimeException e) {
            AbstractGameClientInstance.m_logger.error((Object)"Exception lors du nettoyage des mobiles", (Throwable)e);
        }
        try {
            this.m_uiManager.reloadTextures();
        }
        catch (RuntimeException e) {
            AbstractGameClientInstance.m_logger.error((Object)"Exception lors du nettoyage du TextureManager", (Throwable)e);
        }
    }
    
    @Override
    public void onUIClosed() {
    }
    
    @Override
    public void onUIClosing() {
        final ApplicationResolution res = this.getAppUI().getResolution();
        if (res == null) {
            return;
        }
        this.getGamePreferences().setValue(KeyPreferenceStoreEnum.APPLICATION_RESOLUTION_KEY, res.serialize());
    }
    
    @Override
    public void onUIResolutionChanged(final ApplicationResolution res) {
        this.getGamePreferences().setValue(KeyPreferenceStoreEnum.APPLICATION_RESOLUTION_KEY, res.serialize());
    }
    
    @Override
    public void onUIIconified(final boolean iconified) {
        final Renderer renderer = this.getRenderer();
        if (renderer == null) {
            return;
        }
        renderer.setAsleep(iconified);
        RenderStateManager.getInstance().resetViewport();
        this.getAppUI().forceGLComponentSizeUpdate();
    }
    
    @Override
    public void onUIActivated(final boolean activated) {
        final Renderer renderer = this.getRenderer();
        if (renderer == null) {
            return;
        }
        renderer.setSlowedDown(!activated);
        RenderStateManager.getInstance().resetViewport();
        this.getAppUI().forceGLComponentSizeUpdate();
    }
    
    public abstract void onWorldSceneInitialized();
    
    public void displayDebugBar(final boolean display) {
        if (this.isDebugBarVisible() == display) {
            return;
        }
        if (this.getAppUI() == null) {
            return;
        }
        if (display) {
            if (this.m_debugBar == null) {
                this.m_debugBar = new DebugBar(this);
                this.getAppUI().addDebugBar(this.m_debugBar);
            }
            this.m_debugBar.reset();
            this.addDebugBarComponents(this.m_debugBar);
        }
        else if (this.m_debugBar != null) {
            this.getAppUI().removeDebugBar();
            this.m_debugBar.reset();
            this.m_debugBar = null;
        }
    }
    
    protected void addDebugBarComponents(final DebugBar bar) {
        bar.addDebugComponent(FpsViewer.class);
        bar.addDebugComponent(VSyncSwitch.class);
        bar.addDebugComponent(LightsSwitch.class);
        bar.addDebugComponent(WalkabilityViewer.class);
    }
    
    public boolean isDebugBarVisible() {
        return this.m_debugBar != null && this.m_debugBar.isVisible();
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractGameClientInstance.class);
    }
}
