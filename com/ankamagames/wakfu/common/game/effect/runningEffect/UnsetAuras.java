package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;
import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.effect.*;

public class UnsetAuras extends WakfuRunningEffect
{
    private static final ObjectPool m_staticPool;
    private static final ParameterListSet PARAMETERS_LIST_SET;
    private long m_newTargetId;
    
    @Override
    public ParameterListSet getParametersListSet() {
        return UnsetAuras.PARAMETERS_LIST_SET;
    }
    
    @Override
    public UnsetAuras newInstance() {
        UnsetAuras re;
        try {
            re = (UnsetAuras)UnsetAuras.m_staticPool.borrowObject();
            re.m_pool = UnsetAuras.m_staticPool;
        }
        catch (Exception e) {
            re = new UnsetAuras();
            re.m_pool = null;
            re.m_isStatic = false;
            UnsetAuras.m_logger.error((Object)("Erreur lors d'un checkOut sur un ArenaRunningEffect : " + e.getMessage()));
        }
        re.m_newTargetId = this.m_newTargetId;
        return re;
    }
    
    @Override
    protected void executeOverride(final RunningEffect linkedRE, final boolean trigger) {
        final ArrayList<RunningEffect> reToUnapply = new ArrayList<RunningEffect>();
        for (final RunningEffect runningEffect : this.m_target.getRunningEffectManager()) {
            if (runningEffect.getId() == RunningEffectConstants.SET_AURA.getId()) {
                reToUnapply.add(runningEffect);
            }
        }
        for (final RunningEffect runningEffect2 : reToUnapply) {
            runningEffect2.askForUnapplication();
        }
    }
    
    @Override
    public void effectiveComputeValue(final RunningEffect triggerRE) {
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
        return true;
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<UnsetAuras>() {
            @Override
            public UnsetAuras makeObject() {
                return new UnsetAuras();
            }
        });
        PARAMETERS_LIST_SET = new WakfuRunningEffectParameterListSet(new ParameterList[] { new WakfuRunningEffectParameterList("Param standard : toutes les auras", new WakfuRunningEffectParameter[0]) });
    }
}
