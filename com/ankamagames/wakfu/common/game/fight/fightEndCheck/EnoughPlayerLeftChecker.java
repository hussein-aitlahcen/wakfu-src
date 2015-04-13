package com.ankamagames.wakfu.common.game.fight.fightEndCheck;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;
import java.util.*;

public final class EnoughPlayerLeftChecker implements FightEndChecker
{
    @Override
    public boolean fightShouldContinue(final BasicFight<? extends BasicCharacterInfo> fight) {
        final Collection<? extends BasicCharacterInfo> inPlayFighters = fight.getInPlayOrSimulatingFighters();
        for (final BasicCharacterInfo fighter : inPlayFighters) {
            if (fighter.getType() == 0) {
                return true;
            }
        }
        return false;
    }
}
