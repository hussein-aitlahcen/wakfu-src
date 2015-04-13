package com.ankamagames.wakfu.client.binaryStorage;

import java.sql.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AlmanachDateBinaryData implements BinaryData
{
    protected int m_id;
    protected Timestamp m_date;
    protected int m_almanachEntryId;
    
    public int getId() {
        return this.m_id;
    }
    
    public Timestamp getDate() {
        return this.m_date;
    }
    
    public int getAlmanachEntryId() {
        return this.m_almanachEntryId;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_date = null;
        this.m_almanachEntryId = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_date = new Timestamp(buffer.getLong());
        this.m_almanachEntryId = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ALMANACH_DATE.getId();
    }
}
