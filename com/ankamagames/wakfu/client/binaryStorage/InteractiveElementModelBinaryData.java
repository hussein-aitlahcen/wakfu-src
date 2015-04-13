package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class InteractiveElementModelBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_type;
    protected int m_gfx;
    protected int m_color;
    protected byte m_height;
    protected int m_particleId;
    protected int m_particleOffsetZ;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getType() {
        return this.m_type;
    }
    
    public int getGfx() {
        return this.m_gfx;
    }
    
    public int getColor() {
        return this.m_color;
    }
    
    public byte getHeight() {
        return this.m_height;
    }
    
    public int getParticleId() {
        return this.m_particleId;
    }
    
    public int getParticleOffsetZ() {
        return this.m_particleOffsetZ;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_type = 0;
        this.m_gfx = 0;
        this.m_color = 0;
        this.m_height = 0;
        this.m_particleId = 0;
        this.m_particleOffsetZ = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_type = buffer.getShort();
        this.m_gfx = buffer.getInt();
        this.m_color = buffer.getInt();
        this.m_height = buffer.get();
        this.m_particleId = buffer.getInt();
        this.m_particleOffsetZ = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.INTERACTIVE_ELEMENT_MODEL.getId();
    }
}
