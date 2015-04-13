package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.visitor.operation.*;

public class EquipmentToInventoryResultMessage extends InputOnlyProxyMessage
{
    private final TLongObjectHashMap<BagOperation> m_operations;
    private byte m_equippedPosition;
    private long m_characterId;
    
    public EquipmentToInventoryResultMessage() {
        super();
        this.m_operations = new TLongObjectHashMap<BagOperation>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_equippedPosition = buffer.get();
        this.m_characterId = buffer.getLong();
        while (buffer.hasRemaining()) {
            final long itemUid = buffer.getLong();
            final byte opType = buffer.get();
            final BagOperation op = BagOperation.createFromType(opType);
            op.unSerialize(buffer);
            if (op.getOperationType() == 0) {
                ((AddItemOperation)op).setInsideMove(true);
            }
            else if (op.getOperationType() == 2) {
                ((UpdateItemOperation)op).setInsideMove(true);
            }
            this.m_operations.put(itemUid, op);
        }
        return true;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    public byte getEquippedPosition() {
        return this.m_equippedPosition;
    }
    
    public TLongObjectHashMap<BagOperation> getOperations() {
        return this.m_operations;
    }
    
    @Override
    public int getId() {
        return 11122;
    }
}
