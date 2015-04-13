package com.ankamagames.wakfu.client.core.krosmoz;

import com.ankamagames.wakfu.common.constants.*;
import com.ankamagames.wakfu.client.core.webBrowser.*;

public class KrosmozGameCommandInterfaceFactory
{
    public static final KrosmozGameCommandInterfaceFactory INSTANCE;
    
    public KrosmozGameFrame getGameFrame(final KrosmozGame game, final BrowserEventHandler handler) {
        switch (game) {
            case KROSMASTER_ARENA: {
                return new KrosmasterArenaCommandInterface((KrosmasterArenaBrowserEventHandler)handler);
            }
            case BROWSER:
            case FULL_SCREEN_BROWSER:
            case FULL_SCREEN_BROWSER_WITHOUT_CONTROLS: {
                return new BrowserCommandInterface();
            }
            case SHOP: {
                return new ShopCommandInterface((ShopBrowserHandler)handler);
            }
            default: {
                return null;
            }
        }
    }
    
    static {
        INSTANCE = new KrosmozGameCommandInterfaceFactory();
    }
}
