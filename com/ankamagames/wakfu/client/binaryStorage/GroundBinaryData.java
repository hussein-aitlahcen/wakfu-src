package com.ankamagames.wakfu.client.binaryStorage;

import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class GroundBinaryData implements BinaryData
{
    protected int m_id;
    protected TIntShortHashMap m_resourceFertility;
    protected TShortShortHashMap m_resourceTypeFertility;
    
    public int getId() {
        return this.m_id;
    }
    
    public TIntShortHashMap getResourceFertility() {
        return this.m_resourceFertility;
    }
    
    public TShortShortHashMap getResourceTypeFertility() {
        return this.m_resourceTypeFertility;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_resourceFertility = null;
        this.m_resourceTypeFertility = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        final int resourceFertilityCount = buffer.getInt();
        this.m_resourceFertility = new TIntShortHashMap(resourceFertilityCount);
        for (int iResourceFertility = 0; iResourceFertility < resourceFertilityCount; ++iResourceFertility) {
            final int resourceFertilityKey = buffer.getInt();
            final short resourceFertilityValue = buffer.getShort();
            this.m_resourceFertility.put(resourceFertilityKey, resourceFertilityValue);
        }
        final int resourceTypeFertilityCount = buffer.getInt();
        this.m_resourceTypeFertility = new TShortShortHashMap(resourceTypeFertilityCount);
        for (int iResourceTypeFertility = 0; iResourceTypeFertility < resourceTypeFertilityCount; ++iResourceTypeFertility) {
            final short resourceTypeFertilityKey = buffer.getShort();
            final short resourceTypeFertilityValue = buffer.getShort();
            this.m_resourceTypeFertility.put(resourceTypeFertilityKey, resourceTypeFertilityValue);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.GROUND.getId();
    }
}
