package com.ankamagames.wakfu.client.ui.protocol.message.craft;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIDragItemToCraftMessage extends UIMessage
{
    private final int m_refId;
    
    public UIDragItemToCraftMessage(final int refId) {
        super();
        this.m_refId = refId;
    }
    
    @Override
    public int getId() {
        return 16846;
    }
    
    public int getRefId() {
        return this.m_refId;
    }
}
