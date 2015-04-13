package com.ankamagames.wakfu.client.ui.protocol.message.guild;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIGuildCreateMessage extends UIMessage
{
    private String m_name;
    
    public String getName() {
        return this.m_name;
    }
    
    public void setName(final String name) {
        this.m_name = name;
    }
    
    @Override
    public int getId() {
        return 18204;
    }
}
