package com.ankamagames.framework.graphics.engine.Anm2;

import com.ankamagames.framework.fileFormat.io.*;
import java.io.*;

public final class AnmHeader
{
    public static final int USE_ATLAS = 1;
    public static final int USE_LOCAL_INDEX = 2;
    public static final int USE_PERFECT_HIT_TEST = 4;
    public static final int OPTIMIZED = 8;
    public static final int USE_TRANSFORM_INDEX = 16;
    private byte m_versionNumber;
    private float m_frameRate;
    
    public AnmHeader() {
        super();
        this.m_frameRate = 25.0f;
    }
    
    public void load(final ExtendedDataInputStream bitStream) {
        this.m_versionNumber = bitStream.readByte();
        bitStream.readShort();
        this.m_frameRate = bitStream.readByte();
    }
    
    public void save(final OutputBitStream bitStream) throws IOException {
        bitStream.writeByte(this.m_versionNumber);
        bitStream.writeShort((short)0);
        bitStream.writeByte((byte)this.m_frameRate);
    }
    
    public float getFrameRate() {
        return this.m_frameRate;
    }
    
    public boolean isUseAtlas() {
        return (this.m_versionNumber & 0x1) != 0x0;
    }
    
    public boolean isUseLocalIndex() {
        return (this.m_versionNumber & 0x2) != 0x0;
    }
    
    public void setIndexed() {
        this.m_versionNumber |= 0x2;
    }
    
    public boolean usePerfectHitTest() {
        return (this.m_versionNumber & 0x4) == 0x4;
    }
    
    public void setUsePerfectHitTest() {
        this.m_versionNumber |= 0x4;
    }
    
    public boolean useTransformIndex() {
        return (this.m_versionNumber & 0x10) == 0x10;
    }
    
    public void setUseTransformIndex() {
        this.m_versionNumber |= 0x10;
    }
    
    public boolean isOptimized() {
        return (this.m_versionNumber & 0x8) == 0x8;
    }
    
    public void setOptimized() {
        this.m_versionNumber |= 0x8;
    }
}
