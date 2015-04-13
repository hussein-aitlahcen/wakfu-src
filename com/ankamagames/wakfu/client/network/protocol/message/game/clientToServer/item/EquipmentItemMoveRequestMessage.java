package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import java.nio.*;
import com.ankamagames.wakfu.common.game.item.*;

public class EquipmentItemMoveRequestMessage extends InventoryMoveRequestMessage
{
    protected byte m_posdest;
    
    @Override
    public byte[] encode() {
        final int sizeDatas = 9;
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_uid);
        buffer.put(this.m_posdest);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5205;
    }
    
    public void setDestinationPosition(final EquipmentPosition pos) {
        this.m_posdest = pos.m_id;
    }
    
    public void setDestinationPosition(final byte pos) {
        this.m_posdest = pos;
    }
}
