package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class GemBackgroundInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_backgroundFeedback;
    protected int[] m_havreGemTypes;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getBackgroundFeedback() {
        return this.m_backgroundFeedback;
    }
    
    public int[] getHavreGemTypes() {
        return this.m_havreGemTypes;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_backgroundFeedback = 0;
        this.m_havreGemTypes = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_backgroundFeedback = buffer.getInt();
        this.m_havreGemTypes = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.GEM_BACKGROUND_IE_PARAM.getId();
    }
}
