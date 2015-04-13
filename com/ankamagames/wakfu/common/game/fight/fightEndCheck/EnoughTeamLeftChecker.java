package com.ankamagames.wakfu.common.game.fight.fightEndCheck;

import org.apache.log4j.*;
import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.utils.*;
import java.util.*;

public final class EnoughTeamLeftChecker implements FightEndChecker
{
    private static final Logger m_logger;
    
    @Override
    public boolean fightShouldContinue(final BasicFight<? extends BasicCharacterInfo> fight) {
        boolean enoughTeamsLeft = false;
        final TByteHashSet remainingTeams = new TByteHashSet();
        final Collection<? extends BasicCharacterInfo> inPlayFighters = fight.getInPlayOrSimulatingFighters();
        for (final BasicCharacterInfo f : inPlayFighters) {
            if (f.getTeamId() == -1) {
                EnoughTeamLeftChecker.m_logger.error((Object)fight.withFightId("[FIGHT_REFACTOR] On a un fighter inPlay avec un teamId \u00e0 -1 " + f + " - " + ExceptionFormatter.currentStackTrace(8)));
            }
            else {
                remainingTeams.add(f.getTeamId());
                enoughTeamsLeft |= (remainingTeams.size() >= fight.getMinTeam());
                if (enoughTeamsLeft) {
                    break;
                }
                continue;
            }
        }
        return enoughTeamsLeft;
    }
    
    static {
        m_logger = Logger.getLogger((Class)EnoughTeamLeftChecker.class);
    }
}
