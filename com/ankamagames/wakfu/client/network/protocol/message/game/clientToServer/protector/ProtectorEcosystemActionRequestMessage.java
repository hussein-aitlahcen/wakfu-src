package com.ankamagames.wakfu.client.network.protocol.message.game.clientToServer.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ProtectorEcosystemActionRequestMessage extends OutputOnlyProxyMessage
{
    private int m_protectorId;
    private byte m_actionId;
    private int m_familyId;
    
    public void setProtectorId(final int protectorId) {
        this.m_protectorId = protectorId;
    }
    
    public void setActionId(final byte actionId) {
        this.m_actionId = actionId;
    }
    
    public void setFamilyId(final int familyId) {
        this.m_familyId = familyId;
    }
    
    @Override
    public byte[] encode() {
        final ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.putInt(this.m_protectorId);
        buffer.put(this.m_actionId);
        buffer.putInt(this.m_familyId);
        return this.addClientHeader((byte)3, buffer.array());
    }
    
    @Override
    public int getId() {
        return 15329;
    }
}
