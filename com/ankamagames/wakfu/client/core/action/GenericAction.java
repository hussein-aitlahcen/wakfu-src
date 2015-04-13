package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.script.action.*;

public class GenericAction extends Action
{
    private final Runnable m_runnable;
    
    public GenericAction(final Runnable runnable) {
        super(0, 0, 0);
        this.m_runnable = runnable;
    }
    
    @Override
    public void run() {
        this.m_runnable.run();
        this.fireActionFinishedEvent();
    }
    
    @Override
    protected void onActionFinished() {
    }
}
