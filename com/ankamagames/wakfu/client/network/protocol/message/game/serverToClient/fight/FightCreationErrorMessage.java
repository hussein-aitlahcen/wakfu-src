package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.fight;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class FightCreationErrorMessage extends InputOnlyProxyMessage
{
    private byte m_errorCode;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_errorCode = bb.get();
        return true;
    }
    
    public byte getErrorCode() {
        return this.m_errorCode;
    }
    
    @Override
    public int getId() {
        return 7998;
    }
}
