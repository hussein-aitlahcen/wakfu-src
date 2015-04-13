package com.ankamagames.wakfu.client.core.game.skill;

import com.ankamagames.wakfu.common.game.skill.*;

public class ReferenceSkillManager extends AbstractReferenceSkillManager<ReferenceSkill>
{
    private static final ReferenceSkillManager m_instance;
    
    public static ReferenceSkillManager getInstance() {
        return ReferenceSkillManager.m_instance;
    }
    
    static {
        m_instance = new ReferenceSkillManager();
    }
}
