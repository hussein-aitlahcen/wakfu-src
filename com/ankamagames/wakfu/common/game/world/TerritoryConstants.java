package com.ankamagames.wakfu.common.game.world;

import gnu.trove.*;

public final class TerritoryConstants
{
    public static final TIntHashSet IGNORED_TERRITORIES;
    
    static {
        (IGNORED_TERRITORIES = new TIntHashSet()).add(40201);
        TerritoryConstants.IGNORED_TERRITORIES.add(36901);
        TerritoryConstants.IGNORED_TERRITORIES.add(37301);
        TerritoryConstants.IGNORED_TERRITORIES.add(40901);
    }
}
