package com.ankamagames.wakfu.client.network.protocol.message.craft.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class CraftInventoryMessage extends InputOnlyProxyMessage
{
    private TIntIntHashMap m_inventory;
    
    public CraftInventoryMessage() {
        super();
        this.m_inventory = new TIntIntHashMap();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        while (buffer.hasRemaining()) {
            this.m_inventory.put(buffer.getInt(), buffer.getInt());
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 32006;
    }
    
    public TIntIntHashMap getInventory() {
        return this.m_inventory;
    }
}
