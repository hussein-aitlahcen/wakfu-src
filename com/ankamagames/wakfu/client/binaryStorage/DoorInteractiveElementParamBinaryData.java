package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class DoorInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_visualId;
    protected boolean m_consumeItem;
    protected int m_itemNeeded;
    protected int m_kamaCost;
    protected String m_criterion;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public boolean isConsumeItem() {
        return this.m_consumeItem;
    }
    
    public int getItemNeeded() {
        return this.m_itemNeeded;
    }
    
    public int getKamaCost() {
        return this.m_kamaCost;
    }
    
    public String getCriterion() {
        return this.m_criterion;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_visualId = 0;
        this.m_consumeItem = false;
        this.m_itemNeeded = 0;
        this.m_kamaCost = 0;
        this.m_criterion = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_consumeItem = buffer.readBoolean();
        this.m_itemNeeded = buffer.getInt();
        this.m_kamaCost = buffer.getInt();
        this.m_criterion = buffer.readUTF8().intern();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.DOOR_IE_PARAM.getId();
    }
}
