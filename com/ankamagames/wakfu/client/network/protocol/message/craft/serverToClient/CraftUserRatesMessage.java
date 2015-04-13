package com.ankamagames.wakfu.client.network.protocol.message.craft.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.craftNew.*;

public class CraftUserRatesMessage extends InputOnlyProxyMessage
{
    private ArrayList<CraftUserRateEntry> m_craftUserRateEntries;
    
    public CraftUserRatesMessage() {
        super();
        this.m_craftUserRateEntries = new ArrayList<CraftUserRateEntry>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        while (buffer.hasRemaining()) {
            this.m_craftUserRateEntries.add(CraftSerializer.unSerializeCraftRateEntry(buffer));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 32014;
    }
    
    public ArrayList<CraftUserRateEntry> getCraftUserRateEntries() {
        return this.m_craftUserRateEntries;
    }
}
