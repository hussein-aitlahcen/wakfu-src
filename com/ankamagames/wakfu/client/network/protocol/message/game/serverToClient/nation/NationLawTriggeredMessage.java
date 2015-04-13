package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.nation;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;

public class NationLawTriggeredMessage extends InputOnlyProxyMessage
{
    private int m_nationId;
    private long m_lawId;
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    public long getLawId() {
        return this.m_lawId;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_nationId = buffer.getInt();
        this.m_lawId = buffer.getLong();
        return true;
    }
    
    @Override
    public int getId() {
        return 15134;
    }
}
