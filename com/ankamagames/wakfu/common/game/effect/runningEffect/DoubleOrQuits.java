package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class DoubleOrQuits extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private Elements m_element;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return DoubleOrQuits.PARAMETERS_LIST_SET;
    }
    
    public DoubleOrQuits() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public DoubleOrQuits newInstance() {
        DoubleOrQuits re;
        try {
            re = (DoubleOrQuits)DoubleOrQuits.m_staticPool.borrowObject();
            re.m_pool = DoubleOrQuits.m_staticPool;
        }
        catch (Exception e) {
            re = new DoubleOrQuits();
            re.m_pool = null;
            re.m_isStatic = false;
            DoubleOrQuits.m_logger.error((Object)("Erreur lors d'un checkOut sur un DoubleOrQuits : " + e.getMessage()));
        }
        re.m_element = this.m_element;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.setNotified();
        if (triggerRE == null) {
            DoubleOrQuits.m_logger.error((Object)"Ne peut \u00eatre ex\u00e9cut\u00e9 que via trigger");
            return;
        }
        if (triggerRE.getGenericEffect() == null) {
            DoubleOrQuits.m_logger.error((Object)"Generic effect inconnu, on ne peut pas faire les test n\u00e9cessaires pour executer l'effet");
            return;
        }
        final BitSet additionalTriggers = triggerRE.getGenericEffect().getExecutionTriggersAdditionnal();
        if (additionalTriggers == null) {
            DoubleOrQuits.m_logger.error((Object)"Trigger propres du declencheurs inconnus, on ne peut pas faire les test n\u00e9cessaires pour executer l'effet");
            return;
        }
        if (additionalTriggers.get(2136)) {
            this.activate();
        }
        else {
            this.record(triggerRE);
        }
    }
    
    private void activate() {
        if (this.m_value <= 0 || this.m_element == null) {
            return;
        }
        final boolean dealDamage = DiceRoll.roll(2) == 1;
        if (dealDamage) {
            int bonus = 0;
            if (this.m_caster.hasCharacteristic(FighterCharacteristicType.ECAFLIP_DOUBLE_OR_QUITS_BUFF)) {
                bonus = this.m_caster.getCharacteristicValue(FighterCharacteristicType.ECAFLIP_DOUBLE_OR_QUITS_BUFF);
            }
            final int value = this.m_value * (100 + bonus) / 100;
            final HPLoss damage = HPLoss.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_element, HPLoss.ComputeMode.CLASSIC, value, this.m_target);
            damage.setCaster(this.m_caster);
            ((RunningEffect<DefaultFightInstantEffectWithChatNotif, EC>)damage).setGenericEffect(DefaultFightInstantEffectWithChatNotif.getInstance());
            damage.askForExecution();
            this.m_value *= 2;
            if (this.getParent() != null) {
                this.getParent().forceValue(this.m_value);
            }
        }
        else {
            final HPGain heal = HPGain.checkOut((EffectContext<WakfuEffect>)this.m_context, this.m_element);
            heal.setTarget(this.m_target);
            heal.setCaster(this.m_caster);
            heal.forceValue(this.m_value);
            ((RunningEffect<DefaultFightInstantEffectWithChatNotif, EC>)heal).setGenericEffect(DefaultFightInstantEffectWithChatNotif.getInstance());
            heal.askForExecution();
            this.getParent().getManagerWhereIamStored().removeEffect(this.getParent());
        }
    }
    
    @Override
    public void onApplication() {
        final WakfuEffectExecutionParameters params = (WakfuEffectExecutionParameters)this.getParams();
        if (params != null) {
            this.record(params.getExternalTriggeringEffect());
        }
        super.onApplication();
    }
    
    private void record(final RunningEffect triggerRE) {
        if (triggerRE == null) {
            DoubleOrQuits.m_logger.error((Object)"Pas d'effet declencheur pour enregistrer la valeur");
            return;
        }
        this.m_value = triggerRE.getValue();
        if (this.getParent() != null) {
            this.getParent().forceValue(this.m_value);
        }
        this.m_element = ((WakfuRunningEffect)triggerRE).getElement();
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
    
    @Override
    public void onCheckIn() {
        this.m_element = null;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<DoubleOrQuits>() {
            @Override
            public DoubleOrQuits makeObject() {
                return new DoubleOrQuits();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]) });
    }
}
