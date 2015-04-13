package com.ankamagames.framework.graphics.opengl;

import org.apache.log4j.*;
import java.awt.*;
import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.framework.kernel.core.controllers.*;
import javax.media.opengl.*;
import java.util.*;
import com.ankamagames.framework.graphics.opengl.events.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.engine.profiling.*;
import com.ankamagames.framework.graphics.engine.Anm2.*;
import com.ankamagames.framework.graphics.engine.text.*;
import com.ankamagames.framework.graphics.engine.texture.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.sound.openAL.*;
import org.jetbrains.annotations.*;
import com.sun.opengl.util.*;
import java.io.*;
import java.awt.event.*;
import com.ankamagames.framework.kernel.utils.*;

public class Renderer implements GLEventListener, KeyListener, MouseListener, MouseWheelListener, MouseMotionListener, FocusListener
{
    public static final long MAX_FPS_WHEN_ASLEEP = 2L;
    public static final long MAX_FPS_WHEN_SLOWED_DOWN = 20L;
    protected static final Logger m_logger;
    private static final boolean PROCESS_MOUSE_EVENT;
    public static final Color DEFAULT_BACKGROUND_COLOR;
    private float m_backgroundColorRed;
    private float m_backgroundColorGreen;
    private float m_backgroundColorBlue;
    private float m_backgroundColorAlpha;
    private final Object m_scenesMutex;
    private final ArrayList<GLRenderable> m_scenes;
    private final Object m_renderablesToInitMutex;
    private final ArrayList<GLRenderable> m_renderablesToInitialize;
    private GL m_gl;
    private final ArrayList<MouseController> m_mouseControllers;
    private final ArrayList<KeyboardController> m_keyboardControllers;
    private final ArrayList<FocusController> m_focusControllers;
    private final ArrayList<RendererEventsHandler> m_rendererHandlers;
    private final ArrayList<VSyncEventHandler> m_vSyncHandlers;
    private final HashMap<String, String> m_glInformations;
    private static final boolean OPENGL_DEBUG_VIEW = false;
    private static final boolean OPENGL_DEBUG_LOG = false;
    private boolean m_doubleBuffering;
    private boolean m_requestedSyncWait;
    private boolean m_syncWait;
    private long m_lastTime;
    private int m_deltaTimeIndex;
    private final float[] m_deltaTimes;
    private boolean m_requestScreenShot;
    private ScreenShotHandler m_screenShotHandler;
    private int m_displayWidth;
    private int m_displayHeight;
    private ArrayList<RenderToTextureTask> m_renderToTexture;
    private boolean m_initialized;
    private boolean m_sleep;
    private boolean m_slowedDown;
    private boolean m_sceneUpdateStopped;
    private GLRendererEventListener m_eventListener;
    
    public Renderer() {
        super();
        this.m_backgroundColorRed = 0.0f;
        this.m_backgroundColorGreen = 0.0f;
        this.m_backgroundColorBlue = 0.0f;
        this.m_backgroundColorAlpha = 0.0f;
        this.m_scenesMutex = new Object();
        this.m_scenes = new ArrayList<GLRenderable>();
        this.m_renderablesToInitMutex = new Object();
        this.m_renderablesToInitialize = new ArrayList<GLRenderable>();
        this.m_glInformations = new HashMap<String, String>();
        this.m_requestedSyncWait = true;
        this.m_syncWait = !this.m_requestedSyncWait;
        this.m_lastTime = System.nanoTime();
        this.m_deltaTimes = new float[180];
        this.m_requestScreenShot = false;
        this.m_displayWidth = 0;
        this.m_displayHeight = 0;
        this.m_renderToTexture = null;
        this.m_sleep = false;
        this.m_slowedDown = false;
        this.m_sceneUpdateStopped = false;
        this.m_eventListener = null;
        this.setBackgroundColor(Renderer.DEFAULT_BACKGROUND_COLOR);
        this.m_mouseControllers = new ArrayList<MouseController>();
        this.m_keyboardControllers = new ArrayList<KeyboardController>();
        this.m_focusControllers = new ArrayList<FocusController>();
        this.m_doubleBuffering = true;
        this.m_syncWait = true;
        this.m_vSyncHandlers = new ArrayList<VSyncEventHandler>();
        this.m_rendererHandlers = new ArrayList<RendererEventsHandler>();
    }
    
