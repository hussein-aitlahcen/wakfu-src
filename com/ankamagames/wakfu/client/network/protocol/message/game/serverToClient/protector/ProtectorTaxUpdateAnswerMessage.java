package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ProtectorTaxUpdateAnswerMessage extends InputOnlyProxyMessage
{
    private int m_protectorId;
    private byte[] m_taxData;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_protectorId = buffer.getInt();
        final int size = buffer.getShort() & 0xFFFF;
        buffer.get(this.m_taxData = new byte[size]);
        return true;
    }
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public byte[] getTaxData() {
        return this.m_taxData;
    }
    
    @Override
    public int getId() {
        return 15334;
    }
}
