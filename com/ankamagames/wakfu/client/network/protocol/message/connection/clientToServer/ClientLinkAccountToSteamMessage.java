package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class ClientLinkAccountToSteamMessage extends OutputOnlyProxyMessage
{
    private byte[] m_encryptedLoginAndPassword;
    private long m_steamId;
    
    public ClientLinkAccountToSteamMessage() {
        super();
        this.m_steamId = -1L;
    }
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putInt(this.m_encryptedLoginAndPassword.length);
        ba.put(this.m_encryptedLoginAndPassword);
        ba.putLong(this.m_steamId);
        return this.addClientHeader((byte)8, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 1043;
    }
    
    public void setEncryptedLoginAndPassword(final byte[] encryptedLoginAndPassword) {
        this.m_encryptedLoginAndPassword = encryptedLoginAndPassword.clone();
    }
    
    public void setSteamId(final long steamId) {
        this.m_steamId = steamId;
    }
}
