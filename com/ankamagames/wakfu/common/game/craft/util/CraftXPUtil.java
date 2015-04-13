package com.ankamagames.wakfu.common.game.craft.util;

import com.ankamagames.wakfu.common.game.craft.reference.*;
import com.ankamagames.wakfu.common.account.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class CraftXPUtil
{
    private static final double XP_GAIN_DECAL_LEVEL = 10.0;
    private static final double XP_GAIN_DELTA_LEVEL = 10.0;
    public static final short MAX_PLAYER_LEVEL = 100;
    private static final float RECIPE_XP_BASE_RATIO = 100.0f;
    public static final int MAX_NON_SUBSCRIBED_LEVEL = 10;
    
    public static long getBaseXPGained() {
        return 100L;
    }
    
    public static long getXPGain(final short playerLevel, final short targetLevel, final ReferenceCraft craft, final WakfuAccountInformationHolder wakfuAccountInformationHolder) {
        return getXPGain(playerLevel, targetLevel, craft, 100, wakfuAccountInformationHolder);
    }
    
    public static long getXPGain(final short playerLevel, final short targetLevel, final ReferenceCraft craft, final int xpRatio, final WakfuAccountInformationHolder wakfuAccountInformationHolder) {
        if (!WakfuAccountPermissionContext.SUBSCRIBER.hasPermission(wakfuAccountInformationHolder) && playerLevel >= 10) {
            return 0L;
        }
        final double deltaClamp = playerLevel - (targetLevel + 10.0);
        if (deltaClamp > 10.0) {
            return 0L;
        }
        if (deltaClamp < 0.0) {
            return Math.round(100.0f * craft.getXpFactor() * xpRatio / 100.0f);
        }
        final double delta = targetLevel + 10.0 - playerLevel;
        final float xpGlobalRatio = craft.getXpFactor() * xpRatio / 100.0f;
        return Math.round(getBaseXPGained() / 2.0 * (1.0 + Math.cos(delta * 3.141592653589793 / 10.0)) * xpGlobalRatio);
    }
    
    public static long getTotalXPNeeded(final short level) {
        return 100L * level * level;
    }
    
    public static long getLevelXPNeeded(final short level) {
        return getTotalXPNeeded(level) - getTotalXPNeeded((short)(level - 1));
    }
    
    public static long getNextLevelRemainingXp(final long xp) {
        return getTotalXPNeeded((short)(getCurrentLevel(xp) + 1)) - xp;
    }
    
    public static double getLevel(final long totalXp) {
        return Math.sqrt(totalXp / 100.0);
    }
    
    public static short getCurrentLevel(final long totalXp) {
        return (short)Math.floor(getLevel(totalXp));
    }
    
    public static double getCurrentPercentLevel(final long totalXp) {
        final double level = getLevel(totalXp);
        return level - Math.floor(level);
    }
    
    public static ObjectPair<Short, Double> getDetailLevel(final long totalXp) {
        final double level = getLevel(totalXp);
        final short intLevel = (short)Math.floor(level);
        return new ObjectPair<Short, Double>(intLevel, level - intLevel);
    }
}
