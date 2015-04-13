package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ClimateBonusListBinaryData implements BinaryData
{
    protected int m_buffListId;
    protected int[] m_entries;
    
    public int getBuffListId() {
        return this.m_buffListId;
    }
    
    public int[] getEntries() {
        return this.m_entries;
    }
    
    @Override
    public void reset() {
        this.m_buffListId = 0;
        this.m_entries = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_buffListId = buffer.getInt();
        this.m_entries = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CLIMATE_BONUS_LIST.getId();
    }
}
