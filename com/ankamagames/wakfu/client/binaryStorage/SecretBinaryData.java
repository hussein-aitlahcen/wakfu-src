package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class SecretBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_level;
    protected short m_itemId;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    public short getItemId() {
        return this.m_itemId;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_level = 0;
        this.m_itemId = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_level = buffer.getShort();
        this.m_itemId = buffer.getShort();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.SECRET.getId();
    }
}
