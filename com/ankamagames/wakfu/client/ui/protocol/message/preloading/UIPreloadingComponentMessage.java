package com.ankamagames.wakfu.client.ui.protocol.message.preloading;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.updater.*;

public class UIPreloadingComponentMessage extends UIMessage
{
    private Component m_component;
    private boolean m_completed;
    
    public UIPreloadingComponentMessage(final short id, final Component component, final boolean completed) {
        super(id);
        this.m_component = component;
        this.m_completed = completed;
    }
    
    public Component getComponent() {
        return this.m_component;
    }
    
    public boolean isCompleted() {
        return this.m_completed;
    }
}
