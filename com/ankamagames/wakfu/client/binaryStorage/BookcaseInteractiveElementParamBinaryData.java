package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class BookcaseInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected byte m_size;
    
    public int getId() {
        return this.m_id;
    }
    
    public byte getSize() {
        return this.m_size;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_size = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_size = buffer.get();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.BOOKCASE_IE_PARAM.getId();
    }
}
