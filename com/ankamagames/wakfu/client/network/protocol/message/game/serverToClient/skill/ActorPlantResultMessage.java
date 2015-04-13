package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.skill;

import com.ankamagames.baseImpl.client.proxyclient.base.network.protocol.message.*;
import org.apache.log4j.*;
import java.nio.*;

public class ActorPlantResultMessage extends InputOnlyProxyMessage
{
    protected static final Logger m_logger;
    protected static final boolean DEBUG_MODE = false;
    private long m_itemUID;
    private int m_quantity;
    private boolean m_isSuccess;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_itemUID = buffer.getLong();
        this.m_quantity = buffer.getInt();
        final byte successValue = buffer.get();
        this.m_isSuccess = (successValue == 1);
        return true;
    }
    
    @Override
    public int getId() {
        return 4142;
    }
    
    public long getItemUID() {
        return this.m_itemUID;
    }
    
    public int getQuantity() {
        return this.m_quantity;
    }
    
    public boolean isSuccess() {
        return this.m_isSuccess;
    }
    
    static {
        m_logger = Logger.getLogger((Class)ActorPlantResultMessage.class);
    }
}