    public void setBackgroundColor(final float[] backgroundColor) {
        this.m_backgroundColorRed = backgroundColor[0];
        this.m_backgroundColorGreen = backgroundColor[1];
        this.m_backgroundColorBlue = backgroundColor[2];
        this.m_backgroundColorAlpha = backgroundColor[3];
    }
    
    public void setBackgroundColor(final Color backgroundColor) {
        this.m_backgroundColorRed = backgroundColor.getRed() / 255.0f;
        this.m_backgroundColorGreen = backgroundColor.getGreen() / 255.0f;
        this.m_backgroundColorBlue = backgroundColor.getBlue() / 255.0f;
        this.m_backgroundColorAlpha = backgroundColor.getAlpha() / 255.0f;
    }
    
    public GL getGl() {
        return this.m_gl;
    }
    
    public void setAsleep(final boolean sleep) {
        this.m_sleep = sleep;
    }
    
    public void setSlowedDown(final boolean slowedDown) {
        this.m_slowedDown = slowedDown;
    }
    
    public boolean isSceneUpdateStopped() {
        return this.m_sceneUpdateStopped;
    }
    
    public void setSceneUpdateStopped(final boolean sceneUpdateStopped) {
        this.m_sceneUpdateStopped = sceneUpdateStopped;
    }
    
    public int getDisplayHeight() {
        return this.m_displayHeight;
    }
    
    public int getDisplayWidth() {
        return this.m_displayWidth;
    }
    
    private void enableVSync(final GLAutoDrawable drawable, final boolean syncWait) {
        assert this.m_gl != null : "Unable to call enableVSync if m_gl is not initialised! Did you call it before Renderer::Init ?";
        if (this.m_syncWait == syncWait) {
            return;
        }
        this.m_syncWait = syncWait;
        if (this.m_syncWait) {
            drawable.setAutoSwapBufferMode(true);
            this.m_gl.setSwapInterval(1);
        }
        else {
            drawable.setAutoSwapBufferMode(false);
            this.m_gl.setSwapInterval(0);
        }
        for (final VSyncEventHandler handler : this.m_vSyncHandlers) {
            handler.onVSyncChanged(syncWait);
        }
    }
    
    public void requestVSync(final boolean syncWait) {
        this.m_requestedSyncWait = syncWait;
        this.m_syncWait = !this.m_requestedSyncWait;
    }
    
    public final boolean isVSyncEnabled() {
        return this.m_syncWait;
    }
    
    public void pushScene(final GLRenderable scene, final boolean bPushFront) {
        synchronized (this.m_scenesMutex) {
            synchronized (this.m_renderablesToInitMutex) {
                if (!this.m_scenes.contains(scene)) {
                    if (!bPushFront) {
                        this.m_scenes.add(0, scene);
                        this.m_renderablesToInitialize.add(0, scene);
                    }
                    else {
                        this.m_scenes.add(scene);
                        this.m_renderablesToInitialize.add(scene);
                    }
                }
            }
        }
    }
    
    public void pushSceneAt(final GLRenderable toAdd, final GLRenderable ref, final boolean after) {
        synchronized (this.m_scenesMutex) {
            synchronized (this.m_renderablesToInitMutex) {
                if (this.m_scenes.contains(toAdd)) {
                    return;
                }
                int index = this.m_scenes.indexOf(ref);
                if (index == -1) {
                    index = 0;
                }
                else if (after) {
                    ++index;
                }
                int indexInit = this.m_renderablesToInitialize.indexOf(ref);
                if (indexInit == -1) {
                    indexInit = 0;
                }
                else if (after) {
                    ++indexInit;
                }
                this.m_scenes.add(index, toAdd);
                this.m_renderablesToInitialize.add(indexInit, toAdd);
            }
        }
    }
    
    public void removeScene(final GLRenderable scene) {
        this.m_scenes.remove(scene);
    }
    
