package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class AchievementBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_categoryId;
    protected boolean m_isVisible;
    protected boolean m_notifyOnPass;
    protected boolean m_isActive;
    protected String m_criterion;
    protected String m_activationCriterion;
    protected AchievementGoal[] m_goals;
    protected AchievementReward[] m_rewards;
    protected int m_duration;
    protected int m_cooldown;
    protected boolean m_shareable;
    protected boolean m_repeatable;
    protected boolean m_needsUserAccept;
    protected int m_recommandedLevel;
    protected int m_recommandedPlayers;
    protected boolean m_followable;
    protected int m_displayOnActivationDelay;
    protected long m_periodStartTime;
    protected long m_period;
    protected boolean m_autoCompass;
    protected int m_gfxId;
    protected boolean m_isMercenary;
    protected int m_mercenaryItemId;
    protected byte m_mercenaryRank;
    protected int m_order;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getCategoryId() {
        return this.m_categoryId;
    }
    
    public boolean isVisible() {
        return this.m_isVisible;
    }
    
    public boolean isNotifyOnPass() {
        return this.m_notifyOnPass;
    }
    
    public boolean isActive() {
        return this.m_isActive;
    }
    
    public String getCriterion() {
        return this.m_criterion;
    }
    
    public String getActivationCriterion() {
        return this.m_activationCriterion;
    }
    
    public AchievementGoal[] getGoals() {
        return this.m_goals;
    }
    
    public AchievementReward[] getRewards() {
        return this.m_rewards;
    }
    
    public int getDuration() {
        return this.m_duration;
    }
    
    public int getCooldown() {
        return this.m_cooldown;
    }
    
    public boolean isShareable() {
        return this.m_shareable;
    }
    
    public boolean isRepeatable() {
        return this.m_repeatable;
    }
    
    public boolean isNeedsUserAccept() {
        return this.m_needsUserAccept;
    }
    
    public int getRecommandedLevel() {
        return this.m_recommandedLevel;
    }
    
    public int getRecommandedPlayers() {
        return this.m_recommandedPlayers;
    }
    
    public boolean isFollowable() {
        return this.m_followable;
    }
    
    public int getDisplayOnActivationDelay() {
        return this.m_displayOnActivationDelay;
    }
    
    public long getPeriodStartTime() {
        return this.m_periodStartTime;
    }
    
    public long getPeriod() {
        return this.m_period;
    }
    
    public boolean isAutoCompass() {
        return this.m_autoCompass;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public boolean isMercenary() {
        return this.m_isMercenary;
    }
    
    public int getMercenaryItemId() {
        return this.m_mercenaryItemId;
    }
    
    public byte getMercenaryRank() {
        return this.m_mercenaryRank;
    }
    
    public int getOrder() {
        return this.m_order;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_categoryId = 0;
        this.m_isVisible = false;
        this.m_notifyOnPass = false;
        this.m_isActive = false;
        this.m_criterion = null;
        this.m_activationCriterion = null;
        this.m_goals = null;
        this.m_rewards = null;
        this.m_duration = 0;
        this.m_cooldown = 0;
        this.m_shareable = false;
        this.m_repeatable = false;
        this.m_needsUserAccept = false;
        this.m_recommandedLevel = 0;
        this.m_recommandedPlayers = 0;
        this.m_followable = false;
        this.m_displayOnActivationDelay = 0;
        this.m_periodStartTime = 0L;
        this.m_period = 0L;
        this.m_autoCompass = false;
        this.m_gfxId = 0;
        this.m_isMercenary = false;
        this.m_mercenaryItemId = 0;
        this.m_mercenaryRank = 0;
        this.m_order = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_categoryId = buffer.getInt();
        this.m_isVisible = buffer.readBoolean();
        this.m_notifyOnPass = buffer.readBoolean();
        this.m_isActive = buffer.readBoolean();
        this.m_criterion = buffer.readUTF8().intern();
        this.m_activationCriterion = buffer.readUTF8().intern();
        final int goalCount = buffer.getInt();
        this.m_goals = new AchievementGoal[goalCount];
        for (int iGoal = 0; iGoal < goalCount; ++iGoal) {
            (this.m_goals[iGoal] = new AchievementGoal()).read(buffer);
        }
        final int rewardCount = buffer.getInt();
        this.m_rewards = new AchievementReward[rewardCount];
        for (int iReward = 0; iReward < rewardCount; ++iReward) {
            (this.m_rewards[iReward] = new AchievementReward()).read(buffer);
        }
        this.m_duration = buffer.getInt();
        this.m_cooldown = buffer.getInt();
        this.m_shareable = buffer.readBoolean();
        this.m_repeatable = buffer.readBoolean();
        this.m_needsUserAccept = buffer.readBoolean();
        this.m_recommandedLevel = buffer.getInt();
        this.m_recommandedPlayers = buffer.getInt();
        this.m_followable = buffer.readBoolean();
        this.m_displayOnActivationDelay = buffer.getInt();
        this.m_periodStartTime = buffer.getLong();
        this.m_period = buffer.getLong();
        this.m_autoCompass = buffer.readBoolean();
        this.m_gfxId = buffer.getInt();
        this.m_isMercenary = buffer.readBoolean();
        this.m_mercenaryItemId = buffer.getInt();
        this.m_mercenaryRank = buffer.get();
        this.m_order = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ACHIEVEMENT.getId();
    }
    
    public static class AchievementVariableListener
    {
        protected int m_id;
        protected String m_successCriterion;
        protected int[] m_variableIds;
        
        public int getId() {
            return this.m_id;
        }
        
        public String getSuccessCriterion() {
            return this.m_successCriterion;
        }
        
        public int[] getVariableIds() {
            return this.m_variableIds;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_successCriterion = buffer.readUTF8().intern();
            this.m_variableIds = buffer.readIntArray();
        }
    }
    
    public static class AchievementGoal
    {
        protected int m_id;
        protected boolean m_feedback;
        protected boolean m_hasPositionFeedback;
        protected short m_positionX;
        protected short m_positionY;
        protected short m_positionZ;
        protected short m_positionWorldId;
        protected AchievementVariableListener[] m_vlisteners;
        
        public int getId() {
            return this.m_id;
        }
        
        public boolean isFeedback() {
            return this.m_feedback;
        }
        
        public boolean hasPositionFeedback() {
            return this.m_hasPositionFeedback;
        }
        
        public short getPositionX() {
            return this.m_positionX;
        }
        
        public short getPositionY() {
            return this.m_positionY;
        }
        
        public short getPositionZ() {
            return this.m_positionZ;
        }
        
        public short getPositionWorldId() {
            return this.m_positionWorldId;
        }
        
        public AchievementVariableListener[] getVlisteners() {
            return this.m_vlisteners;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_feedback = buffer.readBoolean();
            this.m_hasPositionFeedback = buffer.readBoolean();
            this.m_positionX = buffer.getShort();
            this.m_positionY = buffer.getShort();
            this.m_positionZ = buffer.getShort();
            this.m_positionWorldId = buffer.getShort();
            final int vlistenerCount = buffer.getInt();
            this.m_vlisteners = new AchievementVariableListener[vlistenerCount];
            for (int iVlistener = 0; iVlistener < vlistenerCount; ++iVlistener) {
                (this.m_vlisteners[iVlistener] = new AchievementVariableListener()).read(buffer);
            }
        }
    }
    
    public static class AchievementReward
    {
        protected int m_id;
        protected int m_type;
        protected int[] m_params;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getType() {
            return this.m_type;
        }
        
        public int[] getParams() {
            return this.m_params;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_type = buffer.getInt();
            this.m_params = buffer.readIntArray();
        }
    }
}
