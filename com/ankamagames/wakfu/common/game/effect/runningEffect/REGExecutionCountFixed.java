package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class REGExecutionCountFixed extends REGExecutionCountParameterized
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return REGExecutionCountFixed.PARAMETERS_LIST_SET;
    }
    
    public REGExecutionCountFixed() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public REGExecutionCountFixed newInstance() {
        REGExecutionCountFixed re;
        try {
            re = (REGExecutionCountFixed)REGExecutionCountFixed.m_staticPool.borrowObject();
            re.m_pool = REGExecutionCountFixed.m_staticPool;
        }
        catch (Exception e) {
            re = new REGExecutionCountFixed();
            re.m_pool = null;
            re.m_isStatic = false;
            REGExecutionCountFixed.m_logger.error((Object)("Erreur lors d'un checkOut sur un REGExecutionCountFunctionCharac : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected int computeExecutionsCountParameter(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null || ((WakfuEffect)this.m_genericEffect).getParamsCount() < 1) {
            return 0;
        }
        return ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected boolean computeShouldStopOnEffectNotExecuted(final RunningEffect triggerRE) {
        return this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2 && ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<REGExecutionCountFixed>() {
            @Override
            public REGExecutionCountFixed makeObject() {
                return new REGExecutionCountFixed();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Nombre d'ex\u00e9cutions fixe", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nombre d'executions", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Nombre d'ex\u00e9cutions fixe, avec condition d'arr\u00eat", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Nombre d'executions", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Doit s'arr\u00eater quand un effet n'est pas ex\u00e9cut\u00e9 (1=oui, 0=non,d\u00e9faut)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
