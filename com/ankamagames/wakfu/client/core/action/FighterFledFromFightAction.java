package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.wakfu.client.core.game.fight.*;

public class FighterFledFromFightAction extends AbstractFightAction
{
    private final long m_controllerId;
    
    public FighterFledFromFightAction(final int uniqueId, final int actionType, final int actionId, final int fightId, final long controllerId) {
        super(uniqueId, actionType, actionId, fightId);
        this.m_controllerId = controllerId;
    }
    
    @Override
    protected void runCore() {
        final FightInfo fightInfo = this.getFight();
        if (!(fightInfo instanceof Fight)) {
            return;
        }
        final Fight fight = (Fight)fightInfo;
        final CharacterInfo controller = fight.getFighterFromId(this.m_controllerId);
        controller.setFleeing(true);
        fight.fighterAbandonFight(this.m_controllerId);
        fight.onControllerRemovedFromFight(controller);
        if (controller instanceof PlayerCharacter) {
            controller.resetFighterAfterFight();
        }
    }
    
    @Override
    protected void onActionFinished() {
    }
}
