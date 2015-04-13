package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class EquipmentToItemInventoryMoveRequestMessage extends OutputOnlyProxyMessage
{
    private long m_itemId;
    private long m_targetBagId;
    private short m_targetPosition;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 18;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_itemId);
        buffer.putLong(this.m_targetBagId);
        buffer.putShort(this.m_targetPosition);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5211;
    }
    
    public void setItemId(final long itemId) {
        this.m_itemId = itemId;
    }
    
    public void setTargetBagId(final long targetBagId) {
        this.m_targetBagId = targetBagId;
    }
    
    public void setTargetPosition(final short targetPosition) {
        this.m_targetPosition = targetPosition;
    }
}
