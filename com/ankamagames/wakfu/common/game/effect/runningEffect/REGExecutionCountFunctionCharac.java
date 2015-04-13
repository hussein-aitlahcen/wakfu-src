package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class REGExecutionCountFunctionCharac extends REGExecutionCountParameterized
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private FighterCharacteristicType m_charac;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return REGExecutionCountFunctionCharac.PARAMETERS_LIST_SET;
    }
    
    public REGExecutionCountFunctionCharac() {
        super();
        this.setTriggersToExecute();
    }
    
    public REGExecutionCountFunctionCharac(final FighterCharacteristicType charac) {
        super();
        this.m_charac = charac;
    }
    
    @Override
    public REGExecutionCountFunctionCharac newInstance() {
        REGExecutionCountFunctionCharac re;
        try {
            re = (REGExecutionCountFunctionCharac)REGExecutionCountFunctionCharac.m_staticPool.borrowObject();
            re.m_pool = REGExecutionCountFunctionCharac.m_staticPool;
        }
        catch (Exception e) {
            re = new REGExecutionCountFunctionCharac();
            re.m_pool = null;
            re.m_isStatic = false;
            REGExecutionCountFunctionCharac.m_logger.error((Object)("Erreur lors d'un checkOut sur un REGExecutionCountFunctionCharac : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    protected int computeExecutionsCountParameter(final RunningEffect triggerRE) {
        if (this.m_caster == null || !this.m_caster.hasCharacteristic(this.m_charac)) {
            return 0;
        }
        return this.m_caster.getCharacteristicValue(this.m_charac);
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
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<REGExecutionCountFunctionCharac>() {
            @Override
            public REGExecutionCountFunctionCharac makeObject() {
                return new REGExecutionCountFunctionCharac();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
