package com.ankamagames.wakfu.client.core.game.ressource;

import gnu.trove.*;

public class MonsterReferenceResource extends ReferenceResource
{
    private final TIntArrayList m_monsterFamilies;
    private short m_birthEvolutionStep;
    
    public MonsterReferenceResource(final int id, final short ressourceType, final short idealTempMin, final short idealTempMax, final short idealRainMin, final short idealRainMax, final boolean isBlocking, final short height, final TIntObjectHashMap<int[]> gfxIds, final int iconGfxId, final boolean useBigChallengeAps) {
        super(id, ressourceType, idealTempMin, idealTempMax, idealRainMin, idealRainMax, isBlocking, height, gfxIds, iconGfxId, useBigChallengeAps);
        this.m_monsterFamilies = new TIntArrayList();
    }
    
    public void addMonsterFamilies(final int[] monsterFamilies) {
        this.m_monsterFamilies.add(monsterFamilies);
    }
    
    public short getBirthEvolutionStep() {
        return this.m_birthEvolutionStep;
    }
    
    public void setBirthEvolutionStep(final short birthEvolutionStep) {
        this.m_birthEvolutionStep = birthEvolutionStep;
    }
    
    public int[] getMonsterFamilies() {
        return this.m_monsterFamilies.toNativeArray();
    }
}
