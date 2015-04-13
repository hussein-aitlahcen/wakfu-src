package com.ankamagames.wakfu.client.core.webBrowser;

import org.apache.log4j.*;
import org.eclipse.swt.awt.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.framework.fileFormat.properties.*;
import org.eclipse.swt.layout.*;
import com.ankamagames.wakfu.client.ui.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.graphics.*;
import javax.swing.*;
import java.awt.*;

public class SWFBrowser
{
    private static final Logger m_logger;
    private BrowserEventListener m_listener;
    private Browser m_browser;
    private Shell m_shell;
    private Composite m_toolBar;
    private SendCommandBrowserFunction m_sendCommandFunction;
    private BrowserEventHandler m_handler;
    private SWTFrame m_swtFrame;
    private SWTLoading m_loadingImage;
    
    public Shell initBrowser(final Display display, final Canvas canvas) {
        (this.m_shell = SWT_AWT.new_Shell(display, canvas)).setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        final GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = 0;
        layout.verticalSpacing = 0;
        layout.marginBottom = 0;
        layout.marginLeft = 0;
        layout.marginRight = 0;
        layout.marginTop = 0;
        this.m_shell.setLayout((Layout)layout);
        SWTApplicationSkin skin = null;
        try {
            final String skinPath = WakfuConfiguration.getInstance().getString("appSkinPath");
            skin = new SWTApplicationSkin(skinPath);
        }
        catch (PropertyException e) {
            SWFBrowser.m_logger.warn((Object)e.getMessage());
        }
        (this.m_swtFrame = new SWTFrame((Composite)this.m_shell, 0, skin)).setLayoutData((Object)new GridData(1808));
        final Composite browserParent = new Composite((Composite)this.m_swtFrame, 0);
        browserParent.setLayout((Layout)new SWTStaticLayout());
        (this.m_loadingImage = new SWTLoading(browserParent, 0)).initialize();
        this.m_loadingImage.setLayoutData((Object)new SWTStaticLayoutData(1.0f, 1.0f, (byte)1));
        this.m_loadingImage.setVisible(false);
        final Control browserComposite = this.createBrowserComposite(browserParent);
        browserComposite.setLayoutData((Object)new SWTStaticLayoutData(1.0f, 1.0f, (byte)1));
        this.m_swtFrame.setContent((Control)browserParent);
        this.m_swtFrame.getCloseButton().addListener(13, (Listener)new Listener() {
            public void handleEvent(final Event event) {
                SWFBrowser.this.m_listener.onClose();
            }
        });
        this.m_shell.open();
        return this.m_shell;
    }
    
    private Control createBrowserComposite(final Composite parent) {
        final GridLayout layout2 = new GridLayout();
        layout2.marginWidth = 0;
        layout2.marginHeight = 0;
        layout2.horizontalSpacing = 0;
        layout2.verticalSpacing = 0;
        layout2.marginBottom = 0;
        layout2.marginLeft = 0;
        layout2.marginRight = 0;
        layout2.marginTop = 0;
        final Composite composite = new Composite(parent, 0);
        composite.setLayout((Layout)layout2);
        (this.m_toolBar = new Composite(composite, 8519680)).setLayout((Layout)new GridLayout(5, false));
        this.m_toolBar.setLayoutData((Object)new GridData(768));
        final Button buttonBack = new Button(this.m_toolBar, 0);
        buttonBack.setText("<=");
        buttonBack.addListener(13, (Listener)new Listener() {
            public void handleEvent(final Event event) {
                SWFBrowser.this.m_browser.back();
            }
        });
        final Button buttonForward = new Button(this.m_toolBar, 0);
        buttonForward.setText("=>");
        buttonBack.addListener(13, (Listener)new Listener() {
            public void handleEvent(final Event event) {
                SWFBrowser.this.m_browser.forward();
            }
        });
        final Label labelAddress = new Label(this.m_toolBar, 0);
        labelAddress.setText("Address");
        final Text textLocation = new Text(this.m_toolBar, 2052);
        textLocation.setLayoutData((Object)new GridData(768));
        final Button buttonGo = new Button(this.m_toolBar, 0);
        buttonGo.setText("Go");
        Browser.clearSessions();
        (this.m_browser = new Browser(composite, 32768)).setLayoutData((Object)new GridData(1808));
        final Listener openURLListener = (Listener)new Listener() {
            public void handleEvent(final Event event) {
                SWFBrowser.this.m_browser.setUrl(textLocation.getText());
            }
        };
        buttonGo.addListener(13, openURLListener);
        textLocation.addListener(14, openURLListener);
        this.m_toolBar.pack();
        this.m_browser.addLocationListener((LocationListener)new LocationAdapter() {
            public void changed(final LocationEvent event) {
                textLocation.setText(event.location);
            }
        });
        this.m_browser.addProgressListener((ProgressListener)new ProgressAdapter() {
            public void completed(final ProgressEvent progressEvent) {
                if (SWFBrowser.this.m_handler != null) {
                    SWFBrowser.this.m_handler.onLoad();
                }
                SWFBrowser.this.m_loadingImage.setVisible(false);
                SWFBrowser.this.m_loadingImage.stop();
            }
        });
        return (Control)composite;
    }
    
