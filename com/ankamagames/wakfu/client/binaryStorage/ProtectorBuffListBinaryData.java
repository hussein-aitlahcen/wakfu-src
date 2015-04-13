package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ProtectorBuffListBinaryData implements BinaryData
{
    protected int m_buffListId;
    protected int[] m_buffLists;
    
    public int getBuffListId() {
        return this.m_buffListId;
    }
    
    public int[] getBuffLists() {
        return this.m_buffLists;
    }
    
    @Override
    public void reset() {
        this.m_buffListId = 0;
        this.m_buffLists = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_buffListId = buffer.getInt();
        this.m_buffLists = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.PROTECTOR_BUFF_LIST.getId();
    }
}
