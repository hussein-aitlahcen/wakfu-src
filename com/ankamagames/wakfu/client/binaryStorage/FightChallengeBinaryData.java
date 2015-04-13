package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class FightChallengeBinaryData implements BinaryData
{
    protected int m_id;
    protected int m_dropWeight;
    protected String m_dropCriterion;
    protected int m_stateId;
    protected int m_listenedEffectSuccess;
    protected int m_listenedEffectFailure;
    protected int m_gfxId;
    protected boolean m_isBase;
    protected int[] m_incompatibleChallenges;
    protected int[] m_incompatibleMonsters;
    protected Reward[] m_rewards;
    
    public int getId() {
        return this.m_id;
    }
    
    public int getDropWeight() {
        return this.m_dropWeight;
    }
    
    public String getDropCriterion() {
        return this.m_dropCriterion;
    }
    
    public int getStateId() {
        return this.m_stateId;
    }
    
    public int getListenedEffectSuccess() {
        return this.m_listenedEffectSuccess;
    }
    
    public int getListenedEffectFailure() {
        return this.m_listenedEffectFailure;
    }
    
    public int getGfxId() {
        return this.m_gfxId;
    }
    
    public boolean isBase() {
        return this.m_isBase;
    }
    
    public int[] getIncompatibleChallenges() {
        return this.m_incompatibleChallenges;
    }
    
    public int[] getIncompatibleMonsters() {
        return this.m_incompatibleMonsters;
    }
    
    public Reward[] getRewards() {
        return this.m_rewards;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_dropWeight = 0;
        this.m_dropCriterion = null;
        this.m_stateId = 0;
        this.m_listenedEffectSuccess = 0;
        this.m_listenedEffectFailure = 0;
        this.m_gfxId = 0;
        this.m_isBase = false;
        this.m_incompatibleChallenges = null;
        this.m_incompatibleMonsters = null;
        this.m_rewards = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_dropWeight = buffer.getInt();
        this.m_dropCriterion = buffer.readUTF8().intern();
        this.m_stateId = buffer.getInt();
        this.m_listenedEffectSuccess = buffer.getInt();
        this.m_listenedEffectFailure = buffer.getInt();
        this.m_gfxId = buffer.getInt();
        this.m_isBase = buffer.readBoolean();
        this.m_incompatibleChallenges = buffer.readIntArray();
        this.m_incompatibleMonsters = buffer.readIntArray();
        final int rewardCount = buffer.getInt();
        this.m_rewards = new Reward[rewardCount];
        for (int iReward = 0; iReward < rewardCount; ++iReward) {
            (this.m_rewards[iReward] = new Reward()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.FIGHT_CHALLENGE.getId();
    }
    
    public static class Reward
    {
        protected int m_id;
        protected String m_criterion;
        protected short m_xpLevel;
        protected short m_dropLevel;
        
        public int getId() {
            return this.m_id;
        }
        
        public String getCriterion() {
            return this.m_criterion;
        }
        
        public short getXpLevel() {
            return this.m_xpLevel;
        }
        
        public short getDropLevel() {
            return this.m_dropLevel;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_criterion = buffer.readUTF8().intern();
            this.m_xpLevel = buffer.getShort();
            this.m_dropLevel = buffer.getShort();
        }
    }
}
