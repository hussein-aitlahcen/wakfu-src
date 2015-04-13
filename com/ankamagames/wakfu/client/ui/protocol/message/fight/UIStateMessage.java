package com.ankamagames.wakfu.client.ui.protocol.message.fight;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.spell.*;

public class UIStateMessage extends UIMessage
{
    private StateClient m_state;
    
    public StateClient getState() {
        return this.m_state;
    }
    
    public void setState(final StateClient state) {
        this.m_state = state;
    }
    
    @Override
    public int getId() {
        return 16151;
    }
}
