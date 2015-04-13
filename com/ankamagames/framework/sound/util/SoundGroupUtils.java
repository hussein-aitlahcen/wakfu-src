package com.ankamagames.framework.sound.util;

public class SoundGroupUtils
{
    public static long getSoundPrefix(final long id) {
        long soundPrefix;
        for (soundPrefix = Math.abs(id); soundPrefix >= 1000L; soundPrefix /= 10L) {}
        return soundPrefix;
    }
}
