package com.ankamagames.wakfu.client;

import org.apache.log4j.*;
import java.util.regex.*;
import javax.swing.*;
import com.ankamagames.wakfu.common.constants.*;
import java.util.*;
import com.ankamagames.baseImpl.graphics.opengl.*;
import java.awt.*;
import org.eclipse.swt.widgets.*;
import java.awt.event.*;
import com.ankamagames.framework.fileFormat.properties.*;
import com.ankamagames.wakfu.client.core.webBrowser.*;
import com.ankamagames.wakfu.client.core.krosmoz.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.kernel.events.*;
import com.ankamagames.wakfu.client.sound.*;

public class SWFWrapper implements ContainerListener
{
    private static final Logger m_logger;
    private static final Pattern RESOLUTION_PATTERN;
    public static final int IDLE_STATE = 0;
    public static final int LOADING = 1;
    public static final int LOADED = 2;
    public static final SWFWrapper INSTANCE;
    private JPanel m_contentPanel;
    private StaticLayoutData2 m_layoutData;
    private Canvas m_canvas;
    private SWFBrowser m_browser;
    private boolean m_opened;
    private KrosmozGame m_game;
    private KrosmozGameFrame m_frame;
    private int m_state;
    private final ArrayList<ContainerListener> m_delegateContainerListeners;
    
    private SWFWrapper() {
        super();
        this.m_opened = false;
        this.m_delegateContainerListeners = new ArrayList<ContainerListener>();
    }
    
    public void init() {
        if (!WakfuSWT.isXulRunnerPathSet()) {
            try {
                WakfuSWT.setXulRunnerPath();
            }
            catch (Exception e) {
                SWFWrapper.m_logger.error((Object)"Impossible d'initialiser XulRunner !", (Throwable)e);
                return;
            }
        }
        final SkinnedGLApplicationUI appUI = (SkinnedGLApplicationUI)WakfuClientInstance.getInstance().getAppUI();
        while (!appUI.isInit()) {
            Thread.yield();
        }
        final JPanel panel = appUI.getGlComponentPanel();
        if (panel == null) {
            return;
        }
        (this.m_contentPanel = new JPanel()).setLayout(new StaticLayout());
        this.m_contentPanel.setOpaque(true);
        this.m_contentPanel.setBackground(Color.BLACK);
        this.m_contentPanel.setVisible(false);
        panel.add(this.m_contentPanel, 0);
        this.createContentPanel();
        panel.validate();
        this.m_state = 0;
    }
    
    private void setComponentToFullScreen() {
        this.m_layoutData.setRelative(true);
        this.m_contentPanel.validate();
    }
    
    public void setComponentToSize(final int x, final int y, final int width, final int height) {
        this.m_layoutData.setX(x);
        this.m_layoutData.setY(y);
        this.m_layoutData.setWidth(width);
        this.m_layoutData.setHeight(height);
        this.m_layoutData.setRelative(false);
        this.m_contentPanel.validate();
    }
    
