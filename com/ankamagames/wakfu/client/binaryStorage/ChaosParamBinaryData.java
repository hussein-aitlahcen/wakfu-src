package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class ChaosParamBinaryData implements BinaryData
{
    protected byte m_chaosLevel;
    protected int m_chaosCollectorParamId;
    
    public byte getChaosLevel() {
        return this.m_chaosLevel;
    }
    
    public int getChaosCollectorParamId() {
        return this.m_chaosCollectorParamId;
    }
    
    @Override
    public void reset() {
        this.m_chaosLevel = 0;
        this.m_chaosCollectorParamId = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_chaosLevel = buffer.get();
        this.m_chaosCollectorParamId = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        throw new UnsupportedOperationException();
    }
}
