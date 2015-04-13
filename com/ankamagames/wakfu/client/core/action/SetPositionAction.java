package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public final class SetPositionAction extends AbstractFightTimedAction
{
    private long m_fighterId;
    private Point3 m_position;
    
    public SetPositionAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final long fighterId, final Point3 position) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_fighterId = fighterId;
        this.m_position = position;
    }
    
    @Override
    protected long onRun() {
        final CharacterInfo fighter = this.getFighterById(this.m_fighterId);
        if (fighter == null) {
            SetPositionAction.m_logger.warn((Object)("[Fight] Reception d'un message de synchronisation de position pour un fighter inconnu du client, qui devrait pourtant etre dans son combat.  FightID = " + this.getFightId() + ", FighterID = " + this.m_fighterId));
            return 0L;
        }
        SetPositionAction.m_logger.info((Object)("Resynchronisation de la position d'un fighter dans notre combat.  FightID = " + this.getFightId() + ", FighterID = " + this.m_fighterId + " \u00e0 la position " + this.m_position));
        fighter.setPosition(this.m_position);
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
