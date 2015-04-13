package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class PetBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_itemRefId;
    protected int m_gfxId;
    protected int m_itemColorRefId;
    protected int m_itemReskinRefId;
    protected int m_health;
    protected long m_minMealInterval;
    protected long m_maxMealInterval;
    protected byte m_xpByMeal;
    protected short m_xpPerLevel;
    protected short m_levelMax;
    protected byte m_mountType;
    protected HealthPenalty[] m_healthPenalties;
    protected HealthItem[] m_healthItems;
    protected MealItem[] m_mealItems;
    protected SleepItem[] m_sleepItems;
    protected int[] m_equipmentItems;
    protected ColorItem[] m_colorItems;
    protected ReskinItem[] m_reskinItems;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getItemRefId() {
        return this.m_itemRefId;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public int getItemColorRefId() {
        return this.m_itemColorRefId;
    }
    
    public int getItemReskinRefId() {
        return this.m_itemReskinRefId;
    }
    
    public int getHealth() {
        return this.m_health;
    }
    
    public long getMinMealInterval() {
        return this.m_minMealInterval;
    }
    
    public long getMaxMealInterval() {
        return this.m_maxMealInterval;
    }
    
    public byte getXpByMeal() {
        return this.m_xpByMeal;
    }
    
    public short getXpPerLevel() {
        return this.m_xpPerLevel;
    }
    
    public short getLevelMax() {
        return this.m_levelMax;
    }
    
    public byte getMountType() {
        return this.m_mountType;
    }
    
    public HealthPenalty[] getHealthPenalties() {
        return this.m_healthPenalties;
    }
    
    public HealthItem[] getHealthItems() {
        return this.m_healthItems;
    }
    
    public MealItem[] getMealItems() {
        return this.m_mealItems;
    }
    
    public SleepItem[] getSleepItems() {
        return this.m_sleepItems;
    }
    
    public int[] getEquipmentItems() {
        return this.m_equipmentItems;
    }
    
    public ColorItem[] getColorItems() {
        return this.m_colorItems;
    }
    
    public ReskinItem[] getReskinItems() {
        return this.m_reskinItems;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_itemRefId = 0;
        this.m_gfxId = 0;
        this.m_itemColorRefId = 0;
        this.m_itemReskinRefId = 0;
        this.m_health = 0;
        this.m_minMealInterval = 0L;
        this.m_maxMealInterval = 0L;
        this.m_xpByMeal = 0;
        this.m_xpPerLevel = 0;
        this.m_levelMax = 0;
        this.m_mountType = 0;
        this.m_healthPenalties = null;
        this.m_healthItems = null;
        this.m_mealItems = null;
        this.m_sleepItems = null;
        this.m_equipmentItems = null;
        this.m_colorItems = null;
        this.m_reskinItems = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_itemRefId = buffer.getInt();
        this.m_gfxId = buffer.getInt();
        this.m_itemColorRefId = buffer.getInt();
        this.m_itemReskinRefId = buffer.getInt();
        this.m_health = buffer.getInt();
        this.m_minMealInterval = buffer.getLong();
        this.m_maxMealInterval = buffer.getLong();
        this.m_xpByMeal = buffer.get();
        this.m_xpPerLevel = buffer.getShort();
        this.m_levelMax = buffer.getShort();
        this.m_mountType = buffer.get();
        final int healthPenaltieCount = buffer.getInt();
        this.m_healthPenalties = new HealthPenalty[healthPenaltieCount];
        for (int iHealthPenaltie = 0; iHealthPenaltie < healthPenaltieCount; ++iHealthPenaltie) {
            (this.m_healthPenalties[iHealthPenaltie] = new HealthPenalty()).read(buffer);
        }
        final int healthItemCount = buffer.getInt();
        this.m_healthItems = new HealthItem[healthItemCount];
        for (int iHealthItem = 0; iHealthItem < healthItemCount; ++iHealthItem) {
            (this.m_healthItems[iHealthItem] = new HealthItem()).read(buffer);
        }
        final int mealItemCount = buffer.getInt();
        this.m_mealItems = new MealItem[mealItemCount];
        for (int iMealItem = 0; iMealItem < mealItemCount; ++iMealItem) {
            (this.m_mealItems[iMealItem] = new MealItem()).read(buffer);
        }
        final int sleepItemCount = buffer.getInt();
        this.m_sleepItems = new SleepItem[sleepItemCount];
        for (int iSleepItem = 0; iSleepItem < sleepItemCount; ++iSleepItem) {
            (this.m_sleepItems[iSleepItem] = new SleepItem()).read(buffer);
        }
        this.m_equipmentItems = buffer.readIntArray();
        final int colorItemCount = buffer.getInt();
        this.m_colorItems = new ColorItem[colorItemCount];
        for (int iColorItem = 0; iColorItem < colorItemCount; ++iColorItem) {
            (this.m_colorItems[iColorItem] = new ColorItem()).read(buffer);
        }
        final int reskinItemCount = buffer.getInt();
        this.m_reskinItems = new ReskinItem[reskinItemCount];
        for (int iReskinItem = 0; iReskinItem < reskinItemCount; ++iReskinItem) {
            (this.m_reskinItems[iReskinItem] = new ReskinItem()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.PET.getId();
    }
    
    public static class HealthPenalty
    {
        protected byte m_penaltyType;
        protected byte m_value;
        
        public byte getPenaltyType() {
            return this.m_penaltyType;
        }
        
        public byte getValue() {
            return this.m_value;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_penaltyType = buffer.get();
            this.m_value = buffer.get();
        }
    }
    
    public static class HealthItem
    {
        protected int m_itemId;
        protected int m_value;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public int getValue() {
            return this.m_value;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_value = buffer.getInt();
        }
    }
    
    public static class MealItem
    {
        protected int m_itemId;
        protected boolean m_visible;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public boolean isVisible() {
            return this.m_visible;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_visible = buffer.readBoolean();
        }
    }
    
    public static class SleepItem
    {
        protected int m_itemId;
        protected long m_duration;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public long getDuration() {
            return this.m_duration;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_duration = buffer.getLong();
        }
    }
    
    public static class ColorItem
    {
        protected int m_itemId;
        protected int m_partId;
        protected int m_colorABGR;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public int getPartId() {
            return this.m_partId;
        }
        
        public int getColorABGR() {
            return this.m_colorABGR;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_partId = buffer.getInt();
            this.m_colorABGR = buffer.getInt();
        }
    }
    
    public static class ReskinItem
    {
        protected int m_itemId;
        protected int m_gfxId;
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public int getGfxId() {
            return this.m_gfxId;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_itemId = buffer.getInt();
            this.m_gfxId = buffer.getInt();
        }
    }
}
