package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import java.nio.*;

public class EquipmentPositionLockedMessage extends InputOnlyProxyMessage
{
    private EquipmentPosition m_position;
    private boolean m_locked;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_position = EquipmentPosition.fromId(buffer.get());
        this.m_locked = (buffer.get() == 1);
        return true;
    }
    
    public EquipmentPosition getPosition() {
        return this.m_position;
    }
    
    public boolean isLocked() {
        return this.m_locked;
    }
    
    @Override
    public int getId() {
        return 5226;
    }
    
    @Override
    public String toString() {
        return "EquipmentPositionLockedMessage{m_position=" + this.m_position + ", m_locked=" + this.m_locked + '}';
    }
}
