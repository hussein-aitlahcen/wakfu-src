package com.ankamagames.wakfu.client.binaryStorage;

import gnu.trove.*;
import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ResourceBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_type;
    protected short m_idealRainMin;
    protected short m_idealRainMax;
    protected short m_idealTemperatureMin;
    protected short m_idealTemperatureMax;
    protected boolean m_isBlocking;
    protected boolean m_useBigChallengeAps;
    protected boolean m_isMonsterEmbryo;
    protected short m_monsterStepHatching;
    protected int[] m_properties;
    protected int[] m_monsterFamilies;
    protected ResourceStep[] m_steps;
    protected short m_height;
    protected int m_iconGfxId;
    protected TIntObjectHashMap<int[]> m_gfxIds;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getType() {
        return this.m_type;
    }
    
    public short getIdealRainMin() {
        return this.m_idealRainMin;
    }
    
    public short getIdealRainMax() {
        return this.m_idealRainMax;
    }
    
    public short getIdealTemperatureMin() {
        return this.m_idealTemperatureMin;
    }
    
    public short getIdealTemperatureMax() {
        return this.m_idealTemperatureMax;
    }
    
    public boolean isBlocking() {
        return this.m_isBlocking;
    }
    
    public boolean isUseBigChallengeAps() {
        return this.m_useBigChallengeAps;
    }
    
    public boolean isMonsterEmbryo() {
        return this.m_isMonsterEmbryo;
    }
    
    public short getMonsterStepHatching() {
        return this.m_monsterStepHatching;
    }
    
    public int[] getProperties() {
        return this.m_properties;
    }
    
    public int[] getMonsterFamilies() {
        return this.m_monsterFamilies;
    }
    
    public ResourceStep[] getSteps() {
        return this.m_steps;
    }
    
    public short getHeight() {
        return this.m_height;
    }
    
    public int getIconGfxId() {
        return this.m_iconGfxId;
    }
    
    public TIntObjectHashMap<int[]> getGfxIds() {
        return this.m_gfxIds;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_type = 0;
        this.m_idealRainMin = 0;
        this.m_idealRainMax = 0;
        this.m_idealTemperatureMin = 0;
        this.m_idealTemperatureMax = 0;
        this.m_isBlocking = false;
        this.m_useBigChallengeAps = false;
        this.m_isMonsterEmbryo = false;
        this.m_monsterStepHatching = 0;
        this.m_properties = null;
        this.m_monsterFamilies = null;
        this.m_steps = null;
        this.m_height = 0;
        this.m_iconGfxId = 0;
        this.m_gfxIds = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_type = buffer.getInt();
        this.m_idealRainMin = buffer.getShort();
        this.m_idealRainMax = buffer.getShort();
        this.m_idealTemperatureMin = buffer.getShort();
        this.m_idealTemperatureMax = buffer.getShort();
        this.m_isBlocking = buffer.readBoolean();
        this.m_useBigChallengeAps = buffer.readBoolean();
        this.m_isMonsterEmbryo = buffer.readBoolean();
        this.m_monsterStepHatching = buffer.getShort();
        this.m_properties = buffer.readIntArray();
        this.m_monsterFamilies = buffer.readIntArray();
        final int stepCount = buffer.getInt();
        this.m_steps = new ResourceStep[stepCount];
        for (int iStep = 0; iStep < stepCount; ++iStep) {
            (this.m_steps[iStep] = new ResourceStep()).read(buffer);
        }
        this.m_height = buffer.getShort();
        this.m_iconGfxId = buffer.getInt();
        final int gfxIdCount = buffer.getInt();
        this.m_gfxIds = new TIntObjectHashMap<int[]>(gfxIdCount);
        for (int iGfxId = 0; iGfxId < gfxIdCount; ++iGfxId) {
            final int gfxIdKey = buffer.getInt();
            final int[] gfxIdValue = buffer.readIntArray();
            this.m_gfxIds.put(gfxIdKey, gfxIdValue);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.RESOURCE.getId();
    }
    
    public static class ResourceStepAction
    {
        protected int m_id;
        protected int m_skillId;
        protected int m_resourceNextIndex;
        protected int m_skillLevelRequired;
        protected int m_skillSimultaneousPlayer;
        protected int m_skillVisualFeedbackId;
        protected int m_duration;
        protected int m_consumableId;
        protected int m_gfxId;
        protected String m_criteria;
        protected int m_collectLootListId;
        protected int m_collectItemId;
        protected int[] m_lootItems;
        protected int m_mruOrder;
        protected boolean m_displayInCraftDialog;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getSkillId() {
            return this.m_skillId;
        }
        
        public int getResourceNextIndex() {
            return this.m_resourceNextIndex;
        }
        
        public int getSkillLevelRequired() {
            return this.m_skillLevelRequired;
        }
        
        public int getSkillSimultaneousPlayer() {
            return this.m_skillSimultaneousPlayer;
        }
        
        public int getSkillVisualFeedbackId() {
            return this.m_skillVisualFeedbackId;
        }
        
        public int getDuration() {
            return this.m_duration;
        }
        
        public int getConsumableId() {
            return this.m_consumableId;
        }
        
        public int getGfxId() {
            return this.m_gfxId;
        }
        
        public String getCriteria() {
            return this.m_criteria;
        }
        
        public int getCollectLootListId() {
            return this.m_collectLootListId;
        }
        
        public int getCollectItemId() {
            return this.m_collectItemId;
        }
        
        public int[] getLootItems() {
            return this.m_lootItems;
        }
        
        public int getMruOrder() {
            return this.m_mruOrder;
        }
        
        public boolean isDisplayInCraftDialog() {
            return this.m_displayInCraftDialog;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_skillId = buffer.getInt();
            this.m_resourceNextIndex = buffer.getInt();
            this.m_skillLevelRequired = buffer.getInt();
            this.m_skillSimultaneousPlayer = buffer.getInt();
            this.m_skillVisualFeedbackId = buffer.getInt();
            this.m_duration = buffer.getInt();
            this.m_consumableId = buffer.getInt();
            this.m_gfxId = buffer.getInt();
            this.m_criteria = buffer.readUTF8().intern();
            this.m_collectLootListId = buffer.getInt();
            this.m_collectItemId = buffer.getInt();
            this.m_lootItems = buffer.readIntArray();
            this.m_mruOrder = buffer.getInt();
            this.m_displayInCraftDialog = buffer.readBoolean();
        }
    }
    
    public static class ResourceStep
    {
        protected int m_index;
        protected ResourceStepAction[] m_actions;
        protected int m_sizeCategoryId;
        protected int m_lightRadius;
        protected int m_lightColor;
        protected int m_apsId;
        protected int m_apsPosZ;
        
        public int getIndex() {
            return this.m_index;
        }
        
        public ResourceStepAction[] getActions() {
            return this.m_actions;
        }
        
        public int getSizeCategoryId() {
            return this.m_sizeCategoryId;
        }
        
        public int getLightRadius() {
            return this.m_lightRadius;
        }
        
        public int getLightColor() {
            return this.m_lightColor;
        }
        
        public int getApsId() {
            return this.m_apsId;
        }
        
        public int getApsPosZ() {
            return this.m_apsPosZ;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_index = buffer.getInt();
            final int actionCount = buffer.getInt();
            this.m_actions = new ResourceStepAction[actionCount];
            for (int iAction = 0; iAction < actionCount; ++iAction) {
                (this.m_actions[iAction] = new ResourceStepAction()).read(buffer);
            }
            this.m_sizeCategoryId = buffer.getInt();
            this.m_lightRadius = buffer.getInt();
            this.m_lightColor = buffer.getInt();
            this.m_apsId = buffer.getInt();
            this.m_apsPosZ = buffer.getInt();
        }
    }
}
