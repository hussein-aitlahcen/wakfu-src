package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class RewardDisplayerInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int[] m_itemIds;
    
    public int getId() {
        return this.m_id;
    }
    
    public int[] getItemIds() {
        return this.m_itemIds;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_itemIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_itemIds = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.REWARD_DISPLAYER_IE_PARAM.getId();
    }
}
