package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.guildStorage;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class GuildStorageMoneyActionRequestMessage extends OutputOnlyProxyMessage
{
    private final int m_deltaMoney;
    
    public GuildStorageMoneyActionRequestMessage(final int deltaMoney) {
        super();
        this.m_deltaMoney = deltaMoney;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(this.m_deltaMoney);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15627;
    }
}
