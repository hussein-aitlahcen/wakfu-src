package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class ClientDispatchAuthenticationMessage extends OutputOnlyProxyMessage
{
    private byte[] m_encryptedLoginAndPassword;
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putInt(this.m_encryptedLoginAndPassword.length);
        ba.put(this.m_encryptedLoginAndPassword);
        return this.addClientHeader((byte)8, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 1026;
    }
    
    public void setEncryptedLoginAndPassword(final byte[] encryptedLoginAndPassword) {
        this.m_encryptedLoginAndPassword = encryptedLoginAndPassword.clone();
    }
}
