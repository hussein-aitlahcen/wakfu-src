package com.ankamagames.wakfu.client.core.game.soap.auth;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.auth.*;
import com.ankamagames.wakfu.client.core.game.steam.*;
import com.ankamagames.steam.common.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.core.game.webShop.*;
import com.ankamagames.wakfu.client.core.game.soap.steam.*;

public class AuthentificationManager
{
    private static final Logger m_logger;
    public static final AuthentificationManager INSTANCE;
    private final Object m_mutex;
    private final List<AuthentificationListener> m_listeners;
    private boolean m_awaiting;
    private Map<String, List<String>> m_sessionParams;
    
    private AuthentificationManager() {
        super();
        this.m_mutex = new Object();
        this.m_listeners = new ArrayList<AuthentificationListener>();
    }
    
    public void getSessionId(final AuthentificationListener listener) {
        synchronized (this.m_mutex) {
            if (this.m_sessionParams != null) {
                listener.onSessionId(this.m_sessionParams);
            }
            else {
                this.m_listeners.add(listener);
                if (!this.m_awaiting) {
                    this.m_awaiting = true;
                    ICEAuthFrame.INSTANCE.getToken(new ICEAuthListener() {
                        @Override
                        public void onToken(final String token) {
                            AuthentificationManager.this.sendRequest(token);
                        }
                        
                        @Override
                        public void onError() {
                            AuthentificationManager.this.onError();
                        }
                    });
                }
            }
        }
    }
    
    public void setSessionParams(final Map<String, List<String>> sessionParams) {
        synchronized (this.m_mutex) {
            AuthentificationManager.m_logger.info((Object)("[" + this.getClass().getSimpleName() + "] R\u00e9ception d'information de session"));
            this.m_sessionParams = sessionParams;
            for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
                this.m_listeners.get(i).onSessionId(sessionParams);
            }
            this.m_listeners.clear();
            this.m_awaiting = false;
        }
    }
    
    private void onError() {
        synchronized (this.m_mutex) {
            this.m_sessionParams = null;
            for (int i = 0, size = this.m_listeners.size(); i < size; ++i) {
                this.m_listeners.get(i).onError();
            }
            this.m_listeners.clear();
            this.m_awaiting = false;
        }
    }
    
    private void sendRequest(final String iceToken) {
        AuthentificationManager.m_logger.info((Object)('[' + this.getClass().getSimpleName() + "] Demande de SSID"));
        synchronized (this.m_mutex) {
            if (SteamClientContext.INSTANCE.isInit()) {
                final SteamUserInfo userInfo = SteamClientContext.INSTANCE.getUserInfo();
                if (userInfo.isOk()) {
                    final String steamId = String.valueOf(SteamUtils.serializeSteamId(SteamClientContext.INSTANCE.getSteamID()));
                    final String lang = WakfuTranslator.getInstance().getLanguage().getActualLocale().getLanguage();
                    final String country = userInfo.getParams().getCountry();
                    final Currency currency = Currency.getByIsoName(userInfo.getParams().getCurrency(), false);
                    SteamAuthentificationLoader.INSTANCE.sendRequest(iceToken, steamId, lang, country, currency.getIsoName());
                    return;
                }
            }
            AuthentificationLoader.INSTANCE.sendRequest(iceToken);
        }
    }
    
    public void clear() {
        synchronized (this.m_mutex) {
            this.m_awaiting = false;
            this.m_listeners.clear();
            this.m_sessionParams = null;
        }
    }
    
    @Override
    public String toString() {
        return "AuthentificationManager{}";
    }
    
    static {
        m_logger = Logger.getLogger((Class)AuthentificationManager.class);
        INSTANCE = new AuthentificationManager();
    }
}
