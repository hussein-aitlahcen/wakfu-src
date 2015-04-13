package com.ankamagames.wakfu.client.ui.progress;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.graphics.ui.progress.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.wakfu.client.ui.*;
import com.ankamagames.wakfu.client.sound.*;

public class WakfuProgressMonitorManager extends AbstractProgressMonitorManager
{
    private static final Logger m_logger;
    private static WakfuProgressMonitorManager m_instance;
    private boolean m_isRunning;
    
    public WakfuProgressMonitorManager() {
        super();
        this.m_isRunning = false;
    }
    
    public static WakfuProgressMonitorManager getInstance() {
        return WakfuProgressMonitorManager.m_instance;
    }
    
    @Override
    protected ProgressMonitor createProgressMonitor() {
        return new WakfuProgressMonitor();
    }
    
    @Override
    protected void showProgressMonitor(final ProgressMonitor monitor) {
        if (!this.m_isRunning) {
            this.m_isRunning = true;
            Xulor.getInstance().load("progressDialog", Dialogs.getDialogPath("progressDialog"), 8448L, (short)19500);
            WakfuSoundManager.getInstance().startLoading();
        }
    }
    
    @Override
    protected void hideProgressMonitor(final ProgressMonitor monitor) {
        if (this.m_isRunning) {
            this.m_isRunning = false;
            Xulor.getInstance().unload("progressDialog");
            WakfuSoundManager.getInstance().stopLoading();
        }
    }
    
    public boolean isRunning() {
        return this.m_isRunning;
    }
    
    static {
        m_logger = Logger.getLogger((Class)WakfuProgressMonitorManager.class);
        WakfuProgressMonitorManager.m_instance = new WakfuProgressMonitorManager();
    }
}
