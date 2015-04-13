package com.ankamagames.wakfu.client.network.protocol.message.global.clientToServer.partySearch;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class PartySearchPlayerFeedbackMessage extends InputOnlyProxyMessage
{
    private byte m_feedbackId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_feedbackId = bb.get();
        return true;
    }
    
    public byte getFeedbackId() {
        return this.m_feedbackId;
    }
    
    @Override
    public int getId() {
        return 20419;
    }
}