    public void addRendererEventsHandler(final RendererEventsHandler handler) {
        if (!this.m_rendererHandlers.contains(handler)) {
            this.m_rendererHandlers.add(handler);
        }
    }
    
    public void removeRendererEventsHandler(final RendererEventsHandler handler) {
        this.m_rendererHandlers.remove(handler);
    }
    
    public void addVSyncEventsHandler(final VSyncEventHandler handler) {
        if (!this.m_vSyncHandlers.contains(handler)) {
            this.m_vSyncHandlers.add(handler);
        }
    }
    
    public void removeVSyncEventsHandler(final VSyncEventHandler handler) {
        this.m_vSyncHandlers.remove(handler);
    }
    
    public void init(final GLAutoDrawable glAutoDrawable) {
        Renderer.m_logger.info((Object)"Renderer.init started");
        this.m_gl = glAutoDrawable.getGL();
        final GLRenderer renderer = RendererType.OpenGL.getRenderer();
        renderer.createDevice(this.m_gl);
        GLDebugger.dump(this.m_gl);
        GLDebugger.getGLInformations(this.m_gl, this.m_glInformations);
        GLDebugger.getInstance().readVersion(this.m_gl);
        final int majorToTest = 1;
        final int minorToTest = 3;
        if (GLDebugger.getInstance().isVersionLowerThan(1, 3)) {
            if (this.m_eventListener != null) {
                final GLDebugger.Version version = GLDebugger.getInstance().getVersion();
                this.m_eventListener.onRendererInitializationError(this, new OpenGLVersionTooOldEvent(1, 3, version.getMajor(), version.getMinor()));
            }
            return;
        }
        this.m_eventListener = null;
        final RenderStateManager stateManager = RenderStateManager.getInstance();
        stateManager.setValuesToDefault();
        this.m_gl.glClearColor(this.m_backgroundColorRed, this.m_backgroundColorGreen, this.m_backgroundColorBlue, this.m_backgroundColorAlpha);
        StencilStateManager.getInstance().clear(this.m_gl);
        stateManager.applyViewport(this.m_gl, 0, 0, glAutoDrawable.getWidth(), glAutoDrawable.getHeight());
        glAutoDrawable.setSize(glAutoDrawable.getWidth(), glAutoDrawable.getHeight());
        this.m_gl.glTexParameterf(3553, 10242, 33071.0f);
        this.m_gl.glTexParameterf(3553, 10243, 33071.0f);
        this.m_gl.glTexParameterf(3553, 10240, 9729.0f);
        this.m_gl.glTexParameterf(3553, 10241, 9728.0f);
        this.m_gl.glHint(3152, 4354);
        this.m_gl.glHint(34031, 4354);
        this.m_gl.glHint(33170, 4354);
        this.m_gl.glDisable(2896);
        stateManager.enableScissor(false);
        stateManager.enableDepthTest(false);
        stateManager.setDepthMask(false);
        this.m_gl.glAlphaFunc(517, 0.0f);
        this.m_gl.glEnable(3008);
        this.m_gl.glDisable(2884);
        stateManager.enableBlend(false);
        this.m_gl.glShadeModel(7425);
        this.m_gl.glPixelZoom(1.0f, 1.0f);
        stateManager.enableTextures(true);
        this.m_gl.glIndexMask(0);
        this.m_gl.glDisable(3024);
        if (this.m_doubleBuffering) {
            this.m_gl.glDrawBuffer(1029);
        }
        else {
            this.m_gl.glDrawBuffer(1028);
        }
        this.enableVSync(glAutoDrawable, this.m_requestedSyncWait);
        stateManager.applyStates(renderer);
        for (final RendererEventsHandler handler : this.m_rendererHandlers) {
            handler.onInit(glAutoDrawable);
        }
        this.m_initialized = true;
        Renderer.m_logger.info((Object)"Renderer.init ended");
    }
    
    public final HashMap<String, String> getDeviceInformations() {
        return this.m_glInformations;
    }
    
