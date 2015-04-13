package com.ankamagames.framework.kernel.core.maths;

import java.util.*;

class SegmentIterator implements Iterator<Point3>
{
    private final Segment m_segment;
    private final boolean m_onXAxis;
    private final int m_constantCoord;
    private final int m_maxCoord;
    private int m_nextCoord;
    
    public SegmentIterator(final Segment segment) {
        super();
        this.m_segment = segment;
        this.m_onXAxis = segment.isOnXAxis();
        if (this.m_onXAxis) {
            this.m_nextCoord = segment.getStart().getX();
            this.m_maxCoord = segment.getEnd().getX();
            this.m_constantCoord = segment.getEnd().getY();
        }
        else {
            this.m_nextCoord = segment.getStart().getY();
            this.m_maxCoord = segment.getEnd().getY();
            this.m_constantCoord = segment.getEnd().getX();
        }
    }
    
    @Override
    public boolean hasNext() {
        return this.m_nextCoord <= this.m_maxCoord;
    }
    
    @Override
    public Point3 next() {
        if (this.m_nextCoord > this.m_maxCoord) {
            throw new NoSuchElementException();
        }
        if (this.m_onXAxis) {
            return new Point3(this.m_nextCoord++, this.m_constantCoord);
        }
        return new Point3(this.m_constantCoord, this.m_nextCoord++);
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
