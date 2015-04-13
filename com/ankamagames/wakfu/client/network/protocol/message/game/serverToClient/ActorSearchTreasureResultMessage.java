package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class ActorSearchTreasureResultMessage extends InputOnlyProxyMessage
{
    protected static final Logger m_logger;
    protected static final boolean DEBUG_MODE = false;
    private boolean m_isSuccess;
    private int m_treasureId;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        final byte successValue = buffer.get();
        this.m_isSuccess = (successValue == 1);
        this.m_treasureId = ((buffer.remaining() > 0) ? buffer.getInt() : -1);
        return true;
    }
    
    @Override
    public int getId() {
        return 4180;
    }
    
    public boolean isSuccess() {
        return this.m_isSuccess;
    }
    
    public int getTreasureId() {
        return this.m_treasureId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActorSearchTreasureResultMessage.class);
    }
}
