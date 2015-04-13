package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.common.game.effect.runningEffect.*;
import com.ankamagames.wakfu.client.core.effect.*;
import com.ankamagames.wakfu.client.core.effect.manager.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;

final class ApplyStateMessageHandler extends UsingFightMessageHandler<ApplyStateMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final ApplyStateMessage msg) {
        final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect = ((Constants<StaticRunningEffect<WakfuEffect, WakfuEffectContainer>>)RunningEffectConstants.getInstance()).getObjectFromId(RunningEffectConstants.RUNNING_STATE.getId());
        final WakfuRunningEffect runningEffect = (WakfuRunningEffect)staticEffect.newInstance(WakfuInstanceEffectContext.getInstance(), EffectManager.getInstance());
        runningEffect.fromBuild(msg.getSerializedEffect());
        if (runningEffect.getTarget() != null && runningEffect.hasDuration()) {
            if (runningEffect.getTarget().getRunningEffectManager() != null) {
                runningEffect.getTarget().getRunningEffectManager().storeEffect(runningEffect);
            }
            runningEffect.onApplication();
            if (!runningEffect.mustBeTriggered()) {
                runningEffect.askForExecution();
            }
        }
        return false;
    }
}
