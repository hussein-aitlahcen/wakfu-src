package com.ankamagames.wakfu.client.ui.protocol.message.server;

import com.ankamagames.wakfu.client.ui.protocol.message.*;
import com.ankamagames.wakfu.client.core.proxyGroup.*;

public class UIServerReferenceMessage extends UIMessage
{
    private WakfuServerView m_reference;
    private long m_forcedAccountId;
    
    public WakfuServerView getServerReference() {
        return this.m_reference;
    }
    
    public void setServerReference(final WakfuServerView reference) {
        this.m_reference = reference;
    }
    
    public void setForcedAccountId(final long forcedAccountId) {
        this.m_forcedAccountId = forcedAccountId;
    }
    
    public long getForcedAccountId() {
        return this.m_forcedAccountId;
    }
}
