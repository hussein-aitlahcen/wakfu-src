package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class CraftBinaryData implements BinaryData
{
    protected int m_craftId;
    protected int m_bookItemId;
    protected float m_xpFactor;
    protected boolean m_innate;
    protected boolean m_conceptualCraft;
    protected boolean m_hiddenCraft;
    
    public int getCraftId() {
        return this.m_craftId;
    }
    
    public int getBookItemId() {
        return this.m_bookItemId;
    }
    
    public float getXpFactor() {
        return this.m_xpFactor;
    }
    
    public boolean isInnate() {
        return this.m_innate;
    }
    
    public boolean isConceptualCraft() {
        return this.m_conceptualCraft;
    }
    
    public boolean isHiddenCraft() {
        return this.m_hiddenCraft;
    }
    
    @Override
    public void reset() {
        this.m_craftId = 0;
        this.m_bookItemId = 0;
        this.m_xpFactor = 0.0f;
        this.m_innate = false;
        this.m_conceptualCraft = false;
        this.m_hiddenCraft = false;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_craftId = buffer.getInt();
        this.m_bookItemId = buffer.getInt();
        this.m_xpFactor = buffer.getFloat();
        this.m_innate = buffer.readBoolean();
        this.m_conceptualCraft = buffer.readBoolean();
        this.m_hiddenCraft = buffer.readBoolean();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.CRAFT.getId();
    }
}
