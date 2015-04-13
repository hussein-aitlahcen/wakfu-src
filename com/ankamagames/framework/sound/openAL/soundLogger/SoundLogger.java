package com.ankamagames.framework.sound.openAL.soundLogger;

import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public class SoundLogger
{
    private static final Hashtable<String, SoundLogAppender> m_appenders;
    
    public static void log(final String message, final byte parentId) {
        for (final SoundLogAppender soundLogAppender : SoundLogger.m_appenders.values()) {
            soundLogAppender.append(new ObjectPair<Byte, String>(parentId, message));
        }
    }
    
    public static void unregisterAppender(final Class clazz) {
        SoundLogger.m_appenders.remove(clazz.getName());
    }
    
    public static SoundLogAppender registerAppender(final Class clazz, final SoundLogAppender soundLogAppender) {
        final String name = clazz.getName();
        if (SoundLogger.m_appenders.containsKey(name)) {
            return SoundLogger.m_appenders.get(name);
        }
        return SoundLogger.m_appenders.put(name, soundLogAppender);
    }
    
    static {
        m_appenders = new Hashtable<String, SoundLogAppender>();
    }
}
