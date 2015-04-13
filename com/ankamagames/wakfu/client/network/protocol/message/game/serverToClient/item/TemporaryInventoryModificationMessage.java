package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class TemporaryInventoryModificationMessage extends InputOnlyProxyMessage
{
    private byte m_modificationType;
    private RawInventoryItem m_rawItem;
    private short m_destinationPosition;
    
    @Override
    public byte[] encode() {
        throw new UnsupportedOperationException("On tente d'encoder un message serveur->client cot\u00e9 client...");
    }
    
    @Override
    public boolean decode(final byte... rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_modificationType = buffer.get();
        this.m_destinationPosition = buffer.getShort();
        if (buffer.hasRemaining()) {
            (this.m_rawItem = new RawInventoryItem()).unserialize(buffer);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 5214;
    }
    
    public RawInventoryItem getRawItem() {
        return this.m_rawItem;
    }
    
    public short getDestinationPosition() {
        return this.m_destinationPosition;
    }
    
    public byte getModificationType() {
        return this.m_modificationType;
    }
}
