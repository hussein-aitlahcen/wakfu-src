package com.ankamagames.wakfu.client.core.game.skill;

import com.ankamagames.wakfu.common.game.skill.*;
import com.ankamagames.wakfu.client.core.*;

public class ReferenceSkill extends AbstractReferenceSkill
{
    private final int m_scriptId;
    
    public ReferenceSkill(final int id, final SkillType type, final int[] associatedItemTypes, final int[] associatedItems, final int maxLevel, final boolean innate, final int scriptId) {
        super(id, type, associatedItemTypes, associatedItems, maxLevel, innate, null);
        this.m_scriptId = scriptId;
    }
    
    public String getName() {
        return WakfuTranslator.getInstance().getString(22, this.getId(), new Object[0]);
    }
    
    public String getDescription() {
        return WakfuTranslator.getInstance().getString(23, this.getId(), new Object[0]);
    }
    
    public int getGfxId() {
        return this.getId();
    }
    
    public int getScriptId() {
        return this.m_scriptId;
    }
}
