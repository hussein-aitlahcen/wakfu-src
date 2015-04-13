package com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ClientPublicKeyMessage extends InputOnlyProxyMessage
{
    private long m_salt;
    private byte[] m_publicKey;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_salt = buffer.getLong();
        buffer.get(this.m_publicKey = new byte[buffer.remaining()]);
        return true;
    }
    
    public long getSalt() {
        return this.m_salt;
    }
    
    public byte[] getPublicKey() {
        return this.m_publicKey;
    }
    
    @Override
    public int getId() {
        return 1034;
    }
}
