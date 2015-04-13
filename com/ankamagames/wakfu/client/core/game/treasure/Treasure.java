package com.ankamagames.wakfu.client.core.game.treasure;

public class Treasure
{
    private int m_id;
    private int m_usedItem;
    private short m_quantity;
    private boolean m_consumeItem;
    private int m_rewardItem;
    private int m_rewardKama;
    private int m_rewardLootChest;
    
    public Treasure(final int id) {
        super();
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public int getUsedItem() {
        return this.m_usedItem;
    }
    
    public short getQuantity() {
        return this.m_quantity;
    }
    
    public boolean isConsumeItem() {
        return this.m_consumeItem;
    }
    
    public int getRewardItem() {
        return this.m_rewardItem;
    }
    
    public int getRewardKama() {
        return this.m_rewardKama;
    }
    
    public int getRewardLootChest() {
        return this.m_rewardLootChest;
    }
    
    public void setUsedItem(final int usedItem) {
        this.m_usedItem = usedItem;
    }
    
    public void setQuantity(final short quantity) {
        this.m_quantity = quantity;
    }
    
    public void setConsumeItem(final boolean consumeItem) {
        this.m_consumeItem = consumeItem;
    }
    
    public void setRewardItem(final int rewardItem) {
        this.m_rewardItem = rewardItem;
    }
    
    public void setRewardKama(final int rewardKama) {
        this.m_rewardKama = rewardKama;
    }
    
    public void setRewardLootChest(final int rewardLootChest) {
        this.m_rewardLootChest = rewardLootChest;
    }
}
