package com.ankamagames.wakfu.client.network.protocol.message.connection.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.steam.common.*;
import com.ankamagames.framework.kernel.core.common.collections.*;

public class ClientDispatchAuthenticationSteamMessage extends OutputOnlyProxyMessage
{
    private long m_steamId;
    private AuthTicket m_ticket;
    
    @Override
    public byte[] encode() {
        final ByteArray ba = new ByteArray();
        ba.putLong(this.m_steamId);
        ba.put(this.m_ticket.serialize());
        return this.addClientHeader((byte)8, ba.toArray());
    }
    
    @Override
    public int getId() {
        return 1041;
    }
    
    public void setSteamId(final long steamId) {
        this.m_steamId = steamId;
    }
    
    public void setTicket(final AuthTicket ticket) {
        this.m_ticket = ticket;
    }
}
