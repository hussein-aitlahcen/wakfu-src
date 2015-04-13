package com.ankamagames.wakfu.common.game.effect;

public final class WakfuEffectContainerUtils
{
    public static long getDefaultUid(final int id, final short level) {
        return (id << 16) + (level & 0xFF);
    }
    
    public static int getRefIdFromDefaultUid(final long uid) {
        return (int)(uid >> 16);
    }
    
    public static short getLevelFromDefaultUid(final long uid) {
        return (short)(uid & 0xFFL);
    }
}
