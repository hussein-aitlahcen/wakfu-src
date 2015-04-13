package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class MonsterTypeDungeonBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_familyId;
    protected int m_dungeonId;
    protected short m_level;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public int getDungeonId() {
        return this.m_dungeonId;
    }
    
    public short getLevel() {
        return this.m_level;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_familyId = 0;
        this.m_dungeonId = 0;
        this.m_level = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_familyId = buffer.getInt();
        this.m_dungeonId = buffer.getInt();
        this.m_level = buffer.getShort();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.MONSTER_TYPE_DUNGEON.getId();
    }
}
