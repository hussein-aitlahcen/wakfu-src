package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.util.*;
import java.nio.*;

public class InstanceProtectorsUpdateMessage extends InputOnlyProxyMessage
{
    private ArrayList<byte[]> m_serializedProtectors;
    
    public InstanceProtectorsUpdateMessage() {
        super();
        this.m_serializedProtectors = new ArrayList<byte[]>();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final byte[] serializedProtectors = new byte[buffer.getShort() & 0xFFFF];
        buffer.get(serializedProtectors);
        final ByteBuffer b = ByteBuffer.wrap(serializedProtectors);
        final short nbProtectors = b.getShort();
        for (int i = 0; i < nbProtectors; ++i) {
            final byte[] serializedProtector = new byte[b.getShort() & 0xFFFF];
            b.get(serializedProtector);
            this.m_serializedProtectors.add(serializedProtector);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 15326;
    }
    
    public ArrayList<byte[]> getSerializedProtectors() {
        return this.m_serializedProtectors;
    }
}
