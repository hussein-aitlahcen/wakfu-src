package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class StoolInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected String m_criterion;
    protected int m_visualId;
    protected ChaosParamBinaryData m_chaosParams;
    
    public int getId() {
        return this.m_id;
    }
    
    public String getCriterion() {
        return this.m_criterion;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public ChaosParamBinaryData getChaosParams() {
        return this.m_chaosParams;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_criterion = null;
        this.m_visualId = 0;
        this.m_chaosParams = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_criterion = buffer.readUTF8().intern();
        this.m_visualId = buffer.getInt();
        if (buffer.get() != 0) {
            (this.m_chaosParams = new ChaosParamBinaryData()).read(buffer);
        }
        else {
            this.m_chaosParams = null;
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.STOOL_IE_PARAM.getId();
    }
}
