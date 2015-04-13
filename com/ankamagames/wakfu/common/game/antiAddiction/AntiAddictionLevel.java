package com.ankamagames.wakfu.common.game.antiAddiction;

import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;
import com.ankamagames.framework.kernel.core.maths.*;
import org.jetbrains.annotations.*;
import org.apache.commons.lang3.*;

public enum AntiAddictionLevel
{
    LEVEL_0(1, 0, 1.0f, new int[0]), 
    LEVEL_1(2, 3600, 1.0f, new int[] { 97862 }), 
    LEVEL_2(3, 7200, 1.0f, new int[] { 119163 }), 
    LEVEL_3(4, 10800, 0.5f, new int[] { 119172 }), 
    LEVEL_4(5, 12600, 0.5f, new int[] { 119173 }), 
    LEVEL_5(6, 14400, 0.5f, new int[] { 119174 }), 
    LEVEL_6(7, 16200, 0.5f, new int[] { 119175 }), 
    LEVEL_7(8, 18000, 0.0f, new int[] { 119176 }), 
    LEVEL_INFINITE(9, 30000000, 0.0f, new int[0]);
    
    public static final GameIntervalConst RESET_ANTI_ADDICTION_DURATION;
    private static final AntiAddictionLevel[] LEVELS;
    private final byte m_id;
    private final GameIntervalConst m_durationThreshold;
    private float m_xpDropRatio;
    private final int[] m_effectIds;
    private static final GameInterval DURATION;
    
    private AntiAddictionLevel(final int id, final int durationThresholdInSecs, final float xpDropRatio, final int[] effectIds) {
        this.m_id = MathHelper.ensureByte(id);
        this.m_durationThreshold = GameInterval.fromSeconds(durationThresholdInSecs);
        this.m_xpDropRatio = xpDropRatio;
        this.m_effectIds = effectIds.clone();
    }
    
    @Nullable
    public static AntiAddictionLevel getFromId(final byte id) {
        for (final AntiAddictionLevel level : values()) {
            if (level.m_id == id) {
                return level;
            }
        }
        return null;
    }
    
    public GameIntervalConst getDurationThreshold() {
        return this.m_durationThreshold;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds.clone();
    }
    
    public static AntiAddictionLevel getForPlayDurationInSeconds(final int durationInSecs) {
        AntiAddictionLevel.DURATION.set(durationInSecs, 0, 0, 0);
        for (int i = AntiAddictionLevel.LEVELS.length - 1; i >= 0; --i) {
            final AntiAddictionLevel level = AntiAddictionLevel.LEVELS[i];
            if (level.m_durationThreshold.lowerThan(AntiAddictionLevel.DURATION)) {
                return level;
            }
        }
        return AntiAddictionLevel.LEVEL_0;
    }
    
    @Nullable
    public static AntiAddictionLevel getForEffectId(final int effectId) {
        if (effectId <= 0) {
            return null;
        }
        for (final AntiAddictionLevel level : AntiAddictionLevel.LEVELS) {
            if (ArrayUtils.contains(level.m_effectIds, effectId)) {
                return level;
            }
        }
        return null;
    }
    
    public float getXpDropRatio() {
        return this.m_xpDropRatio;
    }
    
    static {
        RESET_ANTI_ADDICTION_DURATION = new GameInterval(0, 0, 5, 0);
        LEVELS = values();
        DURATION = new GameInterval(0L);
    }
}
