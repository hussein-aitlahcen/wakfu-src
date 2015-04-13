package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class NationCandidateRegistrationFailedMessage extends InputOnlyProxyMessage
{
    private boolean m_success;
    
    public boolean isSuccess() {
        return this.m_success;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_success = (buffer.get() != 0);
        return true;
    }
    
    @Override
    public int getId() {
        return 15120;
    }
}