    private void createContentPanel() {
        (this.m_canvas = new Canvas()).setBackground(Color.BLACK);
        this.m_layoutData = new StaticLayoutData2(1.0f, 1.0f, (byte)1, 0, 0, 0, 0);
        this.m_contentPanel.add(this.m_canvas, this.m_layoutData);
        while (!this.m_canvas.isDisplayable()) {
            Thread.yield();
        }
        (this.m_browser = new SWFBrowser()).initBrowser(Display.getCurrent(), this.m_canvas);
        this.m_browser.setListener(new BrowserEventListener() {
            @Override
            public void onClose() {
                SWFWrapper.this.unload();
            }
        });
        this.m_canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                WakfuSWT.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        SWFWrapper.this.m_browser.onResize(SWFWrapper.this.m_canvas.getWidth(), SWFWrapper.this.m_canvas.getHeight());
                    }
                });
            }
        });
        this.m_contentPanel.validate();
    }
    
    public int getState() {
        return this.m_state;
    }
    
    private BrowserEventHandler createHandler(final KrosmozGame game) {
        String gameUrl = null;
        try {
            gameUrl = WakfuConfiguration.getInstance().getString("krosmoz.gameUrl." + this.m_game.getId());
        }
        catch (PropertyException e) {
            SWFWrapper.m_logger.warn((Object)e.getMessage());
        }
        switch (game) {
            case KROSMASTER_ARENA: {
                return new KrosmasterArenaBrowserEventHandler(this.m_browser, gameUrl, "getFlashPlayerObject()");
            }
            case BROWSER:
            case FULL_SCREEN_BROWSER:
            case FULL_SCREEN_BROWSER_WITHOUT_CONTROLS: {
                return new DefaultBrowserEventHandler();
            }
            case SHOP: {
                return new ShopBrowserHandler(gameUrl);
            }
            default: {
                return new DefaultBrowserEventHandler();
            }
        }
    }
    
    private void load() {
        if (this.m_opened) {
            return;
        }
        final BrowserEventHandler handler = this.createHandler(this.m_game);
        if (handler == null) {
            SWFWrapper.m_logger.warn((Object)("Impossible de cr\u00e9er un handler pour " + this.m_game));
            return;
        }
        this.m_state = 1;
        this.m_browser.setHandler(handler);
        WakfuSWT.runAsync(new Runnable() {
            @Override
            public void run() {
                SWFWrapper.this.m_browser.showDecorations(SWFWrapper.this.m_game.isDisplayDecorations());
                if (SWFWrapper.this.m_game.isFullScreen()) {
                    SWFWrapper.this.setComponentToFullScreen();
                }
                else {
                    SWFWrapper.this.setComponentToSize(0, 0, SWFWrapper.this.m_game.getSize().width, SWFWrapper.this.m_game.getSize().height);
                }
                SWFWrapper.this.m_browser.prepareBrowser();
                SWFWrapper.this.m_browser.setToolbarVisible(SWFWrapper.this.m_game.isDisplayBrowserControls());
                SWFWrapper.this.m_frame = KrosmozGameCommandInterfaceFactory.INSTANCE.getGameFrame(SWFWrapper.this.m_game, handler);
                SWFWrapper.this.m_frame.setSwfWrapper(SWFWrapper.this);
                WakfuGameEntity.getInstance().pushFrame(SWFWrapper.this.m_frame);
                SWFWrapper.this.m_browser.start();
            }
        });
        this.m_opened = true;
    }
    
    public void unload() {
        if (!this.m_opened) {
            return;
        }
        this.m_state = 0;
        this.m_contentPanel.setVisible(false);
        WakfuSWT.runAsync(new Runnable() {
            @Override
            public void run() {
                SWFWrapper.this.m_browser.clean();
            }
        });
        this.toggleSound(true);
        this.m_opened = false;
        final SkinnedGLApplicationUI appUI = (SkinnedGLApplicationUI)WakfuClientInstance.getInstance().getAppUI();
        appUI.setGLComponentVisible(true);
        WakfuGameEntity.getInstance().removeFrame(this.m_frame);
        this.m_frame = null;
        this.m_game = null;
        this.dispatchUnloadEvent();
    }
    
    public void addDelegateContainerListener(final ContainerListener containerListener) {
        if (!this.m_delegateContainerListeners.contains(containerListener)) {
            this.m_delegateContainerListeners.add(containerListener);
        }
    }
    
    public void removeDelegateContainerListener(final ContainerListener containerListener) {
        this.m_delegateContainerListeners.remove(containerListener);
    }
    
    public void dispatchUnloadEvent() {
        for (int i = this.m_delegateContainerListeners.size() - 1; i >= 0; --i) {
            this.m_delegateContainerListeners.get(i).onClose();
        }
    }
    
    public void toggleDisplay(final KrosmozGame game) {
        if (this.m_opened) {
            this.unload();
        }
        else {
            this.load(game);
        }
    }
    
    private void load(final KrosmozGame game) {
        this.m_game = game;
        this.load();
    }
    
    public boolean isOpened() {
        return this.m_opened;
    }
    
    public SWFBrowser getBrowser() {
        return this.m_browser;
    }
    
    public void invokeFunction(final String command, final Object[] params) {
        WakfuSWT.runAsync(new Runnable() {
            @Override
            public void run() {
                SWFWrapper.this.m_browser.invokeFunction(command, params);
            }
        });
    }
    
    public void displayComponent() {
        if (this.m_state != 1) {
            return;
        }
        this.m_state = 2;
        this.m_contentPanel.setVisible(true);
        this.m_contentPanel.validate();
        this.toggleSound(false);
        if (this.m_game.isFullScreen()) {
            final WakfuApplicationUI appUI = (WakfuApplicationUI)WakfuClientInstance.getInstance().getAppUI();
            appUI.setGLComponentVisible(false);
        }
    }
    
    private void toggleSound(final boolean enable) {
        final float value = enable ? 1.0f : 0.0f;
        WakfuSoundManager.getInstance().fadeGUI(value, 1000);
        WakfuSoundManager.getInstance().fadeAmbiance(value, 1000);
        WakfuSoundManager.getInstance().fadeMusic(value, 1000);
    }
    
    @Override
    public void onClose() {
        SWFWrapper.m_logger.error((Object)"close");
        this.unload();
    }
    
    static {
        m_logger = Logger.getLogger((Class)SWFWrapper.class);
        RESOLUTION_PATTERN = Pattern.compile("([0-9]+)x([0-9]+)");
        INSTANCE = new SWFWrapper();
    }
}
