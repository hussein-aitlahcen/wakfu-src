package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.game.interactiveElement.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import gnu.trove.*;
import com.ankamagames.framework.kernel.core.common.collections.*;
import java.nio.*;

public class DynamicInteractiveElementSpawnMessage extends InputOnlyProxyMessage
{
    private final TLongObjectHashMap<ObjectTriplet<Long, byte[], Long>> m_interactiveElements;
    
    public DynamicInteractiveElementSpawnMessage() {
        super();
        this.m_interactiveElements = new TLongObjectHashMap<ObjectTriplet<Long, byte[], Long>>();
    }
    
    public TLongObjectHashMap<ObjectTriplet<Long, byte[], Long>> getInteractiveElements() {
        return this.m_interactiveElements;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        for (int count = buffer.getShort(), i = 0; i < count; ++i) {
            final long id = buffer.getLong();
            final long templateId = buffer.getLong();
            final short size = buffer.getShort();
            final byte[] datas = new byte[size];
            buffer.get(datas);
            final long position = buffer.getLong();
            this.m_interactiveElements.put(id, new ObjectTriplet<Long, byte[], Long>(templateId, datas, position));
        }
        return true;
    }
    
    @Override
    public int getId() {
        return 204;
    }
}
