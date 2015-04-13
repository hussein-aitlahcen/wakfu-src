package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ApplyStateLevelCapedByAnotherState extends ApplyState
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ApplyStateLevelCapedByAnotherState.PARAMETERS_LIST_SET;
    }
    
    public ApplyStateLevelCapedByAnotherState() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ApplyStateLevelCapedByAnotherState newInstance() {
        ApplyStateLevelCapedByAnotherState re;
        try {
            re = (ApplyStateLevelCapedByAnotherState)ApplyStateLevelCapedByAnotherState.m_staticPool.borrowObject();
            re.m_pool = ApplyStateLevelCapedByAnotherState.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyStateLevelCapedByAnotherState();
            re.m_pool = null;
            re.m_isStatic = false;
            ApplyStateLevelCapedByAnotherState.m_logger.error((Object)("Erreur lors d'un checkOut sur un ApplyStateLevelCapedByAnotherState : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected int getApplyPercent() {
        return ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void extractStateLevel(final short level) {
        this.m_stateLevel = (short)((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final boolean checkOnTarget = ((WakfuEffect)this.m_genericEffect).getParam(4, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1;
        final EffectUser userWithCapingState = checkOnTarget ? this.m_target : this.m_caster;
        if (!(userWithCapingState instanceof CriterionUser)) {
            this.m_stateLevel = 0;
            return;
        }
        final int capingStateId = ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        final int capingStateLevel = ((CriterionUser)userWithCapingState).getStateLevel(capingStateId);
        if (capingStateLevel <= 0) {
            this.m_stateLevel = 0;
            return;
        }
        final State stateToApply = StateManager.getInstance().getState(this.m_stateId);
        if (stateToApply == null) {
            this.m_stateLevel = 0;
            return;
        }
        if (stateToApply.isCumulable()) {
            if (!(this.m_target instanceof CriterionUser)) {
                this.m_stateLevel = 0;
                return;
            }
            final int currentLevel = Math.max(0, ((CriterionUser)this.m_target).getStateLevel(this.m_stateId));
            final short finalLevel = (short)Math.min(this.m_stateLevel + currentLevel, capingStateLevel);
            this.m_stateLevel = (short)Math.max(0, finalLevel - currentLevel);
        }
        else {
            this.m_stateLevel = (short)Math.min(this.m_stateLevel, capingStateLevel);
        }
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ApplyStateLevelCapedByAnotherState>() {
            @Override
            public ApplyStateLevelCapedByAnotherState makeObject() {
                return new ApplyStateLevelCapedByAnotherState();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("apply avec nouveau niveau cap\u00e9 par niveau d'un autre \u00e9tat", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("pourcentage d'application / AREA_HP", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("stateId dont il ne faut pas d\u00e9passer le lvl", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("etat capant sur la cible (0=non (defaut), 1=oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
