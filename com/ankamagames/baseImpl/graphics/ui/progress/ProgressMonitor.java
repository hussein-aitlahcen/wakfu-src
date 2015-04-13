package com.ankamagames.baseImpl.graphics.ui.progress;

public interface ProgressMonitor
{
    void beginTask(String p0, int p1);
    
    void done();
    
    void setTaskName(String p0);
    
    void subTask(String p0);
    
    void worked(int p0);
    
    int getTotalWork();
}
