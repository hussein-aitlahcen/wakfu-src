package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.*;
import org.apache.commons.pool.*;

public final class RunningEffectGroupWithAtLeastNullEffect extends RunningEffectGroup
{
    private static final ObjectPool m_staticPool;
    
    public RunningEffectGroupWithAtLeastNullEffect() {
        super();
        this.setTriggersToExecute();
    }
    
    @Override
    public RunningEffectGroupWithAtLeastNullEffect newInstance() {
        RunningEffectGroupWithAtLeastNullEffect re;
        try {
            re = (RunningEffectGroupWithAtLeastNullEffect)RunningEffectGroupWithAtLeastNullEffect.m_staticPool.borrowObject();
            re.m_pool = RunningEffectGroupWithAtLeastNullEffect.m_staticPool;
        }
        catch (Exception e) {
            re = new RunningEffectGroupWithAtLeastNullEffect();
            re.m_pool = null;
            re.m_isStatic = false;
            RunningEffectGroupWithAtLeastNullEffect.m_logger.error((Object)("Erreur lors d'un checkOut sur un RunningEffectGroupWithAtLeastNullEffect : " + e.getMessage()));
        }
        return re;
    }
    
    @Override
    protected boolean canBeExecutedForEffectGroup(final WakfuEffect e, final int executionCountWithSuccess, final int executionCount) {
        return !this.isLastAndNullEffect(e) || executionCountWithSuccess < 1;
    }
    
    @Override
    public boolean useTarget() {
        return false;
    }
    
    @Override
    public boolean useTargetCell() {
        return true;
    }
    
    private boolean isLastAndNullEffect(final WakfuEffect e) {
        boolean isLastEffect = false;
        if (this.m_effectGroup != null) {
            final WakfuEffect lastEffect = this.m_effectGroup.getEffect(this.m_effectGroup.getEffectsCount() - 1);
            isLastEffect = (e == lastEffect);
        }
        final int actionId = e.getActionId();
        return isLastEffect && (actionId == RunningEffectConstants.NULL_EFFECT.getId() || actionId == RunningEffectConstants.NULL_EFFECT_ON_CELL.getId());
    }
    
    static {
        m_staticPool = new MonitoredPool(new ObjectFactory<RunningEffectGroupWithAtLeastNullEffect>() {
            @Override
            public RunningEffectGroupWithAtLeastNullEffect makeObject() {
                return new RunningEffectGroupWithAtLeastNullEffect();
            }
        });
    }
}
