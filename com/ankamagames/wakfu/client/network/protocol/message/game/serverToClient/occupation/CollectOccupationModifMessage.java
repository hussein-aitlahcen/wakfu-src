package com.ankamagames.wakfu.client.network.protocol.message.game.serverToClient.occupation;

import org.apache.log4j.*;
import java.nio.*;

public class CollectOccupationModifMessage extends AbstractOccupationModificationMessage
{
    protected static final Logger m_logger;
    private byte m_flag;
    private long m_estimatedTime;
    private byte m_craftId;
    private int m_actionId;
    private byte m_usedSlots;
    private int m_x;
    private int m_y;
    private double m_progress;
    private int m_itemRefId;
    
    public CollectOccupationModifMessage() {
        super();
        this.m_flag = 0;
        this.m_estimatedTime = 0L;
        this.m_craftId = 0;
        this.m_actionId = 0;
        this.m_usedSlots = 0;
    }
    
    @Override
    public boolean decode(final byte[] rawDatas) {
        final ByteBuffer buffer = ByteBuffer.wrap(rawDatas);
        this.m_modificationType = buffer.get();
        this.m_occupationType = buffer.getShort();
        this.m_concernedPlayerId = buffer.getLong();
        this.m_estimatedTime = buffer.getLong();
        this.m_flag = buffer.get();
        this.m_x = buffer.getInt();
        this.m_y = buffer.getInt();
        this.m_craftId = buffer.get();
        this.m_actionId = buffer.getInt();
        this.m_usedSlots = buffer.get();
        this.m_progress = buffer.getDouble();
        this.m_itemRefId = buffer.getInt();
        return true;
    }
    
    @Override
    public int getId() {
        return 4174;
    }
    
    public byte getFlag() {
        return this.m_flag;
    }
    
    public long getEstimatedTime() {
        return this.m_estimatedTime;
    }
    
    public byte getCraftId() {
        return this.m_craftId;
    }
    
    public byte getUsedSlots() {
        return this.m_usedSlots;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public int getActionId() {
        return this.m_actionId;
    }
    
    public double getProgress() {
        return this.m_progress;
    }
    
    public int getItemRefId() {
        return this.m_itemRefId;
    }
    
    static {
        m_logger = Logger.getLogger((Class)CollectOccupationModifMessage.class);
    }
}
