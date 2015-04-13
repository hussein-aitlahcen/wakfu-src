package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ZaapLinkBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_start;
    protected int m_end;
    protected int m_cost;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getStart() {
        return this.m_start;
    }
    
    public int getEnd() {
        return this.m_end;
    }
    
    public int getCost() {
        return this.m_cost;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_start = 0;
        this.m_end = 0;
        this.m_cost = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_start = buffer.getInt();
        this.m_end = buffer.getInt();
        this.m_cost = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ZAAP_LINK.getId();
    }
}
