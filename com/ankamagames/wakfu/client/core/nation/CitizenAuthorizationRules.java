package com.ankamagames.wakfu.client.core.nation;

import com.ankamagames.wakfu.common.game.nation.impl.*;
import com.ankamagames.wakfu.common.game.nation.crime.constants.*;
import com.ankamagames.wakfu.common.game.pvp.*;
import com.ankamagames.wakfu.common.game.nation.*;
import com.ankamagames.wakfu.common.game.nation.crime.data.*;

public final class CitizenAuthorizationRules
{
    private static CitizenAuthorizationRules INSTANCE;
    
    public static CitizenAuthorizationRules getInstance() {
        return CitizenAuthorizationRules.INSTANCE;
    }
    
    public boolean hasRightToStandForElectionConcerningCitizenRank(final AbstractCitizenComportement citizen) {
        if (citizen == null) {
            return false;
        }
        final int citizenScore = citizen.getCitizenScoreForNation(citizen.getNationId());
        return CitizenRankManager.getInstance().getRankFromCitizenScore(citizenScore).hasRule(CitizenRankRule.CAN_STAND_FOR_ELECTIONS);
    }
    
    public boolean hasRightOfVoteConcerningCitizenRank(final AbstractCitizenComportement citizen) {
        if (citizen == null) {
            return false;
        }
        final int citizenScore = citizen.getCitizenScoreForNation(citizen.getNationId());
        return CitizenRankManager.getInstance().getRankFromCitizenScore(citizenScore).hasRule(CitizenRankRule.CAN_VOTE);
    }
    
    public boolean canEnterBag(final CitizenComportment comportment, final int currentNationId) {
        final Nation requesterNation = comportment.getNation();
        final NationAlignement alignement = requesterNation.getDiplomacyManager().getAlignment(currentNationId);
        if (alignement == NationAlignement.ENEMY) {
            return !NationPvpHelper.isPvpActive(comportment) && !comportment.isNationEnemy();
        }
        final int score = comportment.getCitizenScoreForNation(requesterNation.getNationId());
        final CitizenRank rank = CitizenRankManager.getInstance().getRankFromCitizenScore(score);
        return !rank.hasRule(CitizenRankRule.CANT_USE_PERSONNAL_SPACE);
    }
    
    static {
        CitizenAuthorizationRules.INSTANCE = new CitizenAuthorizationRules();
    }
}
