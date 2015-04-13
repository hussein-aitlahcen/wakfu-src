package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.datas.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class SetAI extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return SetAI.PARAMETERS_LIST_SET;
    }
    
    @Override
    public SetAI newInstance() {
        SetAI re;
        try {
            re = (SetAI)SetAI.m_staticPool.borrowObject();
            re.m_pool = SetAI.m_staticPool;
        }
        catch (Exception e) {
            re = new SetAI();
            re.m_pool = null;
            re.m_isStatic = false;
            SetAI.m_logger.error((Object)("Erreur lors d'un newInstance sur SetAI : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (this.m_target != null && this.m_target instanceof BasicCharacterInfo) {
            ((BasicCharacterInfo)this.m_target).setControlledByAI(this.m_value == 1);
        }
    }
    
    @Override
    public void unapplyOverride() {
        if (this.m_target != null && this.m_target instanceof BasicCharacterInfo) {
            final BasicCharacterInfo target = (BasicCharacterInfo)this.m_target;
            target.setControlledByAI(target.returnDefaultAIControl());
        }
        super.unapplyOverride();
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        final short level = this.getContainerLevel();
        if (this.m_genericEffect != null) {
            final int value = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.RANDOM);
            if (value > 0) {
                this.m_value = 1;
            }
            else {
                this.m_value = 0;
            }
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<SetAI>() {
            @Override
            public SetAI makeObject() {
                return new SetAI();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Normal", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("ia (1) ou pas d'ia (0)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
