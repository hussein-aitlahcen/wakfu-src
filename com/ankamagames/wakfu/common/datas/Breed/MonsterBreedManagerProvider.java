package com.ankamagames.wakfu.common.datas.Breed;

import org.jetbrains.annotations.*;

public class MonsterBreedManagerProvider
{
    private static AbstractBreedManager m_monsterBreedManager;
    
    public static void setManager(final AbstractBreedManager manager) {
        MonsterBreedManagerProvider.m_monsterBreedManager = manager;
    }
    
    @Nullable
    public static AbstractBreedManager getMonsterBreedManager() {
        return MonsterBreedManagerProvider.m_monsterBreedManager;
    }
}
