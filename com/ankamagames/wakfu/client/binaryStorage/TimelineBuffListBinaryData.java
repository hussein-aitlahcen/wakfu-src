package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class TimelineBuffListBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_typeId;
    protected int m_gfxId;
    protected boolean m_forPlayer;
    protected int[] m_effectIds;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getTypeId() {
        return this.m_typeId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public boolean isForPlayer() {
        return this.m_forPlayer;
    }
    
    public int[] getEffectIds() {
        return this.m_effectIds;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_typeId = 0;
        this.m_gfxId = 0;
        this.m_forPlayer = false;
        this.m_effectIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_typeId = buffer.getInt();
        this.m_gfxId = buffer.getInt();
        this.m_forPlayer = buffer.readBoolean();
        this.m_effectIds = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.TIMELINE_BUFF_LIST.getId();
    }
}
