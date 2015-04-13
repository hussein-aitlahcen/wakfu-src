package com.ankamagames.wakfu.client.core.game.steam;

import org.apache.log4j.*;
import com.ankamagames.steam.wrapper.*;

public class SteamLogger
{
    private static final Logger m_logger;
    
    public static void log(final String message) {
        log(null, message, null);
    }
    
    public static void log(final String message, final EResult result) {
        log(null, message, result);
    }
    
    public static void log(final CSteamID steamId, final String message) {
        log(steamId, message, null);
    }
    
    public static void log(final CSteamID steamId, final String message, final EResult result) {
        final StringBuilder sb = new StringBuilder("[Steam]");
        if (steamId != null) {
            sb.append("[SteamId=").append(steamId.ConvertToUint64()).append(']');
        }
        if (result != null) {
            sb.append("[Result=").append(result.name()).append(']');
        }
        sb.append(' ').append(message);
        SteamLogger.m_logger.info((Object)sb.toString());
    }
    
    static {
        m_logger = Logger.getLogger((Class)SteamLogger.class);
    }
}
