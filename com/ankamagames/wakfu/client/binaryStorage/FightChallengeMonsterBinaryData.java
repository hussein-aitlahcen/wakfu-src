package com.ankamagames.wakfu.client.binaryStorage;

import com.ankamagames.framework.fileFormat.io.binaryStorage2.*;
import com.ankamagames.wakfu.common.binaryStorage.*;

public class FightChallengeMonsterBinaryData implements BinaryData
{
    protected int m_id;
    protected short m_randomRolls;
    protected short m_forcedRolls;
    protected int[] m_forcedChallenges;
    
    public int getId() {
        return this.m_id;
    }
    
    public short getRandomRolls() {
        return this.m_randomRolls;
    }
    
    public short getForcedRolls() {
        return this.m_forcedRolls;
    }
    
    public int[] getForcedChallenges() {
        return this.m_forcedChallenges;
    }
    
    @Override
    public void reset() {
        this.m_id = 0;
        this.m_randomRolls = 0;
        this.m_forcedRolls = 0;
        this.m_forcedChallenges = null;
    }
    
    @Override
    public void read(final RandomByteBufferReader buffer) throws Exception {
        this.m_id = buffer.getInt();
        this.m_randomRolls = buffer.getShort();
        this.m_forcedRolls = buffer.getShort();
        this.m_forcedChallenges = buffer.readIntArray();
    }
    
    @Override
    public final int getDataTypeId() {
        return WakfuBinaryStorableType.FIGHT_CHALLENGE_MONSTER.getId();
    }
}
