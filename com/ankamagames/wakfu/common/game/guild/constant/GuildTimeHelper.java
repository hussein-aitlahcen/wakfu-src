package com.ankamagames.wakfu.common.game.guild.constant;

import com.ankamagames.wakfu.common.game.guild.definition.*;
import com.ankamagames.wakfu.common.game.guild.bonus.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.maths.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class GuildTimeHelper
{
    public static GameDate getActivationDate(final GuildBonus b, final GuildBonusDefinition def, final double learningDurationFactor) {
        return getDate(b.getBuyDate(), def.getLearningDuration(), learningDurationFactor);
    }
    
    public static boolean isActive(final GuildBonus bonus) {
        return !bonus.getActivationDate().isNull();
    }
    
    public static boolean isLearning(final GuildBonus b, final GuildBonusDefinition def, final double learningDurationFactor) {
        if (isActive(b)) {
            return false;
        }
        final GameDate availabilityDate = getDate(b.getBuyDate(), def.getLearningDuration(), learningDurationFactor);
        return availabilityDate.after(WakfuGameCalendar.getInstance().getDate());
    }
    
    public static boolean isLearned(final GuildBonus b, final GuildBonusDefinition def, final double learningDurationFactor) {
        final GameDate availabilityDate = getDate(b.getBuyDate(), def.getLearningDuration(), learningDurationFactor);
        return availabilityDate.beforeOrEquals(WakfuGameCalendar.getInstance().getDate());
    }
    
    public static GameDate getDeactivationDate(final GuildBonus b, final GuildBonusDefinition def, final double durationFactor) {
        return getDate(b.getActivationDate(), def.getDuration(), durationFactor);
    }
    
    private static GameDate getDate(final GameDateConst start, final GameIntervalConst duration, final double durationFactor) {
        assert duration.isPositive() : "La dur\u00e9e devrait \u00eatre > 0";
        final GameDate d = new GameDate(start);
        d.add(new GameInterval(MathHelper.fastCeil(duration.toSeconds() * durationFactor)));
        return d;
    }
}
