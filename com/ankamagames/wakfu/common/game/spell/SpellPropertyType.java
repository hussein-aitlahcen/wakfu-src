package com.ankamagames.wakfu.common.game.spell;

import com.ankamagames.framework.external.*;

public enum SpellPropertyType implements ExportableEnum
{
    FIRE_SPELL(1, "Sort feu"), 
    WATER_SPELL(2, "Sort eau"), 
    EARTH_SPELL(3, "Sort terre"), 
    AIR_SPELL(4, "Sort air"), 
    MOVEMENT_SPELL(5, "Sort de d\u00e9placement"), 
    TELEPORT_SPELL(6, "Sort de t\u00e9l\u00e9portation"), 
    FREEING_SPELL(7, "Sort de d\u00e9gagement"), 
    SINGLE_TARGET_SPELL(8, "Sort monocible"), 
    AOE_SPELL(9, "Sort de zone"), 
    MELEE_SPELL(10, "Sort de mel\u00e9e"), 
    RANGED_SPELL(11, "Sort \u00e0 distance"), 
    USE_GATE_SPELL(12, "Sort utilisable avec les portails"), 
    DO_NOT_DISPLAY_INVALID_CRITERION_CELLS(13, "On n'affiche rien pour les cellules invalides dans l'aper\u00e7u de la zone de lancement du sort"), 
    TEAM_GATES_TARGETABLE(14, "Les portails de la team sont ciblables, quels que soient la port\u00e9e et la ligne de vue"), 
    ONLY_VALID_ON_OUTPUT_WHEN_USE_GATE(15, "Sort valide uniquement sur le portail de sortie si il utilise un portail d'entr\u00e9e");
    
    private final int m_propertyId;
    private final String m_label;
    
    private SpellPropertyType(final int propertyId, final String label) {
        this.m_propertyId = propertyId;
        this.m_label = label;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_propertyId);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static SpellPropertyType getPropertyFromId(final int id) {
        for (final SpellPropertyType prop : values()) {
            if (prop.m_propertyId == id) {
                return prop;
            }
        }
        return null;
    }
}
