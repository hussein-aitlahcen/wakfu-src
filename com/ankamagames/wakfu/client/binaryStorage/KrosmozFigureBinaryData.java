package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class KrosmozFigureBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_year;
    protected int m_addon;
    protected int m_season;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getYear() {
        return this.m_year;
    }
    
    public int getAddon() {
        return this.m_addon;
    }
    
    public int getSeason() {
        return this.m_season;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_year = 0;
        this.m_addon = 0;
        this.m_season = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_year = buffer.getInt();
        this.m_addon = buffer.getInt();
        this.m_season = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.KROSMOZ_FIGURE.getId();
    }
}
