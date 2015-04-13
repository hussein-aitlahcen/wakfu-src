package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ArcadeDungeonBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_worldId;
    protected Challenge[] m_challenges;
    protected RewardList[] m_rewardsList;
    protected RankDef[] m_ranks;
    protected int m_scoreRoundBase;
    protected int m_scoreRoundIncr;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getWorldId() {
        return this.m_worldId;
    }
    
    public Challenge[] getChallenges() {
        return this.m_challenges;
    }
    
    public RewardList[] getRewardsList() {
        return this.m_rewardsList;
    }
    
    public RankDef[] getRanks() {
        return this.m_ranks;
    }
    
    public int getScoreRoundBase() {
        return this.m_scoreRoundBase;
    }
    
    public int getScoreRoundIncr() {
        return this.m_scoreRoundIncr;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_worldId = 0;
        this.m_challenges = null;
        this.m_rewardsList = null;
        this.m_ranks = null;
        this.m_scoreRoundBase = 0;
        this.m_scoreRoundIncr = 0;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_worldId = buffer.getShort();
        final int challengeCount = buffer.getInt();
        this.m_challenges = new Challenge[challengeCount];
        for (int iChallenge = 0; iChallenge < challengeCount; ++iChallenge) {
            (this.m_challenges[iChallenge] = new Challenge()).read(buffer);
        }
        final int rewardsListCount = buffer.getInt();
        this.m_rewardsList = new RewardList[rewardsListCount];
        for (int iRewardsList = 0; iRewardsList < rewardsListCount; ++iRewardsList) {
            (this.m_rewardsList[iRewardsList] = new RewardList()).read(buffer);
        }
        final int rankCount = buffer.getInt();
        this.m_ranks = new RankDef[rankCount];
        for (int iRank = 0; iRank < rankCount; ++iRank) {
            (this.m_ranks[iRank] = new RankDef()).read(buffer);
        }
        this.m_scoreRoundBase = buffer.getInt();
        this.m_scoreRoundIncr = buffer.getInt();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.ARCADE_DUNGEON.getId();
    }
    
    public static class RankDef
    {
        protected int m_levelOrder;
        protected int m_scoreMinD;
        protected int m_scoreMinC;
        protected int m_scoreMinB;
        protected int m_scoreMinA;
        
        public int getLevelOrder() {
            return this.m_levelOrder;
        }
        
        public int getScoreMinD() {
            return this.m_scoreMinD;
        }
        
        public int getScoreMinC() {
            return this.m_scoreMinC;
        }
        
        public int getScoreMinB() {
            return this.m_scoreMinB;
        }
        
        public int getScoreMinA() {
            return this.m_scoreMinA;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_levelOrder = buffer.getInt();
            this.m_scoreMinD = buffer.getInt();
            this.m_scoreMinC = buffer.getInt();
            this.m_scoreMinB = buffer.getInt();
            this.m_scoreMinA = buffer.getInt();
        }
    }
    
    public static class Reward
    {
        protected int m_scoreMin;
        protected int m_itemId;
        protected int m_xpValue;
        protected byte m_rankNeeded;
        
        public int getScoreMin() {
            return this.m_scoreMin;
        }
        
        public int getItemId() {
            return this.m_itemId;
        }
        
        public int getXpValue() {
            return this.m_xpValue;
        }
        
        public byte getRankNeeded() {
            return this.m_rankNeeded;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_scoreMin = buffer.getInt();
            this.m_itemId = buffer.getInt();
            this.m_xpValue = buffer.getInt();
            this.m_rankNeeded = buffer.get();
        }
    }
    
    public static class RewardList
    {
        protected int m_id;
        protected int m_levelOrder;
        protected Reward[] m_rewards;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getLevelOrder() {
            return this.m_levelOrder;
        }
        
        public Reward[] getRewards() {
            return this.m_rewards;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_levelOrder = buffer.getInt();
            final int rewardCount = buffer.getInt();
            this.m_rewards = new Reward[rewardCount];
            for (int iReward = 0; iReward < rewardCount; ++iReward) {
                (this.m_rewards[iReward] = new Reward()).read(buffer);
            }
        }
    }
    
    public static class Challenge
    {
        protected int m_id;
        protected float m_ratio;
        
        public int getId() {
            return this.m_id;
        }
        
        public float getRatio() {
            return this.m_ratio;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_ratio = buffer.getFloat();
        }
    }
}
