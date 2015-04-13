package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class DungeonDisplayerInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_dungeonId;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getDungeonId() {
        return this.m_dungeonId;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_dungeonId = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_dungeonId = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.DUNGEON_DISPLAYER_IE_PARAM.getId();
    }
}
