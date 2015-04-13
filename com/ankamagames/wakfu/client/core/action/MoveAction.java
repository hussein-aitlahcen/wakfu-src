package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.ai.pathfinder.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.movementSelector.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.movementStyle.*;
import com.ankamagames.wakfu.client.alea.animation.*;
import com.ankamagames.wakfu.common.datas.Breed.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.baseImpl.graphics.alea.mobile.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;
import com.ankamagames.wakfu.client.core.game.fight.*;
import java.util.*;

public class MoveAction extends AbstractFightTimedAction
{
    private static final int ELIATROPE_TP_PATH_LENGTH_MIN = 3;
    private static final int DEFAULT_ELIATROPE_TP_DURATION = 1200;
    private PathFindResult m_path;
    private final Direction8 m_directionAtEnd;
    private final boolean m_localFight;
    private final FightMovementType m_fightMovementType;
    
    public MoveAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final long targetId, final FightMovementType movementType, final PathFindResult path, final Direction8 directionAtEnd, final boolean isLocalFight) {
        super(uniqueId, actionType, actionId, fightId);
        this.setTargetId(targetId);
        this.m_fightMovementType = movementType;
        this.m_path = path;
        this.m_directionAtEnd = directionAtEnd;
        this.m_localFight = isLocalFight;
    }
    
    public long onRun() {
        if (this.m_path == null) {
            return 0L;
        }
        final CharacterInfo fighter = this.getFighterById(this.getTargetId());
        if (fighter == null) {
            return 0L;
        }
        final CharacterActor mobile = fighter.getActor();
        mobile.clearActiveParticleSystem();
        final Carrier carrier = this.uncarryFighter(fighter, mobile);
        int uncarryDuration;
        if (carrier != null && carrier instanceof CharacterInfo) {
            final CharacterActor actor = ((CharacterInfo)carrier).getActor();
            actor.addAnimationEndedListener(new AnimationEndedListener() {
                @Override
                public void animationEnded(final AnimatedElement element) {
                    MoveAction.this.playMovement(fighter, mobile);
                    element.removeAnimationEndedListener(this);
                }
            });
            uncarryDuration = actor.getAnimationDuration("Anim04Porte");
            this.m_path = this.m_path.subPath(1, this.m_path.getPathLength());
        }
        else {
            this.playMovement(fighter, mobile);
            uncarryDuration = 0;
        }
        final int cellSpeed = mobile.getMovementSelector().selectMovementStyle(mobile, this.m_path.getPathLength()).getCellSpeed(mobile);
        if (fighter.getLinkedCharacter() != null && ((CharacterInfo)fighter.getLinkedCharacter()).getActor().getCurrentPath() == null) {
            return 0L;
        }
        int duration = this.computeActionDuration(fighter, uncarryDuration, cellSpeed);
        if (this.m_fightMovementType == FightMovementType.SMOOTH_MOVE_ON_AREA) {
            duration -= cellSpeed + 75;
        }
        return duration;
    }
    
    private int computeActionDuration(final CharacterInfo fighter, final int uncarryDuration, final int cellSpeed) {
        if (!this.isEliotropeTpMove(fighter)) {
            return uncarryDuration + this.m_path.getPathLength() * cellSpeed;
        }
        final Mobile mobile = fighter.getActor();
        final int tpAnimsDuration = mobile.getAnimationDuration("AnimCourseTp02") + mobile.getAnimationDuration("AnimCourseTp02-Fin");
        if (tpAnimsDuration == 0 || tpAnimsDuration == Integer.MAX_VALUE) {
            return 1200;
        }
        return tpAnimsDuration;
    }
    
    private void playMovement(final CharacterInfo fighter, final CharacterActor mobile) {
        if (this.m_path.getPathLength() == 0) {
            return;
        }
        if (this.m_fightMovementType == FightMovementType.ON_RAILS) {
            mobile.setMovementSelector(CustomMovementSelector.create(true, mobile, OnRailsMovementStyle.getInstance(), OnRailsMovementStyle.getInstance()));
        }
        if (this.m_fightMovementType == FightMovementType.SMOOTH_MOVE_ON_AREA) {
            mobile.setMovementSelector(CustomMovementSelector.create(true, mobile, RunInFightMovementStyle.getInstance(), RunInFightMovementStyle.getInstance()));
        }
        if (this.isEliotropeTpMove(fighter)) {
            mobile.setAnimation("AnimCourseTp02");
            mobile.addAnimationEndedListener(new EliotropeTpAnimationEndedListener(mobile, fighter));
            return;
        }
        mobile.setPath(this.m_path, true, fighter.getCurrentFight() == null);
        if (fighter.isOnFight()) {
            this.addEndPathListener(fighter, mobile);
        }
        if (this.m_localFight) {
            UIFightCameraFrame.getInstance().onFighterMoved(fighter);
        }
    }
    
    private boolean isEliotropeTpMove(final CharacterInfo fighter) {
        return fighter.getBreed() == AvatarBreed.ELIOTROPE && this.m_fightMovementType == FightMovementType.STANDARD && this.m_path.getPathLength() > 3;
    }
    
    private Carrier uncarryFighter(final CharacterInfo fighter, final CharacterActor mobile) {
        final Carrier carrier = fighter.getCarrier();
        if (carrier == null) {
            return null;
        }
        int[] cell = this.m_path.getFirstStep();
        final Point3 here = new Point3(cell[0], cell[1], (short)cell[2]);
        if (here.equals(fighter.getPosition()) && this.m_path.getPathLength() > 1) {
            cell = this.m_path.getPathStep(1);
            here.set(cell);
        }
        carrier.uncarryTo(here);
        UIFightMovementFrame.getInstance().enableMovement();
        return carrier;
    }
    
    private void addEndPathListener(final CharacterInfo fighter, final CharacterActor mobile) {
        final MobileEndPathListener listener = new MobileEndPathListener() {
            @Override
            public void pathEnded(final PathMobile mobile, final int x, final int y, final short altitude) {
                MoveAction.this.endMoveOperations(mobile, fighter);
                mobile.removeEndPositionListener(this);
            }
        };
        mobile.addEndPositionListener(listener);
    }
    
    private void endMoveOperations(final PathMobile mobile, final CharacterInfo fighter) {
        this.changeDirection(mobile, fighter);
        this.enableFightMovementFrameIfLocalPlayerMoved(fighter);
        this.fireClientEvent(fighter);
    }
    
    private void fireClientEvent(final CharacterInfo fighter) {
        if (WakfuGameEntity.getInstance().getLocalPlayer() == fighter) {
            ClientGameEventManager.INSTANCE.fireEvent(new ClientEventMoveInFight());
        }
    }
    
    private void changeDirection(final PathMobile mobile, final CharacterInfo fighter) {
        final Direction8 direction = fighter.getDirection();
        if (direction.isDirection4()) {
            mobile.setDirection(direction);
        }
        else {
            MoveAction.m_logger.error((Object)"LE MOBILE SE TROUVE DANS UNE DIRECTION 8 A LA FIN D'UN MOUVEMENT");
            mobile.setDirection(direction.getNextDirection4(0));
        }
    }
    
    private void enableFightMovementFrameIfLocalPlayerMoved(final CharacterInfo fighter) {
        if (fighter.isControlledByLocalPlayer() && !fighter.isControlledByAI()) {
            UIFightMovementFrame.getInstance().enableMovement();
        }
    }
    
    @Override
    protected void onActionFinished() {
        if (this.getFight() instanceof Fight) {
            ((Fight)this.getFight()).onFighterMove(this.getFighterById(this.getTargetId()), null, FightMovementType.STANDARD);
        }
        final CharacterInfo fighter = this.getFighterById(this.getTargetId());
        if (fighter != null) {
            fighter.setDirectionWithoutNotifyActor(this.m_directionAtEnd);
        }
        super.onActionFinished();
    }
    
    private class EliotropeTpAnimationEndedListener implements AnimationEndedListener
    {
        private final CharacterActor m_mobile;
        private final CharacterInfo m_fighter;
        
        public EliotropeTpAnimationEndedListener(final CharacterActor mobile, final CharacterInfo fighter) {
            super();
            this.m_mobile = mobile;
            this.m_fighter = fighter;
        }
        
        @Override
        public void animationEnded(final AnimatedElement element) {
            final int[] lastStep = MoveAction.this.m_path.getLastStep();
            final int pathLength = MoveAction.this.m_path.getPathLength();
            final Point3 endPos = new Point3(lastStep);
            if (pathLength <= 1) {
                this.m_fighter.setDirection(this.m_fighter.getPosition().getDirection4To(endPos));
            }
            else {
                this.m_fighter.setDirection(new Point3(MoveAction.this.m_path.getPathStep(pathLength - 2)).getDirection4To(endPos));
            }
            this.m_fighter.setPosition(endPos);
            this.m_mobile.setWorldPosition(lastStep[0], lastStep[1], lastStep[2]);
            MoveAction.this.endMoveOperations(this.m_mobile, this.m_fighter);
            this.m_mobile.setAnimation("AnimCourseTp02-Fin");
            this.m_mobile.removeAnimationEndedListener(this);
        }
    }
}
