package com.ankamagames.wakfu.common.game.effect.runningEffect;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;

public abstract class REGExecutionCountParameterized extends UsingEffectGroupRunningEffect
{
    private static final int MAX_EXECUTIONS_COUNT = 12;
    public static final Logger m_logger;
    private int m_executionsCount;
    private boolean m_shouldStopOnEffectNotExecuted;
    
    public REGExecutionCountParameterized() {
        super();
        this.m_executionsCount = 1;
        this.m_shouldStopOnEffectNotExecuted = false;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        for (int i = 0; i < this.m_executionsCount; ++i) {
            this.executeEffectGroup((WakfuRunningEffect)triggerRE);
            if (this.m_shouldStopOnEffectNotExecuted && this.getEffectsWithoutExecutions() > 0) {
                break;
            }
        }
    }
    
    @Override
    public final void effectiveComputeValue(final RunningEffect triggerRE) {
        super.effectiveComputeValue(triggerRE);
        this.m_executionsCount = Math.min(12, this.computeExecutionsCountParameter(triggerRE));
        this.m_shouldStopOnEffectNotExecuted = this.computeShouldStopOnEffectNotExecuted(triggerRE);
    }
    
    protected abstract int computeExecutionsCountParameter(final RunningEffect p0);
    
    protected abstract boolean computeShouldStopOnEffectNotExecuted(final RunningEffect p0);
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_executionsCount = 1;
    }
    
    @Override
    protected boolean isProbabilityComputationDisabled() {
        return false;
    }
    
    static {
        m_logger = Logger.getLogger((Class)REGExecutionCountParameterized.class);
    }
}
