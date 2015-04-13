package com.ankamagames.wakfu.client.core.game.events.listeners;

import com.ankamagames.baseImpl.graphics.ui.event.*;
import com.ankamagames.wakfu.client.core.game.events.*;
import com.ankamagames.wakfu.client.core.game.events.events.*;

public class ClientEventXulorListener implements DialogLoadListener, DialogUnloadListener
{
    public static final ClientEventXulorListener INSTANCE;
    
    @Override
    public void dialogLoaded(final String id) {
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventUIOpened(id));
    }
    
    @Override
    public void dialogUnloaded(final String id) {
        ClientGameEventManager.INSTANCE.fireEvent(new ClientEventUIClosed(id));
    }
    
    static {
        INSTANCE = new ClientEventXulorListener();
    }
}
