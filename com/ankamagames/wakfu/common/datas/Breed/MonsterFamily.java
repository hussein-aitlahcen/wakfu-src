package com.ankamagames.wakfu.common.datas.Breed;

import com.ankamagames.baseImpl.common.clientAndServer.game.*;
import gnu.trove.*;

public class MonsterFamily implements Referencable
{
    private final int m_familyId;
    private final int m_parentFamilyId;
    private final MonsterFamilyType m_type;
    private final TIntArrayList m_friendlyFamilies;
    private final TIntArrayList m_pestMonsters;
    
    public MonsterFamily(final int familyId, final int parentFamilyId, final MonsterFamilyType type) {
        super();
        this.m_familyId = familyId;
        this.m_parentFamilyId = parentFamilyId;
        this.m_type = type;
        this.m_friendlyFamilies = new TIntArrayList();
        this.m_pestMonsters = new TIntArrayList();
    }
    
    public void addFriendlyFamily(final int familyId) {
        if (!this.m_friendlyFamilies.contains(familyId)) {
            this.m_friendlyFamilies.add(familyId);
        }
    }
    
    public void addPestMonster(final int monsterId) {
        if (!this.m_pestMonsters.contains(monsterId)) {
            this.m_pestMonsters.add(monsterId);
        }
    }
    
    public int getFamilyId() {
        return this.m_familyId;
    }
    
    public TIntArrayList getFriendlyFamilies() {
        return this.m_friendlyFamilies;
    }
    
    public TIntArrayList getPestMonsters() {
        return this.m_pestMonsters;
    }
    
    @Override
    public int getReferenceId() {
        return this.m_familyId;
    }
    
    public int getParentFamilyId() {
        return this.m_parentFamilyId;
    }
    
    public MonsterFamilyType getType() {
        return this.m_type;
    }
}
