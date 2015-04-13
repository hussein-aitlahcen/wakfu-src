package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class MonsterTypePestBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_familyId;
    protected int m_pestMonsterId;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public int getPestMonsterId() {
        return this.m_pestMonsterId;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_familyId = 0;
        this.m_pestMonsterId = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_familyId = buffer.getInt();
        this.m_pestMonsterId = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.MONSTER_TYPE_PEST.getId();
    }
}
