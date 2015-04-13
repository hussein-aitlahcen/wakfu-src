package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.util.summonDouble.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import java.util.*;

abstract class SummonDouble extends WakfuRunningEffect
{
    protected long m_newTargetId;
    protected BasicInvocationCharacteristics m_doubleCharacteristics;
    private SummonDoubleParams m_params;
    private static final ParameterListSet DEFAULT_PARAMETERS_LIST_SET;
    public BinarSerialPart TARGET;
    public BinarSerialPart DOUBLE_CHARACTERISTICS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SummonDouble.DEFAULT_PARAMETERS_LIST_SET;
    }
    
    protected SummonDouble() {
        super();
        this.TARGET = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(SummonDouble.this.m_newTargetId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SummonDouble.this.m_newTargetId = buffer.getLong();
                SummonDouble.this.m_target = null;
            }
        };
        this.DOUBLE_CHARACTERISTICS = new DoubleAdditionalDataDefaultPart();
        this.setTriggersToExecute();
    }
    
    @Override
    public void onCheckIn() {
        this.m_params = null;
        this.m_doubleCharacteristics = null;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (!(this.m_caster instanceof BasicCharacterInfo)) {
            this.setNotified();
            return;
        }
        SummonDouble.m_logger.info((Object)("Instanciation d'une nouvelle invocation avec un id de " + this.m_newTargetId));
        final SummonDoubleParams doubleParams = this.getDoubleParams();
        final BasicCharacterInfo summoner = this.getSummoner();
        final BasicCharacterInfo monster = summoner.summonMonster(this.m_newTargetId, this.getCellForSummon(), doubleParams.getBreedId(), this.m_doubleCharacteristics, doubleParams.isDoubleIndependant(), null);
        if (this.isValueComputationEnabled()) {
            this.m_doubleCharacteristics.setObstacleId(monster.getObstacleId());
        }
        this.notifyExecution(linkedRE, trigger);
        if (this.isValueComputationEnabled() && summoner.getCurrentFight() != null) {
            summoner.getCurrentFight().areaActivationWhenJoiningFight(monster);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_newTargetId = this.m_context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)1);
        final BasicCharacterInfo doubleModel = this.getDoubleModel();
        final SummonDoubleParams doubleParams = this.getDoubleParams();
        final DoubleSpellsFinder spellsFinder = doubleParams.getSpellsFinder();
        final SpellInventory<AbstractSpellLevel> spells = spellsFinder.getSpells(doubleModel, doubleParams, this.getContainerLevel());
        this.m_doubleCharacteristics = doubleParams.getDefaultCharacteristicsInstance().newInstance(doubleModel.getBreed().getBreedId(), doubleModel.getName() + " clone", doubleModel.getCharacteristicValue(FighterCharacteristicType.HP), doubleModel.getLevel(), doubleModel, this.getDoublePower(), spells);
    }
    
    @Override
    public BinarSerialPart getTargetBinarSerialPart() {
        return this.TARGET;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.DOUBLE_CHARACTERISTICS;
    }
    
    protected void createDoubleCharacteristics() {
        final BasicCharacterInfo caster = (BasicCharacterInfo)this.getCaster();
        if (caster.getSpellInventory() == null) {
            this.m_doubleCharacteristics = this.getDoubleParams().getDefaultCharacteristicsInstance().newInstance();
        }
        else {
            this.m_doubleCharacteristics = this.getDoubleParams().getDefaultCharacteristicsInstance().newInstance((short)100, (InventoryContentProvider<AbstractSpellLevel, RawSpellLevel>)caster.getSpellInventory().getContentProvider(), (InventoryContentChecker<AbstractSpellLevel>)caster.getSpellInventory().getContentChecker(), false, false, false);
        }
    }
    
    protected final int getDoublePower() {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null && ((WakfuEffect)this.m_genericEffect).getParamsCount() > 0) {
            return ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        return 100;
    }
    
    protected final BasicCharacterInfo getSummoner() {
        if (this.m_caster instanceof BasicCharacterInfo) {
            return (BasicCharacterInfo)this.m_caster;
        }
        return null;
    }
    
    protected BasicCharacterInfo getDoubleModel() {
        final EffectUser userToDuplicate = this.getDoubleParams().isUsingCasterAsModel() ? this.m_caster : this.m_target;
        if (userToDuplicate instanceof BasicCharacterInfo) {
            return (BasicCharacterInfo)userToDuplicate;
        }
        return null;
    }
    
    protected Point3 getCellForSummon() {
        return this.m_targetCell;
    }
    
    @Override
    public boolean useCaster() {
        return true;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @NotNull
    protected SummonDoubleParams getDoubleParams() {
        if (this.m_params != null) {
            return this.m_params;
        }
        return this.m_params = this.createDoubleParams();
    }
    
    @NotNull
    protected abstract SummonDoubleParams createDoubleParams();
    
    static {
        DEFAULT_PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Params", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Avec Ratio de puissance", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Ratio", WakfuRunningEffectParameterType.CONFIG) }) });
    }
    
    protected class DoubleAdditionalDataDefaultPart extends BinarSerialPart
    {
        protected RawInvocationCharacteristic rawdata;
        
        @Override
        public void serialize(final ByteBuffer buffer) {
            this.rawdata.serialize(buffer);
            if (SummonDouble.this.getDoubleParams().shouldUseLevelGains()) {
                final ByteArray ba = new ByteArray();
                byte spellWithLevelGainSize = 0;
                final SpellInventory<AbstractSpellLevel> spellInventory = ((DoubleInvocationCharacteristics)SummonDouble.this.m_doubleCharacteristics).getSpellInventory();
                for (final AbstractSpellLevel spellLevel : spellInventory) {
                    if (spellLevel.getLevelGain() == 0) {
                        continue;
                    }
                    ++spellWithLevelGainSize;
                    ba.putInt(spellLevel.getReferenceId());
                    ba.putInt(spellLevel.getLevelGain());
                }
                buffer.put(spellWithLevelGainSize);
                buffer.put(ba.toArray());
            }
        }
        
        @Override
        public void unserialize(final ByteBuffer buffer, final int version) {
            (this.rawdata = new RawInvocationCharacteristic()).unserialize(buffer);
            SummonDouble.this.createDoubleCharacteristics();
            SummonDouble.this.m_doubleCharacteristics.fromRaw(this.rawdata);
            if (SummonDouble.this.getDoubleParams().shouldUseLevelGains()) {
                final byte spellWithLevelGainSize = buffer.get();
                final SpellInventory<AbstractSpellLevel> spellInventory = ((DoubleInvocationCharacteristics)SummonDouble.this.m_doubleCharacteristics).getSpellInventory();
                for (int i = 0; i < spellWithLevelGainSize; ++i) {
                    final int spellRefId = buffer.getInt();
                    final int levelGain = buffer.getInt();
                    if (spellInventory != null) {
                        final AbstractSpellLevel spell = spellInventory.getFirstWithReferenceId(spellRefId);
                        if (spell != null) {
                            spell.addLevelGain(levelGain);
                        }
                    }
                }
            }
        }
        
        @Override
        public int expectedSize() {
            this.rawdata = new RawInvocationCharacteristic();
            SummonDouble.this.m_doubleCharacteristics.toRaw(this.rawdata);
            if (SummonDouble.this.getDoubleParams().shouldUseLevelGains()) {
                byte spellWithLevelGainSize = 0;
                final SpellInventory<AbstractSpellLevel> spellInventory = ((DoubleInvocationCharacteristics)SummonDouble.this.m_doubleCharacteristics).getSpellInventory();
                for (final AbstractSpellLevel spellLevel : spellInventory) {
                    if (spellLevel.getLevelGain() == 0) {
                        continue;
                    }
                    ++spellWithLevelGainSize;
                }
                return this.rawdata.serializedSize() + 1 + spellWithLevelGainSize * 8;
            }
            return this.rawdata.serializedSize();
        }
    }
}
