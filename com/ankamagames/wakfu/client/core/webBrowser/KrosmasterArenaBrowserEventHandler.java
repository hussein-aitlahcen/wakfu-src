package com.ankamagames.wakfu.client.core.webBrowser;

import org.apache.log4j.*;
import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.characterInfo.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.framework.kernel.core.common.message.scheduler.process.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.baseImpl.graphics.core.*;
import com.ankamagames.xulor2.*;

public class KrosmasterArenaBrowserEventHandler extends JavascriptBrowserEventHandler
{
    private static final Logger m_logger;
    private static final int PING_DELAY = 600000;
    private static final int TIMEOUT = 5000;
    private static final Runnable TIMEOUT_RUNNABLE;
    private boolean m_authentificated;
    private boolean m_loaded;
    private Runnable m_pingRunnable;
    private Runnable m_timeoutRunnable;
    
    public KrosmasterArenaBrowserEventHandler(final SWFBrowser browser, final String url, final String jsObjectName) {
        super(browser, url, jsObjectName);
        this.m_pingRunnable = new Runnable() {
            @Override
            public void run() {
                final LocalPlayerCharacter localPlayer = WakfuGameEntity.getInstance().getLocalPlayer();
                final Message msg = new PingMessage((byte)0, (int)localPlayer.getId());
                WakfuGameEntity.getInstance().getNetworkEntity().sendMessage(msg);
            }
        };
    }
    
    @Override
    public void initialize() {
        ProcessScheduler.getInstance().schedule(this.m_pingRunnable, 600000L, -1);
    }
    
    @Override
    public void clean() {
        ProcessScheduler.getInstance().remove(this.m_pingRunnable);
        if (this.m_timeoutRunnable != null) {
            ProcessScheduler.getInstance().remove(this.m_timeoutRunnable);
            this.m_timeoutRunnable = null;
        }
    }
    
    @Override
    public void start(final SWFBrowser browser) {
        browser.loadUrl();
        SWFWrapper.INSTANCE.displayComponent();
    }
    
    @Override
    public void onLoad() {
        if (!this.m_loaded) {
            this.m_loaded = true;
            KrosmasterArenaBrowserEventHandler.m_logger.info((Object)"[Krosmaster Arena] Page charg\u00e9e, en attente d'authentification...");
            if (!this.m_authentificated) {
                this.m_timeoutRunnable = KrosmasterArenaBrowserEventHandler.TIMEOUT_RUNNABLE;
                ProcessScheduler.getInstance().schedule(this.m_timeoutRunnable, 5000L, 1);
            }
        }
    }
    
    public void onAuthentification() {
        KrosmasterArenaBrowserEventHandler.m_logger.info((Object)"[Krosmaster Arena] Authentification commenc\u00e9e.");
        this.m_authentificated = true;
        if (this.m_timeoutRunnable != null) {
            ProcessScheduler.getInstance().remove(this.m_timeoutRunnable);
            this.m_timeoutRunnable = null;
        }
    }
    
    @Override
    public String getUrl() {
        final String locale = WakfuTranslator.getInstance().getLanguage().getActualLocale().toString();
        final int volume = (int)(WakfuClientInstance.getInstance().getGamePreferences().getFloatValue(KeyPreferenceStoreEnum.AMBIANCE_SOUNDS_VOLUME_PREFERENCE_KEY) * 100.0f);
        final StringBuilder sb = new StringBuilder(this.m_url);
        sb.append('?');
        sb.append("host=WAKFU");
        sb.append("&lang=").append(locale);
        sb.append("&volume=").append(volume);
        return sb.toString();
    }
    
    static {
        m_logger = Logger.getLogger((Class)KrosmasterArenaBrowserEventHandler.class);
        TIMEOUT_RUNNABLE = new Runnable() {
            @Override
            public void run() {
                KrosmasterArenaBrowserEventHandler.m_logger.info((Object)"[Krosmaster Arena] Timeout de la communication avec WAKFU");
                SWFWrapper.INSTANCE.unload();
                Xulor.getInstance().msgBox(WakfuTranslator.getInstance().getString("error.krosmaster.communication"), 102, 0);
            }
        };
    }
}
