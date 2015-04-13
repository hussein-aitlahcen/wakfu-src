package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import java.util.*;

public class PickUpItemToItemInventoryMessage extends OutputOnlyProxyMessage
{
    private ArrayList<Long> m_itemIds;
    private long m_interactifElementId;
    
    public PickUpItemToItemInventoryMessage() {
        super();
        this.m_itemIds = new ArrayList<Long>();
    }
    
    public void addItemToPickUp(final long itemId) {
        this.m_itemIds.add(itemId);
    }
    
    @Override
    public byte[] encode() {
        final short itemCount = (short)this.m_itemIds.size();
        final int sizeDatas = 10 + itemCount * 8;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_interactifElementId);
        buffer.putShort(itemCount);
        for (final long id : this.m_itemIds) {
            buffer.putLong(id);
        }
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5207;
    }
    
    public void setInteractifElementId(final long interactifElementId) {
        this.m_interactifElementId = interactifElementId;
    }
}
