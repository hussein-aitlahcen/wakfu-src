package com.ankamagames.baseImpl.common.clientAndServer.game.effect;

import com.ankamagames.framework.kernel.core.common.*;

public abstract class EffectExecutionParameters implements Poolable
{
    public abstract void release();
    
    public abstract EffectExecutionParameters newInstance();
    
    public boolean resetLimitedApplyCount() {
        return true;
    }
}
