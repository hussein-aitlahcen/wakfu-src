package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import java.nio.*;

public class InteractiveElementSpawnMessage extends InputOnlyProxyMessage
{
    private final TLongObjectHashMap<byte[]> m_interactiveElements;
    
    public InteractiveElementSpawnMessage() {
        super();
        this.m_interactiveElements = new TLongObjectHashMap<byte[]>();
    }
    
    public TLongObjectHashMap<byte[]> getInteractiveElements() {
        return this.m_interactiveElements;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        for (int count = buffer.getShort(), i = 0; i < count; ++i) {
            final long id = buffer.getLong();
            final short size = buffer.getShort();
            final byte[] datas = new byte[size];
            buffer.get(datas);
            this.m_interactiveElements.put(id, datas);
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 200;
    }
}
