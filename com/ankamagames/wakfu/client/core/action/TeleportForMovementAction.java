package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;
import com.ankamagames.wakfu.client.core.game.actor.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public class TeleportForMovementAction extends AbstractFightTimedAction
{
    private static final int MAX_DURATION_FOR_ACTION = 5000;
    private final long m_fighterId;
    private final Point3 m_position;
    private final String m_startAnim;
    private final String m_endAnim;
    
    public TeleportForMovementAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final long fighterId, final Point3 position) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_fighterId = fighterId;
        this.m_position = position;
        this.m_startAnim = "AnimMouvementTeleport-Debut";
        this.m_endAnim = "AnimMouvementTeleport-Fin";
    }
    
    public TeleportForMovementAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final long fighterId, final Point3 position, final String startAnim, final String endAnim) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_fighterId = fighterId;
        this.m_position = position;
        this.m_startAnim = startAnim;
        this.m_endAnim = endAnim;
    }
    
    @Override
    protected long onRun() {
        final CharacterInfo fighter = this.getFighterById(this.m_fighterId);
        if (fighter == null) {
            TeleportForMovementAction.m_logger.warn((Object)("[Fight] Reception d'un message de teleportpour un fighter inconnu du client, qui devrait pourtant etre dans son combat.  FightID = " + this.getFightId() + ", FighterID = " + this.m_fighterId));
            return 0L;
        }
        final CharacterActor actor = fighter.getActor();
        if (actor.containsAnimation(this.m_startAnim)) {
            actor.addAnimationEndedListener(new AnimationEndedListener() {
                @Override
                public void animationEnded(final AnimatedElement element) {
                    element.removeAnimationEndedListener(this);
                    TeleportForMovementAction.this.finishTeleport(fighter);
                }
            });
            actor.setAnimation(this.m_startAnim);
            this.onStartAnim(actor);
        }
        else {
            this.finishTeleport(fighter);
        }
        final int duration = Math.min(Math.abs(actor.getAnimationDuration(this.m_startAnim) + actor.getAnimationDuration(this.m_endAnim)), 5000);
        return duration;
    }
    
    protected void onStartAnim(final CharacterActor actor) {
    }
    
    private void finishTeleport(final CharacterInfo fighter) {
        fighter.teleport(this.m_position.getX(), this.m_position.getY(), this.m_position.getZ());
        final CharacterActor actor = fighter.getActor();
        if (actor.containsAnimation(this.m_endAnim)) {
            actor.setAnimation(this.m_endAnim);
            this.onEndAnim(fighter);
        }
    }
    
    protected void onEndAnim(final CharacterInfo actor) {
    }
    
    @Override
    protected void onActionFinished() {
        final FightInfo fight = this.getFight();
        if (fight instanceof Fight) {
            final CharacterInfo currentFighter = ((Fight)fight).getTimeline().getCurrentFighter();
            if (currentFighter != null && currentFighter.isControlledByLocalPlayer()) {
                UIFightMovementFrame.getInstance().clearPathLength();
                UIFightMovementFrame.getInstance().enableMovement();
            }
        }
        super.onActionFinished();
    }
}
