package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class MonsterTypeRelashionshipBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_familyFrom;
    protected int m_familyTo;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getFamilyFrom() {
        return this.m_familyFrom;
    }
    
    public int getFamilyTo() {
        return this.m_familyTo;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_familyFrom = 0;
        this.m_familyTo = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_familyFrom = buffer.getInt();
        this.m_familyTo = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.MONSTER_TYPE_RELASHIONSHIP.getId();
    }
}
