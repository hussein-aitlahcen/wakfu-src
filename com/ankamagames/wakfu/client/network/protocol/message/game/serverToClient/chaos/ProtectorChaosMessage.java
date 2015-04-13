package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.chaos;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public final class ProtectorChaosMessage extends InputOnlyProxyMessage
{
    private int m_protectorId;
    private boolean m_isInChaos;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buff = ByteBuffer.wrap(rawDatas);
        this.m_protectorId = buff.getInt();
        this.m_isInChaos = (buff.get() == 0);
        return true;
    }
    
    @Override
    public int getId() {
        return 9303;
    }
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public boolean isInChaos() {
        return this.m_isInChaos;
    }
}
