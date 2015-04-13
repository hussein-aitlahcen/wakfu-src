package com.ankamagames.wakfu.common.game.item.gems;

import com.ankamagames.wakfu.common.game.item.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class GemRarityHelper
{
    public static final int NUM_GEM_VARIATIONS = 10;
    
    public static byte rollIndex(final ItemRarity rarity) {
        switch (rarity) {
            case COMMON: {
                return 0;
            }
            case UNUSUAL: {
                return (byte)MathHelper.random(0, 2);
            }
            case RARE: {
                return 1;
            }
            case MYTHIC: {
                return (byte)MathHelper.random(1, 3);
            }
            case LEGENDARY: {
                return 2;
            }
            default: {
                return 0;
            }
        }
    }
}
