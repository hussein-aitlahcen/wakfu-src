package com.ankamagames.wakfu.common.game.item.bind;

import com.ankamagames.framework.external.*;
import com.ankamagames.framework.kernel.core.maths.*;

public enum ItemBindType implements ExportableEnum
{
    NOT_BOUND(0, "Non li\u00e9", false, false, false), 
    BOUND_ON_EQUIP_TO_CHARACTER(1, "Li\u00e9 au personnage quand \u00e9quip\u00e9", true, false, false), 
    BOUND_ON_PICKUP_TO_CHARACTER(2, "Li\u00e9 au personnage quand ramass\u00e9", true, true, false), 
    BOUND_ON_EQUIP_TO_ACCOUNT(3, "Li\u00e9 au compte quand \u00e9quip\u00e9", false, false, false), 
    BOUND_ON_PICKUP_TO_ACCOUNT(4, "Li\u00e9 au compte quand ramass\u00e9", false, true, false), 
    BOUND_ON_EQUIP_TO_CHARACTER_FROM_SHOP(5, "Ne pas utiliser", true, false, true), 
    BOUND_ON_PICKUP_TO_CHARACTER_FROM_SHOP(6, "Ne pas utiliser", true, true, true), 
    BOUND_ON_EQUIP_TO_ACCOUNT_FROM_SHOP(7, "Ne pas utiliser", false, false, true), 
    BOUND_ON_PICKUP_TO_ACCOUNT_FROM_SHOP(8, "Ne pas utiliser", false, true, true);
    
    private final byte m_id;
    private final String m_desc;
    private final boolean m_character;
    private final boolean m_onPickup;
    private final boolean m_shop;
    private final String m_translationKey;
    
    private ItemBindType(final int id, final String desc, final boolean character, final boolean onPickup, final boolean shop) {
        this.m_character = character;
        this.m_onPickup = onPickup;
        this.m_shop = shop;
        this.m_id = MathHelper.ensureByte(id);
        this.m_desc = desc;
        this.m_translationKey = generateTranslationKey(this.m_onPickup, this.m_character);
    }
    
    private static String generateTranslationKey(final boolean pickup, final boolean character) {
        return "itemBoundType." + (character ? "character" : "account") + '.' + (pickup ? "pickup" : "equip");
    }
    
    public byte getId() {
        return this.m_id;
    }
    
    public boolean isShop() {
        return this.m_shop;
    }
    
    public boolean isOnPickup() {
        return this.m_onPickup;
    }
    
    public boolean isCharacter() {
        return this.m_character;
    }
    
    public String getTranslationKey() {
        return this.m_translationKey;
    }
    
    @Override
    public String getEnumId() {
        return String.valueOf(this.m_id);
    }
    
    @Override
    public String getEnumLabel() {
        return this.m_desc;
    }
    
    @Override
    public String getEnumComment() {
        return "";
    }
    
    public static ItemBindType getFromId(final byte id) {
        for (final ItemBindType type : values()) {
            if (type.m_id == id) {
                return type;
            }
        }
        return ItemBindType.NOT_BOUND;
    }
    
    public static ItemBindType fromParameters(final boolean onPickup, final boolean onCharacter) {
        return fromParameters(onPickup, onCharacter, false);
    }
    
    public static ItemBindType fromParameters(final boolean onPickup, final boolean onCharacter, final boolean fromShop) {
        for (final ItemBindType type : values()) {
            if (type.m_character == onCharacter && type.m_onPickup == onPickup && type.m_shop == fromShop) {
                return type;
            }
        }
        return null;
    }
}
