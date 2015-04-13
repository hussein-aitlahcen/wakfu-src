package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import java.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class HpLossAndPuppetHeal extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return HpLossAndPuppetHeal.PARAMETERS_LIST_SET;
    }
    
    public HpLossAndPuppetHeal() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public HpLossAndPuppetHeal newInstance() {
        HpLossAndPuppetHeal re;
        try {
            re = (HpLossAndPuppetHeal)HpLossAndPuppetHeal.m_staticPool.borrowObject();
            re.m_pool = HpLossAndPuppetHeal.m_staticPool;
        }
        catch (Exception e) {
            re = new HpLossAndPuppetHeal();
            re.m_pool = null;
            re.m_isStatic = false;
            HpLossAndPuppetHeal.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpLossAndPuppetHeal : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.RANDOM);
        }
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified(true);
        if (this.m_target == null) {
            return;
        }
        Elements element = this.getElement();
        if (element == null) {
            element = this.getSpellElement();
        }
        final HPLoss hpLoss = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, element, HPLoss.ComputeMode.CLASSIC, this.m_value, this.m_target);
        ((RunningEffect<DefaultEffect, EC>)hpLoss).setGenericEffect(DefaultEffect.getInstance());
        hpLoss.setCaster(this.m_caster);
        hpLoss.computeModificator(hpLoss.defaultCondition(), this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).checkFlags(1L), this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).isAffectedByLocalisation());
        final int hpLossValue = Math.min(this.m_target.getCharacteristicValue(FighterCharacteristicType.HP), hpLoss.getValue());
        hpLoss.askForExecution();
        final List<EffectUser> puppetsToApplyHpGain = this.getPuppetsToApplyHpGain();
        if (puppetsToApplyHpGain.isEmpty()) {
            return;
        }
        final int hpGainPerPuppet = hpLossValue / puppetsToApplyHpGain.size();
        for (final EffectUser puppet : puppetsToApplyHpGain) {
            final HPGain hpGain = HPGain.checkOut((EffectContext<WakfuEffect>)this.m_context, element);
            hpGain.setTarget(puppet);
            hpGain.forceValue(hpGainPerPuppet);
            hpGain.setParent(this);
            hpGain.setCaster(this.m_caster);
            ((RunningEffect<WakfuEffect, EC>)hpGain).setGenericEffect(((RunningEffect<WakfuEffect, EC>)this).getGenericEffect());
            hpGain.askForExecution();
        }
    }
    
    private List<EffectUser> getPuppetsToApplyHpGain() {
        final Iterator<EffectUser> targets = this.m_context.getTargetInformationProvider().getAllPossibleTargets();
        final List<EffectUser> puppetToApplyHpGain = new ArrayList<EffectUser>();
        while (targets.hasNext()) {
            final EffectUser target = targets.next();
            if (!target.isActiveProperty(FightPropertyType.IS_SADIDA_PUPPET)) {
                continue;
            }
            if (!(target instanceof BasicCharacterInfo)) {
                continue;
            }
            final BasicCharacterInfo targetInfo = (BasicCharacterInfo)target;
            if (targetInfo.getController() != this.m_caster) {
                continue;
            }
            puppetToApplyHpGain.add(target);
        }
        return puppetToApplyHpGain;
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<HpLossAndPuppetHeal>() {
            @Override
            public HpLossAndPuppetHeal makeObject() {
                return new HpLossAndPuppetHeal();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Valeur de la perte de Pdv", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur perte de PdV", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
