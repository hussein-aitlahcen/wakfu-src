package com.ankamagames.wakfu.common.datas.Breed;

public abstract class AbstractMonsterBehaviourModel
{
    protected int m_id;
    
    public AbstractMonsterBehaviourModel(final int id) {
        super();
        this.m_id = id;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public void setId(final int id) {
        this.m_id = id;
    }
}
