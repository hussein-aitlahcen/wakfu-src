package com.ankamagames.baseImpl.common.clientAndServer.world.topology;

public final class CellPathData extends CellData
{
    public static final int MURFIN_OUT_IN_MASK = 128;
    public static final int MURFIN_DOOR_MASK = 64;
    public static final int TRANSITION_A = 48;
    public static final int TRANSITION_B = 16;
    private static final int MURFIN_TYPE_MASK = 192;
    private static final int MASK_TRANSITION = 48;
    public static final int MASK_GROUP_ID = 15;
    private static final int MASK_GROUP_INFO = 207;
    public static final int MURFIN_OUTDOOR = 0;
    public static final int MURFIN_INDOOR = 128;
    public static final int MURFIN_OUTDOOR_DOOR = 64;
    public static final int MURFIN_INDOOR_DOOR = 192;
    public static final int FIGTHO_STERYL = 1;
    public static final int MOBO_STERYL = 2;
    public static final int IE_STERYL = 4;
    public static final int GAP = 8;
    public static final int JUMP = 16;
    public byte m_cost;
    public byte m_murfinInfo;
    public byte m_miscProperties;
    
    public CellPathData() {
        super();
        this.m_murfinInfo = 0;
    }
    
    public CellPathData(final CellPathData cellPathData) {
        super();
        this.m_murfinInfo = 0;
        this.set(cellPathData);
    }
    
    @Deprecated
    public static CellPathData[] createCellPathDataTab() {
        final CellPathData[] cellPathData = new CellPathData[32];
        for (int i = 0; i < cellPathData.length; ++i) {
            cellPathData[i] = new CellPathData();
        }
        return cellPathData;
    }
    
    public static short getZIndex(final CellPathData[] cellPathData, final TopologyMap topologyMap, final int x, final int y, final short z) {
        short sourceIndex = -1;
        final int numZ = topologyMap.getPathData(x, y, cellPathData, 0);
        if (numZ == 1) {
            if (cellPathData[0].m_z == z) {
                sourceIndex = 0;
            }
        }
        else {
            for (int i = 0; i < numZ; ++i) {
                if (cellPathData[i].m_z == z && !cellPathData[i].m_hollow) {
                    sourceIndex = (short)i;
                    break;
                }
            }
        }
        return sourceIndex;
    }
    
    public void set(final CellPathData cellPathData) {
        this.m_x = cellPathData.m_x;
        this.m_y = cellPathData.m_y;
        this.m_z = cellPathData.m_z;
        this.m_height = cellPathData.m_height;
        this.m_hollow = cellPathData.m_hollow;
        this.m_cost = cellPathData.m_cost;
        this.m_murfinInfo = cellPathData.m_murfinInfo;
        this.m_miscProperties = cellPathData.m_miscProperties;
    }
    
    @Override
    public String toString() {
        return new StringBuilder(28).append("CellPathData(").append(this.m_x).append(',').append(this.m_y).append(',').append(this.m_z).append(',').append(this.m_height).append(')').toString();
    }
    
    public int getMurfinType() {
        return this.m_murfinInfo & 0xC0;
    }
    
    public int getMurfinInfo() {
        return this.m_murfinInfo;
    }
    
    public int getTransition() {
        return this.m_murfinInfo & 0x30;
    }
    
    public int getGroupInfo() {
        return this.m_murfinInfo & 0xFFFFFF30;
    }
    
    public int getGroupId() {
        return this.m_murfinInfo & 0xF;
    }
    
    public boolean isFightoSteryl() {
        return (this.m_miscProperties & 0x1) == 0x1;
    }
    
    public boolean isMoboSteryl() {
        return (this.m_miscProperties & 0x2) == 0x2;
    }
    
    public boolean isIESteryl() {
        return (this.m_miscProperties & 0x4) == 0x4;
    }
    
    public boolean isGap() {
        return (this.m_miscProperties & 0x8) == 0x8;
    }
    
    public boolean isJump() {
        return (this.m_miscProperties & 0x10) == 0x10;
    }
    
    public static boolean isIndoor(final int murfinInfo) {
        return (murfinInfo & 0x80) == 0x80;
    }
    
    public static boolean isDoor(final int murfinInfo) {
        return (murfinInfo & 0x40) == 0x40;
    }
    
    @Override
    public CellData createMerged(final CellData c) {
        final CellPathData result = new CellPathData();
        CellData.merge(this, c, result);
        final CellPathData cellPathData = (CellPathData)((c.m_z >= this.m_z) ? c : this);
        result.m_cost = cellPathData.m_cost;
        result.m_murfinInfo = cellPathData.m_murfinInfo;
        result.m_miscProperties = cellPathData.m_miscProperties;
        return result;
    }
}
