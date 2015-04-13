package com.ankamagames.wakfu.common.game.item;

import com.ankamagames.framework.external.*;
import org.jetbrains.annotations.*;

public enum EquipmentPosition implements ExportableEnum
{
    HEAD(0, "desc.equipment.head"), 
    HAIR(1, ""), 
    FACE(2, ""), 
    SHOULDERS(3, "desc.equipment.shoulder"), 
    NECK(4, "desc.equipment.neck"), 
    CHEST(5, "desc.equipment.chest"), 
    ARMS(6, "desc.equipment.arms"), 
    LEFT_HAND(7, "desc.equipment.leftHand"), 
    RIGHT_HAND(8, "desc.equipment.rightHand"), 
    SKIRT(9, "desc.equipment.skirt"), 
    BELT(10, "desc.equipment.belt"), 
    TROUSERS(11, "desc.equipment.trousers"), 
    LEGS(12, "desc.equipment.legs"), 
    BACK(13, "desc.equipment.back"), 
    WING(14, ""), 
    FIRST_WEAPON(15, "desc.equipment.firstWeapon"), 
    SECOND_WEAPON(16, "desc.equipment.secondWeapon"), 
    ACCESSORY(17, "desc.equipment.insigne"), 
    BAG_1(18, ""), 
    BAG_2(19, ""), 
    BAG_3(20, ""), 
    BAG_4(21, ""), 
    PET(22, "desc.equipment.pet"), 
    COSTUME(23, "desc.equipment.costume"), 
    MOUNT(24, "desc.equipment.mount");
    
    public final byte m_id;
    public final String descKey;
    
    private EquipmentPosition(final int id, final String key) {
        this.m_id = (byte)id;
        this.descKey = key;
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public static byte getEquipmentSize() {
        byte maxId = -1;
        for (final EquipmentPosition pos : values()) {
            if (pos.m_id > maxId) {
                maxId = pos.m_id;
            }
        }
        return (byte)(maxId + 1);
    }
    
    @Override
    public String getEnumId() {
        return this.toString();
    }
    
    @Override
    public String getEnumLabel() {
        return this.toString();
    }
    
    @Nullable
    public static EquipmentPosition fromId(final byte id) {
        for (final EquipmentPosition pos : values()) {
            if (pos.m_id == id) {
                return pos;
            }
        }
        return null;
    }
    
    @Override
    public String getEnumComment() {
        return this.getEnumLabel();
    }
}
