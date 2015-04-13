package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.lock;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class LockActivatedMessage extends InputOnlyProxyMessage
{
    private int m_lockId;
    private GameDateConst m_lockDate;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_lockId = buffer.getInt();
        this.m_lockDate = GameDate.fromLong(buffer.getLong());
        return true;
    }
    
    public GameDateConst getLockDate() {
        return this.m_lockDate;
    }
    
    public int getLockId() {
        return this.m_lockId;
    }
    
    @Override
    public int getId() {
        return 15500;
    }
}
