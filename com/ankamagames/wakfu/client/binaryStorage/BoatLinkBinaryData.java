package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class BoatLinkBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_start;
    protected int m_end;
    protected int m_cost;
    protected String m_criteria;
    protected String m_criteriaDisplay;
    protected boolean m_needsToPayEverytime;
    protected TravelLoadingBinaryData m_loading;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getStart() {
        return this.m_start;
    }
    
    public int getEnd() {
        return this.m_end;
    }
    
    public int getCost() {
        return this.m_cost;
    }
    
    public String getCriteria() {
        return this.m_criteria;
    }
    
    public String getCriteriaDisplay() {
        return this.m_criteriaDisplay;
    }
    
    public boolean isNeedsToPayEverytime() {
        return this.m_needsToPayEverytime;
    }
    
    public TravelLoadingBinaryData getLoading() {
        return this.m_loading;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_start = 0;
        this.m_end = 0;
        this.m_cost = 0;
        this.m_criteria = null;
        this.m_criteriaDisplay = null;
        this.m_needsToPayEverytime = false;
        this.m_loading = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_start = buffer.getInt();
        this.m_end = buffer.getInt();
        this.m_cost = buffer.getInt();
        this.m_criteria = buffer.readUTF8().intern();
        this.m_criteriaDisplay = buffer.readUTF8().intern();
        this.m_needsToPayEverytime = buffer.readBoolean();
        (this.m_loading = new TravelLoadingBinaryData()).read(buffer);
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.BOAT_LINK.getId();
    }
}
