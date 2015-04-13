package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class HavenWorldBuildingDecoBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_catalogEntryId;
    protected int m_kamaCost;
    protected int m_ressourceCost;
    protected int m_editorGroupId;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getCatalogEntryId() {
        return this.m_catalogEntryId;
    }
    
    public int getKamaCost() {
        return this.m_kamaCost;
    }
    
    public int getRessourceCost() {
        return this.m_ressourceCost;
    }
    
    public int getEditorGroupId() {
        return this.m_editorGroupId;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_catalogEntryId = 0;
        this.m_kamaCost = 0;
        this.m_ressourceCost = 0;
        this.m_editorGroupId = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_catalogEntryId = buffer.getShort();
        this.m_kamaCost = buffer.getInt();
        this.m_ressourceCost = buffer.getInt();
        this.m_editorGroupId = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.HAVEN_WORLD_BUILDING_DECO_DEFINITION.getId();
    }
}
