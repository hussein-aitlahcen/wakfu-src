package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;

public class TravelLoadingBinaryData implements BinaryData
{
    protected String m_loadingAnimationName;
    protected int m_loadingMinDuration;
    protected int m_loadingFadeInDuration;
    protected int m_loadingFadeOutDuration;
    
    public String getLoadingAnimationName() {
        return this.m_loadingAnimationName;
    }
    
    public int getLoadingMinDuration() {
        return this.m_loadingMinDuration;
    }
    
    public int getLoadingFadeInDuration() {
        return this.m_loadingFadeInDuration;
    }
    
    public int getLoadingFadeOutDuration() {
        return this.m_loadingFadeOutDuration;
    }
    
    @Override
    public void reset() {
        this.m_loadingAnimationName = null;
        this.m_loadingMinDuration = 0;
        this.m_loadingFadeInDuration = 0;
        this.m_loadingFadeOutDuration = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_loadingAnimationName = buffer.readUTF8().intern();
        this.m_loadingMinDuration = buffer.getInt();
        this.m_loadingFadeInDuration = buffer.getInt();
        this.m_loadingFadeOutDuration = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        throw new UnsupportedOperationException();
    }
}
