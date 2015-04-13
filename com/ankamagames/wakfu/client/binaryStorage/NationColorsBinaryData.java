package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class NationColorsBinaryData implements BinaryData
{
    protected int m_id;
    protected String m_color;
    
    public int getId() {
        return this.m_id;
    }
    
    public String getColor() {
        return this.m_color;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_color = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_color = buffer.readUTF8().intern();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.NATION_COLORS.getId();
    }
}
