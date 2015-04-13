package com.ankamagames.wakfu.client.ui.protocol.message.popupInfos;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.framework.reflect.*;

public class UIShowPopupInfosMessage extends UIMessage
{
    private FieldProvider m_content;
    
    public UIShowPopupInfosMessage(final FieldProvider content) {
        super();
        this.m_content = content;
    }
    
    public FieldProvider getContent() {
        return this.m_content;
    }
}
