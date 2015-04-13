package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class EmoteBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_type;
    protected String m_cmd;
    protected boolean m_needTarget;
    protected boolean m_moveToTarget;
    protected boolean m_infiniteDuration;
    protected boolean m_isMusical;
    protected String[] m_scriptParams;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getType() {
        return this.m_type;
    }
    
    public String getCmd() {
        return this.m_cmd;
    }
    
    public boolean isNeedTarget() {
        return this.m_needTarget;
    }
    
    public boolean isMoveToTarget() {
        return this.m_moveToTarget;
    }
    
    public boolean isInfiniteDuration() {
        return this.m_infiniteDuration;
    }
    
    public boolean isMusical() {
        return this.m_isMusical;
    }
    
    public String[] getScriptParams() {
        return this.m_scriptParams;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_type = 0;
        this.m_cmd = null;
        this.m_needTarget = false;
        this.m_moveToTarget = false;
        this.m_infiniteDuration = false;
        this.m_isMusical = false;
        this.m_scriptParams = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_type = buffer.getShort();
        this.m_cmd = buffer.readUTF8().intern();
        this.m_needTarget = buffer.readBoolean();
        this.m_moveToTarget = buffer.readBoolean();
        this.m_infiniteDuration = buffer.readBoolean();
        this.m_isMusical = buffer.readBoolean();
        this.m_scriptParams = buffer.readStringArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.EMOTE.getId();
    }
}
