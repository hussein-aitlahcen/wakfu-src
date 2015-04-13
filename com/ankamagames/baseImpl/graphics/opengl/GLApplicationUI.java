package com.ankamagames.baseImpl.graphics.opengl;

import org.apache.log4j.*;
import javax.swing.*;
import com.ankamagames.framework.graphics.opengl.*;
import java.net.*;
import com.ankamagames.baseImpl.graphics.debug.*;
import com.ankamagames.framework.graphics.opengl.events.*;
import com.ankamagames.framework.kernel.utils.*;
import java.awt.event.*;
import com.ankamagames.baseImpl.graphics.opengl.osx.*;
import java.lang.reflect.*;
import java.awt.*;

public abstract class GLApplicationUI
{
    public static final Logger m_logger;
    public static final boolean DEFAULT_DOUBLE_BUFFERED = true;
    private static final int MINIMUM_BPP = 16;
    protected static final int MINIMUM_UI_WIDTH = 800;
    protected static final int MINIMUM_UI_HEIGHT = 600;
    private static final int DEFAULT_RESOLUTION_WIDTH = 1024;
    private static final int DEFAULT_RESOLUTION_HEIGHT = 768;
    private static final int DEFAULT_RESOLUTION_BPP = 32;
    private static final int DEFAULT_RESOLUTION_FREQUENCY = 0;
    private static final ApplicationResolution.Mode DEFAULT_RESOLUTION_MODE;
    public static final GraphicsEnvironment GRAPHICS_ENVIRONMENT;
    private JFrame m_appFrame;
    private GLApplicationUIEventListener m_eventListener;
    private GLAWTWorkspace m_workspace;
    private Renderer m_renderer;
    private String m_title;
    private ApplicationResolution.Mode m_currentMode;
    
    public GLApplicationUI() {
        super();
        this.m_appFrame = null;
        this.m_eventListener = null;
        this.m_title = null;
    }
    
    protected abstract JFrame createApplicationFrame();
    
    protected abstract URL getIconURL();
    
    public abstract void addDebugBar(final DebugBar p0);
    
    public abstract void removeDebugBar();
    
    public void initialize() {
        this.initialize(new GLCaps());
    }
    
