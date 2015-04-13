package com.ankamagames.wakfu.client.ui.protocol.message.preloading;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.updater.*;

public class UIPreloadingStateMessage extends UIMessage
{
    private State m_state;
    
    public UIPreloadingStateMessage(final short id, final State state) {
        super(id);
        this.m_state = state;
    }
    
    public State getState() {
        return this.m_state;
    }
}
