package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class TreasureBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_usedItem;
    protected int m_rewardItem;
    protected short m_quantity;
    protected int m_rewardKama;
    protected int m_rewardLootChest;
    protected int m_duration;
    protected String m_criterion;
    protected float m_winPercent;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getUsedItem() {
        return this.m_usedItem;
    }
    
    public int getRewardItem() {
        return this.m_rewardItem;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public int getRewardKama() {
        return this.m_rewardKama;
    }
    
    public int getRewardLootChest() {
        return this.m_rewardLootChest;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public String getCriterion() {
        return this.m_criterion;
    }
    
    public float getWinPercent() {
        return this.m_winPercent;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_usedItem = 0;
        this.m_rewardItem = 0;
        this.m_quantity = 0;
        this.m_rewardKama = 0;
        this.m_rewardLootChest = 0;
        this.m_duration = 0;
        this.m_criterion = null;
        this.m_winPercent = 0.0f;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_usedItem = buffer.getInt();
        this.m_rewardItem = buffer.getInt();
        this.m_quantity = buffer.getShort();
        this.m_rewardKama = buffer.getInt();
        this.m_rewardLootChest = buffer.getInt();
        this.m_duration = buffer.getInt();
        this.m_criterion = buffer.readUTF8().intern();
        this.m_winPercent = buffer.getFloat();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.TREASURE.getId();
    }
}
