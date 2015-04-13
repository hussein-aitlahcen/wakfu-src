package com.ankamagames.wakfu.common.game.fight.fightEndCheck;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.common.datas.*;

public interface FightEndChecker
{
    boolean fightShouldContinue(BasicFight<? extends BasicCharacterInfo> p0);
}
