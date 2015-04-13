package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item;

import java.nio.*;
import com.ankamagames.wakfu.common.game.item.*;

public class ItemInventoryToEquipmentMoveRequestMessage extends InventoryMoveRequestMessage
{
    private byte m_posdest;
    private long m_characterId;
    
    @Override
    public byte[] encode() {
        int sizeDatas = 26;
        if (this.m_newuid != 0L) {
            sizeDatas += 8;
        }
        final ByteBuffer buffer = ByteBuffer.allocate(sizeDatas);
        buffer.putLong(this.m_characterId);
        buffer.putLong(this.m_uid);
        buffer.putLong(this.m_source);
        buffer.put(this.m_posdest);
        if (this.m_newuid != 0L) {
            buffer.put((byte)1);
            buffer.putLong(this.m_newuid);
        }
        else {
            buffer.put((byte)0);
        }
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5203;
    }
    
    public void setDestinatairePosition(final EquipmentPosition pos) {
        this.m_posdest = pos.m_id;
    }
    
    public void setDestinatairePosition(final byte pos) {
        this.m_posdest = pos;
    }
    
    public void setCharacterId(final long characterId) {
        this.m_characterId = characterId;
    }
}
