package com.ankamagames.wakfu.client.ui.protocol.message.exchange;

import com.ankamagames.wakfu.client.ui.protocol.message.*;

public class UIExchangeInvitationAcceptRequestMessage extends UIMessage
{
    private long m_invitationId;
    
    @Override
    public int getId() {
        return 16808;
    }
    
    public long getInvitationId() {
        return this.m_invitationId;
    }
    
    public void setInvitationId(final long exchangeId) {
        this.m_invitationId = exchangeId;
    }
}
