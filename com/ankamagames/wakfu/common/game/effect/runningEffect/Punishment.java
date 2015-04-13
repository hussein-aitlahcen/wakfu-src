package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class Punishment extends WakfuRunningEffect
{
    public static final ParameterListSet PARAMETERS_LIST_SET;
    private static final ObjectPool m_staticPool;
    private int m_baseDamage;
    private byte m_threshold;
    private float m_bonusRatio;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Punishment.PARAMETERS_LIST_SET;
    }
    
    public Punishment() {
        super();
    }
    
    Punishment(final int baseDamage, final float bonusRatio, final byte threshold) {
        super();
        this.m_baseDamage = baseDamage;
        this.m_bonusRatio = bonusRatio;
        this.m_threshold = threshold;
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        Punishment re;
        try {
            re = (Punishment)Punishment.m_staticPool.borrowObject();
            re.m_pool = Punishment.m_staticPool;
        }
        catch (Exception e) {
            re = new Punishment();
            re.m_pool = null;
            Punishment.m_logger.error((Object)("Erreur lors d'un newInstance sur un HPLoss : " + e.getMessage()));
        }
        re.m_baseDamage = this.m_baseDamage;
        re.m_threshold = this.m_threshold;
        re.m_bonusRatio = this.m_bonusRatio;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_caster == null || this.m_target == null) {
            return;
        }
        if (!this.m_caster.hasCharacteristic(FighterCharacteristicType.HP)) {
            return;
        }
        if (!this.m_target.hasCharacteristic(FighterCharacteristicType.HP)) {
            return;
        }
        this.extractParameters();
        final AbstractCharacteristic casterHpCharac = this.m_caster.getCharacteristic(FighterCharacteristicType.HP);
        final int casterHpPercent = casterHpCharac.value() * 100 / casterHpCharac.max();
        this.setDamageValue(casterHpPercent);
    }
    
    void setDamageValue(final int casterHpPercent) {
        this.m_value = this.m_baseDamage;
        if (casterHpPercent >= this.m_threshold) {
            return;
        }
        final int percentDiff = this.m_threshold - casterHpPercent;
        final int bonusDamageInPercent = (int)(percentDiff * this.m_bonusRatio);
        this.m_value += this.m_value * bonusDamageInPercent / 100;
    }
    
    private void extractParameters() {
        final short containerLevel = this.getContainerLevel();
        this.m_baseDamage = ((WakfuEffect)this.m_genericEffect).getParam(0, containerLevel, RoundingMethod.RANDOM);
        this.m_threshold = (byte)((WakfuEffect)this.m_genericEffect).getParam(1, containerLevel, RoundingMethod.RANDOM);
        this.m_bonusRatio = ((WakfuEffect)this.m_genericEffect).getParam(2, containerLevel);
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        final HPLoss hpLoss = HPLoss.checkOut(this.getContext(), Elements.FIRE, HPLoss.ComputeMode.CLASSIC, this.m_value, this.m_target);
        hpLoss.setCaster(this.m_caster);
        hpLoss.computeModificator(hpLoss.defaultCondition(), this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).checkFlags(1L), this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).isAffectedByLocalisation());
        ((RunningEffect<WakfuEffect, EC>)hpLoss).setGenericEffect(((RunningEffect<WakfuEffect, EC>)this).getGenericEffect());
        hpLoss.execute(null, false);
        this.setNotified(true);
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2);
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
    protected boolean canBeExecutedOnKO() {
        return true;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Punition", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Dmg de base", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Seuil (en % de vie restant)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Ratio (combien de % de dommages sup pour chaque % de vie en moins, en dessous du seuil", WakfuRunningEffectParameterType.VALUE) }) });
        m_staticPool = new MonitoredPool(new ObjectFactory<Punishment>() {
            @Override
            public Punishment makeObject() {
                return new Punishment();
            }
        });
    }
}
