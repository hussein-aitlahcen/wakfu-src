package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.item;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import com.ankamagames.wakfu.common.game.item.visitor.operation.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class AddBagResultMessage extends InputOnlyProxyMessage
{
    private long m_characterId;
    private final TLongObjectHashMap<BagOperation> m_operations;
    private final RawBag m_bag;
    
    public AddBagResultMessage() {
        super();
        this.m_operations = new TLongObjectHashMap<BagOperation>();
        this.m_bag = new RawBag();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_characterId = buffer.getLong();
        final byte[] data = new byte[buffer.getInt()];
        buffer.get(data);
        this.m_bag.unserialize(ByteBuffer.wrap(data));
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
    
    public RawBag getBag() {
        return this.m_bag;
    }
    
    public long getCharacterId() {
        return this.m_characterId;
    }
    
    @Override
    public int getId() {
        return 5222;
    }
}
