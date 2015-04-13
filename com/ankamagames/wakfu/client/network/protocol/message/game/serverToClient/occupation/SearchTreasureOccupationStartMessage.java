package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation;

import org.apache.log4j.*;
import java.nio.*;

public class SearchTreasureOccupationStartMessage extends AbstractOccupationModificationMessage
{
    protected static final Logger m_logger;
    private long m_duration;
    private int m_visualId;
    private int m_x;
    private int m_y;
    private short m_z;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_modificationType = buffer.get();
        this.m_occupationType = buffer.getShort();
        this.m_concernedPlayerId = buffer.getLong();
        this.m_duration = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        this.m_z = buffer.getShort();
        return true;
    }
    
    @Override
    public int getId() {
        return 4218;
    }
    
    public long getDuration() {
        return this.m_duration;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public short getZ() {
        return this.m_z;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SearchTreasureOccupationStartMessage.class);
    }
}
