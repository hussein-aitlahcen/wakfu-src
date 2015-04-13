package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ExecuteActionCost extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_apToConsume;
    private int m_mpToConsume;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ExecuteActionCost.PARAMETERS_LIST_SET;
    }
    
    public ExecuteActionCost() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public ExecuteActionCost newInstance() {
        ExecuteActionCost re;
        try {
            re = (ExecuteActionCost)ExecuteActionCost.m_staticPool.borrowObject();
            re.m_pool = ExecuteActionCost.m_staticPool;
        }
        catch (Exception e) {
            re = new ExecuteActionCost();
            re.m_pool = null;
            re.m_isStatic = false;
            ExecuteActionCost.m_logger.error((Object)("Erreur lors d'un checkOut sur un ExecuteActionCost : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_apToConsume = this.m_caster.getCharacteristicValue(FighterCharacteristicType.AP);
        this.m_mpToConsume = this.m_caster.getCharacteristicValue(FighterCharacteristicType.MP);
        if (this.m_apToConsume == 0 && this.m_mpToConsume == 0) {
            return;
        }
        final int maxAPToConsume = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (maxAPToConsume >= 0) {
            this.m_apToConsume = Math.min(this.m_apToConsume, maxAPToConsume);
        }
        final int maxMPToConsume = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (maxMPToConsume >= 0) {
            this.m_mpToConsume = Math.min(this.m_mpToConsume, maxMPToConsume);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (this.m_caster == null) {
            return;
        }
        final ActionCost actionCost = ActionCost.checkOut((EffectContext<WakfuEffect>)this.m_context, new SpellCost((byte)this.m_apToConsume, (byte)this.m_mpToConsume, (byte)0), this.m_caster);
        actionCost.setCaster(this.m_caster);
        actionCost.setRunningEffectStatus(RunningEffectStatus.NEUTRAL);
        actionCost.execute(null, false);
    }
    
    @Override
    public boolean dontTriggerAnything() {
        return true;
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
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ExecuteActionCost>() {
            @Override
            public ExecuteActionCost makeObject() {
                return new ExecuteActionCost();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("PA et Pm a consommer", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("PA a consommer (-1 = tout)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("PM a consommer (-1 = tout)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
