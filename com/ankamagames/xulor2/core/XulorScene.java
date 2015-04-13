package com.ankamagames.xulor2.core;

import com.ankamagames.framework.graphics.opengl.base.*;
import org.apache.log4j.*;
import com.ankamagames.xulor2.component.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.ui.*;
import com.ankamagames.framework.graphics.engine.transformer.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.baseImpl.graphics.opengl.*;
import javax.media.opengl.*;
import com.ankamagames.framework.graphics.engine.states.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.graphics.engine.opengl.*;
import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.fx.*;
import com.ankamagames.xulor2.graphics.*;
import com.ankamagames.framework.graphics.engine.entity.*;
import com.ankamagames.framework.graphics.image.*;
import java.awt.event.*;

public class XulorScene extends Scene implements UIScene
{
    private static Logger m_logger;
    public static final boolean DEBUG_XULOR_PROCESS_TIME = false;
    private GL m_gl;
    private Graphics m_graphics;
    private MasterRootContainer m_rootContainer;
    private TreeDepthManager m_treeDepthManager;
    private final ArrayList<UISceneEventListener> m_listeners;
    private final ArrayList<XulorSceneDisplayListener> m_displayListeners;
    private int m_width;
    private int m_height;
    private float m_scaleX;
    private float m_scaleY;
    private boolean m_inPostProcess;
    private static final TransformerSRT m_cameraTransformer;
    private static final int[] SUPPORTED_MOUSE_BUTTONS;
    public static long m_layoutTime;
    public static long m_layoutNumber;
    public static long m_textValidateTime;
    public static long m_dovValidateTime;
    public static long m_validateTime;
    public static long m_preProcessTime;
    public static long m_middleProcessTime;
    public static long m_postProcessTime;
    private static final boolean CHECK_PROCESS_DURATION = false;
    private static final int PROCESSES_CHECK_INTERVAL = 5;
    private static final long MAX_PROCESSES_DURATION = 3000000L;
    public static int m_current_processes;
    private static long m_currentProcessStart;
    private static boolean m_processIsEnabled;
    
    public XulorScene() {
        super();
        this.m_listeners = new ArrayList<UISceneEventListener>();
        this.m_displayListeners = new ArrayList<XulorSceneDisplayListener>();
        this.m_scaleX = 1.0f;
        this.m_scaleY = 1.0f;
        this.m_inPostProcess = false;
        this.addEventListener(Xulor.getInstance());
    }
    
    @Override
    public void addEventListener(final UISceneEventListener listener) {
        if (!this.m_listeners.contains(listener)) {
            this.m_listeners.add(listener);
        }
    }
    
    @Override
    public void removeEventListener(final UISceneEventListener listener) {
        if (this.m_listeners.contains(listener)) {
            this.m_listeners.remove(listener);
        }
    }
    
    public void addDisplayListener(final XulorSceneDisplayListener listener) {
        if (!this.m_displayListeners.contains(listener)) {
            this.m_displayListeners.add(listener);
        }
    }
    
    public void removeDisplayListener(final XulorSceneDisplayListener listener) {
        if (this.m_displayListeners.contains(listener)) {
            this.m_displayListeners.remove(listener);
        }
    }
    
    @Override
    public void setFrustumSize(int frustumWidth, int frustumHeight) {
        this.setScale(frustumWidth / 1000.0f, frustumHeight / 720.0f);
        frustumWidth /= (int)this.m_scaleX;
        frustumHeight /= (int)this.m_scaleY;
        super.setFrustumSize(frustumWidth, frustumHeight);
        final int deltaWidth = frustumWidth - this.m_width;
        final int deltaHeight = frustumHeight - this.m_height;
        this.m_width = frustumWidth;
        this.m_height = frustumHeight;
        if (this.m_graphics != null) {
            this.m_graphics.setDrawableSize(this.m_width, this.m_height);
        }
        if (this.m_rootContainer != null) {
            this.m_rootContainer.setSize(this.m_width, this.m_height);
        }
        final MRUManager mruManager = Xulor.getInstance().getMruManager();
        if (mruManager != null) {
            mruManager.closeCurrentMRU();
        }
        this.fireResize(deltaWidth, deltaHeight);
    }
    
    public MasterRootContainer getMasterRootContainer() {
        return this.m_rootContainer;
    }
    
    public boolean isInPostProcess() {
        return this.m_inPostProcess;
    }
    
    @Override
    public void init(final GLAutoDrawable glAutoDrawable) {
        try {
            super.init(glAutoDrawable);
            ((GLAWTWorkspace)glAutoDrawable).setFocusTraversalKeysEnabled(false);
            this.m_gl = glAutoDrawable.getGL();
            (this.m_graphics = Graphics.getInstance()).setDrawableSize(this.m_width, this.m_height);
            this.m_graphics.initialize(this.m_gl, (GLDrawable)glAutoDrawable);
            final Environment env = Xulor.getInstance().getEnvironment();
            final ElementMap baseElementMap = env.createElementMap("masterRootContainer");
            (this.m_rootContainer = MasterRootContainer.getInstance()).onCheckOut();
            this.m_rootContainer.setSize(this.m_width, this.m_height);
            this.m_rootContainer.setElementMap(baseElementMap);
            this.m_rootContainer.getEntity().getTransformer().setScale(0, this.m_scaleX, this.m_scaleY);
            this.fireInitializationComplete();
        }
        catch (Throwable t) {
            XulorScene.m_logger.error((Object)"Exception", t);
        }
    }
    
