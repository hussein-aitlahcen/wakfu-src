package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.animation.*;
import com.ankamagames.wakfu.client.core.game.actor.*;

public class AnimationAction extends AbstractFightTimedAction
{
    private final CharacterInfo m_fighter;
    private final AttackType m_attackType;
    
    public AnimationAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final long casterId, final AttackType attack) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_fighter = this.getFighterById(casterId);
        this.m_attackType = attack;
    }
    
    @Override
    protected long onRun() {
        if (this.m_fighter == null) {
            return 0L;
        }
        if (!this.m_fighter.hasActor()) {
            return 0L;
        }
        final CharacterActor fighterActor = this.m_fighter.getActor();
        return fighterActor.applyFightAnimation(this.m_attackType);
    }
}
