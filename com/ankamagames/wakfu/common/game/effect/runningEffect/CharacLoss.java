package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class CharacLoss extends CharacModification
{
    private static final ObjectPool m_staticPool;
    
    public CharacLoss() {
        super();
    }
    
    public CharacLoss(final CharacteristicType charac) {
        super(charac);
    }
    
    public CharacLoss(final CharacteristicType charac, final boolean valuePerCentOfCurrentValue) {
        super(charac, valuePerCentOfCurrentValue);
    }
    
    @Override
    public CharacLoss newInstance() {
        CharacLoss re;
        try {
            re = (CharacLoss)CharacLoss.m_staticPool.borrowObject();
            re.m_pool = CharacLoss.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacLoss();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacLoss.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacLoss : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        re.m_valuePerCentOfCurrentValue = this.m_valuePerCentOfCurrentValue;
        return re;
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        if (this.m_charac == null) {
            return;
        }
        if (this.m_charac.getCharacteristicType() == 0) {
            final FighterCharacteristicType charac = (FighterCharacteristicType)this.m_charac;
            if (charac.hasLossTrigger()) {
                this.m_triggers.set(charac.getLossTrigger());
            }
            if (charac == FighterCharacteristicType.CHRAGE) {
                this.m_triggers.set(2140);
            }
        }
    }
    
    @Override
    boolean modificationIsNotApplicable() {
        return super.modificationIsNotApplicable() || (this.m_charac == FighterCharacteristicType.MP && this.getEffectExecutionTarget().isActiveProperty(FightPropertyType.IMMUNE_TO_MP_LOSS));
    }
    
    @Override
    void applyValueModification(final AbstractCharacteristic charac) {
        this.m_value = charac.substract(this.m_value);
        if (this.m_value == 0) {
            this.setNotified();
        }
    }
    
    @Override
    public boolean canBeExecuted() {
        if (this.isValueComputationEnabled()) {
            final int applicationProbability = this.getApplicationProbability();
            if (applicationProbability < 100) {
                final int roll = DiceRoll.roll(100);
                if (applicationProbability < roll) {
                    this.m_value = 0;
                    return false;
                }
            }
        }
        return super.canBeExecuted();
    }
    
    protected int getApplicationProbability() {
        int initialProbability;
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() >= 4) {
            initialProbability = ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.RANDOM);
        }
        else {
            initialProbability = -1;
        }
        if (initialProbability < 0) {
            return 100;
        }
        if (!EffectProbabilityModificators.isEffectConcerned(this)) {
            return initialProbability;
        }
        final int probability = initialProbability + EffectProbabilityModificators.getProbabilityModif(this);
        return MathHelper.clamp(probability, 0, 100);
    }
    
    @Override
    void rollbackCharacModification() {
        this.getEffectExecutionTarget().getCharacteristic(this.m_charac).add(this.m_value);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacLoss>() {
            @Override
            public CharacLoss makeObject() {
                return new CharacLoss();
            }
        });
    }
}
