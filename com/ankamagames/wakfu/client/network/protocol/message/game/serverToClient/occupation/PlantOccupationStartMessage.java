package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation;

import org.apache.log4j.*;
import java.nio.*;

public class PlantOccupationStartMessage extends AbstractOccupationModificationMessage
{
    protected static final Logger m_logger;
    private int m_craftId;
    private long m_duration;
    private int m_visualId;
    private int m_resX;
    private int m_resY;
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_modificationType = buffer.get();
        this.m_occupationType = buffer.getShort();
        this.m_concernedPlayerId = buffer.getLong();
        this.m_craftId = buffer.getInt();
        this.m_duration = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_resX = buffer.getInt();
        this.m_resY = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 4172;
    }
    
    public int getCraftId() {
        return this.m_craftId;
    }
    
    public long getDuration() {
        return this.m_duration;
    }
    
    public int getResX() {
        return this.m_resX;
    }
    
    public int getResY() {
        return this.m_resY;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)PlantOccupationStartMessage.class);
    }
}