    public void display(final GLAutoDrawable glAutoDrawable) {
        if (this.m_sleep || this.m_slowedDown) {
            final long now = System.nanoTime();
            final float elapsedTime = (now - this.m_lastTime) / 1000000.0f;
            long msWait = this.m_sleep ? 500L : 50L;
            msWait -= (long)elapsedTime;
            if (msWait > 1L) {
                try {
                    Thread.sleep(msWait);
                }
                catch (InterruptedException e) {
                    Renderer.m_logger.error((Object)"Exception", (Throwable)e);
                }
            }
        }
        if (this.m_initialized) {
            try {
                final long realTime = System.nanoTime();
                float realDeltaTime = (realTime - this.m_lastTime) / 1000000.0f;
                final int deltaTimeLength = this.m_deltaTimes.length;
                this.m_deltaTimes[++this.m_deltaTimeIndex % deltaTimeLength] = realDeltaTime;
                float dt = 0.0f;
                for (int i = 0; i < deltaTimeLength; ++i) {
                    dt += this.m_deltaTimes[i];
                }
                final float targetedDeltaTime = dt / deltaTimeLength;
                final float lostFrameCount = realDeltaTime / targetedDeltaTime;
                int deltaTime;
                if (lostFrameCount <= 2.0f) {
                    deltaTime = (int)realDeltaTime;
                }
                else {
                    deltaTime = (int)realDeltaTime / 2;
                }
                this.m_lastTime = realTime;
                if (this.isSceneUpdateStopped()) {
                    realDeltaTime = (deltaTime = 0);
                }
                if (this.m_rendererHandlers != null) {
                    for (int j = 0, size = this.m_rendererHandlers.size(); j < size; ++j) {
                        this.m_rendererHandlers.get(j).onDisplay(glAutoDrawable);
                    }
                }
                final GLRenderer renderer = RendererType.OpenGL.getRenderer();
                synchronized (this.m_scenesMutex) {
                    this.enableVSync(glAutoDrawable, this.m_requestedSyncWait);
                    synchronized (this.m_renderablesToInitMutex) {
                        int frustrumWidth = glAutoDrawable.getWidth();
                        frustrumWidth += frustrumWidth % 2;
                        int frustrumHeight = glAutoDrawable.getHeight();
                        frustrumHeight += frustrumHeight % 2;
                        for (int k = 0, size2 = this.m_renderablesToInitialize.size(); k < size2; ++k) {
                            final GLRenderable renderable = this.m_renderablesToInitialize.get(k);
                            renderable.init(glAutoDrawable);
                            renderable.setFrustumSize(frustrumWidth, frustrumHeight);
                        }
                        this.m_renderablesToInitialize.clear();
                    }
                    Profiler.start("process", 0.0f, 0.2f, 0.2f);
                    for (int l = 0, size3 = this.m_scenes.size(); l < size3; ++l) {
                        this.m_scenes.get(l).process(deltaTime);
                    }
                    Profiler.end();
                    AnmManager.getInstance().update(deltaTime);
                    GlyphVectorCache.INSTANCE.update(deltaTime);
                    TextureManager.getInstance().prepareTextures(RendererType.OpenGL.getRenderer());
                }
                synchronized (this.m_scenesMutex) {
                    final RenderStateManager stateManager = RenderStateManager.getInstance();
                    if (this.m_renderToTexture != null) {
                        this.renderToTexture();
                    }
                    stateManager.setDepthMask(false);
                    stateManager.enableDepthTest(false);
                    stateManager.applyStates(renderer);
                    if (!EffectManager.getInstance().canDoPostProcess(renderer)) {
                        StencilStateManager.getInstance().clear(this.m_gl);
                    }
                    Profiler.start("display", 0.0f, 0.9f, 0.9f);
                    for (int m = 0, size4 = this.m_scenes.size(); m < size4; ++m) {
                        this.m_scenes.get(m).display(this.m_gl);
                    }
                    Profiler.end();
                    TextureManager.getInstance().releaseTextures();
                    EngineStats.getInstance().render(renderer);
                    Profiler.render(renderer);
                    Profiler.newFrame();
                    if (this.m_requestScreenShot) {
                        this.takeScreenShot();
                    }
                    if (this.m_doubleBuffering && !this.m_syncWait) {
                        glAutoDrawable.swapBuffers();
                    }
                    SoundManager.getInstance().addWaitingSources();
                }
            }
            catch (Throwable t) {
                Renderer.m_logger.error((Object)"Throwable dans le process du renderer : ", t);
                if (t.getCause() != null) {
                    Renderer.m_logger.error((Object)"Reason : ", t.getCause());
                }
            }
        }
    }
    
