package com.ankamagames.wakfu.common.datas;

public enum CriterionUserType
{
    CHARACTER, 
    PLAYER_CHARACTER(CriterionUserType.CHARACTER), 
    NON_PLAYER_CHARACTER(CriterionUserType.CHARACTER), 
    COMPANION(CriterionUserType.NON_PLAYER_CHARACTER), 
    EFFECT_AREA, 
    DESTRUCTIBLE;
    
    private final CriterionUserType m_parent;
    
    private CriterionUserType(final int ordinal) {
        this.m_parent = null;
    }
    
    private CriterionUserType(final CriterionUserType parent) {
        this.m_parent = parent;
    }
    
    public boolean is(final CriterionUserType potentialParent) {
        for (CriterionUserType type = this; type != null; type = type.m_parent) {
            if (type == potentialParent) {
                return true;
            }
        }
        return false;
    }
}
