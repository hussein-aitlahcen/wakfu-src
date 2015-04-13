package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.characteristics.craft.*;
import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class EcosystemSkillModification extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_isCheckedOut;
    private EcosystemActionType m_ecosystemActionType;
    private ResourceType m_resourceType;
    private static final ObjectPool POOL;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    public EcosystemSkillModification() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(2) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put(EcosystemSkillModification.this.m_ecosystemActionType.getId());
                buffer.put(EcosystemSkillModification.this.m_resourceType.getId());
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                EcosystemSkillModification.this.m_ecosystemActionType = EcosystemActionType.getById(buffer.get());
                EcosystemSkillModification.this.m_resourceType = ResourceType.getById(buffer.get());
            }
        };
    }
    
    public EcosystemSkillModification(final EcosystemActionType ecosystemActionType, final ResourceType resourceType) {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(2) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put(EcosystemSkillModification.this.m_ecosystemActionType.getId());
                buffer.put(EcosystemSkillModification.this.m_resourceType.getId());
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                EcosystemSkillModification.this.m_ecosystemActionType = EcosystemActionType.getById(buffer.get());
                EcosystemSkillModification.this.m_resourceType = ResourceType.getById(buffer.get());
            }
        };
        this.m_ecosystemActionType = ecosystemActionType;
        this.m_resourceType = resourceType;
    }
    
    public static EcosystemSkillModification checkOut() {
        EcosystemSkillModification obj;
        try {
            obj = (EcosystemSkillModification)EcosystemSkillModification.POOL.borrowObject();
            obj.m_isCheckedOut = true;
        }
        catch (Exception e) {
            obj = new EcosystemSkillModification();
            EcosystemSkillModification.m_logger.error((Object)("Erreur lors d'un checkOut sur un objet de type EcosystemSkillModification : " + e.getMessage()));
        }
        return obj;
    }
    
    @Override
    public void release() {
        if (this.m_isCheckedOut) {
            try {
                EcosystemSkillModification.POOL.returnObject(this);
                this.m_isCheckedOut = false;
            }
            catch (Exception e) {
                EcosystemSkillModification.m_logger.error((Object)"Exception lors du retour au pool", (Throwable)e);
            }
        }
        else {
            this.onCheckIn();
        }
    }
    
    @Override
    public void onCheckOut() {
    }
    
    @Override
    public void onCheckIn() {
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        this.m_value = this.modifyCharacValue(this.m_value);
    }
    
    @Override
    public void unapplyOverride() {
        this.modifyCharacValue(-this.m_value);
    }
    
    private int modifyCharacValue(final int value) {
        if (this.m_target == null) {
            return 0;
        }
        if (!(this.m_target instanceof BasicCharacterInfo)) {
            return 0;
        }
        final SkillCharacteristics characteristics = ((BasicCharacterInfo)this.m_target).getSkillCharacteristics();
        final int oldValue = characteristics.getEcosystemCharacteristicEfficiency(this.m_ecosystemActionType, this.m_resourceType);
        characteristics.modifyEcosystemCharacteristicEfficiency(this.m_ecosystemActionType, this.m_resourceType, value);
        final int newValue = characteristics.getEcosystemCharacteristicEfficiency(this.m_ecosystemActionType, this.m_resourceType);
        return newValue - oldValue;
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        final EcosystemSkillModification newInstance = checkOut();
        newInstance.m_resourceType = this.m_resourceType;
        newInstance.m_ecosystemActionType = this.m_ecosystemActionType;
        return newInstance;
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
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() > 1) {
            this.m_ecosystemActionType = EcosystemActionType.getById((byte)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
            this.m_resourceType = ResourceType.getById((short)((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
        }
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return EcosystemSkillModification.PARAMETERS_LIST_SET;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Defaut", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur de modification ", WakfuRunningEffectParameterType.VALUE) }) });
        POOL = new MonitoredPool(new ObjectFactory<EcosystemSkillModification>() {
            @Override
            public EcosystemSkillModification makeObject() {
                return new EcosystemSkillModification();
            }
        });
    }
}