    private void renderToTexture() {
        if (this.m_renderToTexture.size() <= 0) {
            return;
        }
        if (!this.m_renderToTexture.get(0).render()) {
            this.m_renderToTexture.remove(0);
            if (this.m_renderToTexture.size() == 0) {
                this.m_renderToTexture = null;
            }
        }
    }
    
    public boolean requestScreenShot(@NotNull final ScreenShotHandler handler) {
        if (this.m_requestScreenShot || this.m_screenShotHandler != null) {
            return false;
        }
        this.m_requestScreenShot = true;
        this.m_screenShotHandler = handler;
        return true;
    }
    
    private void takeScreenShot() {
        if (!this.m_requestScreenShot || this.m_screenShotHandler == null) {
            return;
        }
        final File outputFile = this.m_screenShotHandler.getOutputFile();
        try {
            Screenshot.writeToFile(outputFile, this.m_displayWidth, this.m_displayHeight);
            this.m_screenShotHandler.onScreenShotTook();
        }
        catch (Exception e) {
            this.m_screenShotHandler.onScreenShotError(e);
            Renderer.m_logger.error((Object)(e.getMessage() + " Impossible d'enregistrer un ScreenShot dans " + outputFile.getAbsolutePath()));
        }
        finally {
            this.m_requestScreenShot = false;
            this.m_screenShotHandler = null;
        }
    }
    
    public void reshape(final GLAutoDrawable drawable, final int x, final int y, int width, int height) {
        height += height % 2;
        width += width % 2;
        if (this.m_displayWidth == width && this.m_displayHeight == height) {
            return;
        }
        this.m_displayWidth = width;
        this.m_displayHeight = height;
        if (drawable.getWidth() != 0 && drawable.getHeight() != 0) {
            for (final GLRenderable s : this.m_scenes) {
                s.setFrustumSize(width, height);
            }
            RendererType.OpenGL.getRenderer().setViewportSize(width, height);
            EffectManager.getInstance().createRenderTargets(width, height);
        }
        for (final RendererEventsHandler handler : this.m_rendererHandlers) {
            handler.onReshape(drawable, x, y, width, height);
        }
    }
    
    public void displayChanged(final GLAutoDrawable drawable, final boolean modeChanged, final boolean deviceChanged) {
        for (final RendererEventsHandler handler : this.m_rendererHandlers) {
            handler.onDisplayChanged(drawable, modeChanged, deviceChanged);
        }
    }
    
