package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.wakfu.common.datas.specific.symbiot.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SummonFromSymbiot extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private float m_primaryCharacteristicModifier;
    private float m_secondaryCharacteristicModifier;
    private long m_newTargetId;
    private BasicInvocationCharacteristics m_summonCharac;
    private byte m_currentIndex;
    private boolean m_cannotSummon;
    public BinarSerialPart TARGET;
    public BinarSerialPart ADDITIONNAL_DATA;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SummonFromSymbiot.PARAMETERS_LIST_SET;
    }
    
    public SummonFromSymbiot() {
        super();
        this.m_currentIndex = -1;
        this.TARGET = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(SummonFromSymbiot.this.m_newTargetId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SummonFromSymbiot.this.m_newTargetId = buffer.getLong();
                SummonFromSymbiot.this.m_target = null;
            }
        };
        this.ADDITIONNAL_DATA = new BinarSerialPart() {
            private RawInvocationCharacteristic rawdata;
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putFloat(SummonFromSymbiot.this.m_primaryCharacteristicModifier);
                buffer.putFloat(SummonFromSymbiot.this.m_secondaryCharacteristicModifier);
                this.rawdata.serialize(buffer);
                if (SummonFromSymbiot.this.m_caster instanceof SymbioticCharacter) {
                    buffer.put(((SymbioticCharacter)SummonFromSymbiot.this.m_caster).getSymbiot().getCurrentCreatureIndex());
                }
                else {
                    buffer.put((byte)0);
                }
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SummonFromSymbiot.this.m_primaryCharacteristicModifier = buffer.getFloat();
                SummonFromSymbiot.this.m_secondaryCharacteristicModifier = buffer.getFloat();
                (this.rawdata = new RawInvocationCharacteristic()).unserialize(buffer);
                SummonFromSymbiot.this.m_currentIndex = buffer.get();
                SummonFromSymbiot.this.m_summonCharac = new BasicInvocationCharacteristics();
                SummonFromSymbiot.this.m_summonCharac.fromRaw(this.rawdata);
                SummonFromSymbiot.this.m_summonCharac.setComeFromSymbiot(true);
            }
            
            @Override
            public int expectedSize() {
                this.rawdata = new RawInvocationCharacteristic();
                SummonFromSymbiot.this.m_summonCharac.toRaw(this.rawdata);
                return 8 + this.rawdata.serializedSize() + 1;
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public SummonFromSymbiot newInstance() {
        SummonFromSymbiot re;
        try {
            re = (SummonFromSymbiot)SummonFromSymbiot.m_staticPool.borrowObject();
            re.m_pool = SummonFromSymbiot.m_staticPool;
        }
        catch (Exception e) {
            re = new SummonFromSymbiot();
            re.m_pool = null;
            re.m_isStatic = false;
            SummonFromSymbiot.m_logger.error((Object)("Erreur lors d'un checkOut sur un SummonFromSymbiot : " + e.getMessage()));
        }
        re.m_cannotSummon = false;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_cannotSummon) {
            SummonFromSymbiot.m_logger.error((Object)"Impossible d'invoquer");
            this.setNotified(true);
            return;
        }
        if (this.m_caster == null) {
            return;
        }
        BasicCharacterInfo monster = null;
        if (this.m_summonCharac != null && this.m_caster instanceof SymbioticCharacter && this.m_caster instanceof BasicCharacterInfo) {
            final SymbioticCharacter symbioticCharacter = (SymbioticCharacter)this.m_caster;
            this.m_summonCharac.setSummonId(this.m_newTargetId);
            if (this.m_currentIndex != -1 && symbioticCharacter.getSymbiot() != null) {
                final BasicInvocationCharacteristics creatureInSymbiot = symbioticCharacter.getSymbiot().getCreatureParametersFromIndex(this.m_currentIndex);
                creatureInSymbiot.setSummonId(this.m_newTargetId);
            }
            SummonFromSymbiot.m_logger.info((Object)("Instanciation d'une nouvelle invocation avec un id de " + this.m_summonCharac.getSummonId()));
            monster = ((BasicCharacterInfo)symbioticCharacter).summonMonster(this.m_summonCharac.getSummonId(), this.m_targetCell, this.m_summonCharac.getTypeId(), this.m_summonCharac, false, null);
            this.nerfSummonCharacteristics(monster);
            monster.getCharacteristic((CharacteristicType)FighterCharacteristicType.DMG_IN_PERCENT).add(this.m_caster.getCharacteristicValue(FighterCharacteristicType.SUMMONING_MASTERY));
            monster.setSummonedFromSymbiot(true);
            if (this.isValueComputationEnabled()) {
                this.m_summonCharac.setObstacleId(monster.getObstacleId());
            }
            if (symbioticCharacter.getSymbiot() != null) {
                symbioticCharacter.getSymbiot().setCreatureAvailability(this.m_currentIndex, false);
                if (!symbioticCharacter.getSymbiot().isAvailable(this.m_currentIndex)) {
                    symbioticCharacter.getSymbiot().setCurrentCreatureNextIndex();
                }
            }
        }
        else if (this.m_caster instanceof BasicCharacterInfo) {
            monster = ((BasicCharacterInfo)this.m_caster).summonMonster(this.m_summonCharac.getSummonId(), this.m_targetCell, this.m_summonCharac.getTypeId(), this.m_summonCharac, false, null);
            this.nerfSummonCharacteristics(monster);
            monster.getCharacteristic((CharacteristicType)FighterCharacteristicType.DMG_IN_PERCENT).add(this.m_caster.getCharacteristicValue(FighterCharacteristicType.SUMMONING_MASTERY));
        }
        this.notifyExecution(linkedRE, trigger);
        if (this.isValueComputationEnabled() && ((BasicCharacterInfo)this.m_caster).getCurrentFight() != null) {
            ((BasicCharacterInfo)this.m_caster).getCurrentFight().areaActivationWhenJoiningFight(monster);
        }
    }
    
    private void nerfSummonCharacteristics(final BasicCharacterInfo monster) {
        OsamodasCreatureCharacModifier.INSTANCE.modifyCharac(monster.getCharacteristics(), this.m_primaryCharacteristicModifier, this.m_secondaryCharacteristicModifier);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
            this.m_primaryCharacteristicModifier = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) / 100.0f;
            this.m_secondaryCharacteristicModifier = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) / 100.0f;
        }
        else {
            this.m_primaryCharacteristicModifier = 1.0f;
            this.m_secondaryCharacteristicModifier = 1.0f;
        }
        this.m_newTargetId = this.m_context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)1);
        final AbstractSymbiot symbiot = ((SymbioticCharacter)this.m_caster).getSymbiot();
        if (symbiot.isAvailable(symbiot.getCurrentCreatureIndex())) {
            this.m_currentIndex = symbiot.getCurrentCreatureIndex();
            (this.m_summonCharac = symbiot.getCreatureParametersFromIndex(this.m_currentIndex)).setComeFromSymbiot(true);
        }
        else {
            this.m_cannotSummon = true;
        }
    }
    
    @Override
    public void onCheckIn() {
        super.onCheckIn();
        this.m_primaryCharacteristicModifier = 1.0f;
        this.m_secondaryCharacteristicModifier = 1.0f;
    }
    
    @Override
    public void onUnApplication() {
        this.m_summonCharac = null;
        super.onUnApplication();
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    @Override
    public BinarSerialPart getTargetBinarSerialPart() {
        return this.TARGET;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATA;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SummonFromSymbiot>() {
            @Override
            public SummonFromSymbiot makeObject() {
                return new SummonFromSymbiot();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Invoque la cr\u00e9ature actuellement s\u00e9lectionn\u00e9e dans le symbiote", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Invoque la cr\u00e9ature s\u00e9lectionn\u00e9e dans le symbiote avec controle de sa puissance", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Puissance des caracs principales (hp/dmg/res)", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Puissance des caracs secondaires (init/tacle/esquive)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
