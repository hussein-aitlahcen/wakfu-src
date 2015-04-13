package com.ankamagames.wakfu.client.core.game.skill;

import com.ankamagames.wakfu.common.rawData.*;
import com.ankamagames.baseImpl.common.clientAndServer.game.inventory.*;

public class SkillProvider implements InventoryContentProvider<Skill, RawSkill>
{
    private static final SkillProvider m_uniqueInstance;
    
    public static SkillProvider getInstance() {
        return SkillProvider.m_uniqueInstance;
    }
    
    @Override
    public Skill unSerializeContent(final RawSkill rawItem) {
        final Skill skill = new Skill();
        if (skill.fromRaw(rawItem)) {
            return skill;
        }
        return null;
    }
    
    static {
        m_uniqueInstance = new SkillProvider();
    }
}
