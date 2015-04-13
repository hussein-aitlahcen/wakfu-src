package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.vault;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.inventory.action.*;
import java.nio.*;

public class VaultInventoryActionRequestMessage extends OutputOnlyProxyMessage
{
    private final InventoryAction m_action;
    
    public VaultInventoryActionRequestMessage(final InventoryAction action) {
        super();
        this.m_action = action;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(1 + this.m_action.serializedSize());
        buffer.put((byte)this.m_action.getType().ordinal());
        this.m_action.serializeIn(buffer);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15672;
    }
}
