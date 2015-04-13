package com.ankamagames.baseImpl.common.clientAndServer.game.fight.turnBased;

import com.ankamagames.baseImpl.common.clientAndServer.game.fight.*;

public class StartTurnEvent implements SpecialEvent
{
    @Override
    public int getId() {
        return 101;
    }
}