    public void setListener(final BrowserEventListener listener) {
        this.m_listener = listener;
    }
    
    public void onResize(final int width, final int height) {
        this.m_shell.setBounds(0, 0, width, height);
        this.m_shell.layout(true, true);
    }
    
    public Browser getBrowser() {
        return this.m_browser;
    }
    
    public void setHandler(final BrowserEventHandler handler) {
        this.m_handler = handler;
    }
    
    public void prepareBrowser() {
        if (this.m_handler == null) {
            throw new IllegalStateException("Impossible de pr\u00e9parer le browser sans handler");
        }
        if (this.m_sendCommandFunction != null) {
            this.disposeFunction();
        }
        this.m_sendCommandFunction = new SendCommandBrowserFunction(this.m_browser);
        this.m_shell.setBackground(new Color((Device)Display.getCurrent(), 0, 0, 0));
        this.m_browser.setBackground(new Color((Device)Display.getCurrent(), 0, 0, 0));
        this.m_handler.initialize();
    }
    
    public void clean() {
        if (this.m_handler != null) {
            this.m_handler.clean();
            this.m_handler = null;
        }
        this.setUrl(null);
    }
    
    public void setToolbarVisible(final boolean visible) {
        final GridData data = (GridData)this.m_toolBar.getLayoutData();
        data.exclude = !visible;
        this.m_toolBar.setVisible(visible);
        this.m_shell.layout();
    }
    
    private void disposeFunction() {
        this.m_sendCommandFunction.dispose();
        this.m_sendCommandFunction = null;
    }
    
    public void loadUrl() {
        if (this.m_handler == null) {
            SWFBrowser.m_logger.warn((Object)"Impossible de charger l'url, le handler est null !");
            return;
        }
        this.setUrl(this.m_handler.getUrl());
    }
    
    public boolean setUrl(final String url) {
        if (url == null) {
            return this.m_browser.setUrl("about:blank");
        }
        this.m_loadingImage.setVisible(true);
        this.m_loadingImage.start();
        return this.m_browser.setUrl(url);
    }
    
    public void showDecorations(final boolean show) {
        this.m_swtFrame.showDecorations(show);
        this.m_shell.layout(true, true);
    }
    
    public boolean invokeFunction(final String functionName, final Object[] params) {
        return this.m_handler == null || this.m_handler.invokeFunction(functionName, params);
    }
    
    public void start() {
        this.m_handler.start(this);
    }
    
    public static void main(final String[] args) {
        WakfuSWT.initDisplay();
        WakfuSWT.runAsync(new Runnable() {
            @Override
            public void run() {
                System.setProperty("sun.awt.xembedserver", "true");
                System.setProperty("org.eclipse.swt.browser.XULRunnerPath", "/Users/jderveeuw/dev/wakfu_trunk/libraries/natives/macosx/universal/xulrunner");
                System.setProperty("MOZ_PLUGIN_PATH", "/Users/jderveeuw/dev/wakfu_trunk/libraries/natives/macosx/universal/xulrunner/plugins");
                final Canvas canvas = new Canvas();
                canvas.setBackground(java.awt.Color.RED);
                canvas.setPreferredSize(new Dimension(800, 600));
                final JPanel panel = new JPanel(new BorderLayout());
                panel.add(canvas, "Center");
                panel.add(new java.awt.Button("################# TEST ###################"), "North");
                final JFrame frame = new JFrame("My SWT Browser");
                frame.setDefaultCloseOperation(2);
                frame.setContentPane(panel);
                frame.pack();
                frame.setVisible(true);
                final SWFBrowser swfBrowser = new SWFBrowser();
                swfBrowser.initBrowser(Display.getCurrent(), canvas);
                swfBrowser.setUrl("http://arena-game.ankama.lan/arena/dev/arena-client/index-wakfu.html");
                swfBrowser.getBrowser().addProgressListener((ProgressListener)new ProgressListener() {
                    public void changed(final ProgressEvent progressEvent) {
                    }
                    
                    public void completed(final ProgressEvent progressEvent) {
                        Display.getCurrent().wake();
                    }
                });
            }
        });
        WakfuSWT.runEventPump();
    }
    
    static {
        m_logger = Logger.getLogger((Class)SWFBrowser.class);
    }
}
