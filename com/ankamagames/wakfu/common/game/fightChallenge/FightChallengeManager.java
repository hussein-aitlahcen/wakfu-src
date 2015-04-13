package com.ankamagames.wakfu.common.game.fightChallenge;

import com.ankamagames.baseImpl.common.clientAndServer.game.loot.*;
import gnu.trove.*;

public class FightChallengeManager
{
    public static final FightChallengeManager INSTANCE;
    private final DropTable<FightChallenge> m_dropTable;
    private final TIntObjectHashMap<FightChallenge> m_challenges;
    private final TIntObjectHashMap<FightChallengeMonsterDefinition> m_monsterDefinitions;
    
    public FightChallengeManager() {
        super();
        this.m_dropTable = new DropTable<FightChallenge>();
        this.m_challenges = new TIntObjectHashMap<FightChallenge>();
        this.m_monsterDefinitions = new TIntObjectHashMap<FightChallengeMonsterDefinition>();
    }
    
    public void registerChallenge(final FightChallenge challenge) {
        this.m_challenges.put(challenge.getId(), challenge);
        if (challenge.isBaseChallenge()) {
            this.m_dropTable.addDrop(challenge);
        }
    }
    
    public void addMonsterDefinition(final FightChallengeMonsterDefinition definition) {
        this.m_monsterDefinitions.put(definition.getMonsterId(), definition);
    }
    
    public FightChallengeMonsterDefinition getMonsterDefinition(final int monsterId) {
        return this.m_monsterDefinitions.get(monsterId);
    }
    
    public FightChallenge dropChallenge(final Object dropUser, final Object dropTarget, final Object dropContent, final Object dropContext) {
        return this.m_dropTable.drop(dropUser, dropTarget, dropContent, dropContext);
    }
    
    public FightChallenge getChallenge(final int challengeId) {
        return this.m_challenges.get(challengeId);
    }
    
    public boolean forEachChallenge(final TObjectProcedure<FightChallenge> procedure) {
        return this.m_challenges.forEachValue(procedure);
    }
    
    @Override
    public String toString() {
        return "FightChallengeManager{m_dropTable=" + this.m_dropTable + ", m_challenges=" + this.m_challenges + ", m_monsterDefinitions=" + this.m_monsterDefinitions + '}';
    }
    
    static {
        INSTANCE = new FightChallengeManager();
    }
}
