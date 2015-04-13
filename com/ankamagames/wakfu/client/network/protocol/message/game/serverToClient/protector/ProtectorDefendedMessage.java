package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ProtectorDefendedMessage extends InputOnlyProxyMessage
{
    private int m_protectorId;
    private int m_nationId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_protectorId = buffer.getInt();
        this.m_nationId = buffer.getInt();
        return true;
    }
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    @Override
    public int getId() {
        return 15302;
    }
}
