package com.ankamagames.wakfu.common.game.havenWorld;

import com.ankamagames.wakfu.common.game.item.*;

public class HavenWorldResourcesCollectorUtil
{
    public static long calculateRessources(final Item item, final short quantity) {
        return Math.max(item.getLevel(), 1) * quantity;
    }
}
