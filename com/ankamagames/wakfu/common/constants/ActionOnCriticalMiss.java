package com.ankamagames.wakfu.common.constants;

import com.ankamagames.framework.external.*;

public enum ActionOnCriticalMiss implements ExportableEnum
{
    NO_EFFECT(0, "Pas d'effet (d\u00e9faut)", "Aucun effet n'est ex\u00e9cut\u00e9, mais le co\u00fbt de lancement est consomm\u00e9"), 
    NO_EFFECT_AND_AP_REMOVED(1, "Pas d'effet, suppression des PA", "Aucun effet n'est ex\u00e9cut\u00e9, co\u00fbt consomm\u00e9 et PA remis \u00e0 0"), 
    NO_EFFECT_AND_AP_MP_WP_REMOVED(2, "Pas d'effet, suppression des PA/PM/PW", "AUcun effet n'est ex\u00e9cut\u00e9, co\u00fbt consomm\u00e9 et les PA/PM/PW remis \u00e0 0");
    
    private final int m_id;
    private final String m_label;
    private final String m_comment;
    
    private ActionOnCriticalMiss(final int id, final String label, final String comment) {
        this.m_id = id;
        this.m_label = label;
        this.m_comment = comment;
    }
    
    public static ActionOnCriticalMiss getFromId(final int actionOnCriticalMissId) {
        for (final ActionOnCriticalMiss type : values()) {
            if (type.m_id == actionOnCriticalMissId) {
                return type;
            }
        }
        return ActionOnCriticalMiss.NO_EFFECT;
    }
    
    @Override
    public String getEnumId() {
        return Integer.toString(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return this.m_comment;
    }
}
