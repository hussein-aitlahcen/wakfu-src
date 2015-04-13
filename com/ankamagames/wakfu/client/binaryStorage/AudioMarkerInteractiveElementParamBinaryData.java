package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AudioMarkerInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_audioMarkerTypeId;
    protected boolean m_isLocalized;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getAudioMarkerTypeId() {
        return this.m_audioMarkerTypeId;
    }
    
    public boolean isLocalized() {
        return this.m_isLocalized;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_audioMarkerTypeId = 0;
        this.m_isLocalized = false;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_audioMarkerTypeId = buffer.getInt();
        this.m_isLocalized = buffer.readBoolean();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.AUDIO_MARKER_IE_PARAM.getId();
    }
}
