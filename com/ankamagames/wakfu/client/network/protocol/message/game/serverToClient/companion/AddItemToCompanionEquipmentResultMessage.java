package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.companion;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.game.item.*;
import gnu.trove.*;
import java.nio.*;
import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.wakfu.common.game.item.visitor.operation.*;

public final class AddItemToCompanionEquipmentResultMessage extends InputOnlyProxyMessage
{
    private long m_companionId;
    private byte m_equipmentPosition;
    private long m_originalItemUid;
    private Item m_item;
    private final TLongObjectHashMap<BagOperation> m_operations;
    
    public AddItemToCompanionEquipmentResultMessage() {
        super();
        this.m_operations = new TLongObjectHashMap<BagOperation>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_companionId = bb.getLong();
        this.m_equipmentPosition = bb.get();
        this.m_originalItemUid = bb.getLong();
        this.m_item = new Item();
        final RawInventoryItem raw = new RawInventoryItem();
        raw.unserialize(bb);
        this.m_item.fromRaw(raw);
        while (bb.hasRemaining()) {
            final long itemUid = bb.getLong();
            final byte opType = bb.get();
            final BagOperation op = BagOperation.createFromType(opType);
            op.unSerialize(bb);
            if (op.getOperationType() == 0) {
                ((AddItemOperation)op).setInsideMove(true);
            }
            else if (op.getOperationType() == 2) {
                ((UpdateItemOperation)op).setInsideMove(true);
            }
            this.m_operations.put(itemUid, op);
        }
        return false;
    }
    
    public long getCompanionId() {
        return this.m_companionId;
    }
    
    public long getOriginalItemUid() {
        return this.m_originalItemUid;
    }
    
    public byte getEquipmentPosition() {
        return this.m_equipmentPosition;
    }
    
    public Item getItem() {
        return this.m_item;
    }
    
    public TLongObjectHashMap<BagOperation> getOperations() {
        return this.m_operations;
    }
    
    @Override
    public int getId() {
        return 5556;
    }
    
    @Override
    public String toString() {
        return "AddItemToCompanionEquipmentResultMessage{m_companionId=" + this.m_companionId + ", m_equipmentPosition=" + this.m_equipmentPosition + ", m_originalItemUid=" + this.m_originalItemUid + ", m_item=" + this.m_item + ", m_operations=" + this.m_operations + '}';
    }
}
