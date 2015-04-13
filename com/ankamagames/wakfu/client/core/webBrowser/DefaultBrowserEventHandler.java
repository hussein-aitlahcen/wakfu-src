package com.ankamagames.wakfu.client.core.webBrowser;

import com.ankamagames.wakfu.client.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.apache.log4j.*;
import org.eclipse.swt.browser.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class DefaultBrowserEventHandler implements BrowserEventHandler
{
    @Override
    public void initialize() {
    }
    
    @Override
    public void clean() {
    }
    
    @Override
    public String getUrl() {
        return "www.google.com";
    }
    
    @Override
    public void start(final SWFBrowser browser) {
        browser.loadUrl();
        SWFWrapper.INSTANCE.displayComponent();
        browser.getBrowser().addOpenWindowListener((OpenWindowListener)new SetInnerBrowser());
    }
    
    @Override
    public boolean invokeFunction(final String functionName, final Object... params) {
        return true;
    }
    
    @Override
    public void onLoad() {
    }
    
    private static class SetInnerBrowser implements OpenWindowListener
    {
        private final Browser m_browser;
        
        SetInnerBrowser() {
            super();
            final Shell shell = new Shell();
            shell.setText("Help Window");
            shell.setLayout((Layout)new FillLayout());
            (this.m_browser = new Browser((Composite)shell, 32768)).addLocationListener((LocationListener)new OpenNativeBrowser());
        }
        
        public void open(final WindowEvent windowEvent) {
            windowEvent.browser = this.m_browser;
        }
        
        @Override
        public String toString() {
            return "SetInnerBrowser{m_browser=" + this.m_browser + '}';
        }
    }
    
    private static class OpenNativeBrowser extends LocationAdapter
    {
        private static final Logger LOG;
        
        public void changed(final LocationEvent locationEvent) {
            try {
                Desktop.getDesktop().browse(new URI(locationEvent.location));
            }
            catch (IOException e) {
                OpenNativeBrowser.LOG.error((Object)String.format("Unable to brose to %s", locationEvent.location), (Throwable)e);
            }
            catch (URISyntaxException e2) {
                OpenNativeBrowser.LOG.error((Object)String.format("Bad URL: %s", locationEvent.location), (Throwable)e2);
            }
        }
        
        static {
            LOG = Logger.getLogger((Class)OpenNativeBrowser.class);
        }
    }
}
