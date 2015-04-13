package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;

final class RunningEffectUnapplicationMessageHandler extends UsingFightMessageHandler<RunningEffectUnapplicationMessage, Fight>
{
    private static Logger m_logger;
    
    @Override
    public boolean onMessage(final RunningEffectUnapplicationMessage msg) {
        final int actionTypeId = msg.getFightActionType().getId();
        final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect = ((Constants<StaticRunningEffect<WakfuEffect, WakfuEffectContainer>>)RunningEffectConstants.getInstance()).getObjectFromId(msg.getRunningEffectId());
        if (staticEffect == null) {
            RunningEffectUnapplicationMessageHandler.m_logger.error((Object)("Impossible d'instancier un runningEffect :" + msg.getRunningEffectId() + " inconnu"));
            return false;
        }
        final UnApplyEffectAction spellEffectAction = new UnApplyEffectAction(msg.getUniqueId(), actionTypeId, msg.getFightId(), msg.getActionId(), staticEffect, msg.getRuid(), msg.getTargetId(), msg.getSerializedRunningEffect());
        spellEffectAction.setTriggerActionUniqueId(msg.getTriggeringActionUniqueId());
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), spellEffectAction);
        return false;
    }
    
    static {
        RunningEffectUnapplicationMessageHandler.m_logger = Logger.getLogger((Class)RunningEffectUnapplicationMessageHandler.class);
    }
}
