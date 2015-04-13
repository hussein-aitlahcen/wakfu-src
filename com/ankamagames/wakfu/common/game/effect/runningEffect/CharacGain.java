package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public class CharacGain extends CharacModification
{
    private static final ObjectPool m_staticPool;
    
    public CharacGain() {
        super();
    }
    
    public CharacGain(final CharacteristicType charac) {
        super(charac);
    }
    
    public CharacGain(final CharacteristicType charac, final boolean valuePerCentOfCurrentValue) {
        super(charac, valuePerCentOfCurrentValue);
    }
    
    @Override
    public CharacGain newInstance() {
        CharacGain re;
        try {
            re = (CharacGain)CharacGain.m_staticPool.borrowObject();
            re.m_pool = CharacGain.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacGain();
            re.m_pool = null;
            CharacGain.m_logger.error((Object)("Erreur lors d'un checkOut sur un CharacGain : " + e.getMessage()));
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
            if (charac.hasGainTrigger()) {
                this.m_triggers.set(charac.getGainTrigger());
            }
            if (this.m_charac == FighterCharacteristicType.CHRAGE) {
                this.m_triggers.set(2140);
            }
        }
    }
    
    @Override
    boolean modificationIsNotApplicable() {
        if (super.modificationIsNotApplicable()) {
            return true;
        }
        if (this.m_charac != FighterCharacteristicType.PROSPECTION) {
            return false;
        }
        final EffectUser target = this.getEffectExecutionTarget();
        if (!(target instanceof BasicCharacterInfo)) {
            return false;
        }
        final BasicCharacterInfo info = (BasicCharacterInfo)target;
        return info.getType() == 5 && !this.hasProperty(RunningEffectPropertyType.COMPANION_ALLOWED_PROSPECTION_BUFF);
    }
    
    protected void applyValueModification(final AbstractCharacteristic charac) {
        this.m_value = charac.add(this.m_value);
    }
    
    @Override
    void rollbackCharacModification() {
        this.getEffectExecutionTarget().getCharacteristic(this.m_charac).substract(this.m_value);
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacGain>() {
            @Override
            public CharacGain makeObject() {
                return new CharacGain();
            }
        });
    }
}
