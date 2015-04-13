package com.ankamagames.framework.kernel.core.maths;

import org.apache.log4j.*;
import java.util.*;

public class Segment implements Iterable<Point3>
{
    public static final Logger m_logger;
    private final Point3 m_start;
    private final Point3 m_end;
    
    public Segment(final Point3 start, final Point3 end) {
        super();
        if (!start.isAlignedWith(end)) {
            throw new IllegalArgumentException("Can't create a segment from unaligned points : " + start + ", " + end);
        }
        if (start.equalsIgnoringAltitude(end)) {
            throw new IllegalArgumentException("Can't create a segment from a point : " + start + ", " + end);
        }
        final long startPos = pointPos(start);
        final long endPos = pointPos(end);
        if (startPos < endPos) {
            this.m_start = new Point3(start);
            this.m_end = new Point3(end);
        }
        else {
            this.m_start = new Point3(end);
            this.m_end = new Point3(start);
        }
    }
    
    public Segment(final Segment segmentToCopy) {
        this(segmentToCopy.m_start, segmentToCopy.m_end);
    }
    
    @Override
    public Iterator<Point3> iterator() {
        return new SegmentIterator(this);
    }
    
    public boolean isOnXAxis() {
        return this.m_start.getY() == this.m_end.getY();
    }
    
    public boolean isOnYAxis() {
        return this.m_start.getX() == this.m_end.getX();
    }
    
    public boolean contains(final Point3 pt) {
        return this.getRelativePosition(pt) == RelativePosition.INSIDE;
    }
    
    public RelativePosition getRelativePosition(final Point3 pt) {
        if (pt == null) {
            return RelativePosition.NOT_ALIGNED;
        }
        if (!this.isOnSameLine(pt)) {
            return RelativePosition.NOT_ALIGNED;
        }
        final long ptPos = pointPos(pt);
        if (ptPos < pointPos(this.m_start)) {
            return RelativePosition.BEFORE;
        }
        if (ptPos > pointPos(this.m_end)) {
            return RelativePosition.AFTER;
        }
        return RelativePosition.INSIDE;
    }
    
    public boolean isOnSameLine(final Point3 pt) {
        return pt != null && (this.m_start.equalsIgnoringAltitude(pt) || this.m_end.equalsIgnoringAltitude(pt) || this.m_start.isAlignedWith(this.m_end, pt));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Segment segment = (Segment)o;
        Label_0062: {
            if (this.m_end != null) {
                if (this.m_end.equalsIgnoringAltitude(segment.m_end)) {
                    break Label_0062;
                }
            }
            else if (segment.m_end == null) {
                break Label_0062;
            }
            return false;
        }
        if (this.m_start != null) {
            if (this.m_start.equalsIgnoringAltitude(segment.m_start)) {
                return true;
            }
        }
        else if (segment.m_start == null) {
            return true;
        }
        return false;
    }
    
    public boolean canBeExpandedTo(final Point3 pt) {
        final RelativePosition relativePosition = this.getRelativePosition(pt);
        return relativePosition == RelativePosition.AFTER || relativePosition == RelativePosition.BEFORE;
    }
    
    public boolean expandTo(final Point3 pt) {
        final RelativePosition relativePosition = this.getRelativePosition(pt);
        switch (relativePosition) {
            case BEFORE: {
                this.m_start.set(pt);
                return true;
            }
            case AFTER: {
                this.m_end.set(pt);
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public Point3 getStart() {
        return new Point3(this.m_start);
    }
    
    public Point3 getEnd() {
        return new Point3(this.m_end);
    }
    
    public int getSize() {
        return (int)(pointPos(this.m_end) - pointPos(this.m_start) + 1L);
    }
    
    @Override
    public int hashCode() {
        int result = (this.m_start != null) ? this.m_start.hashCodeIgnoringAltitude() : 0;
        result = 31 * result + ((this.m_end != null) ? this.m_end.hashCodeIgnoringAltitude() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "{Segment (" + this.m_start + ")-(" + this.m_end + ")}";
    }
    
    private static long pointPos(final Point3 p) {
        return p.getX() + p.getY();
    }
    
    static {
        m_logger = Logger.getLogger((Class)Segment.class);
    }
    
    public enum RelativePosition
    {
        NOT_ALIGNED, 
        INSIDE, 
        BEFORE, 
        AFTER;
    }
}
