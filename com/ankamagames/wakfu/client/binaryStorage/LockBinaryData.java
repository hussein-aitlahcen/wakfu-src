package com.ankamagames.wakfu.client.binaryStorage;

import java.sql.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class LockBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_lockedItemId;
    protected int m_lockValue;
    protected long m_periodDuration;
    protected Timestamp m_unlockDate;
    protected boolean m_availableForCitizensOnly;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getLockedItemId() {
        return this.m_lockedItemId;
    }
    
    public int getLockValue() {
        return this.m_lockValue;
    }
    
    public long getPeriodDuration() {
        return this.m_periodDuration;
    }
    
    public Timestamp getUnlockDate() {
        return this.m_unlockDate;
    }
    
    public boolean isAvailableForCitizensOnly() {
        return this.m_availableForCitizensOnly;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_lockedItemId = 0;
        this.m_lockValue = 0;
        this.m_periodDuration = 0L;
        this.m_unlockDate = null;
        this.m_availableForCitizensOnly = false;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_lockedItemId = buffer.getInt();
        this.m_lockValue = buffer.getInt();
        this.m_periodDuration = buffer.getLong();
        this.m_unlockDate = new Timestamp(buffer.getLong());
        this.m_availableForCitizensOnly = buffer.readBoolean();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.LOCK.getId();
    }
}
