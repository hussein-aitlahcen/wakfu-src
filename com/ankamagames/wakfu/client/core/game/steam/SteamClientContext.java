package com.ankamagames.wakfu.client.core.game.steam;

import com.ankamagames.baseImpl.common.clientAndServer.steam.*;
import com.ankamagames.steam.client.userstats.*;
import org.apache.log4j.*;
import com.ankamagames.steam.client.*;
import com.ankamagames.wakfu.client.steam.*;
import com.ankamagames.wakfu.client.core.game.soap.steam.*;
import com.ankamagames.steam.client.transaction.*;
import com.google.common.primitives.*;
import com.ankamagames.wakfu.client.ui.protocol.frame.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.steam.wrapper.*;

public class SteamClientContext extends SteamUserContext implements SteamUserStatsListener
{
    private static final Logger m_logger;
    public static final SteamClientContext INSTANCE;
    private SteamClient m_client;
    private SteamUserInfo m_userInfo;
    private SteamMicroTxnResponseHandler m_microTransactionHandler;
    private GameOverlayActivatedListener m_gameOverlayActivatedListener;
    
    public boolean initialize(final SteamClient client) {
        this.m_client = client;
        final boolean isOk = this.initialize(this.m_client.getSteamId());
        this.getUserStatsHandler().setListener((SteamUserStatsListener)this);
        SteamDisplayer.INSTANCE.setConnected(isOk);
        GetUserInfo.sendRequest(this.m_client.getSteamId(), new GetUserInfoListener() {
            @Override
            public void onSteamUserInfo(final SteamUserInfo info) {
                SteamClientContext.this.m_userInfo = info;
            }
            
            @Override
            public void onError() {
                SteamClientContext.m_logger.error((Object)"[Steam] Impossible de r\u00e9cup\u00e9rer les infos compl\u00e9mentaires.");
            }
        });
        (this.m_microTransactionHandler = new SteamMicroTxnResponseHandler()).setListener((SteamTransactionListener)new SteamTransactionListener() {
            public void onMicroTxnAuthorizationResponse(final MicroTxnAuthorizationResponse_t response) {
                final UnsignedLong unsignedLong = UnsignedLong.valueOf(response.getM_ulOrderID());
                final long orderId = unsignedLong.longValue();
                final boolean authorized = response.getM_bAuthorized() != 0;
                UIWebShopFrame.getInstance().steamFinalizeTxn((int)orderId, authorized);
            }
        });
        this.m_gameOverlayActivatedListener = new GameOverlayActivatedListener() {
            public void onCallback(final GameOverlayActivated_t pParam) {
                if (pParam.getM_bActive() != 0) {
                    CursorFactory.getInstance().unlock();
                }
            }
        };
        return isOk;
    }
    
    public SteamClient getClient() {
        return this.m_client;
    }
    
    public SteamUserInfo getUserInfo() {
        return this.m_userInfo;
    }
    
    public String getLanguage() {
        return SteamApi.SteamApps().GetCurrentGameLanguage();
    }
    
    public boolean resetUserStats() {
        return this.getUserStatsHandler().resetUserStats();
    }
    
    public boolean setStat(final String name, final int value) {
        return this.getUserStatsHandler().setStat(name, value);
    }
    
    public boolean setStat(final String name, final float value) {
        return this.getUserStatsHandler().setStat(name, value);
    }
    
    public boolean isOverlayEnabled() {
        return SteamApi.SteamUtils().IsOverlayEnabled();
    }
    
    public void onUserStatsReceived(final UserStatsReceived_t data) {
        SteamLogger.log(data.getM_steamIDUser(), "UserStatsReceived", data.getM_eResult());
        if (data.getM_eResult() != EResult.k_EResultOK) {
            this.getUserStatsHandler().requestUserStats();
        }
    }
    
    public void onUserStatsStored(final UserStatsStored_t data) {
        SteamLogger.log(this.getSteamID(), "UserStatsStored GameId=" + data.getM_nGameID().longValue(), data.getM_eResult());
    }
    
    public void onUserAchievementStored(final UserAchievementStored_t data) {
        SteamLogger.log(this.getSteamID(), "Achievement stored : " + data.getM_rgchAchievementName());
    }
    
    public String toString() {
        return "SteamClientContext{m_client=" + this.m_client + '}';
    }
    
    static {
        m_logger = Logger.getLogger((Class)SteamClientContext.class);
        INSTANCE = new SteamClientContext();
    }
}
