package com.ankamagames.framework.script.action;

import com.ankamagames.framework.kernel.core.common.message.scheduler.clock.*;
import com.ankamagames.framework.kernel.core.common.message.*;

public abstract class TimedAction extends Action implements MessageHandler
{
    private static int uid;
    private static final int MAX_REASONNABLE_DURATION_IN_MS_FOR_ONE_ACTION = 30000;
    protected long m_clockId;
    
    public static int getNextUid() {
        if (TimedAction.uid == Integer.MAX_VALUE) {
            TimedAction.uid = 0;
        }
        else {
            ++TimedAction.uid;
        }
        return TimedAction.uid;
    }
    
    public TimedAction(final int uniqueId, final int actionType, final int actionId) {
        super(uniqueId, actionType, actionId);
    }
    
    @Override
    public final void run() {
        long runTime = this.onRun();
        if (runTime > 30000L) {
            TimedAction.m_logger.error((Object)("Attention ! Une action " + this.getClass().getSimpleName() + " dure plus d'une minute : " + runTime + " ms \u00e7a parait long, il y a peut etre un probleme"));
            runTime = 0L;
        }
        if (runTime == 0L) {
            this.fireActionFinishedEvent();
        }
        else if (runTime > 0L) {
            this.m_clockId = MessageScheduler.getInstance().addClock(this, runTime, -1, 1);
        }
    }
    
    protected abstract long onRun();
    
    @Override
    public boolean onMessage(final Message message) {
        if (message.getId() == Integer.MIN_VALUE) {
            this.fireActionFinishedEvent();
            return false;
        }
        return true;
    }
    
    @Override
    public long getId() {
        return -1L;
    }
    
    @Override
    public void setId(final long id) {
    }
}
