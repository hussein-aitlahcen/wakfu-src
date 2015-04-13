package com.ankamagames.wakfu.client.network.protocol.message.global.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class ClientNationDiplomacyChangeResultMessage extends InputOnlyProxyMessage
{
    private int m_error;
    private long m_param;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_error = buffer.getInt();
        this.m_param = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 20036;
    }
    
    public int getError() {
        return this.m_error;
    }
    
    public long getParam() {
        return this.m_param;
    }
}
