package com.ankamagames.wakfu.client.core.krosmoz;

import org.apache.log4j.*;
import com.ankamagames.wakfu.client.core.webBrowser.*;
import com.ankamagames.framework.kernel.core.common.message.*;
import com.ankamagames.wakfu.client.core.auth.*;
import com.ankamagames.wakfu.client.core.*;
import com.ankamagames.wakfu.client.*;
import com.ankamagames.framework.net.http.*;
import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.common.game.time.calendar.*;

public class KrosmasterArenaCommandInterface extends KrosmozGameFrame
{
    private static final Logger m_logger;
    public static final String ARENA_SSO_REQUEST = "requestArenaSSO";
    public static final String ARENA_CLOSE_REQUEST = "requestArenaClose";
    public static final String ARENA_REQUEST_URL = "requestURL";
    private static final String RECEIVE_ARENA_SSO = "receiveArenaSSO";
    private KrosmasterArenaBrowserEventHandler m_browserHandler;
    
    public KrosmasterArenaCommandInterface(final KrosmasterArenaBrowserEventHandler handler) {
        super(new MessageRunner[0]);
        this.m_browserHandler = handler;
    }
    
    @Override
    public void onCommandReceived(final String command, final Object... params) {
        if (command.equals("requestArenaSSO")) {
            this.m_browserHandler.onAuthentification();
            ICEAuthFrame.INSTANCE.getToken(new ICEAuthListener() {
                @Override
                public void onToken(final String token) {
                    final SWFWrapper swfWrapper = KrosmasterArenaCommandInterface.this.getSwfWrapper();
                    final String name = WakfuGameEntity.getInstance().getLocalAccount().getAccountNickName();
                    final Object[] params = { name, token, KrosmozGameFrame.GAME_ID };
                    swfWrapper.invokeFunction("receiveArenaSSO", params);
                    swfWrapper.displayComponent();
                }
                
                @Override
                public void onError() {
                }
            });
        }
        else if (command.equals("requestArenaClose")) {
            closeArena();
        }
        else if (command.equals("requestURL")) {
            KrosmasterArenaCommandInterface.m_logger.info((Object)("requestUrl recu de Arena params={" + params + "}"));
            if (params.length == 2 && params[0] instanceof String && params[1] instanceof String) {
                requestURL((String)params[0], (String)params[1]);
            }
        }
    }
    
    private static void requestURL(final String url, final String target) {
        if (target == null || !target.equals("_top")) {
            BrowserManager.openUrlInBrowser(url);
            return;
        }
        SWFWrapper.INSTANCE.unload();
        final KrosmozGame game = KrosmozGame.byId(KrosmozGame.KROSMASTER_ARENA.getId());
        if (WakfuGameCalendar.getInstance().getDate().before(game.getUnlockDate())) {
            return;
        }
        SWFWrapper.INSTANCE.toggleDisplay(game);
    }
    
    private static void closeArena() {
        SWFWrapper.INSTANCE.unload();
    }
    
    static {
        m_logger = Logger.getLogger((Class)KrosmasterArenaCommandInterface.class);
    }
}
