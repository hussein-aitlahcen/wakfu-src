package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.item.loot.*;
import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.game.item.referenceItem.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossFunctionLoot extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_ratio;
    private boolean m_improvedDrop;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossFunctionLoot.PARAMETERS_LIST_SET;
    }
    
    public HpLossFunctionLoot() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HpLossFunctionLoot newInstance() {
        HpLossFunctionLoot re;
        try {
            re = (HpLossFunctionLoot)HpLossFunctionLoot.m_staticPool.borrowObject();
            re.m_pool = HpLossFunctionLoot.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossFunctionLoot();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossFunctionLoot.m_logger.error((Object)("Erreur lors d'un checkOut sur un DropFromLootArea : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 1) {
            return;
        }
        this.m_ratio = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() < 2) {
            this.m_improvedDrop = false;
            return;
        }
        this.m_improvedDrop = (((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (!this.isValueComputationEnabled()) {
            return;
        }
        if (!(this.m_context instanceof WakfuFightEffectContext)) {
            this.setNotified();
            return;
        }
        if (!(this.m_caster instanceof Looter) || !(this.m_target instanceof Dropper)) {
            this.setNotified();
            return;
        }
        final AbstractFight fight = ((WakfuFightEffectContext)this.m_context).getFight();
        this.m_value = fight.dropEnutrofPurse((Dropper)this.m_target, (Looter)this.m_caster, false, this.m_improvedDrop);
        if (this.m_value == 0) {
            this.setNotified();
            return;
        }
        this.notifyExecution(triggerRE, trigger);
        final AbstractReferenceItem referenceItem = ReferenceItemManager.getInstance().getReferenceItem(this.m_value);
        final short itemLevel = referenceItem.getLevel();
        final int hpLossValue = this.m_ratio * itemLevel;
        final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, Elements.WATER, HPLoss.ComputeMode.CLASSIC, hpLossValue, null);
        hpLoss.disableValueComputation();
        ((RunningEffect<WakfuEffect, EC>)hpLoss).setGenericEffect(((RunningEffect<WakfuEffect, EC>)this).getGenericEffect());
        hpLoss.setCaster(this.m_caster);
        final EffectExecutionResult effectExecutionResult = hpLoss.run(((RunningEffect<WakfuEffect, EC>)this).getGenericEffect(), (WakfuEffectContainer)this.m_effectContainer, this.m_context, this.m_caster, this.m_targetCell.getX(), this.m_targetCell.getY(), this.m_targetCell.getZ(), null, this.getParams());
        if (effectExecutionResult != null) {
            effectExecutionResult.release();
        }
        hpLoss.release();
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
    public void onCheckIn() {
        this.m_ratio = 0;
        this.m_improvedDrop = false;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossFunctionLoot>() {
            @Override
            public HpLossFunctionLoot makeObject() {
                return new HpLossFunctionLoot();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Ratio dgts/lvl", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Ratio degats/level", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Drop am\u00e9liorer", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Ratio degats/level", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Drop Am\u00e9lior\u00e9 (1 = oui, 0 = non (defaut)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
