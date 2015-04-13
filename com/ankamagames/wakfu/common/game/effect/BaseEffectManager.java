package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.wakfu.common.game.effect.genericEffect.*;
import org.jetbrains.annotations.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;

public abstract class BaseEffectManager extends AbstractEffectManager<WakfuEffect>
{
    @Nullable
    @Override
    public final WakfuEffect getEffect(final int effectId) {
        if (DefaultEffectManager.getInstance().isDefaultEffect(effectId)) {
            return DefaultEffectManager.getInstance().getDefaultEffect(effectId);
        }
        WakfuEffect wakfuEffect = super.getEffect(effectId);
        if (wakfuEffect == null) {
            wakfuEffect = this.createEffect(effectId);
            if (wakfuEffect != null) {
                this.addEffect(wakfuEffect);
            }
        }
        return wakfuEffect;
    }
    
    @Nullable
    protected abstract WakfuEffect createEffect(final int p0);
}
