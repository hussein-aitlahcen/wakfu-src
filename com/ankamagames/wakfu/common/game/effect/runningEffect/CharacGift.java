package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class CharacGift extends CharacBuff
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_swapCasterAndTarget;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacGift.PARAMETERS_LIST_SET;
    }
    
    private CharacGift() {
        super();
        this.m_swapCasterAndTarget = false;
    }
    
    public CharacGift(final FighterCharacteristicType charac) {
        super(charac);
        this.m_swapCasterAndTarget = false;
        this.setTriggersToExecute();
    }
    
    @Override
    public CharacGift newInstance() {
        CharacGift re;
        try {
            re = (CharacGift)CharacGift.m_staticPool.borrowObject();
            re.m_pool = CharacGift.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacGift();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacGift.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacGift : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        re.m_swapCasterAndTarget = this.m_swapCasterAndTarget;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target != null && this.m_target.hasCharacteristic(this.m_charac) && this.m_caster != null && this.m_caster.hasCharacteristic(this.m_charac)) {
            if (this.m_swapCasterAndTarget) {
                final EffectUser tmp = this.m_caster;
                this.m_caster = this.m_target;
                this.m_target = tmp;
            }
            final int currentValue = this.m_caster.getCharacteristicValue(this.m_charac);
            if (this.m_value == -1) {
                this.m_value = currentValue;
            }
            else {
                this.m_value = Math.min(this.m_value, currentValue);
            }
            super.executeOverride(linkedRE, trigger);
            final AbstractCharacteristic characteristic = this.m_caster.getCharacteristic(this.m_charac);
            characteristic.add(-this.m_value);
            characteristic.updateMaxValue(-this.m_value);
        }
        else {
            this.setNotified(true);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_addCurrentValue = true;
        this.m_substractCurrentValue = true;
        this.m_swapCasterAndTarget = false;
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
            if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 2) {
                this.m_swapCasterAndTarget = (((WakfuEffect)this.m_genericEffect).getParam(1) == 1.0f);
            }
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_executed && this.m_caster != null && this.m_caster.hasCharacteristic(this.m_charac) && this.m_target.hasCharacteristic(this.m_charac)) {
            this.m_caster.getCharacteristic(this.m_charac).updateMaxValue(this.m_value);
        }
        super.unapplyOverride();
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
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacGift>() {
            @Override
            public CharacGift makeObject() {
                return new CharacGift((CharacGift$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Don de charac (debuff pour soi buff pour l'autre)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (-1 = tout)", WakfuRunningEffectParameterType.VALUE) }), new WakfuRunningEffectParameterList("Don de charac (buff pour soi , debuff pour l'autre)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (-1 = tout)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Swap target et caster (1 = oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
