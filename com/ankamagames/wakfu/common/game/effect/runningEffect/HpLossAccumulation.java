package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossAccumulation extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_damageAbsorption;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossAccumulation.PARAMETERS_LIST_SET;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (this.getParent() == null) {
            return;
        }
        if (triggerRE instanceof HPLoss) {
            final int hpLoss = triggerRE.getValue();
            final int hpLossAbsorbed = hpLoss * this.m_damageAbsorption / 100;
            this.m_value = hpLossAbsorbed;
            final int previousValue = this.getParent().getValue();
            final int forcedValue = previousValue + hpLossAbsorbed;
            this.getParent().forceValue(forcedValue);
            triggerRE.forceValue(hpLoss - hpLossAbsorbed);
        }
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        HpLossAccumulation re;
        try {
            re = (HpLossAccumulation)HpLossAccumulation.m_staticPool.borrowObject();
            re.m_pool = HpLossAccumulation.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossAccumulation();
            re.m_pool = null;
            HpLossAccumulation.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossAccumulation : " + e.getMessage()));
        }
        re.m_damageAbsorption = this.m_damageAbsorption;
        return re;
    }
    
    @Override
    public boolean useCaster() {
        return false;
    }
    
    @Override
    public boolean useTarget() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return false;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 0) {
            this.m_damageAbsorption = 100;
        }
        else {
            this.m_damageAbsorption = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.isValueComputationEnabled() && this.m_value > 0) {
            if (this.getParent() != null) {
                return;
            }
            if (this.getContext() instanceof WakfuFightEffectContext) {
                final WakfuFightEffectContext context = (WakfuFightEffectContext)this.getContext();
                final boolean fightEnding = context.getFight().getStatus() == AbstractFight.FightStatus.DESTRUCTION;
                if (fightEnding) {
                    return;
                }
            }
            final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(this.getEffectId());
            if (effectGroup == null || effectGroup.getEffectsCount() == 0) {
                HpLossAccumulation.m_logger.error((Object)("Pas de sous effet pour l'accumulation de perte de pdv " + this.getEffectId()));
                super.unapplyOverride();
                return;
            }
            final WakfuEffect effect = effectGroup.getEffect(0);
            final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(true, false, null);
            params.setForcedValue(this.m_value);
            try {
                effect.execute(this.getEffectContainer(), this.m_caster, this.getContext(), RunningEffectConstants.getInstance(), this.m_target.getWorldCellX(), this.m_target.getWorldCellY(), this.m_target.getWorldCellAltitude(), this.m_target, params, false);
            }
            catch (Exception e) {
                HpLossAccumulation.m_logger.error((Object)"Exception levee", (Throwable)e);
            }
            params.release();
        }
        super.unapplyOverride();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossAccumulation>() {
            @Override
            public HpLossAccumulation makeObject() {
                return new HpLossAccumulation();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Pas de params", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("% de D\u00e9gats absorb\u00e9", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("% absorb\u00e9", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
