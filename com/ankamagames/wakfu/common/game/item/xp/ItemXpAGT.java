package com.ankamagames.wakfu.common.game.item.xp;

import org.apache.commons.lang3.*;

public class ItemXpAGT
{
    private static final int[] ITEM_REF_IDS;
    
    public static boolean contains(final int itemRefId) {
        return ArrayUtils.contains(ItemXpAGT.ITEM_REF_IDS, itemRefId);
    }
    
    static {
        ITEM_REF_IDS = new int[] { 12097, 12191, 12184, 12185, 12186, 12187, 12188, 12189, 12190, 12192, 12193, 12194, 12195, 12196, 12197, 12178, 12183, 15107, 15108, 15109, 15110 };
    }
}
