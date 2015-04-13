package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.personalSpace.flea;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import com.ankamagames.wakfu.common.rawData.*;
import java.nio.*;

public class FetchTransactionLogResultMessage extends InputOnlyProxyMessage
{
    private RawTransactionLog m_transactionLog;
    
    public RawTransactionLog getTransactionLog() {
        return this.m_transactionLog;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        this.m_transactionLog = new RawTransactionLog();
        return this.m_transactionLog.unserialize(ByteBuffer.wrap(rawDatas));
    }
    
    @Override
    public int getId() {
        return 10042;
    }
}
