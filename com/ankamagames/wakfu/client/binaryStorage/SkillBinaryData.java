package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class SkillBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_type;
    protected int m_scriptId;
    protected int m_mruGfxId;
    protected String m_mruKey;
    protected String m_animLinkage;
    protected int[] m_associatedItemTypes;
    protected int[] m_associatedItems;
    protected int m_maxLevel;
    protected boolean m_isInnate;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getType() {
        return this.m_type;
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
    
    public int getMruGfxId() {
        return this.m_mruGfxId;
    }
    
    public String getMruKey() {
        return this.m_mruKey;
    }
    
    public String getAnimLinkage() {
        return this.m_animLinkage;
    }
    
    public int[] getAssociatedItemTypes() {
        return this.m_associatedItemTypes;
    }
    
    public int[] getAssociatedItems() {
        return this.m_associatedItems;
    }
    
    public int getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public boolean isInnate() {
        return this.m_isInnate;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_type = 0;
        this.m_scriptId = 0;
        this.m_mruGfxId = 0;
        this.m_mruKey = null;
        this.m_animLinkage = null;
        this.m_associatedItemTypes = null;
        this.m_associatedItems = null;
        this.m_maxLevel = 0;
        this.m_isInnate = false;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_type = buffer.getInt();
        this.m_scriptId = buffer.getInt();
        this.m_mruGfxId = buffer.getInt();
        this.m_mruKey = buffer.readUTF8().intern();
        this.m_animLinkage = buffer.readUTF8().intern();
        this.m_associatedItemTypes = buffer.readIntArray();
        this.m_associatedItems = buffer.readIntArray();
        this.m_maxLevel = buffer.getInt();
        this.m_isInnate = buffer.readBoolean();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.SKILL.getId();
    }
}
