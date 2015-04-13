package com.ankamagames.framework.kernel.core.common.message.scheduler.process;

class SchedulerOperation
{
    private final SchedulerOperationType m_op;
    private final ScheduledProcess m_process;
    private final Runnable m_runnable;
    
    SchedulerOperation(final SchedulerOperationType op, final ScheduledProcess process, final Runnable runnable) {
        super();
        this.m_op = op;
        this.m_process = process;
        this.m_runnable = runnable;
    }
    
    public SchedulerOperationType getOp() {
        return this.m_op;
    }
    
    public ScheduledProcess getProcess() {
        return this.m_process;
    }
    
    public Runnable getRunnable() {
        return this.m_runnable;
    }
}
