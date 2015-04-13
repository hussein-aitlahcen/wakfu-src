package com.ankamagames.wakfu.client.core.game.fight;

import com.ankamagames.wakfu.client.core.utils.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;

public class FightCountdown extends Countdown
{
    public FightCountdown() {
        super(10, 5, null);
    }
    
    @Override
    protected void setDuration(final int duration) {
        super.setDuration(duration);
        if (duration == 5) {
            UIFightFrame.getInstance().applyCountDownEnd();
        }
        if (duration <= 5) {
            UIFightTurnFrame.getInstance().countDownFeedback();
        }
    }
}
