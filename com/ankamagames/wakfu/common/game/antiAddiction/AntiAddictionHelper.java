package com.ankamagames.wakfu.common.game.antiAddiction;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class AntiAddictionHelper
{
    public static AntiAddictionLevel getCurrentLevel(final GameDateConst lastConnectionDate, final GameIntervalConst usedQuota, final GameDateConst now) {
        final GameInterval onlineTime = lastConnectionDate.timeTo(now);
        onlineTime.add(usedQuota);
        return AntiAddictionLevel.getForPlayDurationInSeconds((int)onlineTime.toSeconds());
    }
    
    public static GameIntervalConst getCurrentUsedTime(final GameIntervalConst usedQuota) {
        final GameInterval remainingUsedQuota = new GameInterval(usedQuota);
        return remainingUsedQuota;
    }
}
