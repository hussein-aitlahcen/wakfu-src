package com.ankamagames.wakfu.client.binaryStorage;

import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class CharacGainPerLevelBinaryData implements BinaryData
{
    protected short m_breedId;
    protected TByteFloatHashMap m_gains;
    
    public short getBreedId() {
        return this.m_breedId;
    }
    
    public TByteFloatHashMap getGains() {
        return this.m_gains;
    }
    
    @Override
    public void reset() {
        this.m_breedId = 0;
        this.m_gains = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_breedId = buffer.getShort();
        final int gainCount = buffer.getInt();
        this.m_gains = new TByteFloatHashMap(gainCount);
        for (int iGain = 0; iGain < gainCount; ++iGain) {
            final byte gainKey = buffer.get();
            final float gainValue = buffer.getFloat();
            this.m_gains.put(gainKey, gainValue);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CHARAC_GAIN_PER_LEVEL.getId();
    }
}
