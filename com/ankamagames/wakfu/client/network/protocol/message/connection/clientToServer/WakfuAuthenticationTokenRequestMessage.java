package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class WakfuAuthenticationTokenRequestMessage extends OutputOnlyProxyMessage
{
    private int m_serverId;
    private long m_accountId;
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putInt(this.m_serverId);
        ba.putLong(this.m_accountId);
        return this.addClientHeader((byte)8, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 1211;
    }
    
    public void setServerId(final int serverId) {
        this.m_serverId = serverId;
    }
    
    public void setAccountId(final long accountId) {
        this.m_accountId = accountId;
    }
    
    @Override
    public String toString() {
        return "GameAuthenticationTokenRequestMessage{m_serverId=" + this.m_serverId + '}';
    }
}
