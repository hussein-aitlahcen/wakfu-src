package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.item.action;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.client.core.game.item.action.*;
import java.nio.*;

public class ItemActionRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_itemId;
    private final AbstractClientItemAction m_action;
    
    public ItemActionRequestMessage(final long id, final AbstractClientItemAction action) {
        super();
        this.m_itemId = id;
        this.m_action = action;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer bb = ByteBuffer.allocate(12 + this.m_action.serializedSize());
        bb.putInt(this.m_action.getId());
        bb.putLong(this.m_itemId);
        this.m_action.serialize(bb);
        return this.addClientHeader((byte)3, bb.array());
    }
    
    @Override
    public int getId() {
        return 5309;
    }
}
