package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;

final class UnapplyStateMessageHandler extends UsingFightMessageHandler<UnapplyStateMessage, Fight>
{
    @Override
    public boolean onMessage(final UnapplyStateMessage msg) {
        final WakfuRunningEffect runningEffect = this.createRunningEffect();
        runningEffect.fromBuild(msg.getSerializedEffect());
        final EffectUser target = runningEffect.getTarget();
        if (runningEffect.hasDuration() && target != null && target.getRunningEffectManager() != null) {
            target.getRunningEffectManager().removeEffect(runningEffect);
        }
        runningEffect.release();
        return false;
    }
    
    private WakfuRunningEffect createRunningEffect() {
        final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect = ((Constants<StaticRunningEffect<WakfuEffect, WakfuEffectContainer>>)RunningEffectConstants.getInstance()).getObjectFromId(RunningEffectConstants.RUNNING_STATE.getId());
        return (WakfuRunningEffect)staticEffect.newInstance(WakfuInstanceEffectContext.getInstance(), EffectManager.getInstance());
    }
}
