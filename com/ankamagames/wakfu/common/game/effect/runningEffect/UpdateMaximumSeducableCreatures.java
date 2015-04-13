package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class UpdateMaximumSeducableCreatures extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_isIncrement;
    private byte m_oldValue;
    private final BinarSerialPart m_additionalData;
    
    public UpdateMaximumSeducableCreatures() {
        super();
        this.m_additionalData = new BinarSerialPart(2) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                buffer.put((byte)(UpdateMaximumSeducableCreatures.this.m_isIncrement ? 1 : 0));
                buffer.put(UpdateMaximumSeducableCreatures.this.m_oldValue);
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                UpdateMaximumSeducableCreatures.this.m_isIncrement = (buffer.get() == 1);
                UpdateMaximumSeducableCreatures.this.m_oldValue = buffer.get();
            }
        };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return UpdateMaximumSeducableCreatures.PARAMETERS_LIST_SET;
    }
    
    @Override
    public UpdateMaximumSeducableCreatures newInstance() {
        UpdateMaximumSeducableCreatures re;
        try {
            re = (UpdateMaximumSeducableCreatures)UpdateMaximumSeducableCreatures.m_staticPool.borrowObject();
            re.m_pool = UpdateMaximumSeducableCreatures.m_staticPool;
        }
        catch (Exception e) {
            re = new UpdateMaximumSeducableCreatures();
            re.m_pool = null;
            re.m_isStatic = false;
            UpdateMaximumSeducableCreatures.m_logger.error((Object)("Erreur lors d'un checkOut sur un SetGlyph : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (!(this.m_target instanceof SymbioticCharacter)) {
            UpdateMaximumSeducableCreatures.m_logger.error((Object)"Execution impossible, la cible n'est pas du bon type");
            this.setNotified(true);
            return;
        }
        final SymbioticCharacter target = (SymbioticCharacter)this.m_target;
        this.m_oldValue = target.getMaximumSeducableCreatures();
        target.setMaximumSeducableCreatures((byte)(this.m_isIncrement ? (this.m_oldValue + this.m_value) : this.m_value));
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_isIncrement = (((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) == 0);
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_executed && this.m_target instanceof SymbioticCharacter) {
            final SymbioticCharacter target = (SymbioticCharacter)this.m_target;
            target.setMaximumSeducableCreatures(this.m_oldValue);
        }
        super.unapplyOverride();
    }
    
    @Override
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.m_additionalData;
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
        super.onCheckIn();
        this.m_oldValue = 0;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<UpdateMaximumSeducableCreatures>() {
            @Override
            public UpdateMaximumSeducableCreatures makeObject() {
                return new UpdateMaximumSeducableCreatures();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Param standard", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("Type de la valeur (0:increment, 1:nouvelle valeur)", WakfuRunningEffectParameterType.VALUE) }) });
    }
}
