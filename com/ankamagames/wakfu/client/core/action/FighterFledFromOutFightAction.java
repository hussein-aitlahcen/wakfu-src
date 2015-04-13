package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.client.core.game.fight.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;

public final class FighterFledFromOutFightAction extends AbstractFightAction
{
    private long m_fighterId;
    
    public FighterFledFromOutFightAction(final int uniqueId, final int actionType, final int actionId, final int fightId) {
        super(uniqueId, actionType, actionId, fightId);
    }
    
    @Override
    protected void runCore() {
        final FightInfo fight = this.getFight();
        final ExternalFightInfo info = (ExternalFightInfo)fight;
        final CharacterInfo fighter = CharacterInfoManager.getInstance().getCharacter(this.m_fighterId);
        if (fighter == null) {
            FighterFledFromOutFightAction.m_logger.error((Object)("[_FL_] Le fighter inconnu id=" + this.m_fighterId + " quitte le fight externe id=" + fight.getId()));
            return;
        }
        info.removeAndReloadFighter(fighter);
        fighter.resetFighterAfterFight();
    }
    
    @Override
    protected void onActionFinished() {
    }
    
    public void setFighterId(final long fighterId) {
        this.m_fighterId = fighterId;
    }
}
