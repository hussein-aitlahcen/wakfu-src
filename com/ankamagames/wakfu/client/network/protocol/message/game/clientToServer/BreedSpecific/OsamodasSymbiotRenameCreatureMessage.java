package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.BreedSpecific;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.framework.kernel.utils.*;
import java.nio.*;

public class OsamodasSymbiotRenameCreatureMessage extends OutputOnlyProxyMessage
{
    private byte m_creatureIndex;
    private String m_creatureName;
    
    @Override
    public byte[] encode() {
        final byte[] name = StringUtils.toUTF8(this.m_creatureName);
        final ByteBuffer buffer = ByteBuffer.allocate(2 + name.length);
        buffer.put(this.m_creatureIndex);
        buffer.put((byte)name.length);
        buffer.put(name);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 5405;
    }
    
    public void setCreatureIndex(final byte creatureId) {
        this.m_creatureIndex = creatureId;
    }
    
    public void setCreatureName(final String creatureName) {
        this.m_creatureName = creatureName;
    }
}
