package com.ankamagames.wakfu.common.game.effect.runningEffect;

import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.maths.*;

public abstract class DynamicallyDefinedRunningEffect extends WakfuRunningEffect
{
    public void run(final WakfuEffect genericEffect, final EffectContainer container, final EffectContext context, final EffectUser launcher, final Point3 targetCell, final boolean forceNow) {
        throw new UnsupportedOperationException("DynamicallyDefinedRunningEffect can't use the 'run' method. Use 'apply' instead. " + this);
    }
}
