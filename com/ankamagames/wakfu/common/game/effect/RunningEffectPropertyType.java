package com.ankamagames.wakfu.common.game.effect;

import com.ankamagames.framework.external.*;

public enum RunningEffectPropertyType implements ExportableEnum
{
    ALWAYS_TRIGGER_LEVEL_1(1, "Se d\u00e9clenche toujours sur ne d\u00e9clenche rien niveau 1"), 
    DONT_USE_BLOCK(2, "Ne prend pas en compte la parade"), 
    DONT_DISPLAY_ZONE(3, "N'affiche pas la zone de l'effet dans le client"), 
    ZONE_EFFECT(4, "Effet de zone"), 
    DONT_TRIGGER_ANYTHING_LEVEL_1(5, "Ne d\u00e9clenche rien niveau 1"), 
    DONT_TRIGGER_ANYTHING_LEVEL_2(6, "Ne d\u00e9clenche rien niveau 2"), 
    TOTEM_LINK(7, "Donne les infos du lien du totem"), 
    SORT_TARGET_BY_PERCENT_HEALTH_ASC(8, "Tri les cibles par % de Hp restant dans l'ordre croissant"), 
    KEEP_WHEN_OFF_PLAY(9, "N'est pas d\u00e9sappliqu\u00e9 quand le porteur passe OffPlay"), 
    HAVEN_WORLD_GUILD_MEMBER_BUFF(10, "Bonus de HavreMonde destin\u00e9s aux membres de la guilde"), 
    HAVEN_WORLD_VISITOR_BUFF(11, "Bonus de HavreMonde destin\u00e9s aux visiteurs "), 
    HAVEN_WORLD_GUILD_MEMBER_WORLD_BUFF(12, "Bonus de HavreMonde destin\u00e9s aux membres de la guilde appliqu\u00e9 partout hors du HM"), 
    IGNORE_STABILISATION(13, "Ignore la stabilisation (pour les effets de mouvement pousser/tirer...)"), 
    SORT_TARGET_BY_DISTANCE_ASC(14, "Tri les cibles par distance croissante"), 
    CAN_BE_EXECUTED_ON_KO(15, "Peut \u00eatre ex\u00e9cut\u00e9 sur un personnage KO"), 
    TRANSMIT_LEVEL_TO_CHILDREN(16, "L'effet transmet son niveau \u00e0 ses sous effets"), 
    BUFF_BYPASS_OUT_FIGHT_MAX(17, "L'effet peut outrepasser le maximum PA/PM hors combat"), 
    TRIGGER_SCENARIO_EVENT(18, "L'effet peut d\u00e9clencher un \u00e9v\u00e8nement de sc\u00e9nario apr\u00e8s son ex\u00e9cution"), 
    DONT_APPLY_PROBABILITY_MODIFCATOR(19, "L'effet n'applique pas l'hypermouvement ou l'hyperaction"), 
    GUILD_EFFECT(20, "Effet de guilde"), 
    COMPANION_ALLOWED_PROSPECTION_BUFF(21, "Un gain de prospection autoris\u00e9 pour les companions"), 
    SINGLE_TARGET_EFFECT(22, "Effet monocible"), 
    MELEE_EFFECT(23, "Effet de mel\u00e9e"), 
    RANGED_EFFECT(24, "Effet \u00e0 distance"), 
    SORT_TARGET_BY_PERCENT_HEALTH_DESC(25, "Tri les cibles par % de Hp restant dans l'ordre d\u00e9croissant"), 
    SORT_TARGET_BY_DISTANCE_DESC(26, "Tri les cibles par distance d\u00e9croissante"), 
    DONT_SPOT_INVI_IN_AI(27, "Ne met pas \u00e0 jour la position des perso invi dans l'IA");
    
    private final byte m_propertyId;
    private final String m_label;
    
    private RunningEffectPropertyType(final int propertyId, final String label) {
        this.m_label = label;
        this.m_propertyId = (byte)propertyId;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_propertyId);
    }
    
    public byte getPropertyId() {
        return this.m_propertyId;
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_label;
    }
    
    @Override
    public String getEnumComment() {
        return null;
    }
    
    public static RunningEffectPropertyType getPropertyFromId(final int id) {
        for (final RunningEffectPropertyType prop : values()) {
            if (prop.m_propertyId == id) {
                return prop;
            }
        }
        return null;
    }
}
