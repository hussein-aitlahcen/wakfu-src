package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import org.apache.log4j.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.effect.runningEffect.*;
import com.ankamagames.wakfu.common.game.effect.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.script.*;
import com.ankamagames.framework.script.libraries.scriptedAction.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.baseImpl.common.clientAndServer.utils.*;

final class RunningEffectActionMessageHandler extends UsingFightMessageHandler<RunningEffectActionMessage, Fight>
{
    private static final Logger LOGGER;
    
    @Override
    public boolean onMessage(final RunningEffectActionMessage msg) {
        final int actionTypeId = msg.getFightActionType().getId();
        final StaticRunningEffect<WakfuEffect, WakfuEffectContainer> staticEffect = ((Constants<StaticRunningEffect<WakfuEffect, WakfuEffectContainer>>)RunningEffectConstants.getInstance()).getObjectFromId(msg.getRunningEffectId());
        if (staticEffect == null) {
            RunningEffectActionMessageHandler.LOGGER.error((Object)("Impossible d'instancier un runningEffect :" + msg.getRunningEffectId() + " inconnu"));
            return false;
        }
        if (msg.getSerializedTargetsWithCorrespondingAction() != null && !msg.getSerializedTargetsWithCorrespondingAction().isEmpty()) {
            for (final ObjectTriplet<Integer, Integer, byte[]> serializedTargetWithCorrespondingAction : msg.getSerializedTargetsWithCorrespondingAction()) {
                final SpellEffectAction spellEffectAction = new SpellEffectAction(msg.getUniqueId(), actionTypeId, msg.getFightId(), serializedTargetWithCorrespondingAction.getFirst(), staticEffect, msg.getSerializedRunningEffect(), msg.isTriggered(), serializedTargetWithCorrespondingAction.getThird());
                spellEffectAction.setTriggerActionUniqueId(serializedTargetWithCorrespondingAction.getSecond());
                spellEffectAction.initEffectCore();
                FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), spellEffectAction);
                spellEffectAction.addJavaFunctionsLibrary(new ScriptedActionFunctionsLibrary(spellEffectAction.getGroup()));
            }
        }
        else {
            final SpellEffectAction spellEffectAction2 = new SpellEffectAction(msg.getUniqueId(), actionTypeId, msg.getFightId(), msg.getActionId(), staticEffect, msg.getSerializedRunningEffect(), msg.isTriggered(), null);
            spellEffectAction2.setTriggerActionUniqueId(msg.getTriggeringActionUniqueId());
            spellEffectAction2.initEffectCore();
            FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), spellEffectAction2);
            spellEffectAction2.addJavaFunctionsLibrary(new ScriptedActionFunctionsLibrary(spellEffectAction2.getGroup()));
        }
        return false;
    }
    
    static {
        LOGGER = Logger.getLogger((Class)RunningEffectActionMessageHandler.class);
    }
}
