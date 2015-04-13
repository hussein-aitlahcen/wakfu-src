package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class UpdateValue extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private int m_what;
    private boolean m_mustset;
    
    public UpdateValue() {
        super();
        this.m_what = -1;
        this.m_mustset = false;
    }
    
    @Override
    public ParameterListSet getParametersListSet() {
        return UpdateValue.PARAMETERS_LIST_SET;
    }
    
    @Override
    public UpdateValue newInstance() {
        UpdateValue re;
        try {
            re = (UpdateValue)UpdateValue.m_staticPool.borrowObject();
            re.m_pool = UpdateValue.m_staticPool;
        }
        catch (Exception e) {
            re = new UpdateValue();
            re.m_isStatic = false;
            re.m_pool = null;
            UpdateValue.m_logger.error((Object)("Erreur lors d'un checkOut sur un UpdateValue : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        if (linkedRE != null && this.m_what >= 0) {
            linkedRE.update(this.m_what, this.m_value, this.m_mustset);
        }
        this.setNotified(true);
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
        if (this.m_genericEffect != null) {
            final short level = this.getContainerLevel();
            this.m_what = ((WakfuEffect)this.m_genericEffect).getParam(0, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_value = ((WakfuEffect)this.m_genericEffect).getParam(1, level, RoundingMethod.LIKE_PREVIOUS_LEVEL);
            this.m_mustset = (((WakfuEffect)this.m_genericEffect).getParam(2, level, RoundingMethod.LIKE_PREVIOUS_LEVEL) != 0);
        }
    }
    
    @Override
    public void unapplyOverride() {
        super.unapplyOverride();
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
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<UpdateValue>() {
            @Override
            public UpdateValue makeObject() {
                return new UpdateValue();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("modifie la valeur courante de l'effet d\u00e9clencheur", new WakfuRunningEffectParameter[] { new WakfuRunningEffectParameter("0:HP %/1:HP... (RunningEffectUpdateType)", WakfuRunningEffectParameterType.CONFIG), new WakfuRunningEffectParameter("Valeur", WakfuRunningEffectParameterType.VALUE), new WakfuRunningEffectParameter("incremental(0) or set(1)", WakfuRunningEffectParameterType.CONFIG) }) });
    }
}
