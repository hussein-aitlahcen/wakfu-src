package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class TemporaryInventoryItemMovedMessage extends InputOnlyProxyMessage
{
    protected static final Logger m_logger;
    private long m_sourceItemUniqueId;
    private short m_itemQuantity;
    private long m_newItemUniqueId;
    private byte m_destinationContainerType;
    private byte m_destinationPosition;
    
    public TemporaryInventoryItemMovedMessage() {
        super();
        this.m_sourceItemUniqueId = -1L;
        this.m_itemQuantity = -1;
        this.m_newItemUniqueId = 0L;
        this.m_destinationContainerType = -1;
        this.m_destinationPosition = -1;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        if (rawDatas.length == 13 || rawDatas.length == 21) {
            final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
            this.m_sourceItemUniqueId = buffer.getLong();
            this.m_itemQuantity = buffer.getShort();
            this.m_destinationContainerType = buffer.get();
            this.m_destinationPosition = buffer.get();
            final byte hasNewId = buffer.get();
            if (hasNewId == 1) {
                this.m_newItemUniqueId = buffer.getLong();
            }
            else {
                this.m_newItemUniqueId = 0L;
            }
        }
        return false;
    }
    
    @Override
    public int getId() {
        return 5216;
    }
    
    public long getSourceItemUniqueId() {
        return this.m_sourceItemUniqueId;
    }
    
    public short getItemQuantity() {
        return this.m_itemQuantity;
    }
    
    public long getNewItemUniqueId() {
        return this.m_newItemUniqueId;
    }
    
    public byte getDestinationContainerType() {
        return this.m_destinationContainerType;
    }
    
    public byte getDestinationPosition() {
        return this.m_destinationPosition;
    }
    
    static {
        m_logger = Logger.getLogger((Class)TemporaryInventoryItemMovedMessage.class);
    }
}
