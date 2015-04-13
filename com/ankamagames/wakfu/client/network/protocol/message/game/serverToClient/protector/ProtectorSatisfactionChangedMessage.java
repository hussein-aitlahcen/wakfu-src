package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ProtectorSatisfactionChangedMessage extends InputOnlyProxyMessage
{
    private int m_protectorId;
    private byte m_protectorSatisfaction;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_protectorId = buffer.getInt();
        this.m_protectorSatisfaction = buffer.get();
        return true;
    }
    
    @Override
    public int getId() {
        return 15328;
    }
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public byte getProtectorSatisfaction() {
        return this.m_protectorSatisfaction;
    }
}
