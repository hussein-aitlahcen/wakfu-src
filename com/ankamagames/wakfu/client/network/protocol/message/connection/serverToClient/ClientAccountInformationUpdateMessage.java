package com.ankamagames.wakfu.client.network.protocol.message.connection.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ClientAccountInformationUpdateMessage extends InputOnlyProxyMessage
{
    private byte[] m_serializedAccountInformations;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        final int serializedAccountInformationSize = bb.getInt();
        bb.get(this.m_serializedAccountInformations = new byte[serializedAccountInformationSize]);
        return true;
    }
    
    @Override
    public int getId() {
        return 1028;
    }
    
    public byte[] getSerializedAccountInformations() {
        return this.m_serializedAccountInformations.clone();
    }
}
