package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class ClientNationSynchronizationMessage extends InputOnlyProxyMessage
{
    private final TIntObjectHashMap<byte[]> m_nations;
    
    public ClientNationSynchronizationMessage() {
        super();
        this.m_nations = new TIntObjectHashMap<byte[]>();
    }
    
    public TIntObjectIterator<byte[]> nationsIterator() {
        return this.m_nations.iterator();
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        while (buffer.hasRemaining()) {
            final int nationId = buffer.getInt();
            final int size = buffer.getInt();
            final byte[] data = new byte[size];
            buffer.get(data);
            this.m_nations.put(nationId, data);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 20000;
    }
}
