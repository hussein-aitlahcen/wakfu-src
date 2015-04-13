package com.ankamagames.wakfu.client.ui.protocol.message.havenWorld;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.havenWorld.*;

public class UIHavenWorldQuotationMessage extends UIMessage
{
    private final HavenWorldQuotation m_element;
    
    public UIHavenWorldQuotationMessage(final HavenWorldQuotation element) {
        super();
        this.m_element = element;
    }
    
    public HavenWorldQuotation getElement() {
        return this.m_element;
    }
}
