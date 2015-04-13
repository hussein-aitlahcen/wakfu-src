package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation;

import org.apache.log4j.*;
import java.nio.*;

public class SimpleOccupationModificationMessage extends AbstractOccupationModificationMessage
{
    protected static final Logger m_logger;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.decode(buffer);
        return true;
    }
    
    protected void decode(final ByteBuffer buffer) {
        this.m_modificationType = buffer.get();
        this.m_occupationType = buffer.getShort();
        this.m_concernedPlayerId = buffer.getLong();
        buffer.get(this.m_data = new byte[buffer.getInt()]);
    }
    
    @Override
    public int getId() {
        return 4170;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SimpleOccupationModificationMessage.class);
    }
}
