package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class CharacSet extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private CharacteristicType m_charac;
    private int m_valueDiff;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacSet.PARAMETERS_LIST_SET;
    }
    
    private CharacSet() {
        super();
        this.m_valueDiff = 0;
    }
    
    public CharacSet(final CharacteristicType charac) {
        super();
        this.m_valueDiff = 0;
        this.m_charac = charac;
        this.setTriggersToExecute();
    }
    
    @Override
    public CharacSet newInstance() {
        CharacSet re;
        try {
            re = (CharacSet)CharacSet.m_staticPool.borrowObject();
            re.m_pool = CharacSet.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacSet();
            re.m_pool = null;
            CharacSet.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacGain : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    public CharacteristicType getCharacteristicType() {
        return this.m_charac;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        switch (this.m_charac.getCharacteristicType()) {
            case 0: {
                this.setFighterCharac();
                break;
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() > 0) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
        }
        else {
            this.m_value = this.getCaster().getCharacteristicValue(this.m_charac);
        }
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
    public void unapplyOverride() {
        if (this.m_executed && this.hasDuration()) {
            switch (this.m_charac.getCharacteristicType()) {
                case 0: {
                    this.unsetFighterCharac();
                    break;
                }
            }
        }
        super.unapplyOverride();
    }
    
    private void setFighterCharac() {
        if (this.m_target != null && this.m_target.hasCharacteristic(this.m_charac)) {
            final int valueBefore = this.getCaster().getCharacteristicValue(this.m_charac);
            final AbstractCharacteristic targetCharacteristic = this.m_target.getCharacteristic(this.m_charac);
            this.setCharacToValue(targetCharacteristic);
            if (this.m_charac == FighterCharacteristicType.INIT && this.m_context.getTimeline() != null) {
                this.m_context.getTimeline().updateDynamicOrder();
            }
            final int modification = this.getCaster().getCharacteristicValue(this.m_charac) - valueBefore;
            if (this.m_charac instanceof FighterCharacteristicType) {
                final FighterCharacteristicType fighterCharacteristicType = (FighterCharacteristicType)this.m_charac;
                if (fighterCharacteristicType.hasGainTrigger() && modification > 0) {
                    this.m_triggers.set(fighterCharacteristicType.getGainTrigger());
                }
                if (fighterCharacteristicType.hasLossTrigger() && modification < 0) {
                    this.m_triggers.set(fighterCharacteristicType.getLossTrigger());
                }
            }
        }
        else {
            this.setNotified(true);
        }
    }
    
    private void unsetFighterCharac() {
        if (this.m_target != null && this.m_target.hasCharacteristic(this.m_charac)) {
            this.rollbackToOriginalValue(this.m_target.getCharacteristic(this.m_charac));
            if (this.m_charac == FighterCharacteristicType.INIT && this.m_context != null && this.m_context.getTimeline() != null) {
                this.m_context.getTimeline().updateDynamicOrder();
            }
        }
    }
    
    private void setCharacToValue(final AbstractCharacteristic charac) {
        final int value = charac.value();
        charac.set(this.m_value);
        this.m_value = charac.value();
        this.m_valueDiff = this.m_value - value;
    }
    
    private void rollbackToOriginalValue(final AbstractCharacteristic charac) {
        final int value = charac.value() - this.m_valueDiff;
        charac.set(value);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacSet>() {
            @Override
            public CharacSet makeObject() {
                return new CharacSet((CharacSet$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Set de Charac, valeur = charac du caster (tention !)", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Set de Charac (tention !)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("valeur (eventuellement en %)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
