package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.baseImpl.common.clientAndServer.game.characteristic.*;

public enum ItemPropertyType implements PropertyType
{
    NONE(0), 
    CHANGE_DAMAGE_ELEMENT_PHYSICAL(1), 
    CHANGE_DAMAGE_ELEMENT_FIRE(2), 
    CHANGE_DAMAGE_ELEMENT_WATER(3), 
    CHANGE_DAMAGE_ELEMENT_EARTH(4), 
    CHANGE_DAMAGE_ELEMENT_AIR(5), 
    CHANGE_DAMAGE_ELEMENT_STASIS(6);
    
    private byte m_propertyId;
    
    private ItemPropertyType(final int propertyId) {
        this.m_propertyId = (byte)propertyId;
    }
    
    @Override
    public byte getId() {
        return this.m_propertyId;
    }
    
    @Override
    public byte getPropertyTypeId() {
        return 2;
    }
    
    public static ItemPropertyType getPropertyFromId(final int id) {
        for (final ItemPropertyType prop : values()) {
            if (prop.getId() == id) {
                return prop;
            }
        }
        return null;
    }
}
