package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.lock;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import java.nio.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.time.calendar.*;

public class LockIncrementedMessage extends InputOnlyProxyMessage
{
    private int m_lockId;
    private int m_currentLockValue;
    private GameDateConst m_currentLockValueLastModification;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_lockId = buffer.getInt();
        this.m_currentLockValue = buffer.getInt();
        this.m_currentLockValueLastModification = GameDate.fromLong(buffer.getLong());
        return true;
    }
    
    public int getCurrentLockValue() {
        return this.m_currentLockValue;
    }
    
    public GameDateConst getCurrentLockValueLastModification() {
        return this.m_currentLockValueLastModification;
    }
    
    public int getLockId() {
        return this.m_lockId;
    }
    
    @Override
    public int getId() {
        return 15502;
    }
}
