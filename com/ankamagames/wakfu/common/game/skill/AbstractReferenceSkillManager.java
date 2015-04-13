package com.ankamagames.wakfu.common.game.skill;

import org.apache.log4j.*;
import gnu.trove.*;

public class AbstractReferenceSkillManager<ReferenceSkill extends AbstractReferenceSkill>
{
    protected static final Logger m_logger;
    private final TIntObjectHashMap<ReferenceSkill> m_skills;
    
    protected AbstractReferenceSkillManager() {
        super();
        this.m_skills = new TIntObjectHashMap<ReferenceSkill>();
    }
    
    public void addReferenceSkill(final ReferenceSkill skill) {
        this.m_skills.put(skill.getId(), skill);
    }
    
    public TIntObjectIterator<ReferenceSkill> getReferenceSkillsIterator() {
        return this.m_skills.iterator();
    }
    
    public ReferenceSkill getReferenceSkill(final int skillId) {
        return this.m_skills.get(skillId);
    }
    
    public ReferenceSkill getReferenceSkillByItemType(final SkillType skillType, final short itemTypeId) {
        final TIntObjectIterator<ReferenceSkill> iter = this.m_skills.iterator();
        while (iter.hasNext()) {
            iter.advance();
            final ReferenceSkill skill = iter.value();
            if (skill.getType() == skillType && skill.getAssociatedItemTypes().contains(itemTypeId)) {
                return skill;
            }
        }
        return null;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractReferenceSkillManager.class);
    }
}
