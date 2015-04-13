package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.visitor.operation.*;

public class RemoveBagResultMessage extends InputOnlyProxyMessage
{
    private final TLongObjectHashMap<BagOperation> m_operations;
    private long m_removedBagUid;
    private long m_destinationCharacterId;
    
    public RemoveBagResultMessage() {
        super();
        this.m_operations = new TLongObjectHashMap<BagOperation>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_destinationCharacterId = buffer.getLong();
        this.m_removedBagUid = buffer.getLong();
        while (buffer.hasRemaining()) {
            final long itemUid = buffer.getLong();
            final byte opType = buffer.get();
            final BagOperation op = BagOperation.createFromType(opType);
            op.unSerialize(buffer);
            if (op instanceof AddItemOperation) {
                ((AddItemOperation)op).setInsideMove(true);
            }
            this.m_operations.put(itemUid, op);
        }
        return true;
    }
    
    public TLongObjectHashMap<BagOperation> getOperations() {
        return this.m_operations;
    }
    
    public long getDestinationCharacterId() {
        return this.m_destinationCharacterId;
    }
    
    public long getRemovedBagUid() {
        return this.m_removedBagUid;
    }
    
    @Override
    public int getId() {
        return 5224;
    }
}
