package com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.common;

import com.ankamagames.wakfu.client.network.protocol.frame.fight.messageHandler.*;
import com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.action.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.framework.script.action.*;
import com.ankamagames.framework.kernel.core.common.message.*;

final class FighterMoveMessageHandler extends UsingFightMessageHandler<FighterMoveMessage, Fight>
{
    @Override
    public boolean onMessage(final FighterMoveMessage msg) {
        final Fight fight = WakfuGameEntity.getInstance().getLocalPlayer().getCurrentOrObservedFight();
        final boolean isLocalFight = fight != null && fight.getId() == msg.getFightId();
        final PathFindResult pathResult = msg.getPathResult();
        Action action = null;
        FightMovementType movementType = msg.getMovementType();
        if (movementType == null) {
            movementType = FightMovementType.STANDARD;
        }
        switch (movementType) {
            case STANDARD:
            case ON_RAILS:
            case SMOOTH_MOVE_ON_AREA: {
                action = new MoveAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), msg.getFightId(), msg.getFighterId(), movementType, pathResult, Direction8.getDirectionFromIndex(msg.getDirectionAtEnd()), isLocalFight);
                break;
            }
            case TELEPORT: {
                final int[] lastStep = pathResult.getLastStep();
                final Point3 lastStepPos = new Point3(lastStep);
                action = new TeleportAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), msg.getFightId(), msg.getFighterId(), lastStepPos);
                break;
            }
            case TELEPORT_WITH_ANIMATION: {
                final int[] lastStep = pathResult.getLastStep();
                final Point3 lastStepPos = new Point3(lastStep);
                action = new TeleportForMovementAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), msg.getFightId(), msg.getFighterId(), lastStepPos);
                break;
            }
            case USE_GATE_TELEPORT: {
                final int[] lastStep = pathResult.getLastStep();
                final Point3 lastStepPos = new Point3(lastStep);
                action = new TeleportThroughGateMovementAction(msg.getUniqueId(), msg.getFightActionType().getId(), msg.getActionId(), msg.getFightId(), msg.getFighterId(), lastStepPos);
                break;
            }
        }
        FightActionGroupManager.getInstance().addActionToPendingGroup(msg.getFightId(), action);
        return false;
    }
    
    private enum MovementType
    {
        MOVE, 
        TELEPORT;
    }
}
