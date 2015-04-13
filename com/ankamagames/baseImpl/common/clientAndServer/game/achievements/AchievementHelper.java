package com.ankamagames.baseImpl.common.clientAndServer.game.achievements;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class AchievementHelper
{
    private static final GameDate TIME;
    
    public static boolean isActive(final AchievementInstance achievement, final GameDateConst now) {
        if (!achievement.isActive()) {
            return false;
        }
        if (achievement.getPeriodStartDate() == null) {
            return true;
        }
        final GameDateConst closestStartTime = now.closestDatePeriod(achievement.getPeriodStartDate(), achievement.getPeriod());
        AchievementHelper.TIME.set(achievement.getStartTime());
        if (closestStartTime.beforeOrEquals(AchievementHelper.TIME)) {
            return true;
        }
        achievement.setActive(false);
        return false;
    }
    
    public static boolean canResetAchievement(final AchievementInstance achievement, final GameDateConst now) {
        return (!achievement.isComplete() || achievement.isRepeatable()) && (achievement.isComplete() || !isActive(achievement, now)) && !isAchievementInCooldown(achievement, now) && !isAchievementInCurrentPeriod(achievement, now) && (!isFailed(achievement, now) || achievement.canRepeatIfFailed());
    }
    
    public static boolean isFailed(final AchievementInstance achievement, final GameDateConst now) {
        return !isActive(achievement, now) && achievement.getStartTime() != 0L;
    }
    
    public static boolean isAchievementInCurrentPeriod(final AchievementInstance achievement, final GameDateConst now) {
        if (achievement.getPeriodStartDate() == null || achievement.getStartTime() == 0L) {
            return false;
        }
        AchievementHelper.TIME.set(achievement.getStartTime());
        final GameDate closestStartTime = new GameDate(now.closestDatePeriod(achievement.getPeriodStartDate(), achievement.getPeriod()));
        return closestStartTime.beforeOrEquals(AchievementHelper.TIME);
    }
    
    public static boolean isAchievementInCooldown(final AchievementInstance achievement, final GameDateConst now) {
        if (achievement.getCooldown() != 0L && achievement.getLastCompletedTime() != 0L) {
            AchievementHelper.TIME.set(achievement.getLastCompletedTime());
            AchievementHelper.TIME.add((int)(achievement.getCooldown() / 1000L), 0, 0, 0);
            if (now.before(AchievementHelper.TIME)) {
                return true;
            }
            achievement.setLastCompletedTime(0L);
        }
        return false;
    }
    
    public static boolean isAchievementRunning(final AchievementInstance achievement, final GameDateConst now) {
        if (!achievement.isComplete() && achievement.getDuration() != 0 && achievement.getStartTime() != 0L) {
            AchievementHelper.TIME.set(achievement.getStartTime());
            AchievementHelper.TIME.add(achievement.getDuration() / 1000, 0, 0, 0);
            if (now.before(AchievementHelper.TIME)) {
                return true;
            }
        }
        return false;
    }
    
    public static GameIntervalConst getCoolDown(final AchievementInstance achievement, final GameDateConst now) {
        if (achievement.getLastCompletedTime() == 0L) {
            return GameIntervalConst.EMPTY_INTERVAL;
        }
        final GameDate time = GameDate.fromLong(achievement.getLastCompletedTime());
        time.add((int)(achievement.getCooldown() / 1000L), 0, 0, 0);
        return now.timeTo(time);
    }
    
    public static GameIntervalConst getPeriodCoolDown(final AchievementDefinition achievement, final GameDateConst now) {
        if (achievement.getPeriodStartDate() == null) {
            return GameIntervalConst.EMPTY_INTERVAL;
        }
        final GameDate closestStartTime = new GameDate(now.closestDatePeriod(achievement.getPeriodStartDate(), achievement.getPeriod()));
        closestStartTime.add(achievement.getPeriod());
        return now.timeTo(closestStartTime);
    }
    
    public static GameIntervalConst getRemainingTime(final AchievementInstance achievement, final GameDateConst now) {
        if (achievement.getDuration() == 0) {
            return GameIntervalConst.EMPTY_INTERVAL;
        }
        final GameDate time = GameDate.fromLong(achievement.getStartTime());
        time.add(achievement.getDuration() / 1000, 0, 0, 0);
        return now.timeTo(time);
    }
    
    public static GameIntervalConst getPeriodRemainingTime(final AchievementInstance achievement, final GameDateConst now) {
        if (achievement.getPeriodStartDate() == null) {
            return GameIntervalConst.EMPTY_INTERVAL;
        }
        AchievementHelper.TIME.set(achievement.getStartTime());
        final GameDate closestStartTime = new GameDate(now.closestDatePeriod(achievement.getPeriodStartDate(), achievement.getPeriod()));
        if (!closestStartTime.beforeOrEquals(AchievementHelper.TIME)) {
            return GameIntervalConst.EMPTY_INTERVAL;
        }
        closestStartTime.add(achievement.getPeriod());
        return now.timeTo(closestStartTime);
    }
    
    static {
        TIME = new GameDate(GameDate.getNullDate());
    }
}
