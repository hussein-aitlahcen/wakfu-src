package com.ankamagames.wakfu.common.datas.Breed;

public abstract class AbstractMonsterEvolutionStep
{
    protected final int m_id;
    protected final short m_evolvedBreedId;
    
    public AbstractMonsterEvolutionStep(final int id, final short evolvedBreedId) {
        super();
        this.m_id = id;
        this.m_evolvedBreedId = evolvedBreedId;
    }
    
    public short getEvolvedBreedId() {
        return this.m_evolvedBreedId;
    }
    
    public int getId() {
        return this.m_id;
    }
}
