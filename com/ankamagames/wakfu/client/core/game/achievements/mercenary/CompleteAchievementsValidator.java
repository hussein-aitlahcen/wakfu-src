package com.ankamagames.wakfu.client.core.game.achievements.mercenary;

import com.google.common.base.*;
import javax.annotation.*;
import com.ankamagames.wakfu.client.core.*;

public class CompleteAchievementsValidator implements Predicate<Integer>
{
    public boolean apply(@Nullable final Integer input) {
        return input != null && WakfuGameEntity.getInstance().getLocalPlayer().getAchievementsContext().isAchievementComplete(input);
    }
}
