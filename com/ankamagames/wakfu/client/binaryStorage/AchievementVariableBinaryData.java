package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AchievementVariableBinaryData implements BinaryData
{
    protected int m_id;
    protected String m_name;
    protected boolean m_exportForSteam;
    
    public int getId() {
        return this.m_id;
    }
    
    public String getName() {
        return this.m_name;
    }
    
    public boolean isExportForSteam() {
        return this.m_exportForSteam;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_name = null;
        this.m_exportForSteam = false;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_name = buffer.readUTF8().intern();
        this.m_exportForSteam = buffer.readBoolean();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ACHIEVEMENT_VARIABLE.getId();
    }
}
