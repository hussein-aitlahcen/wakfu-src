package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class ApplyStateFunctionActionCost extends ApplyStateFunctionPaPmPw
{
    private static final ObjectPool m_staticPool;
    
    public ApplyStateFunctionActionCost() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ApplyStateFunctionActionCost newInstance() {
        ApplyStateFunctionActionCost re;
        try {
            re = (ApplyStateFunctionActionCost)ApplyStateFunctionActionCost.m_staticPool.borrowObject();
            re.m_pool = ApplyStateFunctionActionCost.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyStateFunctionActionCost();
            re.m_pool = null;
            re.m_isStatic = false;
            ApplyStateFunctionActionCost.m_logger.error((Object)("Erreur lors d'un checkOut sur un ApplyStateFunctionActionCost : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified(true);
        if (triggerRE == null || !(triggerRE instanceof ActionCost)) {
            ApplyStateFunctionActionCost.m_logger.error((Object)"On ne peut pas utiliser cet effet avec autre chose qu'un d\u00e9clenchement li\u00e9 \u00e0 un ActionCost");
            return;
        }
        final ActionCost actionCost = (ActionCost)triggerRE;
        final byte ap = actionCost.getApUseFromValue();
        final byte mp = actionCost.getMpUseFromValue();
        final byte wp = actionCost.getWpUseFromValue();
        final short stateLevel = (short)(ap * this.m_stateLevelPerPA + mp * this.m_stateLevelPerPM + wp * this.m_stateLevelPerPW);
        final ApplyState applyState = ApplyState.checkout((EffectContext<WakfuEffect>)this.m_context, this.m_target, this.m_stateId, stateLevel, false);
        applyState.setCaster(this.m_caster);
        ((RunningEffect<FX, WakfuEffectContainer>)applyState).setEffectContainer((WakfuEffectContainer)this.m_effectContainer);
        ((RunningEffect<WakfuEffect, EC>)applyState).setGenericEffect((WakfuEffect)this.m_genericEffect);
        applyState.bypassResistancesCheck();
        applyState.execute(triggerRE, false);
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
        return false;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ApplyStateFunctionActionCost>() {
            @Override
            public ApplyStateFunctionActionCost makeObject() {
                return new ApplyStateFunctionActionCost();
            }
        });
    }
}
