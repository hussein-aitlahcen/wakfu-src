package com.ankamagames.wakfu.client.core.game.companion;

import com.ankamagames.wakfu.client.core.game.characterInfo.monsters.*;
import com.ankamagames.wakfu.common.datas.specific.*;
import gnu.trove.*;

public class CompanionMonsterBreedManager
{
    private boolean m_initialized;
    public static final CompanionMonsterBreedManager INSTANCE;
    private final TIntHashSet m_breedIds;
    
    public CompanionMonsterBreedManager() {
        super();
        this.m_initialized = false;
        this.m_breedIds = new TIntHashSet();
    }
    
    public TIntHashSet getMonsterBreedIds() {
        if (!this.m_initialized) {
            this.init();
        }
        return this.m_breedIds;
    }
    
    private void init() {
        final TIntObjectIterator<MonsterBreed> it = MonsterBreedManager.getInstance().getFullList().iterator();
        while (it.hasNext()) {
            it.advance();
            final MonsterBreed breed = it.value();
            for (final int worldProperty : breed.getBaseWorldProperties()) {
                if (worldProperty == WorldPropertyType.COMPANION.getId()) {
                    this.m_breedIds.add(breed.getBreedId());
                }
            }
        }
    }
    
    static {
        INSTANCE = new CompanionMonsterBreedManager();
    }
}
