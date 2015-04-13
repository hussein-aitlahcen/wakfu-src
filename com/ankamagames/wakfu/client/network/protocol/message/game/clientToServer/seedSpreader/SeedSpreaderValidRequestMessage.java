package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.seedSpreader;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.util.*;

public class SeedSpreaderValidRequestMessage extends OutputOnlyProxyMessage
{
    private final long m_seedSpreaderId;
    private final ArrayList<ObjectPair<Long, Short>> m_items;
    
    public SeedSpreaderValidRequestMessage(final long seedSpreaderId) {
        super();
        this.m_items = new ArrayList<ObjectPair<Long, Short>>();
        this.m_seedSpreaderId = seedSpreaderId;
    }
    
    public void addItem(final long id, final short quantity) {
        this.m_items.add(new ObjectPair<Long, Short>(id, quantity));
    }
    
    @Override
    public byte[] encode() {
        final ByteArray byteArray = new ByteArray();
        byteArray.putLong(this.m_seedSpreaderId);
        for (final ObjectPair<Long, Short> item : this.m_items) {
            byteArray.putLong(item.getFirst());
            byteArray.putShort(item.getSecond());
        }
        return this.addClientHeader((byte)3, byteArray.toArray());
    }
    
    @Override
    public int getId() {
        return 15943;
    }
}