    private void setScene(final GL gl) {
        final GLRenderer renderer = RendererType.OpenGL.getRenderer();
        final RenderStateManager stateManager = RenderStateManager.getInstance();
        renderer.m_stateManager.setVertexArrayComponents(0);
        stateManager.enableBlend(true);
        stateManager.setBlendFunc(BlendModes.SrcAlpha, BlendModes.InvSrcAlpha);
        stateManager.setColorScale(1.0f);
        stateManager.applyMatrixMode(gl, MatrixModes.TEXTURE, Matrix44.IDENTITY);
        stateManager.applyMatrixMode(gl, MatrixModes.PROJECTION, Matrix44.IDENTITY);
        final ViewPort vp = this.getViewPort();
        stateManager.applyViewportAndOrtho2D(gl, vp);
        XulorScene.m_cameraTransformer.setIdentity();
        XulorScene.m_cameraTransformer.setTranslation(0.0f, -2.0f * vp.getHalfResY(), 0.0f);
        renderer.setCameraMatrix(XulorScene.m_cameraTransformer.getMatrix());
        RenderStateManager.getInstance().reset();
    }
    
    @Override
    public void display(final GL gl) {
        if (!Engine.getInstance().isInitialiazed()) {
            return;
        }
        this.setScene(gl);
        final GLRenderer renderer = RendererType.OpenGL.getRenderer();
        try {
            if (this.m_rootContainer != null && this.m_rootContainer.getVisible()) {
                this.m_rootContainer.getEntity().render(renderer);
            }
            for (int listenersCount = this.m_displayListeners.size(), i = 0; i < listenersCount; ++i) {
                this.m_displayListeners.get(i).display(renderer, this.m_graphics);
            }
        }
        catch (Throwable t) {
            XulorScene.m_logger.error((Object)"Exception", t);
        }
    }
    
    public static boolean checkForProcessDuration() {
        return true;
    }
    
    public static void resetProcessCheck() {
    }
    
    @Override
    public void process(final int deltaTime) {
        EffectManager.getInstance().update(0);
        if (!Engine.getInstance().isInitialiazed()) {
            return;
        }
        if (this.m_rootContainer != null && !this.m_rootContainer.getVisible()) {
            return;
        }
        final long start = 0L;
        final long clientProcess = 0L;
        final long processSetItem = 0L;
        final long treeDepthManagerSort = 0L;
        final long xulorParticles = 0L;
        try {
            super.process(deltaTime);
            this.fireProcess(deltaTime);
            if (this.m_rootContainer == null) {
                return;
            }
            if (RenderableContainerManager.getInstance().needsSorting()) {
                RenderableContainerManager.getInstance().sort();
            }
            RenderableContainerManager.getInstance().processAll();
            TreeDepthManager.getInstance().processAll(deltaTime);
            XulorParticleSystemManager.INSTANCE.process(deltaTime);
        }
        catch (Throwable t) {
            XulorScene.m_logger.error((Object)"Exception", t);
        }
    }
    
    private void setScale(final float scaleX, final float scaleY) {
        if (scaleX >= 1.0f && scaleY >= 1.0f) {
            this.m_scaleX = 1.0f;
            this.m_scaleY = 1.0f;
        }
        else {
            this.m_scaleX = Math.min(scaleX, scaleY);
            this.m_scaleY = this.m_scaleX;
        }
        if (this.m_rootContainer != null) {
            this.applyScale(this.m_rootContainer.getEntity());
        }
    }
    
    public float getScale() {
        return this.m_scaleX;
    }
    
    public boolean isScaled() {
        return this.m_scaleX != 1.0f || this.m_scaleY != 1.0f;
    }
    
    public void applyScale(final Entity entity) {
        entity.getTransformer().setScale(0, this.m_scaleX, this.m_scaleY);
    }
    
    public void scale(final PooledRectangle rect) {
        rect.setX((int)(rect.getX() * this.m_scaleX));
        rect.setY((int)(rect.getY() * this.m_scaleY));
        rect.setWidth((int)(rect.getWidth() * this.m_scaleX));
        rect.setHeight((int)(rect.getHeight() * this.m_scaleY));
    }
    
    public int getScaleMouseX(final int mouseX) {
        return (int)(mouseX / this.m_scaleX);
    }
    
    public int getScaleMouseY(final int mouseY) {
        return (int)(mouseY / this.m_scaleY);
    }
    
    @Override
    public boolean keyPressed(final KeyEvent keyEvent) {
        return this.m_rootContainer != null && this.m_rootContainer.getVisible() && this.m_rootContainer.keyPressed(keyEvent);
    }
    
