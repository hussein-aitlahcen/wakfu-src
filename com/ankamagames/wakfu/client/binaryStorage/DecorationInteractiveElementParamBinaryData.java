package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class DecorationInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int[] m_havreGemTypes;
    
    public int getId() {
        return this.m_id;
    }
    
    public int[] getHavreGemTypes() {
        return this.m_havreGemTypes;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_havreGemTypes = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_havreGemTypes = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.DECORATION_IE_PARAM.getId();
    }
}
