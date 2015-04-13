package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;

public abstract class QueryResultsResultsMessage extends InputOnlyProxyMessage
{
    private int m_queryResultCode;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer bb = ByteBuffer.wrap(rawDatas);
        this.decodeQueryResultCode(bb);
        return true;
    }
    
    @Override
    public int getId() {
        return 103;
    }
    
    protected void decodeQueryResultCode(final ByteBuffer buffer) {
        this.m_queryResultCode = buffer.getInt();
    }
    
    public int getQueryResultCode() {
        return this.m_queryResultCode;
    }
}
