package com.ankamagames.wakfu.client.core.game.soap.shop;

import com.ankamagames.wakfu.common.configuration.*;

public final class ShopConstants
{
    public static final String COMPANION_KEY = "companions";
    public static final String PET_HEAL_ARTICLE = "petHeal";
    public static final String PET_FOOD_ARTICLE = "petFood";
    public static final String PET_LEVEL_UP_ARTICLE = "petLevelUp";
    public static final String OGRINS_CATEGORY_KEY = "ogrins";
    public static final String CHARACTER_SLOT_1 = "characterSlot1";
    public static final String CHARACTER_SLOT_2 = "characterSlot2";
    public static final String CHARACTER_SLOT_3 = "characterSlot3";
    public static final String VAULT_UPGRADE_1 = "vaultUpgrade1";
    public static final String VAULT_UPGRADE_2 = "vaultUpgrade2";
    public static final String VAULT_UPGRADE_3 = "vaultUpgrade3";
    public static final String VAULT_UPGRADE_4 = "vaultUpgrade4";
    
    public static String getShopKey() {
        return SystemConfiguration.INSTANCE.getStringValue(SystemConfigurationType.SHOP_KEY);
    }
}
