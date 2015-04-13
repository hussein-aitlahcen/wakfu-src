package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.framework.kernel.core.common.serialization.*;
import java.nio.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class ChangeController extends SpellsNeededForIAEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private boolean m_targetOriginallyControlledByIA;
    private boolean m_useCasterController;
    private final BinarSerialPart m_additionalData;
    
    public ChangeController() {
        super();
        this.m_additionalData = new BinarSerialPart(1) {
            @Override
            public void serialize(final ByteBuffer buffer) {
                ChangeController.this.m_targetSpellInventory.serialize(buffer);
                buffer.put((byte)(ChangeController.this.m_useCasterController ? 1 : 0));
            }
            
            @Override
            public void unserialize(final ByteBuffer buffer, final int version) {
                ChangeController.this.m_targetSpellInventory.unserialize(buffer, Version.SERIALIZATION_VERSION);
                ChangeController.this.m_useCasterController = (buffer.get() == 1);
            }
            
            @Override
            public int expectedSize() {
                return ChangeController.this.m_targetSpellInventory.expectedSize() + 1;
            }
        };
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return ChangeController.PARAMETERS_LIST_SET;
    }
    
    @Override
    public ChangeController newInstance() {
        ChangeController re;
        try {
            re = (ChangeController)ChangeController.m_staticPool.borrowObject();
            re.m_pool = ChangeController.m_staticPool;
        }
        catch (Exception e) {
            re = new ChangeController();
            re.m_pool = null;
            re.m_isStatic = false;
            ChangeController.m_logger.error((Object)("Erreur lors d'un newInstance sur ChangeController : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect triggerRE, final boolean trigger) {
        if (this.m_caster == null || this.m_target == null) {
            this.setNotified();
            return;
        }
        if (!(this.m_caster instanceof BasicCharacterInfo)) {
            return;
        }
        final BasicCharacterInfo caster = (BasicCharacterInfo)this.m_caster;
        long newControllerId;
        if (this.m_useCasterController && caster.getController() != null) {
            newControllerId = caster.getController().getId();
        }
        else {
            newControllerId = this.m_caster.getId();
        }
        caster.setCurrentController(this.m_target.getId(), newControllerId);
        if (this.isValueComputationEnabled()) {
            this.createSpellInventoryForController();
        }
        this.m_targetOriginallyControlledByIA = ((BasicCharacterInfo)this.m_target).isControlledByAI();
        if (((BasicCharacterInfo)this.m_caster).isControlledByAI()) {
            ((BasicCharacterInfo)this.m_target).setControlledByAI(true);
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_executed && this.m_target instanceof BasicCharacterInfo) {
            final BasicCharacterInfo controlled = (BasicCharacterInfo)this.m_target;
            controlled.returnToOriginalController();
            controlled.setControlledByAI(this.m_targetOriginallyControlledByIA);
        }
        super.unapplyOverride();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect == null) {
            return;
        }
        if (((WakfuEffect)this.m_genericEffect).getParamsCount() >= 1) {
            this.m_useCasterController = (((WakfuEffect)this.m_genericEffect).getParam(0, this.getContainerLevel(), RoundingMethod.LIKE_PREVIOUS_LEVEL) == 1);
        }
        else {
            this.m_useCasterController = false;
        }
    }
    
    @Override
    public boolean useCaster() {
        return true;
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
    public BinarSerialPart getAdditionalDatasBinarSerialPart() {
        return this.m_additionalData;
    }
    
    @Override
    public void onCheckIn() {
        this.m_useCasterController = false;
        super.onCheckIn();
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<ChangeController>() {
            @Override
            public ChangeController makeObject() {
                return new ChangeController();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Normal", new WakfuRunningEffectParameter[0]), new WakfuRunningEffectParameterList("Original controleur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("Controleur courant du caster (0 = non, 1 = oui)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
