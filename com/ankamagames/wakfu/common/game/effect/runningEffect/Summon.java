package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.wakfu.common.game.fighter.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class Summon extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private long m_newTargetId;
    private BasicInvocationCharacteristics m_summonCharac;
    private PropertyManager<FightPropertyType> m_properties;
    private byte m_teamId;
    private boolean m_summonIsOwnController;
    public BinarSerialPart TARGET;
    public BinarSerialPart ADDITIONNAL_DATA;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return Summon.PARAMETERS_LIST_SET;
    }
    
    public Summon() {
        super();
        this.m_summonIsOwnController = false;
        this.TARGET = new BinarSerialPart(8) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.putLong(Summon.this.m_newTargetId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                Summon.this.m_newTargetId = buffer.getLong();
                Summon.this.m_target = null;
            }
        };
        this.ADDITIONNAL_DATA = new BinarSerialPart() {
            private RawInvocationCharacteristic invocationCharacteristics;
            private RawProperties properties;
            
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(Summon.this.m_summonIsOwnController ? 1 : 0));
                this.invocationCharacteristics.serialize(buffer);
                this.properties.serialize(buffer);
                buffer.put(Summon.this.m_teamId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                Summon.this.m_summonIsOwnController = (buffer.get() == 1);
                (this.invocationCharacteristics = new RawInvocationCharacteristic()).unserialize(buffer);
                (this.properties = new RawProperties()).unserialize(buffer);
                Summon.this.m_summonCharac = new BasicInvocationCharacteristics();
                Summon.this.m_summonCharac.fromRaw(this.invocationCharacteristics);
                Summon.this.m_properties = (PropertyManager<FightPropertyType>)new PropertyManager();
                Summon.this.m_properties.fromRaw(this.properties);
                Summon.this.m_summonCharac.setTeamId(buffer.get());
            }
            
            @Override
            public int expectedSize() {
                this.invocationCharacteristics = new RawInvocationCharacteristic();
                Summon.this.m_summonCharac.toRaw(this.invocationCharacteristics);
                this.properties = new RawProperties();
                Summon.this.m_properties.toRaw(this.properties);
                return 1 + this.invocationCharacteristics.serializedSize() + this.properties.serializedSize() + 1;
            }
        };
        this.setTriggersToExecute();
    }
    
    @Override
    public void setTriggersToExecute() {
        super.setTriggersToExecute();
        this.m_triggers.set(2130);
    }
    
    @Override
    public Summon newInstance() {
        Summon re;
        try {
            re = (Summon)Summon.m_staticPool.borrowObject();
            re.m_pool = Summon.m_staticPool;
        }
        catch (Exception e) {
            re = new Summon();
            re.m_pool = null;
            re.m_isStatic = false;
            Summon.m_logger.error((Object)("Erreur lors d'un checkOut sur un Summon : " + e.getMessage()));
        }
        re.m_summonCharac = null;
        re.m_properties = null;
        re.m_summonIsOwnController = false;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_summonCharac != null && this.m_caster instanceof BasicCharacterInfo) {
            Summon.m_logger.info((Object)("Instanciation d'une nouvelle invocation avec un id de " + this.m_newTargetId));
            final BasicCharacterInfo basicCharacterInfo = (BasicCharacterInfo)this.m_caster;
            final BasicCharacterInfo monster = basicCharacterInfo.summonMonster(this.m_summonCharac.getSummonId(), this.m_targetCell, this.m_summonCharac.getTypeId(), this.m_summonCharac, this.m_summonIsOwnController, this.m_properties);
            if (this.isValueComputationEnabled()) {
                this.m_summonCharac.setObstacleId(monster.getObstacleId());
            }
            monster.setControlledByAI(true);
            this.notifyExecution(linkedRE, trigger);
            if (this.isValueComputationEnabled() && ((BasicCharacterInfo)this.m_caster).getCurrentFight() != null) {
                ((BasicCharacterInfo)this.m_caster).getCurrentFight().areaActivationWhenJoiningFight(monster);
            }
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        short forcedLevel = -1;
        short cappedLevel = -1;
        this.m_newTargetId = this.m_context.getEffectUserInformationProvider().getNextFreeEffectUserId((byte)1);
        if (this.m_genericEffect == null) {
            return;
        }
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        int levelType;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 1) {
            levelType = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        else {
            levelType = -1;
        }
        final AbstractBreedManager breedManager = MonsterBreedManagerProvider.getMonsterBreedManager();
        switch (levelType) {
            case -1: {
                if (this.m_effectContainer != null) {
                    forcedLevel = (cappedLevel = this.getContainerLevel());
                    break;
                }
                Summon.m_logger.error((Object)"[Summon] Impossible d'invoquer une creature avec le mm niveau que le sort : l'effectContainer est null");
                return;
            }
            case -2: {
                if (this.m_caster != null) {
                    forcedLevel = ((BasicCharacterInfo)this.m_caster).getLevel();
                    cappedLevel = ((BasicCharacterInfo)this.m_caster).getLevel();
                    break;
                }
                Summon.m_logger.error((Object)"[Summon] Impossible d'invoquer une creature avec le mm niveau que le caster car celui-ci est null");
                return;
            }
            case -3: {
                if (breedManager != null) {
                    final Breed breed = breedManager.getBreedFromId((short)this.m_value);
                    if (breed != null) {
                        forcedLevel = ((AbstractMonsterBreed)breed).getLevelMin();
                        cappedLevel = ((AbstractMonsterBreed)breed).getLevelMin();
                    }
                }
                if (forcedLevel == -1) {
                    Summon.m_logger.error((Object)"[Summon] Impossible d'invoquer une creature, on ne peut pas recup\u00e9rer son niveau");
                    return;
                }
                break;
            }
            default: {
                if (levelType > 0) {
                    forcedLevel = (short)levelType;
                    cappedLevel = (short)levelType;
                    break;
                }
                Summon.m_logger.error((Object)("[Summon] Erreur de saisie : le levelType doit etre positif ou -1 ou -2, l\u00e0 il vaut " + levelType));
                return;
            }
        }
        this.m_summonIsOwnController = (((WakfuEffect)this.m_genericEffect).getParamsCount() > 2 && ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.RANDOM) >= 1);
        Direction8 direction;
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 3) {
            final int directionId = ((WakfuEffect)this.m_genericEffect).getParam(3, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
            direction = Direction8.getDirectionFromIndex(directionId);
        }
        else {
            direction = Direction8.NONE;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 4) {
            this.m_teamId = (byte)((WakfuEffect)this.m_genericEffect).getParam(4, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        else if (this.m_caster instanceof BasicCharacterInfo) {
            this.m_teamId = ((BasicCharacterInfo)this.m_caster).getTeamId();
        }
        if (this.m_summonCharac == null) {
            this.m_summonCharac = new BasicInvocationCharacteristics((short)this.m_value, "", -1, forcedLevel, cappedLevel);
        }
        this.m_summonCharac.setForcedLevel(forcedLevel);
        this.m_summonCharac.setSummonId(this.m_newTargetId);
        this.m_summonCharac.setDirection(direction);
        this.m_summonCharac.setTeamId(this.m_teamId);
        if (this.m_properties == null) {
            this.m_properties = new PropertyManager<FightPropertyType>();
        }
        if (breedManager != null) {
            final Breed breed2 = breedManager.getBreedFromId((short)this.m_value);
            for (final int propertyType : breed2.getBaseFightProperties()) {
                this.m_properties.setPropertyValue(FightPropertyType.getPropertyFromId(propertyType), (byte)1);
            }
        }
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
    public boolean unapplicationMustBeNotified() {
        return false;
    }
    
    @Override
    public void onUnApplication() {
        this.m_summonCharac = null;
        this.m_properties = null;
        super.onUnApplication();
    }
    
    @Override
    public BinarSerialPart getTargetBinarSerialPart() {
        return this.TARGET;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATA;
    }
    
    public BasicInvocationCharacteristics getSummonCharac() {
        return this.m_summonCharac;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<Summon>() {
            @Override
            public Summon makeObject() {
                return new Summon();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Standard avec level du sort pour la creature", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id du monstre \u00e0 invoquer", WakfuRunningEffectParameterType.ID) }), new WakfuRunningEffectParameterList("Standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id du monstre \u00e0 invoquer", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level de l'invoc (-1 = niveau du sort, -2 = niveau du caster, -3 = niveau min du mob, > 0 valeur fixe \u00e0 donner)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Sp\u00e9cifie que l'invocation est son propre controlleur (attention)", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id du monstre \u00e0 invoquer", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level de l'invoc (-1 = niveau du sort, -2 = niveau du caster, -3 = niveau min du mob, > 0 valeur fixe \u00e0 donner)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("controlleur = invocation : 1 sinon 0", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Sp\u00e9cifie la direction (par defaut celle du caster", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id du monstre \u00e0 invoquer", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level de l'invoc (-1 = niveau du sort, -2 = niveau du caster, -3 = niveau min du mob, > 0 valeur fixe \u00e0 donner)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("controlleur = invocation : 1 sinon 0", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Direction (-1 par d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG) }), new WakfuRunningEffectParameterList("Sp\u00e9cifie l'equipe", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("id du monstre \u00e0 invoquer", WakfuRunningEffectParameterType.ID), new WakfuRunningEffectParameter("level de l'invoc (-1 = niveau du sort, -2 = niveau du caster, -3 = niveau min du mob, > 0 valeur fixe \u00e0 donner)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("controlleur = invocation : 1 sinon 0", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Direction (-1 par d\u00e9faut)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Equipe (0 = rouge, 1=bleue)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
