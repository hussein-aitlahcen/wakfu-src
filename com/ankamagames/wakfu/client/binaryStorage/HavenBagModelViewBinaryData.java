package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class HavenBagModelViewBinaryData implements BinaryData
{
    protected int m_id;
    protected boolean m_restrictionWorld;
    protected boolean m_restrictionMarket;
    protected int m_backgroundMapId;
    protected boolean m_innate;
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean isRestrictionWorld() {
        return this.m_restrictionWorld;
    }
    
    public boolean isRestrictionMarket() {
        return this.m_restrictionMarket;
    }
    
    public int getBackgroundMapId() {
        return this.m_backgroundMapId;
    }
    
    public boolean isInnate() {
        return this.m_innate;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_restrictionWorld = false;
        this.m_restrictionMarket = false;
        this.m_backgroundMapId = 0;
        this.m_innate = false;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_restrictionWorld = buffer.readBoolean();
        this.m_restrictionMarket = buffer.readBoolean();
        this.m_backgroundMapId = buffer.getInt();
        this.m_innate = buffer.readBoolean();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.HAVEN_BAG_MODEL_VIEW.getId();
    }
}
