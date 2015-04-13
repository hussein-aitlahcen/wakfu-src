package com.ankamagames.wakfu.common.game.fightChallenge;

import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;

public class FightChallengeMonsterDefinition
{
    private final DropTable<FightChallenge> m_dropTable;
    private final int m_monsterId;
    private final short m_randomRolls;
    private final short m_forcedRolls;
    
    public FightChallengeMonsterDefinition(final int monsterId, final short randomRolls, final short forcedRolls) {
        super();
        this.m_dropTable = new DropTable<FightChallenge>();
        this.m_monsterId = monsterId;
        this.m_randomRolls = randomRolls;
        this.m_forcedRolls = forcedRolls;
    }
    
    public void registerChallenge(final FightChallenge challenge) {
        this.m_dropTable.addDrop(challenge);
    }
    
    public int getMonsterId() {
        return this.m_monsterId;
    }
    
    public short getRandomRolls() {
        return this.m_randomRolls;
    }
    
    public short getForcedRolls() {
        return this.m_forcedRolls;
    }
    
    public FightChallenge dropChallenge(final Object dropUser, final Object dropTarget, final Object dropContent, final Object dropContext) {
        return this.m_dropTable.drop(dropUser, dropTarget, dropContent, dropContext);
    }
    
    @Override
    public String toString() {
        return "FightChallengeMonsterDefinition{m_dropTable=" + this.m_dropTable + ", m_monsterId=" + this.m_monsterId + ", m_randomRolls=" + this.m_randomRolls + ", m_forcedRolls=" + this.m_forcedRolls + '}';
    }
}
