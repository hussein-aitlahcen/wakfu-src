package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class VariableEffect extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return VariableEffect.PARAMETERS_LIST_SET;
    }
    
    @Override
    public VariableEffect newInstance() {
        return null;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        throw new UnsupportedOperationException("Ce Running Effect ne s'execute pas");
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        throw new UnsupportedOperationException("Ce Running Effect ne s'execute pas");
    }
    
    @Override
    public boolean useCaster() {
        throw new UnsupportedOperationException("Ce Running Effect ne s'execute pas");
    }
    
    @Override
    public boolean useTarget() {
        throw new UnsupportedOperationException("Ce Running Effect ne s'execute pas");
    }
    
    @Override
    public boolean useTargetCell() {
        throw new UnsupportedOperationException("Ce Running Effect ne s'execute pas");
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[0]);
    }
}
