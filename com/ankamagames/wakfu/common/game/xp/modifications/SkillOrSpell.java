package com.ankamagames.wakfu.common.game.xp.modifications;

public class SkillOrSpell
{
    protected final int m_refId;
    protected final boolean m_isSkill;
    
    public SkillOrSpell(final boolean skill, final int refId) {
        super();
        this.m_isSkill = skill;
        this.m_refId = refId;
    }
    
    public int getRefId() {
        return this.m_refId;
    }
    
    public boolean isSkill() {
        return this.m_isSkill;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final SkillOrSpell skillOrSpell = (SkillOrSpell)obj;
        return this.m_isSkill == skillOrSpell.m_isSkill && this.m_refId == skillOrSpell.m_refId;
    }
    
    @Override
    public int hashCode() {
        return 31 * this.m_refId + (this.m_isSkill ? 1 : 0);
    }
}
