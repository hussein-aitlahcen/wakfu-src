package com.ankamagames.wakfu.client.ui.protocol.message.options;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.console.*;

public class UIBindShortcutMessage extends UIMessage
{
    private ShortcutFieldProvider m_shortcut;
    
    public UIBindShortcutMessage(final ShortcutFieldProvider shortcut) {
        super();
        this.m_shortcut = shortcut;
    }
    
    public ShortcutFieldProvider getShortcut() {
        return this.m_shortcut;
    }
    
    public void setShortcut(final ShortcutFieldProvider shortcut) {
        this.m_shortcut = shortcut;
    }
}
