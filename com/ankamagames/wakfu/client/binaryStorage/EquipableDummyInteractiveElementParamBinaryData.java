package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class EquipableDummyInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected String m_animName;
    protected byte m_sex;
    
    public int getId() {
        return this.m_id;
    }
    
    public String getAnimName() {
        return this.m_animName;
    }
    
    public byte getSex() {
        return this.m_sex;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_animName = null;
        this.m_sex = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_animName = buffer.readUTF8().intern();
        this.m_sex = buffer.get();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.EQUIPABLE_DUMMY_IE_PARAM.getId();
    }
}
