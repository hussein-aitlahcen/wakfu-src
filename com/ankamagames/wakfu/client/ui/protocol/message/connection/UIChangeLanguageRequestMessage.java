package com.ankamagames.wakfu.client.ui.protocol.message.connection;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.kernel.core.translator.*;

public class UIChangeLanguageRequestMessage extends UIMessage
{
    private Language m_language;
    
    @Override
    public int getId() {
        return 16384;
    }
    
    public Language getLanguage() {
        return this.m_language;
    }
    
    public void setLanguage(final Language language) {
        this.m_language = language;
    }
}
