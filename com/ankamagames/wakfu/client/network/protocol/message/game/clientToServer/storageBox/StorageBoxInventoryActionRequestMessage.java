package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.storageBox;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;
import java.nio.*;

public class StorageBoxInventoryActionRequestMessage extends OutputOnlyProxyMessage
{
    private final int m_compartmentId;
    private final InventoryAction m_action;
    
    public StorageBoxInventoryActionRequestMessage(final int compartmentId, final InventoryAction action) {
        super();
        this.m_compartmentId = compartmentId;
        this.m_action = action;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(5 + this.m_action.serializedSize());
        buffer.putInt(this.m_compartmentId);
        buffer.put((byte)this.m_action.getType().ordinal());
        this.m_action.serializeIn(buffer);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15975;
    }
}
