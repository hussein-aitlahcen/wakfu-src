package com.ankamagames.wakfu.common.game.havenWorld;

class PartitionModel implements Partition
{
    private final short m_x;
    private final short m_y;
    private short m_topLeftPatch;
    private short m_topRightPatch;
    private short m_bottomLeftPatch;
    private short m_bottomRightPatch;
    
    PartitionModel(final short x, final short y) {
        super();
        this.m_x = x;
        this.m_y = y;
    }
    
    PartitionModel(final short x, final short y, final short topLeftPatch, final short topRightPatch, final short bottomLeftPatch, final short bottomRightPatch) {
        this(x, y);
        this.m_topLeftPatch = topLeftPatch;
        this.m_topRightPatch = topRightPatch;
        this.m_bottomLeftPatch = bottomLeftPatch;
        this.m_bottomRightPatch = bottomRightPatch;
    }
    
    @Override
    public short getX() {
        return this.m_x;
    }
    
    @Override
    public short getY() {
        return this.m_y;
    }
    
    @Override
    public short getTopLeftPatch() {
        return this.m_topLeftPatch;
    }
    
    @Override
    public short getTopRightPatch() {
        return this.m_topRightPatch;
    }
    
    @Override
    public short getBottomLeftPatch() {
        return this.m_bottomLeftPatch;
    }
    
    @Override
    public short getBottomRightPatch() {
        return this.m_bottomRightPatch;
    }
    
    public boolean modifyPatches(final short topLeftPatch, final short topRightPatch, final short bottomLeftPatch, final short bottomRightPatch) {
        final boolean changed = this.m_topLeftPatch != topLeftPatch || this.m_topRightPatch != topRightPatch || this.m_bottomLeftPatch != bottomLeftPatch || this.m_bottomRightPatch != bottomRightPatch;
        this.m_topLeftPatch = topLeftPatch;
        this.m_topRightPatch = topRightPatch;
        this.m_bottomLeftPatch = bottomLeftPatch;
        this.m_bottomRightPatch = bottomRightPatch;
        return changed;
    }
    
    @Override
    public String toString() {
        return "PartitionModel{m_x=" + this.m_x + ", m_y=" + this.m_y + '}';
    }
}
