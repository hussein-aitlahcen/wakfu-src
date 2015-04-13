package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.protector;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ProtectorManagementAnswerMessage extends InputOnlyProxyMessage
{
    private int m_protectorId;
    private byte[] m_managementData;
    private byte[] m_managedTerritoryClimate;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_protectorId = buffer.getInt();
        int size = buffer.getShort() & 0xFFFF;
        buffer.get(this.m_managementData = new byte[size]);
        size = (buffer.getShort() & 0xFFFF);
        if (size > 0) {
            buffer.get(this.m_managedTerritoryClimate = new byte[size]);
        }
        return true;
    }
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public byte[] getManagementData() {
        return this.m_managementData;
    }
    
    public byte[] getManagedTerritoryClimate() {
        return this.m_managedTerritoryClimate;
    }
    
    @Override
    public int getId() {
        return 15320;
    }
}
