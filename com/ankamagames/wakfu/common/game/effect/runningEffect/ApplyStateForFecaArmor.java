package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.manager.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public final class ApplyStateForFecaArmor extends ApplyState
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final int DEFAULT_PLATE_COUNT = 0;
    private static final int NEUTRAL_ARMOR_FLAG_STATE = 1139;
    private byte m_plateCount;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ApplyStateForFecaArmor.PARAMETERS_LIST_SET;
    }
    
    public ApplyStateForFecaArmor() {
        super();
        this.m_plateCount = 0;
        this.ADDITIONNAL_DATAS = new BinarSerialPart(5) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putShort(ApplyStateForFecaArmor.this.m_stateId);
                buffer.putShort(ApplyStateForFecaArmor.this.m_stateLevel);
                buffer.put(ApplyStateForFecaArmor.this.m_plateCount);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                ApplyStateForFecaArmor.this.m_stateId = buffer.getShort();
                ApplyStateForFecaArmor.this.m_stateLevel = buffer.getShort();
                ApplyStateForFecaArmor.this.m_plateCount = buffer.get();
                ApplyStateForFecaArmor.this.m_stateUniqueId = State.getUniqueIdFromBasicInformation(ApplyStateForFecaArmor.this.m_stateId, ApplyStateForFecaArmor.this.m_stateLevel);
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public boolean canBeExecuted() {
        this.bypassResistancesCheck();
        return super.canBeExecuted();
    }
    
    @Override
    public ApplyStateForFecaArmor newInstance() {
        ApplyStateForFecaArmor re;
        try {
            re = (ApplyStateForFecaArmor)ApplyStateForFecaArmor.m_staticPool.borrowObject();
            re.m_pool = ApplyStateForFecaArmor.m_staticPool;
        }
        catch (Exception e) {
            re = new ApplyStateForFecaArmor();
            re.m_pool = null;
            re.m_isStatic = false;
            ApplyStateForFecaArmor.m_logger.error((Object)("Erreur lors d'un checkOut sur un ApplyStateForFecaArmor : " + e.getMessage()));
        }
        re.m_plateCount = this.m_plateCount;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        super.executeOverride(linkedRE, trigger);
        if (this.m_target == null || this.m_caster == null) {
            return;
        }
        if (!this.m_target.hasCharacteristic(FighterCharacteristicType.ARMOR_PLATE)) {
            return;
        }
        if (this.m_target.getCharacteristicValue(FighterCharacteristicType.ARMOR_PLATE) <= 0) {
            this.m_target.getCharacteristic(FighterCharacteristicType.ARMOR_PLATE).set(this.m_plateCount);
        }
    }
    
    @Override
    protected void modifyStateRunningEffectIfNecessary(final StateRunningEffect stateRunningEffect) {
        final State state = stateRunningEffect.getState();
        if (state != null) {
            state.setFecaArmor(true);
        }
        stateRunningEffect.setForFecaArmor(true);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final short level = this.getContainerLevel();
        this.m_stateId = (short)((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.extractStateLevel(level);
        this.m_stateUniqueId = State.getUniqueIdFromBasicInformation(this.m_stateId, this.m_stateLevel);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 3) {
            this.m_plateCount = (byte)((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        else {
            this.m_plateCount = 0;
        }
        final EffectUser neutralArmorCaster = this.getNeutralArmorCaster();
        if (neutralArmorCaster.hasCharacteristic(FighterCharacteristicType.ARMOR_PLATE_BONUS)) {
            this.m_plateCount += (byte)neutralArmorCaster.getCharacteristicValue(FighterCharacteristicType.ARMOR_PLATE_BONUS);
        }
    }
    
    private EffectUser getNeutralArmorCaster() {
        EffectUser neutralArmorCaster = this.m_caster;
        final StateRunningEffect state = ((TimedRunningEffectManager)this.m_target.getRunningEffectManager()).getRunningState(1139);
        if (state == null) {
            ApplyStateForFecaArmor.m_logger.error((Object)"L'\u00e9tat marqueur de l'armure neutre n'est pas present sur la cible");
        }
        else {
            neutralArmorCaster = state.getCaster();
        }
        return neutralArmorCaster;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_plateCount = 0;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ApplyStateForFecaArmor>() {
            @Override
            public ApplyStateForFecaArmor makeObject() {
                return new ApplyStateForFecaArmor();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Applique l'armure", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Sp\u00e9cifie le nombre de plaque", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("stateId", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("nombre de plaque de base (3 par d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