    @Override
    public boolean keyReleased(final KeyEvent keyEvent) {
        return this.m_rootContainer != null && this.m_rootContainer.getVisible() && this.m_rootContainer.keyReleased(keyEvent);
    }
    
    @Override
    public boolean keyTyped(final KeyEvent keyEvent) {
        return this.m_rootContainer != null && this.m_rootContainer.getVisible() && this.m_rootContainer.keyTyped(keyEvent);
    }
    
    @Override
    public boolean mouseClicked(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseEntered(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseExited(final MouseEvent mouseEvent) {
        return false;
    }
    
    @Override
    public boolean mouseDragged(final MouseEvent mouseEvent) {
        if (!this.isSupportedMouseButton(mouseEvent.getButton())) {
            return false;
        }
        if (this.m_rootContainer != null && this.m_rootContainer.getVisible()) {
            MouseEvent event = mouseEvent;
            if (this.isScaled()) {
                event = new MouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiersEx(), this.getScaleMouseX(mouseEvent.getX()), this.getScaleMouseY(mouseEvent.getY()), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
            }
            return this.m_rootContainer.mouseMoved(event);
        }
        return false;
    }
    
    @Override
    public boolean mouseMoved(final MouseEvent mouseEvent) {
        if (this.m_rootContainer != null && this.m_rootContainer.getVisible()) {
            MouseEvent event = mouseEvent;
            if (this.isScaled()) {
                event = new MouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiersEx(), this.getScaleMouseX(mouseEvent.getX()), this.getScaleMouseY(mouseEvent.getY()), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
            }
            return this.m_rootContainer.mouseMoved(event);
        }
        return false;
    }
    
    @Override
    public boolean mousePressed(final MouseEvent mouseEvent) {
        if (!this.isSupportedMouseButton(mouseEvent.getButton())) {
            return false;
        }
        if (this.m_rootContainer != null && this.m_rootContainer.getVisible()) {
            MouseEvent event = mouseEvent;
            if (this.isScaled()) {
                event = new MouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiersEx(), this.getScaleMouseX(mouseEvent.getX()), this.getScaleMouseY(mouseEvent.getY()), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
            }
            return this.m_rootContainer.mousePressed(event);
        }
        return false;
    }
    
    @Override
    public boolean mouseReleased(final MouseEvent mouseEvent) {
        if (!this.isSupportedMouseButton(mouseEvent.getButton())) {
            return false;
        }
        if (this.m_rootContainer != null && this.m_rootContainer.getVisible()) {
            MouseEvent event = mouseEvent;
            if (this.isScaled()) {
                event = new MouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiersEx(), this.getScaleMouseX(mouseEvent.getX()), this.getScaleMouseY(mouseEvent.getY()), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getButton());
            }
            return this.m_rootContainer.mouseReleased(event);
        }
        return false;
    }
    
    @Override
    public boolean mouseWheelMoved(final MouseWheelEvent mouseEvent) {
        if (this.m_rootContainer != null && this.m_rootContainer.getVisible()) {
            MouseWheelEvent event = mouseEvent;
            if (this.isScaled()) {
                event = new MouseWheelEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiersEx(), this.getScaleMouseX(mouseEvent.getX()), this.getScaleMouseY(mouseEvent.getY()), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), mouseEvent.getScrollType(), mouseEvent.getScrollAmount(), mouseEvent.getWheelRotation());
            }
            return this.m_rootContainer.mouseWheelMoved(event);
        }
        return false;
    }
    
    private boolean isSupportedMouseButton(final int mouseButton) {
        for (final int button : XulorScene.SUPPORTED_MOUSE_BUTTONS) {
            if (mouseButton == button) {
                return true;
            }
        }
        return false;
    }
    
    protected void fireInitializationComplete() {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).onSceneInitializationComplete(this);
        }
    }
    
    protected void fireProcess(final int deltaTime) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).onProcess(this, deltaTime);
        }
    }
    
    protected void fireResize(final int deltaWidth, final int deltaHeight) {
        for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
            this.m_listeners.get(i).onResize(this, deltaWidth, deltaHeight);
        }
    }
    
    @Override
    public boolean focusGained(final FocusEvent e) {
        FocusManager.getInstance().setEnabled(true);
        return false;
    }
    
    @Override
    public boolean focusLost(final FocusEvent e) {
        FocusManager.getInstance().setEnabled(false);
        Xulor.getInstance().getShortcutManager().releaseCurrentKeyCode();
        MasterRootContainer.getInstance().setMovePointMode(false);
        return false;
    }
    
    static {
        XulorScene.m_logger = Logger.getLogger((Class)XulorScene.class);
        m_cameraTransformer = new TransformerSRT();
        SUPPORTED_MOUSE_BUTTONS = new int[] { 0, 1, 2, 3 };
        XulorScene.m_current_processes = 0;
        XulorScene.m_currentProcessStart = 0L;
    }
}
