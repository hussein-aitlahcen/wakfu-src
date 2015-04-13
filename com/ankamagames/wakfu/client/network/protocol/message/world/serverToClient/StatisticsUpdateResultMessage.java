package com.ankamagames.wakfu.client.network.protocol.message.world.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;

public final class StatisticsUpdateResultMessage extends InputOnlyProxyMessage
{
    private ArrayList<byte[]> m_serializedNodeSets;
    private long m_timestamp;
    
    public StatisticsUpdateResultMessage() {
        super();
        this.m_serializedNodeSets = new ArrayList<byte[]>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.m_timestamp = bb.getLong();
        for (int n = bb.getInt(), i = 0; i < n; ++i) {
            final byte[] data = new byte[bb.getInt()];
            bb.get(data);
            this.m_serializedNodeSets.add(data);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 2058;
    }
    
    public ArrayList<byte[]> getSerializedNodeSets() {
        return this.m_serializedNodeSets;
    }
    
    public long getTimestamp() {
        return this.m_timestamp;
    }
}