    public void keyTyped(final KeyEvent keyEvent) {
        try {
            for (final KeyboardController controller : this.m_keyboardControllers) {
                if (controller.keyTyped(keyEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void keyReleased(final KeyEvent keyEvent) {
        try {
            for (final KeyboardController controller : this.m_keyboardControllers) {
                if (controller.keyReleased(keyEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void keyPressed(final KeyEvent keyEvent) {
        try {
            for (final KeyboardController controller : this.m_keyboardControllers) {
                if (controller.keyPressed(keyEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void pushKeyboardController(final KeyboardController controller, final boolean bPushFront) {
        if (!this.m_keyboardControllers.contains(controller)) {
            if (!bPushFront) {
                this.m_keyboardControllers.add(controller);
            }
            else {
                this.m_keyboardControllers.add(0, controller);
            }
        }
    }
    
    public void removeKeyboardController(final KeyboardController controller) {
        this.m_keyboardControllers.remove(controller);
    }
    
    private MouseEvent processMouseEvent(final MouseEvent event) {
        if (Renderer.PROCESS_MOUSE_EVENT && event.getButton() == 1 && event.isControlDown()) {
            final int modifiers = event.getModifiersEx() ^ 0x80;
            return new MouseEvent(event.getComponent(), event.getID(), event.getWhen(), modifiers, event.getX(), event.getY(), event.getClickCount(), event.isPopupTrigger(), 3);
        }
        return event;
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        try {
            mouseEvent = this.processMouseEvent(mouseEvent);
            for (final MouseController controller : this.m_mouseControllers) {
                if (controller.mouseClicked(mouseEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void mousePressed(MouseEvent mouseEvent) {
        try {
            mouseEvent = this.processMouseEvent(mouseEvent);
            for (final MouseController controller : this.m_mouseControllers) {
                if (controller.mousePressed(mouseEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void mouseReleased(MouseEvent mouseEvent) {
        try {
            mouseEvent = this.processMouseEvent(mouseEvent);
            for (final MouseController controller : this.m_mouseControllers) {
                if (controller.mouseReleased(mouseEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void mouseEntered(final MouseEvent mouseEvent) {
        try {
            for (final MouseController controller : this.m_mouseControllers) {
                if (controller.mouseEntered(mouseEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void mouseExited(final MouseEvent mouseEvent) {
        try {
            for (final MouseController controller : this.m_mouseControllers) {
                if (controller.mouseExited(mouseEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void mouseDragged(MouseEvent mouseEvent) {
        try {
            mouseEvent = this.processMouseEvent(mouseEvent);
            for (final MouseController controller : this.m_mouseControllers) {
                if (controller.mouseDragged(mouseEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void mouseMoved(final MouseEvent mouseEvent) {
        try {
            for (final MouseController controller : this.m_mouseControllers) {
                if (controller.mouseMoved(mouseEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void mouseWheelMoved(final MouseWheelEvent mouseEvent) {
        try {
            for (final MouseController controller : this.m_mouseControllers) {
                if (controller.mouseWheelMoved(mouseEvent)) {
                    break;
                }
            }
        }
        catch (Throwable t) {
            Renderer.m_logger.error((Object)"Erreur : ", t);
        }
    }
    
    public void pushMouseController(final MouseController controller, final boolean bPushFront) {
        if (!this.m_mouseControllers.contains(controller)) {
            if (!bPushFront) {
                this.m_mouseControllers.add(controller);
            }
            else {
                this.m_mouseControllers.add(0, controller);
            }
        }
    }
    
    public void removeMouseController(final MouseController controller) {
        this.m_mouseControllers.remove(controller);
    }
    
    public boolean isSyncWait() {
        return this.m_syncWait;
    }
    
    public void setSyncWait(final boolean syncWait) {
        this.m_syncWait = syncWait;
    }
    
    public boolean isDoubleBuffering() {
        return this.m_doubleBuffering;
    }
    
    public void setDoubleBuffering(final boolean doubleBuffering) {
        this.m_doubleBuffering = doubleBuffering;
    }
    
    public void setEventListener(final GLRendererEventListener listener) {
        this.m_eventListener = listener;
    }
    
    public void pushFocusController(final FocusController controller, final boolean bPushFront) {
        if (!this.m_focusControllers.contains(controller)) {
            if (!bPushFront) {
                this.m_focusControllers.add(controller);
            }
            else {
                this.m_focusControllers.add(0, controller);
            }
        }
    }
    
    public void removeFocusController(final FocusController controller) {
        this.m_focusControllers.remove(controller);
    }
    
    public void focusGained(final FocusEvent e) {
        for (final FocusController controller : this.m_focusControllers) {
            if (controller.focusGained(e)) {
                break;
            }
        }
    }
    
    public void focusLost(final FocusEvent e) {
        for (final FocusController controller : this.m_focusControllers) {
            if (controller.focusLost(e)) {
                break;
            }
        }
    }
    
    public void addRenderToTextureTask(final RenderToTextureTask renderToTexture) {
        if (this.m_renderToTexture == null) {
            this.m_renderToTexture = new ArrayList<RenderToTextureTask>();
        }
        this.m_renderToTexture.add(renderToTexture);
    }
    
    static {
        m_logger = Logger.getLogger((Class)Renderer.class);
        PROCESS_MOUSE_EVENT = (OS.getCurrentOS() == OS.MAC);
        DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    }
    
    public interface RenderToTextureTask
    {
        boolean render();
    }
}
