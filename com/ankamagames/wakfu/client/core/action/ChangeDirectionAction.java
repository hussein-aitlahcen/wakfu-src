package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public class ChangeDirectionAction extends AbstractFightAction
{
    private final Direction8 m_direction;
    
    public ChangeDirectionAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final Direction8 direction) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_direction = direction;
    }
    
    @Override
    protected void runCore() {
        final CharacterInfo fighter = this.getFighterById(this.getInstigatorId());
        if (fighter == null) {
            return;
        }
        fighter.getActor().clearActiveParticleSystem();
        fighter.setDirection(this.m_direction);
    }
    
    @Override
    protected void onActionFinished() {
        final CharacterInfo instigator = this.getFighterById(this.getInstigatorId());
        final CharacterInfo target = this.getFighterById(this.getTargetId());
        ScriptedActionUtils.refreshShortcutsIfNecessary(instigator, target);
    }
}
