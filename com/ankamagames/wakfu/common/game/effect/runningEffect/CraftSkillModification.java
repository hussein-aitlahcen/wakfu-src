package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.wakfu.common.game.characteristics.skill.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class CraftSkillModification extends WakfuRunningEffect
{
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_isCheckedOut;
    private CraftSkillType m_craftSkillType;
    private int m_craftId;
    private static final ObjectPool POOL;
    public BinarSerialPart ADDITIONNAL_DATAS;
    
    public CraftSkillModification() {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(5) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put(CraftSkillModification.this.m_craftSkillType.getId());
                buffer.putInt(CraftSkillModification.this.m_craftId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CraftSkillModification.this.m_craftSkillType = CraftSkillType.getById(buffer.get());
                CraftSkillModification.this.m_craftId = buffer.getInt();
            }
        };
    }
    
    public CraftSkillModification(final CraftSkillType craftSkillType) {
        super();
        this.ADDITIONNAL_DATAS = new BinarSerialPart(5) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put(CraftSkillModification.this.m_craftSkillType.getId());
                buffer.putInt(CraftSkillModification.this.m_craftId);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                CraftSkillModification.this.m_craftSkillType = CraftSkillType.getById(buffer.get());
                CraftSkillModification.this.m_craftId = buffer.getInt();
            }
        };
        this.m_craftSkillType = craftSkillType;
    }
    
    public static CraftSkillModification checkOut() {
        CraftSkillModification obj;
        try {
            obj = (CraftSkillModification)CraftSkillModification.POOL.borrowObject();
            obj.m_isCheckedOut = true;
        }
        catch (Exception e) {
            obj = new CraftSkillModification();
            CraftSkillModification.m_logger.error((Object)("Erreur lors d'un checkOut sur un objet de type EcosystemSkillModification : " + e.getMessage()));
        }
        return obj;
    }
    
    @Override
    public void release() {
        if (this.m_isCheckedOut) {
            try {
                CraftSkillModification.POOL.returnObject(this);
                this.m_isCheckedOut = false;
            }
            catch (Exception e) {
                CraftSkillModification.m_logger.error((Object)"Exception lors du retour au pool", (Throwable)e);
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
        final int oldValue = characteristics.getCraftCharacteristicEfficiency(this.m_craftSkillType, this.m_craftId);
        characteristics.modifyCraftCharacteristicEfficiency(this.m_craftSkillType, this.m_craftId, value);
        final int newValue = characteristics.getCraftCharacteristicEfficiency(this.m_craftSkillType, this.m_craftId);
        return newValue - oldValue;
    }
    
    @Override
    public RunningEffect<WakfuEffect, WakfuEffectContainer> newInstance() {
        final CraftSkillModification newInstance = checkOut();
        newInstance.m_craftId = this.m_craftId;
        newInstance.m_craftSkillType = this.m_craftSkillType;
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
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() == 2) {
            this.m_craftId = ((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
        else {
            this.m_craftSkillType = CraftSkillType.getById((byte)((WakfuEffect)this.m_genericEffect).getParam(1, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL));
            this.m_craftId = ((WakfuEffect)this.m_genericEffect).getParam(2, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL);
        }
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return CraftSkillModification.PARAMETERS_LIST_SET;
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.ADDITIONNAL_DATAS;
    }
    
    static {
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Defaut", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur de modification ", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Id du m\u00e9tier)", WakfuRunningEffectParameterType.CONFIG) }) });
        POOL = new MonitoredPool(new ObjectFactory<CraftSkillModification>() {
            @Override
            public CraftSkillModification makeObject() {
                return new CraftSkillModification();
            }
        });
    }
}
