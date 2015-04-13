package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class BackgroundInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_visualId;
    protected int m_backgroundFeedback;
    protected ChaosParamBinaryData m_chaosParams;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public int getBackgroundFeedback() {
        return this.m_backgroundFeedback;
    }
    
    public ChaosParamBinaryData getChaosParams() {
        return this.m_chaosParams;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_visualId = 0;
        this.m_backgroundFeedback = 0;
        this.m_chaosParams = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_backgroundFeedback = buffer.getInt();
        if (buffer.get() != 0) {
            (this.m_chaosParams = new ChaosParamBinaryData()).read(buffer);
        }
        else {
            this.m_chaosParams = null;
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.BACKGROUND_IE_PARAM.getId();
    }
}
