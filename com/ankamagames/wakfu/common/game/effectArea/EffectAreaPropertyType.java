package com.ankamagames.wakfu.common.game.effectArea;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;
import com.ankamagames.framework.external.*;

public enum EffectAreaPropertyType implements PropertyType, ExportableEnum
{
    GRIP((byte)0, "Stoppe le mouvement"), 
    NOT_ACTIVATED_BY_ALLIES((byte)1, "Ne peut pas \u00eatre activ\u00e9e par les alli\u00e9s"), 
    AI_AVOID_AREA((byte)2, "L'IA \u00e9vite d'aller sur la zone"), 
    NOT_NEGATIVE_FOR_RED_TEAM((byte)3, "Zone non n\u00e9gative l'\u00e9quipe rouge"), 
    INVISIBLE_FOR_ENEMIES((byte)4, "Invisible pour les ennemis"), 
    OWNER_IS_CASTER((byte)5, "Le propri\u00e9taire de la zone est le caster des effets (pi\u00e8ge/bombe uniquement)."), 
    AI_END_TURN_ON_THIS((byte)6, "L'ia essaie de terminer son tour sur cette zone"), 
    NORMAL_HP_LOSS((byte)7, "Ne r\u00e9duit pas les pertes de pdv \u00e0 0"), 
    NO_OVERHEAD((byte)8, "N'affiche pas l'overhead de la zone"), 
    USE_ALL_DMG((byte)9, "Prend en compte les d\u00e9g\u00e2ts \u00e9l\u00e9mentaires et secondaires  du caster (pi\u00e8ges uniquement)");
    
    private final byte m_propertyId;
    private final String m_label;
    
    private EffectAreaPropertyType(final byte propertyId, final String label) {
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
    
    @Override
    public byte getId() {
        return this.m_propertyId;
    }
    
    @Override
    public byte getPropertyTypeId() {
        return 4;
    }
    
    public static EffectAreaPropertyType getPropertyFromId(final int id) {
        for (final EffectAreaPropertyType prop : values()) {
            if (prop.m_propertyId == id) {
                return prop;
            }
        }
        return null;
    }
}
