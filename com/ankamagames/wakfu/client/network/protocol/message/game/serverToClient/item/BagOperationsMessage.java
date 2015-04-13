package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.visitor.operation.*;
import java.nio.*;

public class BagOperationsMessage extends InputOnlyProxyMessage
{
    private final TLongObjectHashMap<BagOperation> m_operations;
    
    public BagOperationsMessage() {
        super();
        this.m_operations = new TLongObjectHashMap<BagOperation>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        while (buffer.hasRemaining()) {
            final long itemUid = buffer.getLong();
            final byte opType = buffer.get();
            final BagOperation op = BagOperation.createFromType(opType);
            op.unSerialize(buffer);
            this.m_operations.put(itemUid, op);
        }
        return true;
    }
    
    public TLongObjectHashMap<BagOperation> getOperations() {
        return this.m_operations;
    }
    
    @Override
    public int getId() {
        return 11118;
    }
}
