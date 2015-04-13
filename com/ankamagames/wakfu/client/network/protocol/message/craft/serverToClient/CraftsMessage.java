package com.ankamagames.wakfu.client.network.protocol.message.craft.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;
import com.ankamagames.wakfu.common.game.craftNew.*;

public class CraftsMessage extends InputOnlyProxyMessage
{
    private ArrayList<Craft> m_crafts;
    
    public CraftsMessage() {
        super();
        this.m_crafts = new ArrayList<Craft>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        while (buffer.hasRemaining()) {
            this.m_crafts.add(CraftSerializer.unSerializeCraft(buffer));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 32012;
    }
    
    public ArrayList<Craft> getCrafts() {
        return this.m_crafts;
    }
}
