package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class SkillActionBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_mruGfxId;
    protected String m_mruKey;
    protected int[] m_associatedItems;
    protected String m_animLinkage;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getMruGfxId() {
        return this.m_mruGfxId;
    }
    
    public String getMruKey() {
        return this.m_mruKey;
    }
    
    public int[] getAssociatedItems() {
        return this.m_associatedItems;
    }
    
    public String getAnimLinkage() {
        return this.m_animLinkage;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_mruGfxId = 0;
        this.m_mruKey = null;
        this.m_associatedItems = null;
        this.m_animLinkage = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_mruGfxId = buffer.getInt();
        this.m_mruKey = buffer.readUTF8().intern();
        this.m_associatedItems = buffer.readIntArray();
        this.m_animLinkage = buffer.readUTF8().intern();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.SKILL_ACTION.getId();
    }
}
