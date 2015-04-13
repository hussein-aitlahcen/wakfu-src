package com.ankamagames.wakfu.client.core.game.steam;

import org.apache.log4j.*;
import com.ankamagames.steam.client.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.steam.wrapper.*;

public class SteamHelper
{
    private static final Logger m_logger;
    
    public static SteamClient createClient() {
        final SteamClient steamClient = new SteamClient();
        final boolean initOk = steamClient.init(false);
        if (initOk) {
            ProcessScheduler.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    steamClient.runCallbacks();
                }
            }, 1000L, -1);
            return steamClient;
        }
        return null;
    }
    
    public static boolean initializeUserContext(final SteamClient client) {
        return SteamClientContext.INSTANCE.initialize(client);
    }
    
    public static void debugInfo() {
        final CSteamID steamID = SteamClientContext.INSTANCE.getSteamID();
        SteamLogger.log(steamID, "");
        SteamLogger.log("Overlay Enabled : " + SteamClientContext.INSTANCE.isOverlayEnabled());
    }
    
    static {
        m_logger = Logger.getLogger((Class)SteamHelper.class);
    }
}
