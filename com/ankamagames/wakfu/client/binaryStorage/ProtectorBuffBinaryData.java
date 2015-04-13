package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ProtectorBuffBinaryData implements BinaryData
{
    protected int m_buffId;
    protected int m_gfxId;
    protected String m_criteria;
    protected byte m_origin;
    protected int[] m_effects;
    
    public int getBuffId() {
        return this.m_buffId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public String getCriteria() {
        return this.m_criteria;
    }
    
    public byte getOrigin() {
        return this.m_origin;
    }
    
    public int[] getEffects() {
        return this.m_effects;
    }
    
    @Override
    public void reset() {
        this.m_buffId = 0;
        this.m_gfxId = 0;
        this.m_criteria = null;
        this.m_origin = 0;
        this.m_effects = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_buffId = buffer.getInt();
        this.m_gfxId = buffer.getInt();
        this.m_criteria = buffer.readUTF8().intern();
        this.m_origin = buffer.get();
        this.m_effects = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.PROTECTOR_BUFF.getId();
    }
}
