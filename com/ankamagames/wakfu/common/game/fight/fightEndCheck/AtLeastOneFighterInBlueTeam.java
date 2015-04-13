package com.ankamagames.wakfu.common.game.fight.fightEndCheck;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public final class AtLeastOneFighterInBlueTeam implements FightEndChecker
{
    @Override
    public boolean fightShouldContinue(final BasicFight<? extends BasicCharacterInfo> fight) {
        final Collection<? extends BasicCharacterInfo> blueTeamFighters = fight.getFightersPresentInTimelineInPlayOrSimulatingInTeam((byte)1);
        return !blueTeamFighters.isEmpty();
    }
}
