package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public final class TeleportAction extends AbstractFightTimedAction
{
    private long m_fighterId;
    private Point3 m_position;
    
    public TeleportAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final long fighterId, final Point3 position) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_fighterId = fighterId;
        this.m_position = position;
    }
    
    @Override
    protected long onRun() {
        final CharacterInfo fighter = this.getFighterById(this.m_fighterId);
        if (fighter == null) {
            TeleportAction.m_logger.warn((Object)("[Fight] Reception d'un message de teleportpour un fighter inconnu du client, qui devrait pourtant etre dans son combat.  FightID = " + this.getFightId() + ", FighterID = " + this.m_fighterId));
            return 0L;
        }
        fighter.teleport(this.m_position.getX(), this.m_position.getY(), this.m_position.getZ());
        return 0L;
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
