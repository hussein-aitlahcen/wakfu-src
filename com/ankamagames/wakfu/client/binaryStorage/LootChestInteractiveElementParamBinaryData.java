package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class LootChestInteractiveElementParamBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_visualId;
    protected int m_cooldown;
    protected int m_cost;
    protected int m_itemIdCost;
    protected int m_itemQuantityCost;
    protected boolean m_doConsumeItem;
    protected int m_nbActivation;
    protected int m_distributionDuration;
    protected String m_criteria;
    protected ChaosParamBinaryData m_chaosParams;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getVisualId() {
        return this.m_visualId;
    }
    
    public int getCooldown() {
        return this.m_cooldown;
    }
    
    public int getCost() {
        return this.m_cost;
    }
    
    public int getItemIdCost() {
        return this.m_itemIdCost;
    }
    
    public int getItemQuantityCost() {
        return this.m_itemQuantityCost;
    }
    
    public boolean isDoConsumeItem() {
        return this.m_doConsumeItem;
    }
    
    public int getNbActivation() {
        return this.m_nbActivation;
    }
    
    public int getDistributionDuration() {
        return this.m_distributionDuration;
    }
    
    public String getCriteria() {
        return this.m_criteria;
    }
    
    public ChaosParamBinaryData getChaosParams() {
        return this.m_chaosParams;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_visualId = 0;
        this.m_cooldown = 0;
        this.m_cost = 0;
        this.m_itemIdCost = 0;
        this.m_itemQuantityCost = 0;
        this.m_doConsumeItem = false;
        this.m_nbActivation = 0;
        this.m_distributionDuration = 0;
        this.m_criteria = null;
        this.m_chaosParams = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_visualId = buffer.getInt();
        this.m_cooldown = buffer.getInt();
        this.m_cost = buffer.getInt();
        this.m_itemIdCost = buffer.getInt();
        this.m_itemQuantityCost = buffer.getInt();
        this.m_doConsumeItem = buffer.readBoolean();
        this.m_nbActivation = buffer.getInt();
        this.m_distributionDuration = buffer.getInt();
        this.m_criteria = buffer.readUTF8().intern();
        if (buffer.get() != 0) {
            (this.m_chaosParams = new ChaosParamBinaryData()).read(buffer);
        }
        else {
            this.m_chaosParams = null;
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.LOOT_CHEST_IE_PARAM.getId();
    }
}
