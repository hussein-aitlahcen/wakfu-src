package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.spell.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SummonImage extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private static final short MAX_IMAG_SPELL_COUNT = 0;
    private short m_breedId;
    private long m_newTargetId;
    protected ImageCharacteristics m_imageCharacteristics;
    public BinarSerialPart TARGET;
    public BinarSerialPart IMAGE_CHARACTERISTICS;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SummonImage.PARAMETERS_LIST_SET;
    }
    
    public SummonImage() {
        super();
        this.TARGET = new BinarSerialPart(10) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(SummonImage.this.m_newTargetId);
                buffer.putShort(SummonImage.this.m_breedId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                SummonImage.this.m_newTargetId = buffer.getLong();
                SummonImage.this.m_target = null;
                SummonImage.this.m_breedId = buffer.getShort();
            }
        };
        this.IMAGE_CHARACTERISTICS = new BinarSerialPart() {
            private RawInvocationCharacteristic rawdata;
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                this.rawdata.serialize(buffer);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                final BasicCharacterInfo caster = (BasicCharacterInfo)SummonImage.this.getCaster();
                (this.rawdata = new RawInvocationCharacteristic()).unserialize(buffer);
                if (caster.getSpellInventory() == null) {
                    SummonImage.this.m_imageCharacteristics = ImageCharacteristics.getDefaultInstance().newInstance();
                }
                else {
                    SummonImage.this.m_imageCharacteristics = ImageCharacteristics.getDefaultInstance().newInstance((short)100, (InventoryContentProvider<AbstractSpellLevel, RawSpellLevel>)caster.getSpellInventory().getContentProvider(), (InventoryContentChecker<AbstractSpellLevel>)caster.getSpellInventory().getContentChecker(), false, false, false);
                }
                SummonImage.this.m_imageCharacteristics.fromRaw(this.rawdata);
            }
            
            @Override
            public int expectedSize() {
                this.rawdata = new RawInvocationCharacteristic();
                SummonImage.this.m_imageCharacteristics.toRaw(this.rawdata);
                return this.rawdata.serializedSize();
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public SummonImage newInstance() {
        SummonImage re;
        try {
            re = (SummonImage)SummonImage.m_staticPool.borrowObject();
            re.m_pool = SummonImage.m_staticPool;
        }
        catch (Exception e) {
            re = new SummonImage();
            re.m_pool = null;
            re.m_isStatic = false;
            SummonImage.m_logger.error((Object)("Erreur lors d'un checkOut sur un SummonImage : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_caster instanceof BasicCharacterInfo) {
            SummonImage.m_logger.info((Object)("Instanciation d'une nouvelle invocation avec un id de " + this.m_newTargetId));
            final BasicCharacterInfo caster = (BasicCharacterInfo)this.m_caster;
            final BasicCharacterInfo monster = caster.summonMonster(this.m_newTargetId, this.m_targetCell, this.m_breedId, this.m_imageCharacteristics, false, null);
            if (this.isValueComputationEnabled()) {
                this.m_imageCharacteristics.setObstacleId(monster.getObstacleId());
            }
            this.notifyExecution(linkedRE, trigger);
            if (this.isValueComputationEnabled() && ((BasicCharacterInfo)this.m_caster).getCurrentFight() != null) {
                ((BasicCharacterInfo)this.m_caster).getCurrentFight().areaActivationWhenJoiningFight(monster);
            }
            monster.addProperty(FightPropertyType.IS_A_COPY_OF_HIS_CONTROLLER);
            monster.addProperty(FightPropertyType.NO_KO);
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        final BasicCharacterInfo caster = (BasicCharacterInfo)this.m_caster;
        final short level = this.getContainerLevel();
        this.m_breedId = (short)((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (this.m_value <= 0) {
            this.m_value = caster.getLevel();
        }
        this.m_newTargetId = this.m_context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)1);
        final SpellInventory<AbstractSpellLevel> casterSpellInventory = (SpellInventory<AbstractSpellLevel>)caster.getSpellInventory();
        final SpellInventory<AbstractSpellLevel> imageSpellInventory = new SpellInventory<AbstractSpellLevel>((short)0, (InventoryContentProvider<AbstractSpellLevel, RawSpellLevel>)casterSpellInventory.getContentProvider(), (InventoryContentChecker<AbstractSpellLevel>)casterSpellInventory.getContentChecker(), false, false, false);
        this.m_imageCharacteristics = ImageCharacteristics.getDefaultInstance().newInstance(caster.getBreed().getBreedId(), caster.getName() + " clone", caster.getCharacteristicValue(FighterCharacteristicType.HP), (short)this.m_value, caster, imageSpellInventory);
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
        return this.IMAGE_CHARACTERISTICS;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SummonImage>() {
            @Override
            public SummonImage makeObject() {
                return new SummonImage();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Invocation d'une image", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("ID de la breed qui servira de base \u00e0 l'image", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Niveau de l'invocation (<= 0 = niveau du controller)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
