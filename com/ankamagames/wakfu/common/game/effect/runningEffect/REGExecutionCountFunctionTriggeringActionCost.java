package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class REGExecutionCountFunctionTriggeringActionCost extends REGExecutionCountParameterized
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return REGExecutionCountFunctionTriggeringActionCost.PARAMETERS_LIST_SET;
    }
    
    public REGExecutionCountFunctionTriggeringActionCost() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public REGExecutionCountFunctionTriggeringActionCost newInstance() {
        REGExecutionCountFunctionTriggeringActionCost re;
        try {
            re = (REGExecutionCountFunctionTriggeringActionCost)REGExecutionCountFunctionTriggeringActionCost.m_staticPool.borrowObject();
            re.m_pool = REGExecutionCountFunctionTriggeringActionCost.m_staticPool;
        }
        catch (Exception e) {
            re = new REGExecutionCountFunctionTriggeringActionCost();
            re.m_pool = null;
            re.m_isStatic = false;
            REGExecutionCountFunctionTriggeringActionCost.m_logger.error((Object)("Erreur lors d'un checkOut sur un REGExecutionCountFunctionCharac : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected int computeExecutionsCountParameter(final RunningEffect triggerRE) {
        if (triggerRE == null) {
            REGExecutionCountFunctionTriggeringActionCost.m_logger.error((Object)"Unable to execute a REGExecutionCountFunctionTriggeringActionCost without triggering effect");
            return 0;
        }
        if (!(triggerRE instanceof ActionCost)) {
            REGExecutionCountFunctionTriggeringActionCost.m_logger.error((Object)"Unable to execute a REGExecutionCountFunctionTriggeringActionCost without actionCost triggering effect");
            return 0;
        }
        final float executionsPerPA = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel());
        final float executionsPerPM = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel());
        final float executionsPerPW = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel());
        final ActionCost actionCost = (ActionCost)triggerRE;
        final int apCost = actionCost.getApUseFromValue();
        final int mpCost = actionCost.getMpUseFromValue();
        final int wpCost = actionCost.getWpUseFromValue();
        int executionsCount = Math.round(apCost * executionsPerPA + mpCost * executionsPerPM + wpCost * executionsPerPW);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4) {
            executionsCount = Math.min(executionsCount, ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
        }
        return executionsCount;
    }
    
    @Override
    protected boolean computeShouldStopOnEffectNotExecuted(final RunningEffect triggerRE) {
        return false;
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<REGExecutionCountFunctionTriggeringActionCost>() {
            @Override
            public REGExecutionCountFunctionTriggeringActionCost makeObject() {
                return new REGExecutionCountFunctionTriggeringActionCost();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Nombre d'ex\u00e9cution en fonction des PA/PM/PW utilis\u00e9s", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Execution par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Execution par PM", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Execution par PW", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Nombre d'ex\u00e9cution en fonction des PA/PM/PW utilis\u00e9s, avec max", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Execution par PA", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Execution par PM", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Execution par PW", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Nombre d'executions max", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
