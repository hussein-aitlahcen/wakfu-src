package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class DividedHpLossUnlessThreshold extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_threshold;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return DividedHpLossUnlessThreshold.PARAMETERS_LIST_SET;
    }
    
    public DividedHpLossUnlessThreshold() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public DividedHpLossUnlessThreshold newInstance() {
        DividedHpLossUnlessThreshold re;
        try {
            re = (DividedHpLossUnlessThreshold)DividedHpLossUnlessThreshold.m_staticPool.borrowObject();
            re.m_pool = DividedHpLossUnlessThreshold.m_staticPool;
        }
        catch (Exception e) {
            re = new DividedHpLossUnlessThreshold();
            re.m_pool = null;
            re.m_isStatic = false;
            DividedHpLossUnlessThreshold.m_logger.error((Object)("Erreur lors d'un checkOut sur un DividedHpLossUnlessThreshold : " + e.getMessage()));
        }
        re.m_threshold = this.m_threshold;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_threshold = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        this.setNotified();
        final AbstractEffectGroup effectGroup = (AbstractEffectGroup)AbstractEffectGroupManager.getInstance().getEffectGroup(this.getEffectId());
        if (effectGroup == null || effectGroup.getEffectsCount() == 0) {
            DividedHpLossUnlessThreshold.m_logger.error((Object)("Il faut un sous-effet de type HpLoss " + this.getEffectId()));
            return;
        }
        final WakfuEffect effect = effectGroup.getEffect(0);
        final List<EffectUser> targets = RunningEffectUtils.getTargets(this, false);
        final int forcedBaseValue = this.computeForcedBaseValue(targets);
        this.executeEffect(effect, targets, forcedBaseValue);
    }
    
    private void executeEffect(final WakfuEffect effect, final List<EffectUser> targets, final int forcedBaseValue) {
        final WakfuEffectExecutionParameters params = WakfuEffectExecutionParameters.checkOut(true, false, null);
        params.addListener(new WakfuRunningEffectListener() {
            @Override
            public void onAfterExecution(final WakfuRunningEffect effect) {
            }
            
            @Override
            public void valueComputed(final WakfuRunningEffect effect) {
                if (!(effect instanceof HPLoss)) {
                    return;
                }
                final HPLoss hpLoss = (HPLoss)effect;
                hpLoss.forceValue(forcedBaseValue);
                hpLoss.computeModificator(hpLoss.m_condition);
            }
        });
        try {
            for (final EffectUser target : targets) {
                effect.execute(this.getEffectContainer(), this.m_caster, this.getContext(), RunningEffectConstants.getInstance(), target.getWorldCellX(), target.getWorldCellY(), target.getWorldCellAltitude(), target, params, false);
            }
        }
        catch (Exception e) {
            DividedHpLossUnlessThreshold.m_logger.error((Object)"Exception levee", (Throwable)e);
        }
        finally {
            params.release();
        }
    }
    
    private int computeForcedBaseValue(final List<EffectUser> targets) {
        int forcedBaseValue;
        if (targets.size() >= this.m_threshold) {
            forcedBaseValue = this.m_value;
        }
        else {
            forcedBaseValue = this.m_value / targets.size();
        }
        return forcedBaseValue;
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
        this.m_threshold = 0;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<DividedHpLossUnlessThreshold>() {
            @Override
            public DividedHpLossUnlessThreshold makeObject() {
                return new DividedHpLossUnlessThreshold();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Divise les d\u00e9g\u00e2ts en dessous d'un certain nombre de cible", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur de base de la perte de pdv", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Seuil", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
