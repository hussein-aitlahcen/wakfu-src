package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class MonsterTypeBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_parentId;
    protected byte m_type;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getParentId() {
        return this.m_parentId;
    }
    
    public byte getType() {
        return this.m_type;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_parentId = 0;
        this.m_type = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_parentId = buffer.getInt();
        this.m_type = buffer.get();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.MONSTER_TYPE.getId();
    }
}
