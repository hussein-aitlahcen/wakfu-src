package com.ankamagames.wakfu.common.game.personalSpace.impl;

import gnu.trove.*;

public class DimensionalBagPersonalSkin
{
    public static final int DEFAULT_DIM_BAG_TEMPLATE_ID = 3700;
    public static final int DEFAULT_WORLD_DIM_BAG_VIEW_ID = 408;
    public static final int DEFAULT_MARKET_DIM_BAG_VIEW_ID = 467;
    private static final int[] AMAKNA_VIEWS;
    private static final int[] BONTA_VIEWS;
    private static final int[] BRAKMAR_VIEWS;
    private static final int[] SUFOKIA_VIEWS;
    private static final TIntHashSet m_unvalidViewIds;
    private static int m_nationId;
    
    public static boolean isForNation(final int playerNationId, final int modelId) {
        if (DimensionalBagPersonalSkin.m_nationId != playerNationId) {
            DimensionalBagPersonalSkin.m_nationId = playerNationId;
            DimensionalBagPersonalSkin.m_unvalidViewIds.clear();
            fillUnvalidView();
        }
        return !DimensionalBagPersonalSkin.m_unvalidViewIds.contains(modelId);
    }
    
    private static void fillUnvalidView() {
        if (DimensionalBagPersonalSkin.m_nationId != 30) {
            DimensionalBagPersonalSkin.m_unvalidViewIds.addAll(DimensionalBagPersonalSkin.AMAKNA_VIEWS);
        }
        if (DimensionalBagPersonalSkin.m_nationId != 31) {
            DimensionalBagPersonalSkin.m_unvalidViewIds.addAll(DimensionalBagPersonalSkin.BONTA_VIEWS);
        }
        if (DimensionalBagPersonalSkin.m_nationId != 32) {
            DimensionalBagPersonalSkin.m_unvalidViewIds.addAll(DimensionalBagPersonalSkin.BRAKMAR_VIEWS);
        }
        if (DimensionalBagPersonalSkin.m_nationId != 33) {
            DimensionalBagPersonalSkin.m_unvalidViewIds.addAll(DimensionalBagPersonalSkin.SUFOKIA_VIEWS);
        }
    }
    
    static {
        AMAKNA_VIEWS = new int[] { 480, 479, 478, 467 };
        BONTA_VIEWS = new int[] { 510, 509, 508, 507 };
        BRAKMAR_VIEWS = new int[] { 840, 839, 838, 837 };
        SUFOKIA_VIEWS = new int[] { 836, 835, 834, 833 };
        m_unvalidViewIds = new TIntHashSet();
        DimensionalBagPersonalSkin.m_nationId = Integer.MAX_VALUE;
    }
}
