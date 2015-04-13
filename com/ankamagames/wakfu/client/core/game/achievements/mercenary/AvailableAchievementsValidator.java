package com.ankamagames.wakfu.client.core.game.achievements.mercenary;

import com.google.common.base.*;
import javax.annotation.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.achievements.*;

public class AvailableAchievementsValidator implements Predicate<Integer>
{
    public boolean apply(@Nullable final Integer input) {
        return input != null && WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().canResetAchievement(input) && WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().isAchievementActivable(input, WakfuGameEntity.getInstance().getLocalPlayer());
    }
}
