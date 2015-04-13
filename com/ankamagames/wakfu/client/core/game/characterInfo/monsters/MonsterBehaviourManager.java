package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import gnu.trove.*;

public class MonsterBehaviourManager
{
    private static final MonsterBehaviourManager m_instance;
    private final TIntObjectHashMap<MonsterBehaviour> m_behaviours;
    private final TIntObjectHashMap<TIntHashSet> m_behavioursTypesByMonster;
    
    private MonsterBehaviourManager() {
        super();
        this.m_behaviours = new TIntObjectHashMap<MonsterBehaviour>();
        this.m_behavioursTypesByMonster = new TIntObjectHashMap<TIntHashSet>();
        this.m_behaviours.put(111111, new MonsterBehaviour(111111, -1, 111111, false));
        this.m_behaviours.put(111112, new MonsterBehaviour(111112, -1, 111112, false));
    }
    
    public static MonsterBehaviourManager getInstance() {
        return MonsterBehaviourManager.m_instance;
    }
    
    public MonsterBehaviour getBehaviour(final int id) {
        return this.m_behaviours.get(id);
    }
    
    public void addBehaviour(final int monsterId, final MonsterBehaviour behaviour) {
        if (!this.m_behavioursTypesByMonster.contains(monsterId)) {
            this.m_behavioursTypesByMonster.put(monsterId, new TIntHashSet());
        }
        this.m_behaviours.put(behaviour.getId(), behaviour);
        this.m_behavioursTypesByMonster.get(monsterId).add(behaviour.getType());
    }
    
    public boolean monsterHasType(final int monsterId, final int typeId) {
        return this.m_behavioursTypesByMonster.contains(monsterId) && this.m_behavioursTypesByMonster.get(monsterId).contains(typeId);
    }
    
    static {
        m_instance = new MonsterBehaviourManager();
    }
}
