package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class HavenWorldPatchDefinitionBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_patchId;
    protected int m_kamaCost;
    protected int m_categoryId;
    protected int m_soundId;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getPatchId() {
        return this.m_patchId;
    }
    
    public int getKamaCost() {
        return this.m_kamaCost;
    }
    
    public int getCategoryId() {
        return this.m_categoryId;
    }
    
    public int getSoundId() {
        return this.m_soundId;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_patchId = 0;
        this.m_kamaCost = 0;
        this.m_categoryId = 0;
        this.m_soundId = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_patchId = buffer.getInt();
        this.m_kamaCost = buffer.getInt();
        this.m_categoryId = buffer.getInt();
        this.m_soundId = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.HAVEN_WORLD_PATCH_DEFINITION.getId();
    }
}
