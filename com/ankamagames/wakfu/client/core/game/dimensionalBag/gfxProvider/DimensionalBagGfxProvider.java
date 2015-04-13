package com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider;

import com.ankamagames.framework.external.*;
import com.ankamagames.wakfu.common.game.personalSpace.impl.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.groundPattern.*;
import java.util.*;
import com.ankamagames.wakfu.client.core.game.dimensionalBag.gfxProvider.groundPattern.impl.*;
import gnu.trove.*;

public class DimensionalBagGfxProvider implements GfxIdProvider
{
    private static final DimensionalBagGfxProvider m_instance;
    public static final int INVALID_GFX = -1;
    public static final int NO_GFX = 0;
    private static final EnumMap<GemType, RoomPattern> ROOMS_PATTERNS;
    private static final RoomPattern CORRIDOR_PATTERN;
    
    private static void addRoomPattern(final GemType type, final GroundPattern ground, final BorderPattern border) {
        DimensionalBagGfxProvider.ROOMS_PATTERNS.put(type, new RoomPattern(border, ground));
    }
    
    public static RoomPattern getCorridor() {
        return DimensionalBagGfxProvider.CORRIDOR_PATTERN;
    }
    
    public static RoomPattern getRoomPattern(final int roomType) {
        return DimensionalBagGfxProvider.ROOMS_PATTERNS.get(GemType.getFromItemReferenceId(roomType));
    }
    
    public static RoomPattern getDefaultRoomPattern() {
        return DimensionalBagGfxProvider.ROOMS_PATTERNS.get(GemType.getDefaultGemType());
    }
    
    @Override
    public TIntHashSet getGfxIds() {
        final TIntHashSet elementIds = new TIntHashSet();
        for (final RoomPattern pattern : DimensionalBagGfxProvider.ROOMS_PATTERNS.values()) {
            pattern.getAllElementIds(elementIds);
        }
        DimensionalBagGfxProvider.CORRIDOR_PATTERN.getAllElementIds(elementIds);
        return elementIds;
    }
    
    public static DimensionalBagGfxProvider getInstance() {
        return DimensionalBagGfxProvider.m_instance;
    }
    
    public static void main(final String[] args) {
        final TIntHashSet gfxIds = getInstance().getGfxIds();
        final TIntIterator iter = gfxIds.iterator();
        while (iter.hasNext()) {
            System.out.print(" " + iter.next());
        }
    }
    
    static {
        m_instance = new DimensionalBagGfxProvider();
        final BorderPattern border = new BorderPattern(new int[] { 34360 }, new int[] { 34359 }, 34362, 34358, 34361, 34383);
        final BorderPattern borderResource = new BorderPattern(new int[] { 34470 }, new int[] { 34469 }, 34472, 34468, 34471, 34473);
        ROOMS_PATTERNS = new EnumMap<GemType, RoomPattern>(GemType.class);
        addRoomPattern(GemType.GEM_ID_MERCHANT, new PseudoRandomPattern(new int[] { 34350, 34351 }), border);
        addRoomPattern(GemType.GEM_ID_DECORATION, new PseudoRandomPattern(new int[] { 34352, 34353 }), border);
        addRoomPattern(GemType.GEM_ID_LIVING_ROOM, new PseudoRandomPattern(new int[] { 20562, 20563 }), border);
        addRoomPattern(GemType.GEM_ID_CRAFT, new PseudoRandomPattern(new int[] { 34356, 34357 }), border);
        addRoomPattern(GemType.GEM_ID_RESOURCES, new PseudoRandomPattern(new int[] { 34354, 34355 }), borderResource);
        addRoomPattern(GemType.GEM_ID_FARMING, new PseudoRandomPattern(new int[] { 34354, 34355 }), borderResource);
        addRoomPattern(GemType.GEM_ID_DUNGEON, new PseudoRandomPattern(new int[] { 10128 }), border);
        CORRIDOR_PATTERN = new RoomPattern(new BorderPattern(), new DirectionalPattern(34349, 34348));
    }
}
