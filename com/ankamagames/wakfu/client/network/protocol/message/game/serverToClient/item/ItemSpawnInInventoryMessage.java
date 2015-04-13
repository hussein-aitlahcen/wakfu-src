package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.bind.*;
import java.nio.*;

public class ItemSpawnInInventoryMessage extends InputOnlyProxyMessage
{
    private long m_itemId;
    private int m_itemRefId;
    private short m_quantity;
    private long m_destinationId;
    private short m_destinationPosition;
    private ItemBindType m_bindType;
    private long m_bindId;
    
    @Override
    public byte[] encode() {
        throw new UnsupportedOperationException("On tente d'encoder un message serveur->client cot\u00e9 client...");
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_itemId = buffer.getLong();
        this.m_itemRefId = buffer.getInt();
        this.m_quantity = buffer.getShort();
        this.m_destinationId = buffer.getLong();
        this.m_destinationPosition = buffer.getShort();
        this.m_bindType = ItemBindType.getFromId(buffer.get());
        this.m_bindId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 5210;
    }
    
    public long getItemId() {
        return this.m_itemId;
    }
    
    public long getDestinationId() {
        return this.m_destinationId;
    }
    
    public int getItemRefId() {
        return this.m_itemRefId;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public short getDestinationPosition() {
        return this.m_destinationPosition;
    }
    
    public ItemBindType getBindType() {
        return this.m_bindType;
    }
    
    public long getBindId() {
        return this.m_bindId;
    }
}
