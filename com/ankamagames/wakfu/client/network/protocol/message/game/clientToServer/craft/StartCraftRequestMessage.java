package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.craft;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class StartCraftRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_craftTableId;
    private final int m_recipeId;
    
    public StartCraftRequestMessage(final long craftTableId, final int recipeId) {
        super();
        this.m_craftTableId = craftTableId;
        this.m_recipeId = recipeId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putLong(this.m_craftTableId);
        buffer.putInt(this.m_recipeId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15711;
    }
}
