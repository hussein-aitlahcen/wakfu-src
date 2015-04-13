package com.ankamagames.wakfu.client.core.game.characterInfo.monsters;

import gnu.trove.*;

public class MonsterEvolutionStepManager
{
    private static final MonsterEvolutionStepManager m_instance;
    private final TIntObjectHashMap<MonsterEvolutionStep> m_evolutionSteps;
    
    private MonsterEvolutionStepManager() {
        super();
        this.m_evolutionSteps = new TIntObjectHashMap<MonsterEvolutionStep>();
    }
    
    public static MonsterEvolutionStepManager getInstance() {
        return MonsterEvolutionStepManager.m_instance;
    }
    
    public MonsterEvolutionStep getEvolutionStep(final int id) {
        return this.m_evolutionSteps.get(id);
    }
    
    public void addEvolutionStep(final MonsterEvolutionStep step) {
        this.m_evolutionSteps.put(step.getId(), step);
    }
    
    static {
        m_instance = new MonsterEvolutionStepManager();
    }
}