    public void initialize(final GLCaps caps) {
        GLApplicationUI.m_logger.info((Object)"Initializing GLApplication UI");
        this.m_workspace = new GLAWTWorkspace(caps);
        GLApplicationUI.m_logger.info((Object)"GLappUI : Workspace created");
        this.m_workspace.setFocusable(true);
        this.m_renderer = new Renderer();
        GLApplicationUI.m_logger.info((Object)"GLappUI : Renderer created");
        this.m_renderer.setEventListener(new GLRendererEventListener() {
            @Override
            public void onRendererInitializationError(final Renderer renderer, final GLRendererEvent event) {
                GLApplicationUI.m_logger.fatal((Object)("Error while initializing Renderer : " + event.toString()));
                if (GLApplicationUI.this.m_eventListener != null) {
                    GLApplicationUI.this.m_eventListener.onUIInitializationError(renderer, event.toString());
                }
                System.exit(0);
            }
        });
        this.m_renderer.setDoubleBuffering(true);
        this.m_workspace.setRenderer(this.m_renderer);
        (this.m_appFrame = this.createApplicationFrame()).addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
            }
        });
        GLApplicationUI.m_logger.info((Object)"GLappUI : Frame created");
        this.m_appFrame.setVisible(false);
        this.m_appFrame.setTitle(this.m_title);
        final URL iconUrl = this.getIconURL();
        if (iconUrl != null) {
            final Image icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
            this.m_appFrame.setIconImage(icon);
            if (OS.isMacOs()) {
                try {
                    final Class appClass = Class.forName("com.apple.eawt.Application");
                    final Method getInstanceMethod = appClass.getMethod("getApplication", (Class[])new Class[0]);
                    final Object appInstance = getInstanceMethod.invoke(null, new Object[0]);
                    final Method setDockIconMethod = appClass.getMethod("setDockIconImage", Image.class);
                    setDockIconMethod.invoke(appInstance, icon);
                }
                catch (Exception e) {
                    GLApplicationUI.m_logger.error((Object)("Exception while trying to set dock icon " + icon + " : "), (Throwable)e);
                }
            }
        }
        this.m_appFrame.setDefaultCloseOperation(2);
        this.m_appFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowIconified(final WindowEvent windowEvent) {
                if (GLApplicationUI.this.m_eventListener != null) {
                    GLApplicationUI.this.m_eventListener.onUIIconified(true);
                }
            }
            
            @Override
            public void windowDeiconified(final WindowEvent windowEvent) {
                if (GLApplicationUI.this.m_eventListener != null) {
                    GLApplicationUI.this.m_eventListener.onUIIconified(false);
                }
            }
            
            @Override
            public void windowClosed(final WindowEvent windowEvent) {
                if (GLApplicationUI.this.m_eventListener != null) {
                    GLApplicationUI.this.m_eventListener.onUIClosed();
                }
                System.exit(0);
            }
            
            @Override
            public void windowDeactivated(final WindowEvent e) {
                if (GLApplicationUI.this.m_eventListener != null) {
                    GLApplicationUI.this.m_eventListener.onUIActivated(false);
                }
            }
            
            @Override
            public void windowActivated(final WindowEvent e) {
                if (GLApplicationUI.this.m_eventListener != null) {
                    GLApplicationUI.this.m_eventListener.onUIActivated(true);
                }
            }
        });
        if (OS.isMacOs()) {
            OSXEventAdapter.registerAdapter(new AppEventAdapter() {
                private ApplicationResolution m_resolution;
                private boolean m_hidden = false;
                
                @Override
                public void appMovedToBackground() {
                    if (GLApplicationUI.this.m_eventListener != null) {
                        GLApplicationUI.this.m_eventListener.onUIActivated(false);
                    }
                }
                
                @Override
                public void appRaisedToForeground() {
                    if (GLApplicationUI.this.m_eventListener != null) {
                        GLApplicationUI.this.m_eventListener.onUIActivated(true);
                    }
                }
                
                @Override
                public void appHidden() {
                    if (this.m_hidden) {
                        return;
                    }
                    this.m_hidden = true;
                    if (GLApplicationUI.this.m_currentMode == ApplicationResolution.Mode.FULLSCREEN) {
                        this.m_resolution = GLApplicationUI.this.getResolution();
                        final GraphicsDevice device = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
                        if (device.getFullScreenWindow() == GLApplicationUI.this.m_appFrame) {
                            device.setFullScreenWindow(null);
                        }
                    }
                    if (GLApplicationUI.this.m_eventListener != null) {
                        GLApplicationUI.this.m_eventListener.onUIIconified(true);
                    }
                }
                
                @Override
                public void appUnHidden() {
                    if (!this.m_hidden) {
                        return;
                    }
                    this.m_hidden = false;
                    if (GLApplicationUI.this.m_currentMode == ApplicationResolution.Mode.FULLSCREEN) {
                        final GraphicsDevice device = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
                        if (device.getFullScreenWindow() == null) {
                            device.setFullScreenWindow(GLApplicationUI.this.m_appFrame);
                        }
                        final DisplayMode bestSuitedMode = getBestSuitedFullscreenDisplayMode(device, this.m_resolution.getWidth(), this.m_resolution.getHeight(), this.m_resolution.getBpp(), this.m_resolution.getFrequency());
                        if (bestSuitedMode != null) {
                            device.setDisplayMode(bestSuitedMode);
                        }
                    }
                    if (GLApplicationUI.this.m_eventListener != null) {
                        GLApplicationUI.this.m_eventListener.onUIIconified(false);
                    }
                }
                
                @Override
                public void appReOpened() {
                }
                
                @Override
                public void screenAboutToSleep() {
                }
                
                @Override
                public void screenAwoke() {
                }
                
                @Override
                public void systemAboutToSleep() {
                }
                
                @Override
                public void systemAwoke() {
                }
                
                @Override
                public void userSessionActived() {
                }
                
                @Override
                public void userSessionDeactivated() {
                }
            });
        }
    }
    
    public void close() {
        if (this.m_eventListener != null) {
            this.m_eventListener.onUIClosing();
        }
        if (this.m_appFrame != null) {
            this.m_appFrame.dispose();
        }
        else {
            System.exit(0);
        }
    }
    
    public GLAWTWorkspace getGLComponent() {
        return this.m_workspace;
    }
    
    public void setGLComponentVisible(final boolean visible) {
        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    GLApplicationUI.this.setGLComponentVisible(visible);
                }
            });
            return;
        }
        if (this.m_workspace != null) {
            this.m_workspace.setVisible(visible);
        }
    }
    
    public void forceGLComponentSizeUpdate() {
        if (!EventQueue.isDispatchThread()) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    GLApplicationUI.this.forceGLComponentSizeUpdate();
                }
            });
            return;
        }
        if (this.m_workspace != null) {
            this.m_workspace.setSize(this.m_workspace.getSize());
        }
    }
    
    public Rectangle getGLComponentRect() {
        if (this.m_workspace != null) {
            return this.m_workspace.getBounds();
        }
        return new Rectangle(0, 0, 0, 0);
    }
    
    public Renderer getRenderer() {
        return this.m_renderer;
    }
    
    public void startGLRendering() {
        this.setVisible(true);
        if (!this.m_workspace.getAnimator().isAnimating()) {
            this.m_workspace.getAnimator().start();
        }
        GLApplicationUI.m_logger.info((Object)"GLappUI : GL Rendering started");
    }
    
    public void setTitle(final String title) {
        this.m_title = title;
        if (this.m_appFrame != null) {
            this.m_appFrame.setTitle(this.m_title);
        }
    }
    
    public void setCursor(final Cursor cursor) {
        this.m_appFrame.setCursor(cursor);
    }
    
    public int getXPosOnScreen() {
        return this.m_appFrame.getX();
    }
    
    public int getYPosOnScreen() {
        return this.m_appFrame.getY();
    }
    
    public Dimension getSize() {
        return this.m_appFrame.getSize();
    }
    
    public int getWidth() {
        return this.m_appFrame.getWidth();
    }
    
    public int getHeight() {
        return this.m_appFrame.getHeight();
    }
    
    public void setVisible(final boolean visible) {
        if (this.m_appFrame == null) {
            return;
        }
        this.m_appFrame.setVisible(visible);
    }
    
    public void setEventListener(final GLApplicationUIEventListener eventListener) {
        this.m_eventListener = eventListener;
    }
    
    protected GLApplicationUIEventListener getEventListener() {
        return this.m_eventListener;
    }
    
    public void applyResolution(ApplicationResolution wantedRes) {
        if (wantedRes.isUndefined()) {
            wantedRes = this.getDefaultResolution();
        }
        GLApplicationUI.m_logger.info((Object)("Applying resolution : " + wantedRes));
        switch (wantedRes.getMode()) {
            case FULLSCREEN: {
                final boolean fullScreenSuccess = this.applyFullScreenResolution(wantedRes.getWidth(), wantedRes.getHeight(), wantedRes.getBpp(), wantedRes.getFrequency());
                if (!fullScreenSuccess) {
                    this.applyWindowedFullscreenResolution();
                    break;
                }
                break;
            }
            case WINDOWED_FULLSCREEN: {
                this.applyWindowedFullscreenResolution();
                break;
            }
            case WINDOWED: {
                this.applyWindowedResolution(wantedRes.getWidth(), wantedRes.getHeight());
                final Rectangle workingRect = this.getCurrentScreenWorkingRect();
                if (!workingRect.contains(this.m_appFrame.getLocation())) {
                    this.m_appFrame.setLocation(workingRect.x, workingRect.y);
                    break;
                }
                break;
            }
        }
        final ApplicationResolution appliedResolution = this.getResolution();
        if (!appliedResolution.equals(wantedRes)) {
            GLApplicationUI.m_logger.info((Object)("Resolution applied : " + appliedResolution));
        }
        if (this.m_eventListener != null) {
            this.m_eventListener.onUIResolutionChanged(appliedResolution);
        }
    }
    
    public ApplicationResolution getResolution() {
        final GraphicsDevice device = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
        final DisplayMode currentDisplayMode = device.getDisplayMode();
        final int resBpp = currentDisplayMode.getBitDepth();
        return new ApplicationResolution(this.getWidth(), this.getHeight(), resBpp, currentDisplayMode.getRefreshRate(), this.m_currentMode);
    }
    
    protected void applyWindowedFullscreenResolution() {
        this.m_currentMode = ApplicationResolution.Mode.WINDOWED_FULLSCREEN;
        final GraphicsDevice device = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
        if (device.getFullScreenWindow() == this.m_appFrame) {
            device.setFullScreenWindow(null);
        }
        if (OS.isMacOs()) {
            try {
                FullscreenUtils.INSTANCE.showMenuAndDock(false);
            }
            catch (Throwable e) {
                GLApplicationUI.m_logger.error((Object)"Impossible de cacher le menu et le doc Mac", e);
            }
        }
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.m_appFrame.setBounds(0, 0, screenSize.width, screenSize.height);
        this.m_appFrame.setExtendedState(6);
        this.m_appFrame.setVisible(true);
        this.m_appFrame.setResizable(false);
    }
    
    protected void applyWindowedResolution(final int resWidth, final int resHeight) {
        this.m_currentMode = ApplicationResolution.Mode.WINDOWED;
        final GraphicsDevice device = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
        if (device.getFullScreenWindow() == this.m_appFrame) {
            device.setFullScreenWindow(null);
        }
        if (OS.isMacOs()) {
            try {
                FullscreenUtils.INSTANCE.showMenuAndDock(true);
            }
            catch (Throwable e) {
                GLApplicationUI.m_logger.error((Object)"Impossible d'afficher le menu et le doc Mac", e);
            }
        }
        final Rectangle screenWorkingSize = this.getCurrentScreenWorkingRect();
        final int appWidth = Math.max(Math.min(resWidth, screenWorkingSize.width), 800);
        final int appHeight = Math.max(Math.min(resHeight, screenWorkingSize.height), 600);
        this.m_appFrame.setSize(appWidth, appHeight);
        if (appWidth == screenWorkingSize.width && appHeight == screenWorkingSize.height) {
            final int restoreAppWidth = Math.min(1024, screenWorkingSize.width);
            final int restoreAppHeight = Math.min(768, screenWorkingSize.height);
            this.m_appFrame.setSize(restoreAppWidth, restoreAppHeight);
            this.m_appFrame.setExtendedState(6);
        }
        else {
            this.m_appFrame.setExtendedState(0);
        }
        this.m_appFrame.setResizable(true);
        this.m_appFrame.setVisible(true);
    }
    
    protected boolean applyFullScreenResolution(final int resWidth, final int resHeight, final int resBpp, final int frequency) {
        if (OS.isMacOs()) {
            try {
                FullscreenUtils.INSTANCE.showMenuAndDock(true);
            }
            catch (Throwable e) {
                GLApplicationUI.m_logger.error((Object)"Impossible d'afficher le menu et le doc Mac", e);
            }
        }
        final GraphicsDevice device = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
        if (!device.isFullScreenSupported()) {
            GLApplicationUI.m_logger.debug((Object)"Fullscreen mode not supported, defaulting to simulated fullscreen");
        }
        final DisplayMode bestSuitedMode = getBestSuitedFullscreenDisplayMode(device, resWidth, resHeight, resBpp, frequency);
        if (bestSuitedMode == null) {
            GLApplicationUI.m_logger.warn((Object)("No available displayMode corresponding to " + resWidth + "x" + resHeight + "x" + resBpp));
            return false;
        }
        if (device.getFullScreenWindow() != this.m_appFrame) {
            device.setFullScreenWindow(this.m_appFrame);
        }
        if (device.getDisplayMode() == bestSuitedMode) {
            this.m_currentMode = ApplicationResolution.Mode.FULLSCREEN;
            return true;
        }
        if (!device.isDisplayChangeSupported()) {
            GLApplicationUI.m_logger.warn((Object)"Unable to change display mode. Defaulting to windowed mode");
            device.setFullScreenWindow(null);
            return false;
        }
        try {
            device.setDisplayMode(bestSuitedMode);
        }
        catch (Exception e2) {
            GLApplicationUI.m_logger.error((Object)("Unable to set mode " + displayModeToString(bestSuitedMode) + ". Defaulting to windowed mode"), (Throwable)e2);
            device.setFullScreenWindow(null);
            return false;
        }
        final DisplayMode mode = device.getDisplayMode();
        this.m_currentMode = ApplicationResolution.Mode.FULLSCREEN;
        this.m_appFrame.setBounds(0, 0, mode.getWidth(), mode.getHeight());
        return true;
    }
    
    public Rectangle getDefaultScreenWorkingRect() {
        final GraphicsDevice device = GLApplicationUI.GRAPHICS_ENVIRONMENT.getDefaultScreenDevice();
        return this.getScreenWorkingRect(device);
    }
    
    public Rectangle getCurrentScreenWorkingRect() {
        if (this.m_appFrame == null) {
            return this.getDefaultScreenWorkingRect();
        }
        final Rectangle frameRect = this.m_appFrame.getBounds();
        GraphicsDevice bestDevice = null;
        int bestSurface = -1;
        for (final GraphicsDevice device : GLApplicationUI.GRAPHICS_ENVIRONMENT.getScreenDevices()) {
            final Rectangle deviceBounds = device.getDefaultConfiguration().getBounds();
            final Rectangle intersection = deviceBounds.intersection(frameRect);
            final int surface = intersection.width * intersection.height;
            if (surface > bestSurface) {
                bestDevice = device;
                bestSurface = surface;
            }
        }
        if (bestDevice != null) {
            return this.getScreenWorkingRect(bestDevice);
        }
        return this.getDefaultScreenWorkingRect();
    }
    
    private Rectangle getScreenWorkingRect(final GraphicsDevice device) {
        final GraphicsConfiguration screenConf = device.getDefaultConfiguration();
        final Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(screenConf);
        final Rectangle bounds;
        final Rectangle screenBounds = bounds = screenConf.getBounds();
        bounds.x += screenInsets.left;
        final Rectangle rectangle = screenBounds;
        rectangle.width -= screenInsets.left + screenInsets.right;
        final Rectangle rectangle2 = screenBounds;
        rectangle2.y += screenInsets.top;
        final Rectangle rectangle3 = screenBounds;
        rectangle3.height -= screenInsets.top + screenInsets.bottom;
        return screenBounds;
    }
    
    private static DisplayMode getBestSuitedFullscreenDisplayMode(final GraphicsDevice device, final int width, final int height, final int bpp, final int frequency) {
        int bitDepth = bpp;
        if (bpp == -1) {
            bitDepth = 32;
        }
        final DisplayMode modeWith32bpp = _getBestSuitedFullscreenDisplayMode(device, width, height, bitDepth, frequency);
        if (modeWith32bpp != null) {
            return modeWith32bpp;
        }
        return _getBestSuitedFullscreenDisplayMode(device, width, height, device.getDisplayMode().getBitDepth(), frequency);
    }
    
    private static DisplayMode _getBestSuitedFullscreenDisplayMode(final GraphicsDevice device, final int width, final int height, final int bpp, final int frequency) {
        final boolean frequencyFixed = frequency != 0;
        DisplayMode bestSuited = null;
        for (final DisplayMode mode : device.getDisplayModes()) {
            int modeBitDepth = mode.getBitDepth();
            if (modeBitDepth == -1) {
                modeBitDepth = bpp;
            }
            if (mode.getWidth() == width && mode.getHeight() == height) {
                if (modeBitDepth == bpp) {
                    if (!frequencyFixed || frequency == mode.getRefreshRate()) {
                        if (bestSuited == null) {
                            bestSuited = mode;
                        }
                        else {
                            final int previousRate = bestSuited.getRefreshRate();
                            final int thisRate = mode.getRefreshRate();
                            if (thisRate >= 50 && thisRate < previousRate) {
                                bestSuited = mode;
                            }
                        }
                    }
                }
            }
        }
        return bestSuited;
    }
    
    public ApplicationResolution getDefaultResolution() {
        return new ApplicationResolution(1024, 768, 32, 0, GLApplicationUI.DEFAULT_RESOLUTION_MODE);
    }
    
    public Dimension getMinimumSize() {
        return new Dimension(800, 600);
    }
    
    public int getMinimumBpp() {
        return 16;
    }
    
    public JFrame getAppFrame() {
        return this.m_appFrame;
    }
    
    private static String displayModeToString(final DisplayMode mode) {
        return "{Mode " + mode.getWidth() + 'x' + mode.getHeight() + 'x' + mode.getBitDepth() + ' ' + mode.getRefreshRate() + "Hz}";
    }
    
    static {
        m_logger = Logger.getLogger((Class)GLApplicationUI.class);
        DEFAULT_RESOLUTION_MODE = ApplicationResolution.Mode.WINDOWED;
        GRAPHICS_ENVIRONMENT = GraphicsEnvironment.getLocalGraphicsEnvironment();
    }
}
