package com.ankamagames.wakfu.common.game.skill;

import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.xp.*;

public enum SkillType implements ExportableEnum
{
    WEAPON_SKILL(1, "Arme", SpellXpTable.getInstance()), 
    CRAFT_SKILL(2, "Artisanat", NulllXpTable.getInstance()), 
    COLLECT_SKILL(3, "R\u00e9colte", HarvestSkillXpTable.getInstance()), 
    PLANT_SKILL(4, "Plantation", NulllXpTable.getInstance()), 
    LUXURY_SKILL(5, "Luxe", NulllXpTable.getInstance());
    
    private final String m_label;
    private final int m_typeId;
    private final XpTable m_XpTableRef;
    
    private SkillType(final int id, final String label, final XpTable tableref) {
        this.m_typeId = id;
        this.m_label = label;
        this.m_XpTableRef = tableref;
    }
    
    public XpTable getXpTableRef() {
        return this.m_XpTableRef;
    }
    
    @Override
    public String getEnumId() {
        return Integer.valueOf(this.m_typeId).toString();
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    public int getTypeId() {
        return this.m_typeId;
    }
    
    public static SkillType getFromId(final int id) {
        final SkillType[] arr$;
        final SkillType[] types = arr$ = values();
        for (final SkillType type : arr$) {
            if (type.getTypeId() == id) {
                return type;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
}
