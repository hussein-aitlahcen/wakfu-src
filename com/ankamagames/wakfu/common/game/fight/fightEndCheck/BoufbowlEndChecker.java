package com.ankamagames.wakfu.common.game.fight.fightEndCheck;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;

public final class BoufbowlEndChecker implements FightEndChecker
{
    @Override
    public boolean fightShouldContinue(final BasicFight<? extends BasicCharacterInfo> fight) {
        return !fight.getFightersInPlay().isEmpty();
    }
}
