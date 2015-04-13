package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.wakfu.common.game.item.*;

public class GemsUpgradeHelper
{
    public static final int HAMMER_ITEM_ID = 16164;
    public static final int HAMMER_ITEM_2_ID = 18258;
    private static final int KAMA_COST_FACTOR = 30;
    private static final GemsUpgradeData[] UPGRADE_DATA_LIST;
    
    private static double getKamaCostFactorFromRarity(final ItemRarity rarity) {
        switch (rarity) {
            case ADMIN: {
                return 0.0;
            }
            case COMMON: {
                return 0.2;
            }
            case UNUSUAL: {
                return 0.4;
            }
            case RARE: {
                return 0.6;
            }
            case MYTHIC: {
                return 0.8;
            }
            default: {
                return 1.0;
            }
        }
    }
    
    public static int getKamaNeeded(final GemsHolder gemmedItem, final byte currentLevel) {
        return (int)(gemmedItem.getLevel() * (currentLevel + 2) * 30 * getKamaCostFactorFromRarity(gemmedItem.getRarity()));
    }
    
    public static GemsUpgradeData getNeededItems(final byte currentLevel) {
        if (currentLevel < 0 || currentLevel >= GemsUpgradeHelper.UPGRADE_DATA_LIST.length) {
            return null;
        }
        return GemsUpgradeHelper.UPGRADE_DATA_LIST[currentLevel];
    }
    
    static {
        UPGRADE_DATA_LIST = new GemsUpgradeData[] { new GemsUpgradeData(1, ItemRarity.RARE), new GemsUpgradeData(1, ItemRarity.RARE), new GemsUpgradeData(2, ItemRarity.RARE), new GemsUpgradeData(2, ItemRarity.RARE), new GemsUpgradeData(2, ItemRarity.MYTHIC), new GemsUpgradeData(2, ItemRarity.MYTHIC), new GemsUpgradeData(2, ItemRarity.LEGENDARY), new GemsUpgradeData(2, ItemRarity.LEGENDARY), new GemsUpgradeData(2, ItemRarity.LEGENDARY) };
    }
}
