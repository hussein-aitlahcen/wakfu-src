package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.outFight;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.action.FightBeginning.*;
import com.ankamagames.framework.kernel.utils.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class EndFightCreationMessageHandler extends UsingFightMessageHandler<EndFightCreationMessage, ExternalFightInfo>
{
    @Override
    public boolean onMessage(final EndFightCreationMessage msg) {
        final int fightId = msg.getFightId();
        if (ExternalFightCreationActions.INSTANCE.m_fightIsInFightCreation.contains(fightId)) {
            if (ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions1.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions1.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_playAnimationAction1.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_playAnimationAction1.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions2.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions2.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_playAnimationAction2.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_playAnimationAction2.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_tauntAction1.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_tauntAction1.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions3.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions3.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions4.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions4.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_tauntAction2.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_tauntAction2.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_displayCharacterCircleAction.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, (Action)ExternalFightCreationActions.INSTANCE.m_displayCharacterCircleAction.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_characterMoveWithEndedListenerAction.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_characterMoveWithEndedListenerAction.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_characterTeleportAction.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_characterTeleportAction.get(fightId));
            }
            if (ExternalFightCreationActions.INSTANCE.m_startPlacementAction.get(fightId) != null) {
                FightActionGroupManager.getInstance().addActionToPendingGroup(fightId, ExternalFightCreationActions.INSTANCE.m_startPlacementAction.get(fightId));
            }
            TroveUtils.removeFirstValue(ExternalFightCreationActions.INSTANCE.m_fightIsInFightCreation, fightId);
            ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions1.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions2.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions3.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_lookAtTheFoeActions4.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_playAnimationAction1.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_playAnimationAction2.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_startPlacementAction.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_tauntAction1.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_tauntAction2.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_displayCharacterCircleAction.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_characterMoveWithEndedListenerAction.remove(fightId);
            ExternalFightCreationActions.INSTANCE.m_characterTeleportAction.remove(fightId);
            final FightInfo fight = FightManager.getInstance().getFightById(fightId);
            FightActionGroupManager.getInstance().executePendingGroup(fight);
            return false;
        }
        return true;
    }
}
