package com.ankamagames.wakfu.common.game.skill;

public abstract class ReferenceSkillSpecificParameters
{
    protected final float[] m_params;
    private final Definition m_def;
    
    protected ReferenceSkillSpecificParameters(final Definition def, final float[] params) {
        super();
        this.m_def = def;
        if (def.m_paramCountExpected != params.length) {
            throw new UnsupportedOperationException("Nombre de param\u00e8tre incorrect pour la creation de param\u00e8tres de skill");
        }
        this.m_params = params;
    }
    
    public Definition getReferenceSkillSpecificParametersDef() {
        return this.m_def;
    }
    
    public enum Definition
    {
        WEAPON_REFERENCE_SKILL_PARAMS(1);
        
        private final int m_paramCountExpected;
        
        private Definition(final int paramsCountExpected) {
            this.m_paramCountExpected = paramsCountExpected;
        }
    }
}
