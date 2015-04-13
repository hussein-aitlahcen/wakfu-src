package com.ankamagames.wakfu.common.datas.Breed;

import org.apache.log4j.*;
import gnu.trove.*;

public class MonsterFamilyManager
{
    protected static final Logger m_logger;
    private static final MonsterFamilyManager m_instance;
    private final TIntObjectHashMap<MonsterFamily> m_monsterFamilies;
    
    public MonsterFamilyManager() {
        super();
        this.m_monsterFamilies = new TIntObjectHashMap<MonsterFamily>();
    }
    
    public void addMonsterFamily(final MonsterFamily monsterFamily) {
        final int familyId = monsterFamily.getFamilyId();
        if (!this.m_monsterFamilies.containsKey(familyId)) {
            this.m_monsterFamilies.put(familyId, monsterFamily);
        }
        else {
            MonsterFamilyManager.m_logger.warn((Object)"tentative de surchage de la d\u00e9finition d'une famille dans le MonsterFamilyManager");
        }
    }
    
    public MonsterFamily getMonsterFamily(final int familyId) {
        return this.m_monsterFamilies.get(familyId);
    }
    
    public TIntObjectIterator<MonsterFamily> iterator() {
        return this.m_monsterFamilies.iterator();
    }
    
    public MonsterFamily getEcosystemSubFamily(final MonsterFamily family) {
        return this.getSubFamily(family, MonsterFamilyType.ECOSYSTEM);
    }
    
    public MonsterFamily getDungeonSubFamily(final MonsterFamily family) {
        return this.getSubFamily(family, MonsterFamilyType.DUNGEON);
    }
    
    public MonsterFamily getSpecialSubFamily(final MonsterFamily family) {
        return this.getSubFamily(family, MonsterFamilyType.SPECIAL);
    }
    
    private MonsterFamily getSubFamily(final MonsterFamily family, final MonsterFamilyType type) {
        final TIntObjectIterator<MonsterFamily> it = this.m_monsterFamilies.iterator();
        while (it.hasNext()) {
            it.advance();
            final MonsterFamily childFamily = it.value();
            if (childFamily.getParentFamilyId() == family.getFamilyId() && childFamily.getType() == type) {
                return childFamily;
            }
        }
        return null;
    }
    
    public static MonsterFamilyManager getInstance() {
        return MonsterFamilyManager.m_instance;
    }
    
    static {
        m_logger = Logger.getLogger((Class)MonsterFamilyManager.class);
        m_instance = new MonsterFamilyManager();
    }
}
