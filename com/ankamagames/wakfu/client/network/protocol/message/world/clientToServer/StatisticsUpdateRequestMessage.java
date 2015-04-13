package com.ankamagames.wakfu.client.network.protocol.message.world.clientToServer;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import java.util.*;

public final class StatisticsUpdateRequestMessage extends OutputOnlyProxyMessage
{
    private ArrayList<byte[]> m_serializeRequests;
    private long m_timestamp;
    
    public StatisticsUpdateRequestMessage() {
        super();
        this.m_serializeRequests = new ArrayList<byte[]>();
    }
    
    @Override
    public byte[] encode() {
        int size = 0;
        for (final byte[] data : this.m_serializeRequests) {
            size += data.length + 4;
        }
        final ByteBuffer bb = ByteBuffer.allocate(12 + size);
        bb.putLong(this.m_timestamp);
        bb.putInt(this.m_serializeRequests.size());
        for (final byte[] data2 : this.m_serializeRequests) {
            bb.putInt(data2.length);
            bb.put(data2);
        }
        return this.addClientHeader((byte)2, bb.array());
    }
    
    @Override
    public int getId() {
        return 2057;
    }
    
    public void addSerialisedRequest(final byte[] data) {
        this.m_serializeRequests.add(data);
    }
    
    public void setTimeStamp(final long timeStamp) {
        this.m_timestamp = timeStamp;
    }
    
    public static int getSizeOfEmptyMessage() {
        return 12;
    }
    
    public int getSerializedRequestsCount() {
        return this.m_serializeRequests.size();
    }
}
