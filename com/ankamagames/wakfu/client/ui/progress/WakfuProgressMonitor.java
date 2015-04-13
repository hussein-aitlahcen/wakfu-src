package com.ankamagames.wakfu.client.ui.progress;

import com.ankamagames.baseImpl.graphics.ui.progress.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.wakfu.client.core.*;

public class WakfuProgressMonitor implements ProgressMonitor
{
    private static final String PROGRESS_TASK_NAME = "progress.task.name";
    private static final String PROGRESS_SUBTASK_NAME = "progress.subtask.name";
    private static final String PROGRESS_VALUE = "progress.value";
    private int m_totalWork;
    
    public WakfuProgressMonitor() {
        super();
        this.m_totalWork = 1;
    }
    
    @Override
    public void beginTask(final String name, final int totalWork) {
        this.m_totalWork = totalWork;
        PropertiesProvider.getInstance().setPropertyValue("progress.task.name", name);
        PropertiesProvider.getInstance().setPropertyValue("progress.value", 0.0);
    }
    
    @Override
    public void done() {
    }
    
    @Override
    public void setTaskName(String name) {
        if (name == null) {
            name = WakfuTranslator.getInstance().getString("loading");
        }
        PropertiesProvider.getInstance().setPropertyValue("progress.task.name", name);
    }
    
    @Override
    public void subTask(String name) {
        if (name == null) {
            name = "";
        }
        PropertiesProvider.getInstance().setPropertyValue("progress.subtask.name", name);
    }
    
    @Override
    public void worked(final int work) {
        if (this.m_totalWork != 0) {
            PropertiesProvider.getInstance().setPropertyValue("progress.value", work / this.m_totalWork);
        }
    }
    
    @Override
    public int getTotalWork() {
        return this.m_totalWork;
    }
    
    public void setTotalWork(final int totalWork) {
        this.m_totalWork = totalWork;
    }
}
