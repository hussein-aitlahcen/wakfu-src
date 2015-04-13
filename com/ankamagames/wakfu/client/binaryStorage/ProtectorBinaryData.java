package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class ProtectorBinaryData implements BinaryData
{
    protected int m_protectorId;
    protected int m_monsterId;
    protected int m_buffListId;
    protected int m_buffListIdToBuy;
    protected int m_scenarioLootListId;
    protected int m_scenarioLootListIdToBuy;
    protected int m_scenarioLootListIdChaos;
    protected int m_scenarioLootListIdEcosystem;
    protected int m_climateListIdToBuy;
    protected int m_nationId;
    protected int m_territory;
    protected int m_fightStake;
    protected int m_positionX;
    protected int m_positionY;
    protected short m_positionZ;
    protected int[] m_craftLearnt;
    protected ProtectorSecret[] m_secrets;
    protected ProtectorFaunaWill[] m_faunaWill;
    protected ProtectorFloraWill[] m_floraWill;
    
    public int getProtectorId() {
        return this.m_protectorId;
    }
    
    public int getMonsterId() {
        return this.m_monsterId;
    }
    
    public int getBuffListId() {
        return this.m_buffListId;
    }
    
    public int getBuffListIdToBuy() {
        return this.m_buffListIdToBuy;
    }
    
    public int getScenarioLootListId() {
        return this.m_scenarioLootListId;
    }
    
    public int getScenarioLootListIdToBuy() {
        return this.m_scenarioLootListIdToBuy;
    }
    
    public int getScenarioLootListIdChaos() {
        return this.m_scenarioLootListIdChaos;
    }
    
    public int getScenarioLootListIdEcosystem() {
        return this.m_scenarioLootListIdEcosystem;
    }
    
    public int getClimateListIdToBuy() {
        return this.m_climateListIdToBuy;
    }
    
    public int getNationId() {
        return this.m_nationId;
    }
    
    public int getTerritory() {
        return this.m_territory;
    }
    
    public int getFightStake() {
        return this.m_fightStake;
    }
    
    public int getPositionX() {
        return this.m_positionX;
    }
    
    public int getPositionY() {
        return this.m_positionY;
    }
    
    public short getPositionZ() {
        return this.m_positionZ;
    }
    
    public int[] getCraftLearnt() {
        return this.m_craftLearnt;
    }
    
    public ProtectorSecret[] getSecrets() {
        return this.m_secrets;
    }
    
    public ProtectorFaunaWill[] getFaunaWill() {
        return this.m_faunaWill;
    }
    
    public ProtectorFloraWill[] getFloraWill() {
        return this.m_floraWill;
    }
    
    @Override
    public void reset() {
        this.m_protectorId = 0;
        this.m_monsterId = 0;
        this.m_buffListId = 0;
        this.m_buffListIdToBuy = 0;
        this.m_scenarioLootListId = 0;
        this.m_scenarioLootListIdToBuy = 0;
        this.m_scenarioLootListIdChaos = 0;
        this.m_scenarioLootListIdEcosystem = 0;
        this.m_climateListIdToBuy = 0;
        this.m_nationId = 0;
        this.m_territory = 0;
        this.m_fightStake = 0;
        this.m_positionX = 0;
        this.m_positionY = 0;
        this.m_positionZ = 0;
        this.m_craftLearnt = null;
        this.m_secrets = null;
        this.m_faunaWill = null;
        this.m_floraWill = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_protectorId = buffer.getInt();
        this.m_monsterId = buffer.getInt();
        this.m_buffListId = buffer.getInt();
        this.m_buffListIdToBuy = buffer.getInt();
        this.m_scenarioLootListId = buffer.getInt();
        this.m_scenarioLootListIdToBuy = buffer.getInt();
        this.m_scenarioLootListIdChaos = buffer.getInt();
        this.m_scenarioLootListIdEcosystem = buffer.getInt();
        this.m_climateListIdToBuy = buffer.getInt();
        this.m_nationId = buffer.getInt();
        this.m_territory = buffer.getInt();
        this.m_fightStake = buffer.getInt();
        this.m_positionX = buffer.getInt();
        this.m_positionY = buffer.getInt();
        this.m_positionZ = buffer.getShort();
        this.m_craftLearnt = buffer.readIntArray();
        final int secretCount = buffer.getInt();
        this.m_secrets = new ProtectorSecret[secretCount];
        for (int iSecret = 0; iSecret < secretCount; ++iSecret) {
            (this.m_secrets[iSecret] = new ProtectorSecret()).read(buffer);
        }
        final int faunaWillCount = buffer.getInt();
        this.m_faunaWill = new ProtectorFaunaWill[faunaWillCount];
        for (int iFaunaWill = 0; iFaunaWill < faunaWillCount; ++iFaunaWill) {
            (this.m_faunaWill[iFaunaWill] = new ProtectorFaunaWill()).read(buffer);
        }
        final int floraWillCount = buffer.getInt();
        this.m_floraWill = new ProtectorFloraWill[floraWillCount];
        for (int iFloraWill = 0; iFloraWill < floraWillCount; ++iFloraWill) {
            (this.m_floraWill[iFloraWill] = new ProtectorFloraWill()).read(buffer);
        }
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.PROTECTOR.getId();
    }
    
    public static class ProtectorFaunaWill
    {
        protected int m_typeId;
        protected short m_min;
        protected short m_max;
        
        public int getTypeId() {
            return this.m_typeId;
        }
        
        public short getMin() {
            return this.m_min;
        }
        
        public short getMax() {
            return this.m_max;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_typeId = buffer.getInt();
            this.m_min = buffer.getShort();
            this.m_max = buffer.getShort();
        }
    }
    
    public static class ProtectorFloraWill
    {
        protected int m_typeId;
        protected short m_min;
        protected short m_max;
        
        public int getTypeId() {
            return this.m_typeId;
        }
        
        public short getMin() {
            return this.m_min;
        }
        
        public short getMax() {
            return this.m_max;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_typeId = buffer.getInt();
            this.m_min = buffer.getShort();
            this.m_max = buffer.getShort();
        }
    }
    
    public static class ProtectorSecret
    {
        protected int m_id;
        protected int m_achievementGoalId;
        protected int m_secretGfxId;
        protected int m_discoveredGfxId;
        
        public int getId() {
            return this.m_id;
        }
        
        public int getAchievementGoalId() {
            return this.m_achievementGoalId;
        }
        
        public int getSecretGfxId() {
            return this.m_secretGfxId;
        }
        
        public int getDiscoveredGfxId() {
            return this.m_discoveredGfxId;
        }
        
        public void read(final RandomByteBufferReader buffer) throws Exception {
            this.m_id = buffer.getInt();
            this.m_achievementGoalId = buffer.getInt();
            this.m_secretGfxId = buffer.getInt();
            this.m_discoveredGfxId = buffer.getInt();
        }
    }
}
