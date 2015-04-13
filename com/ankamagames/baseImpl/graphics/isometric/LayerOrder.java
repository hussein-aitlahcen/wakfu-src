package com.ankamagames.baseImpl.graphics.isometric;

public enum LayerOrder
{
    GROUND, 
    HIGHTLIGHT, 
    IE, 
    RESOURCE_SMALL, 
    DEAD_NPC, 
    OBJECT_LOOTED, 
    APS, 
    EFFECT_AREA_IE, 
    DYNAMIC_ELEMENT, 
    MOBILE, 
    PLAYER, 
    RESOURCE_HIGH, 
    APS_FRONT;
    
    private final byte m_deltaZ;
    public static final int LAYER_COUNT;
    public static final int SORTED_BIT_USED = 15;
    public static final int SORTED_MASK = 32767;
    public static final int MAX_SORTED_COUNT = 32766;
    private static final int SUB_LAYER_BIT_USED = 5;
    private static final int LAYER_BIT_USED = 13;
    private static final long LAYER_MASK = 8191L;
    private static final int LAYER_BIT_OFFSET = 6;
    private static final int XY_BIT_USED = 14;
    private static final long XY_MASK = 16383L;
    private static final long XY_SHIFT = 8192L;
    private static final int X_BIT_OFFSET = 19;
    private static final int Y_BIT_OFFSET = 34;
    
    private LayerOrder(final int ordinal) {
        this.m_deltaZ = (byte)this.ordinal();
    }
    
    public final byte getDeltaZ() {
        return this.m_deltaZ;
    }
    
    public static int getDeltaZ(final int layer) {
        return layer;
    }
    
    public static boolean checkSortable(final long zOrder) {
        final long maxZOrder = 281474976710656L;
        return zOrder >= 0L && zOrder < 281474976710656L;
    }
    
    public static long computeZOrder(final long x, final long y, final int zOrder, final int deltaZ) {
        assert Math.abs(x) < 16383L;
        assert Math.abs(y) < 16383L;
        assert zOrder >= 0 && zOrder < 8191L;
        assert deltaZ >= 0 && deltaZ < 31;
        return (y + 8192L & 0x3FFFL) << 34 | (x + 8192L & 0x3FFFL) << 19 | (zOrder & 0x1FFFL) << 6 | deltaZ;
    }
    
    static {
        LAYER_COUNT = values().length;
        assert LayerOrder.LAYER_COUNT < 8191;
    }
}
