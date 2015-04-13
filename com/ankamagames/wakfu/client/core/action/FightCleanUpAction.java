package com.ankamagames.wakfu.client.core.action;

import com.ankamagames.wakfu.common.game.fight.*;
import com.ankamagames.wakfu.client.sound.*;

public class FightCleanUpAction extends AbstractFightTimedAction
{
    private static final int REPORT_DELAY = 1000;
    
    public FightCleanUpAction(final int uniqueId, final int actionId, final int fightId) {
        super(uniqueId, FightActionType.FIGHT_CLEANUP_ACTION.getId(), actionId, fightId);
    }
    
    @Override
    protected long onRun() {
        return 1000L;
    }
    
    @Override
    protected void onActionFinished() {
        WakfuSoundManager.getInstance().exitFight();
        super.onActionFinished();
    }
}
