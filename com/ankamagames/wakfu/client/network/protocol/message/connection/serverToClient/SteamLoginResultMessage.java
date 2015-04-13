package com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class SteamLoginResultMessage extends InputOnlyProxyMessage
{
    private long m_salt;
    private byte[] m_publicKey;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_salt = buffer.getLong();
        buffer.get(this.m_publicKey = new byte[buffer.remaining()]);
        return false;
    }
    
    @Override
    public int getId() {
        return 1042;
    }
    
    public long getSalt() {
        return this.m_salt;
    }
    
    public byte[] getPublicKey() {
        return this.m_publicKey;
    }
}
