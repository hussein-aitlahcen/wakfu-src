package com.ankamagames.wakfu.common.game.skill;

import gnu.trove.*;

public abstract class AbstractReferenceSkill
{
    private final int m_id;
    private final SkillType m_type;
    private final TIntArrayList m_associatedItemTypes;
    private final TIntArrayList m_associatedItems;
    private final int m_maxLevel;
    private final boolean m_innate;
    private ReferenceSkillSpecificParameters m_specificParameters;
    
    protected AbstractReferenceSkill(final int id, final SkillType type, final int[] associatedItemTypes, final int[] associatedItems, final int maxLevel, final boolean innate, final ReferenceSkillSpecificParameters params) {
        super();
        this.m_associatedItemTypes = new TIntArrayList();
        this.m_associatedItems = new TIntArrayList();
        this.m_id = id;
        this.m_type = type;
        this.m_associatedItemTypes.add(associatedItemTypes);
        this.m_associatedItems.add(associatedItems);
        this.m_maxLevel = maxLevel;
        this.m_innate = innate;
        this.m_specificParameters = params;
    }
    
    public ReferenceSkillSpecificParameters getSpecificParameters() {
        return this.m_specificParameters;
    }
    
    public int getId() {
        return this.m_id;
    }
    
    public SkillType getType() {
        return this.m_type;
    }
    
    public TIntArrayList getAssociatedItemTypes() {
        return this.m_associatedItemTypes;
    }
    
    public TIntArrayList getAssociatedItems() {
        return this.m_associatedItems;
    }
    
    public int getMaxLevel() {
        return this.m_maxLevel;
    }
    
    public boolean isInnate() {
        return this.m_innate;
    }
    
    @Override
    public String toString() {
        return "AbstractReferenceSkill{m_id=" + this.m_id + ", m_type=" + this.m_type + ", m_associatedItemTypes=" + this.m_associatedItemTypes + ", m_associatedItems=" + this.m_associatedItems + ", m_maxLevel=" + this.m_maxLevel + ", m_innate=" + this.m_innate + ", m_specificParameters=" + this.m_specificParameters + '}';
    }
}
