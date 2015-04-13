package com.ankamagames.baseImpl.common.clientAndServer.steam;

import com.ankamagames.steam.wrapper.*;
import com.ankamagames.steam.client.userstats.*;
import com.ankamagames.steam.client.friends.*;

public class SteamUserContext
{
    private CSteamID m_steamID;
    private SteamUserStatsHandler m_userStatsHandler;
    private SteamFriendsHandler m_friendsHandler;
    private boolean m_init;
    
    public boolean initialize(final CSteamID steamId) {
        this.m_init = true;
        this.m_steamID = steamId;
        this.m_userStatsHandler = new SteamUserStatsHandler();
        this.m_friendsHandler = new SteamFriendsHandler();
        return this.m_userStatsHandler.requestUserStats();
    }
    
    public boolean isInit() {
        return this.m_init;
    }
    
    public CSteamID getSteamID() {
        return this.m_steamID;
    }
    
    public SteamUserStatsHandler getUserStatsHandler() {
        return this.m_userStatsHandler;
    }
    
    public SteamFriendsHandler getFriendsHandler() {
        return this.m_friendsHandler;
    }
    
    public void cleanUp() {
        this.m_userStatsHandler.cleanUp();
        this.m_friendsHandler.cleanUp();
    }
}
