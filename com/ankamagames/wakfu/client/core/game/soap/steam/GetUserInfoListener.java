package com.ankamagames.wakfu.client.core.game.soap.steam;

public interface GetUserInfoListener
{
    void onSteamUserInfo(SteamUserInfo p0);
    
    void onError();
}
