package com.ankamagames.wakfu.common.constants;

import com.ankamagames.framework.kernel.core.maths.*;
import gnu.trove.*;
import org.apache.commons.lang3.*;
import com.ankamagames.wakfu.common.game.havenWorld.definition.entry.*;

public class HavenWorldConstants
{
    public static final int HAVEN_WORLD_ORIGIN_X = -4;
    public static final int HAVEN_WORLD_ORIGIN_Y = -10;
    public static final int HAVEN_WORLD_WIDTH = 8;
    public static final int HAVEN_WORLD_HEIGHT = 11;
    public static final int HAVEN_WORLD_ECOZONE_MODEL_ID = 56401;
    public static final Point3 HAVEN_WORLD_SIDOA_CHIEF_POSITION;
    public static final short HAVEN_WORLD_SIDOA_CHIEF_TEMPLATE_ID = 159;
    public static final Point3 HAVEN_WORLD_RESOURCES_COLLECTOR_POSITION;
    public static final int HAVEN_WORLD_RESOURCES_COLLECTOR_TEMPLATE_ID = 19119;
    public static final Point3 HAVEN_WORLD_PRIMS_POSITION;
    public static final int HAVEN_WORLD_PRIMS_TEMPLATE_ID = 19198;
    public static final short HAVEN_WORLD_SIDOA_GROUP_TEMPLATE_ID = 161;
    public static final int[] HAVEN_WORLD_ENTRY_POINT_AS_ARRAY;
    public static final int[] HAVEN_WORLD_RESCUE_POINT_AS_ARRAY;
    public static final int HAVEN_WORLD_EXIT_TEMPLATE_ID = 16530;
    public static final Point3 HAVEN_WORLD_EXIT_POSITION;
    public static final short[] RESOURCES_TYPE_IDS;
    public static final int MAX_RESOURCES_QUANTITY = 50000000;
    private static final int TIER_I = 12;
    private static final int TIER_II = 13;
    private static final int TIER_III = 14;
    private static final int TIER_IV = 15;
    public static final int MAX_MINERAL_PATCH_AUTHORIZED = 20;
    public static final TIntHashSet MINERAL_CATEGORY;
    private static final TIntObjectHashMap<int[]> BUILDING_TYPES;
    public static final byte CATEGORY_INVALID = 0;
    public static final byte CATEGORY_BUILDING = 1;
    public static final byte CATEGORY_DECO = 2;
    public static final byte CATEGORY_DUNGEON = 3;
    private static final int TEMPORARY_CATEGORY = 7;
    private static final int DUNGEON_CATEGORY = 18;
    
    public static boolean isGroundBase(final int groundId) {
        return groundId == 311;
    }
    
    public static boolean isHavenWorldAutoRespawnResource(final short resourceType) {
        return resourceType == 7 || resourceType == 23 || resourceType == 22 || resourceType == 20;
    }
    
    public static boolean isBuildingTypeLinked(final int sourceType, final int destType) {
        final int[] types = HavenWorldConstants.BUILDING_TYPES.get(sourceType);
        return ArrayUtils.contains(types, destType);
    }
    
    public static byte getCategoryFromCatalogEntry(final BuildingCatalogEntry entry) {
        if (entry.isDecoOnly()) {
            return 2;
        }
        if (entry.getCategoryId() == 7) {
            return 0;
        }
        if (entry.getCategoryId() == 18) {
            return 3;
        }
        return 1;
    }
    
    static {
        HAVEN_WORLD_SIDOA_CHIEF_POSITION = new Point3(0, -52, (short)0);
        HAVEN_WORLD_RESOURCES_COLLECTOR_POSITION = new Point3(-1, -52, (short)0);
        HAVEN_WORLD_PRIMS_POSITION = new Point3(0, -42, (short)0);
        HAVEN_WORLD_ENTRY_POINT_AS_ARRAY = new int[] { 0, -33, 0 };
        HAVEN_WORLD_RESCUE_POINT_AS_ARRAY = new int[] { 0, -50, 0 };
        HAVEN_WORLD_EXIT_POSITION = new Point3(0, -32, (short)0);
        RESOURCES_TYPE_IDS = new short[] { 226, 471, 683, 118, 100 };
        MINERAL_CATEGORY = new TIntHashSet(new int[] { 11, 21, 14, 28 });
        (BUILDING_TYPES = new TIntObjectHashMap<int[]>()).put(12, new int[] { 12 });
        HavenWorldConstants.BUILDING_TYPES.put(13, new int[] { 12, 13 });
        HavenWorldConstants.BUILDING_TYPES.put(14, new int[] { 12, 13, 14 });
        HavenWorldConstants.BUILDING_TYPES.put(15, new int[] { 12, 13, 14, 15 });
    }
}
