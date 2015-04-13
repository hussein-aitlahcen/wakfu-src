package com.ankamagames.framework.ai;

import com.ankamagames.framework.kernel.core.maths.*;

public final class DistanceUtils
{
    public static int getBordersDistance(final PhysicalRadiusOwner p1, final PhysicalRadiusOwner p2) {
        return Math.max(0, getIntersectionDistance(p1, p2) - 1);
    }
    
    public static int getCentersDistance(final PhysicalRadiusOwner p1, final PhysicalRadiusOwner p2) {
        return Math.abs(p1.getWorldCellX() - p2.getWorldCellX()) + Math.abs(p1.getWorldCellY() - p2.getWorldCellY());
    }
    
    public static int getIntersectionDistance(final PhysicalRadiusOwner p1, final PhysicalRadiusOwner p2) {
        final int radiusSum = p1.getPhysicalRadius() + p2.getPhysicalRadius();
        final int distanceX = Math.max(0, Math.abs(p1.getWorldCellX() - p2.getWorldCellX()) - radiusSum);
        final int distanceY = Math.max(0, Math.abs(p1.getWorldCellY() - p2.getWorldCellY()) - radiusSum);
        return distanceX + distanceY;
    }
    
    public static int getBorderDistance(final PhysicalRadiusOwner p, final Point3 pos) {
        return Math.max(0, getIntersectionDistance(p, pos) - 1);
    }
    
    public static int getCenterDistance(final PhysicalRadiusOwner p, final Point3 pos) {
        return Math.abs(p.getWorldCellX() - pos.getX()) + Math.abs(p.getWorldCellY() - pos.getY());
    }
    
    public static int getIntersectionDistance(final PhysicalRadiusOwner p, final Point3 pos) {
        final int distanceX = Math.max(0, Math.abs(p.getWorldCellX() - pos.getX()) - p.getPhysicalRadius());
        final int distanceY = Math.max(0, Math.abs(p.getWorldCellY() - pos.getY()) - p.getPhysicalRadius());
        return distanceX + distanceY;
    }
    
    public static int getIntersectionDistance(final PhysicalRadiusOwner p, final int posX, final int posY) {
        final int distanceX = Math.max(0, Math.abs(p.getWorldCellX() - posX) - p.getPhysicalRadius());
        final int distanceY = Math.max(0, Math.abs(p.getWorldCellY() - posY) - p.getPhysicalRadius());
        return distanceX + distanceY;
    }
}
