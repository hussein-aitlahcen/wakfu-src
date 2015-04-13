package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.item.visitor.operation.*;
import com.ankamagames.wakfu.common.game.item.*;

public class InventoryToEquipmentResultMessage extends InputOnlyProxyMessage
{
    private final TLongObjectHashMap<BagOperation> m_operations;
    private final RawInventoryItem m_rawItem;
    private byte m_equippedPosition;
    private long m_characterId;
    
    public InventoryToEquipmentResultMessage() {
        super();
        this.m_operations = new TLongObjectHashMap<BagOperation>();
        this.m_rawItem = new RawInventoryItem();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        this.m_equippedPosition = buffer.get();
        this.m_rawItem.unserialize(buffer);
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
    
    public Item getEquippedItem() {
        return ReferenceItemManager.getInstance().unSerializeContent(this.m_rawItem);
    }
    
    public TLongObjectHashMap<BagOperation> getOperations() {
        return this.m_operations;
    }
    
    @Override
    public int getId() {
        return 11120;
    }
}
