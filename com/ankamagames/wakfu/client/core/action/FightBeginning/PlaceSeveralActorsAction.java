package com.ankamagames.wakfu.client.core.action.FightBeginning;

import com.ankamagames.framework.script.action.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import org.apache.log4j.*;
import gnu.trove.*;

public class PlaceSeveralActorsAction extends TimedAction
{
    private final TLongArrayList m_fighterToPlace;
    private boolean m_shouldReturnDurationForAction;
    public boolean m_teleportFighters;
    private static final int MAX_DISTANCE_FOR_MOVING_FIGHTERS = 10;
    
    public PlaceSeveralActorsAction(final int uniqueId, final int actionType, final int actionId, final TLongObjectHashMap<Point3> positionsByFighterId) {
        super(uniqueId, actionType, actionId);
        this.m_shouldReturnDurationForAction = false;
        this.m_teleportFighters = false;
        this.m_fighterToPlace = new TLongArrayList();
        final TLongObjectIterator<Point3> it = positionsByFighterId.iterator();
        while (it.hasNext()) {
            it.advance();
            final long fighterId = it.key();
            this.m_fighterToPlace.add(fighterId);
            FightPlacementManager.INSTANCE.setActionForFighter(fighterId, this);
        }
    }
    
    public long onRun() {
        try {
            final int longestPathDuration = this.moveFighters();
            if (!this.m_shouldReturnDurationForAction) {
                return 0L;
            }
            return longestPathDuration;
        }
        catch (Exception e) {
            PlaceSeveralActorsAction.m_logger.error((Object)"Exception levee", (Throwable)e);
            return 0L;
        }
    }
    
    public void setTeleportFighters(final boolean teleportFighters) {
        this.m_teleportFighters = teleportFighters;
    }
    
    private int moveFighters() {
        int longestPathDuration = 0;
        for (int i = 0, n = this.m_fighterToPlace.size(); i < n; ++i) {
            final long characterId = this.m_fighterToPlace.get(i);
            final CharacterInfo character = CharacterInfoManager.getInstance().getCharacter(characterId);
            if (character != null) {
                if (FightPlacementManager.INSTANCE.isFighterCorrespondingAction(characterId, this)) {
                    final Point3 destination = FightPlacementManager.INSTANCE.getPlacement(characterId);
                    FightPlacementManager.INSTANCE.removePlacement(characterId);
                    final CharacterActor actor = character.getActor();
                    if (this.m_teleportFighters || character.isActiveProperty(WorldPropertyType.TELEPORT_ON_ENTER_FIGHT)) {
                        final MovementSelector movementSelector = actor.getMovementSelector();
                        actor.setMovementSelector(NoneMovementSelector.getInstance());
                        character.teleport(destination.getX(), destination.getY(), destination.getZ());
                        actor.setMovementSelector(movementSelector);
                    }
                    else if (character.getPosition().getDistance(destination) > 10) {
                        character.teleport(destination.getX(), destination.getY(), destination.getZ());
                    }
                    else {
                        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                        if (character == localPlayer) {
                            this.m_shouldReturnDurationForAction = true;
                            localPlayer.setCanMoveAndInteract(false);
                            this.addEndPathListener(character);
                        }
                        final CharacterActor mobile = actor;
                        mobile.clearActiveParticleSystem();
                        final PathFindResult path = mobile.getPathResult(destination, false, false);
                        final int pathLength = path.getPathLength();
                        final int cellSpeed = mobile.getMovementSelector().selectMovementStyle(mobile, pathLength).getCellSpeed(mobile);
                        final int pathDuration = pathLength * cellSpeed;
                        if (path.isPathFound() && pathLength > 0) {
                            if (longestPathDuration < pathDuration) {
                                longestPathDuration = pathDuration;
                            }
                            mobile.applyPathResult(path, true);
                        }
                        else {
                            character.teleport(destination.getX(), destination.getY(), destination.getZ());
                        }
                    }
                }
            }
        }
        return longestPathDuration;
    }
    
    private void addEndPathListener(final CharacterInfo fighter) {
        final MobileEndPathListener listener = new MobileEndPathListener() {
            @Override
            public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                final Direction8 direction = fighter.getDirection();
                if (direction.isDirection4()) {
                    mobile.setDirection(direction);
                }
                else {
                    PlaceSeveralActorsAction.m_logger.error((Object)"LE MOBILE SE TROUVE DANS UNE DIRECTION 8 A LA FIN D'UN MOUVEMENT");
                    mobile.setDirection(direction.getNextDirection4(0));
                }
                PlaceSeveralActorsAction.this.enableFightMovementFrameIfLocalPlayerMoved(fighter);
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                if (PlaceSeveralActorsAction.this.m_fighterToPlace.contains(localPlayer.getId())) {
                    localPlayer.setCanMoveAndInteract(true);
                }
                mobile.removeEndPositionListener(this);
            }
        };
        fighter.getActor().addEndPositionListener(listener);
    }
    
    private void enableFightMovementFrameIfLocalPlayerMoved(final CharacterInfo fighter) {
        if (fighter.isControlledByLocalPlayer()) {
            UIFightMovementFrame.getInstance().enableMovement();
        }
    }
    
    @Override
    protected void onActionFinished() {
        final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
        if (this.m_fighterToPlace.contains(localPlayer.getId())) {
            localPlayer.setCanMoveAndInteract(true);
        }
    }
}
