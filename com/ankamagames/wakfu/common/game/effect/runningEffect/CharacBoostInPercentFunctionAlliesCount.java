package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class CharacBoostInPercentFunctionAlliesCount extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private final BinarSerialPart m_addedHpPart;
    private FighterCharacteristicType m_charac;
    private int m_addedHp;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CharacBoostInPercentFunctionAlliesCount.PARAMETERS_LIST_SET;
    }
    
    private CharacBoostInPercentFunctionAlliesCount() {
        super();
        this.m_addedHpPart = new BinarSerialPart(4) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(CharacBoostInPercentFunctionAlliesCount.this.m_addedHp);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacBoostInPercentFunctionAlliesCount.this.m_addedHp = buffer.getInt();
            }
        };
        this.setTriggersToExecute();
    }
    
    public CharacBoostInPercentFunctionAlliesCount(final FighterCharacteristicType charac) {
        super();
        this.m_addedHpPart = new BinarSerialPart(4) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putInt(CharacBoostInPercentFunctionAlliesCount.this.m_addedHp);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CharacBoostInPercentFunctionAlliesCount.this.m_addedHp = buffer.getInt();
            }
        };
        this.setTriggersToExecute();
        this.m_charac = charac;
    }
    
    @Override
    public CharacBoostInPercentFunctionAlliesCount newInstance() {
        CharacBoostInPercentFunctionAlliesCount re;
        try {
            re = (CharacBoostInPercentFunctionAlliesCount)CharacBoostInPercentFunctionAlliesCount.m_staticPool.borrowObject();
            re.m_pool = CharacBoostInPercentFunctionAlliesCount.m_staticPool;
        }
        catch (Exception e) {
            re = new CharacBoostInPercentFunctionAlliesCount();
            re.m_pool = null;
            re.m_isStatic = false;
            CharacBoostInPercentFunctionAlliesCount.m_logger.error((Object)("Erreur lors d'un checkOut sur un HpBoostInPercentFunctionAlliesCount : " + e.getMessage()));
        }
        re.m_charac = this.m_charac;
        return re;
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (this.m_target == null || !this.m_target.hasCharacteristic(this.m_charac) || !(this.m_target instanceof BasicCharacterInfo)) {
            return;
        }
        final EffectContext context = this.getContext();
        if (!(context instanceof WakfuFightEffectContext)) {
            return;
        }
        final WakfuFightEffectContext fightContext = (WakfuFightEffectContext)context;
        final AbstractFight fight = fightContext.getFight();
        if (fight == null) {
            return;
        }
        final int alliesCount = fight.getFightersPresentInTimelineInPlayInTeam(((BasicCharacterInfo)this.m_target).getTeamId()).size() - 1;
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_value *= alliesCount;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_target == null || !this.m_target.hasCharacteristic(this.m_charac)) {
            this.setNotified();
            return;
        }
        if (this.m_value == 0) {
            this.setNotified();
            return;
        }
        final AbstractCharacteristic hp = this.m_target.getCharacteristic(this.m_charac);
        final int previousMax = hp.max();
        hp.updateMaxPercentModifier(this.m_value);
        if (this.isValueComputationEnabled()) {
            this.m_addedHp = hp.max() - previousMax;
        }
        hp.add(this.m_addedHp);
    }
    
    @Override
    public void unapplyOverride() {
        if (!this.m_executed) {
            super.unapplyOverride();
            return;
        }
        if (this.m_target != null && this.m_target.hasCharacteristic(this.m_charac) && this.m_value != 0) {
            final AbstractCharacteristic hp = this.m_target.getCharacteristic(this.m_charac);
            if (hp.value() > 0) {
                hp.substract((this.m_addedHp > hp.value()) ? (hp.value() - 1) : this.m_addedHp);
            }
            hp.updateMaxPercentModifier(-this.m_value);
        }
        super.unapplyOverride();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.m_addedHpPart;
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
        this.m_addedHp = 0;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<CharacBoostInPercentFunctionAlliesCount>() {
            @Override
            public CharacBoostInPercentFunctionAlliesCount makeObject() {
                return new CharacBoostInPercentFunctionAlliesCount((CharacBoostInPercentFunctionAlliesCount$1)null);
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur en %", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
