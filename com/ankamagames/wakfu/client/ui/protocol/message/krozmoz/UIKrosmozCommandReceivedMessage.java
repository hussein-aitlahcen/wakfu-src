package com.ankamagames.wakfu.client.ui.protocol.message.krozmoz;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIKrosmozCommandReceivedMessage extends UIMessage
{
    private final String m_command;
    private final Object[] m_params;
    
    public UIKrosmozCommandReceivedMessage(final String command, final Object... params) {
        super();
        this.m_command = command;
        this.m_params = params;
    }
    
    @Override
    public int getId() {
        return 17350;
    }
    
    public String getCommand() {
        return this.m_command;
    }
    
    public Object[] getParams() {
        return this.m_params;
    }
}
