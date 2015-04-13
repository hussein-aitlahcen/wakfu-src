package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class CensoredEntryBinaryData implements BinaryData
{
    protected int m_id;
    protected boolean m_deepSearch;
    protected int m_language;
    protected int m_censorType;
    protected String m_text;
    
    public int getId() {
        return this.m_id;
    }
    
    public boolean isDeepSearch() {
        return this.m_deepSearch;
    }
    
    public int getLanguage() {
        return this.m_language;
    }
    
    public int getCensorType() {
        return this.m_censorType;
    }
    
    public String getText() {
        return this.m_text;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_deepSearch = false;
        this.m_language = 0;
        this.m_censorType = 0;
        this.m_text = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_deepSearch = buffer.readBoolean();
        this.m_language = buffer.getInt();
        this.m_censorType = buffer.getInt();
        this.m_text = buffer.readUTF8().intern();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CENSORED_ENTRY.getId();
    }
}
