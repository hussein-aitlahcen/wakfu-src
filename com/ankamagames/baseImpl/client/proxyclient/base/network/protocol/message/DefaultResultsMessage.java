package com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message;

import java.nio.*;

public class DefaultResultsMessage extends QueryResultsResultsMessage
{
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
}
