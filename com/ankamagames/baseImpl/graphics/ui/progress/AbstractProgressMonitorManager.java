package com.ankamagames.baseImpl.graphics.ui.progress;

public abstract class AbstractProgressMonitorManager
{
    public static final int DEFAULT_SHOW_DELAY = 200;
    private long m_showDelay;
    private long m_firstCallTimer;
    private ProgressMonitor m_monitor;
    
    public AbstractProgressMonitorManager() {
        super();
        this.m_showDelay = 200L;
        this.m_firstCallTimer = 0L;
    }
    
    public ProgressMonitor getProgressMonitor() {
        return this.getProgressMonitor(false);
    }
    
    public ProgressMonitor getProgressMonitor(final boolean forceShow) {
        if (this.m_monitor == null) {
            this.m_monitor = this.createProgressMonitor();
        }
        if (forceShow || (this.m_firstCallTimer != 0L && System.currentTimeMillis() - this.m_firstCallTimer > this.m_showDelay)) {
            this.showProgressMonitor(this.m_monitor);
        }
        if (this.m_firstCallTimer == 0L) {
            this.m_firstCallTimer = System.currentTimeMillis();
        }
        return this.m_monitor;
    }
    
    public void done() {
        this.m_firstCallTimer = 0L;
        if (this.m_monitor != null) {
            this.m_monitor.done();
            this.m_monitor.setTaskName("");
            this.m_monitor.subTask("");
            this.hideProgressMonitor(this.m_monitor);
        }
    }
    
    public void setShowDelay(final long showDelay) {
        this.m_showDelay = showDelay;
    }
    
    protected abstract ProgressMonitor createProgressMonitor();
    
    protected abstract void showProgressMonitor(final ProgressMonitor p0);
    
    protected abstract void hideProgressMonitor(final ProgressMonitor p0);
    
    public long getFirstCallTimer() {
        return this.m_firstCallTimer;
    }
}
