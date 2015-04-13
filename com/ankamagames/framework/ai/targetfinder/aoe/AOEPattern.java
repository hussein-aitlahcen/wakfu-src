package com.ankamagames.framework.ai.targetfinder.aoe;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class AOEPattern
{
    private final Set<Point3> m_normalizedCells;
    private final boolean m_invariantByRotation;
    private static final Logger m_logger;
    private final Point3 m_tmpPoint3;
    private final Point3 m_anotherTmpPoint3;
    
    public AOEPattern(final Iterable<int[]> normalizedCells, final boolean invariantByRotation) {
        super();
        this.m_normalizedCells = new LinkedHashSet<Point3>();
        this.m_tmpPoint3 = new Point3();
        this.m_anotherTmpPoint3 = new Point3();
        for (final int[] cell : normalizedCells) {
            this.m_normalizedCells.add(new Point3(cell[0], cell[1]));
        }
        this.m_invariantByRotation = invariantByRotation;
    }
    
    public Iterable<int[]> apply(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final Direction8 sourceOrientation) {
        final AOETransform transform = this.aoeTransform(cellCenterX, cellCenterY, cellSourceX, cellSourceY, sourceOrientation);
        final List<int[]> result = new ArrayList<int[]>(this.m_normalizedCells.size());
        for (final Point3 point : this.m_normalizedCells) {
            result.add(transform.apply(point.getX(), point.getY()));
        }
        return result;
    }
    
    public boolean isPointInside(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final Direction8 sourceOrientation, final int cellQueriedX, final int cellQueriedY, final short cellQueriedAltitude) {
        final AOETransform transform = this.aoeTransform(cellCenterX, cellCenterY, cellSourceX, cellSourceY, sourceOrientation);
        this.m_tmpPoint3.set(transform.applyInvert(cellQueriedX, cellQueriedY));
        return this.m_normalizedCells.contains(this.m_tmpPoint3);
    }
    
    public boolean isAreaInside(final int cellCenterX, final int cellCenterY, final short cellCenterAltitude, final int cellSourceX, final int cellSourceY, final short cellSourceAltitude, final Direction8 sourceOrientation, final int areaQueriedCenterX, final int areaQueriedCenterY, final short cellQueriedAltitude, final byte areaQueriedRadiusSize) {
        final AOETransform transform = this.aoeTransform(cellCenterX, cellCenterY, cellSourceX, cellSourceY, sourceOrientation);
        this.m_tmpPoint3.set(transform.applyInvert(areaQueriedCenterX, areaQueriedCenterY));
        if (areaQueriedRadiusSize <= 0) {
            return this.m_normalizedCells.contains(this.m_tmpPoint3);
        }
        for (int x = -areaQueriedRadiusSize; x <= areaQueriedRadiusSize; ++x) {
            for (int y = -areaQueriedRadiusSize; y <= areaQueriedRadiusSize; ++y) {
                this.m_anotherTmpPoint3.set(this.m_tmpPoint3.getX() + x, this.m_tmpPoint3.getY() + y, (short)0);
                if (this.m_normalizedCells.contains(this.m_anotherTmpPoint3)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private AOETransform aoeTransform(final int cellCenterX, final int cellCenterY, final int cellSourceX, final int cellSourceY, final Direction8 sourceOrientation) {
        final Direction8 rotationDirection = new Vector3i(cellCenterX - cellSourceX, cellCenterY - cellSourceY, 0).toDirection4PreferingIfNulVector(sourceOrientation);
        return AOETransform.aoeTransform(cellCenterX, cellCenterY, rotationDirection, this.m_invariantByRotation);
    }
    
    static {
        m_logger = Logger.getLogger((Class)AOEPattern.class);
    }
}
