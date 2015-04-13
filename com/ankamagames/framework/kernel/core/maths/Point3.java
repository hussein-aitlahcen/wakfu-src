package com.ankamagames.framework.kernel.core.maths;

import org.apache.log4j.*;
import org.jetbrains.annotations.*;

public class Point3
{
    private static final Logger m_logger;
    private int m_x;
    private int m_y;
    private short m_z;
    
    public Point3() {
        super();
    }
    
    public Point3(final Point3 p) {
        super();
        this.set(p.m_x, p.m_y, p.m_z);
    }
    
    public Point3(final int[] coords) {
        super();
        this.set(coords);
    }
    
    public Point3(final int x, final int y, final short z) {
        super();
        this.set(x, y, z);
    }
    
    public Point3(final Point3 origin, final Vector3i vector) {
        super();
        this.set(origin.m_x + vector.getX(), origin.m_y + vector.getY(), (short)(origin.m_z + vector.getZ()));
    }
    
    public Point3(final int x, final int y) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_z = 0;
    }
    
    public Point2 toPoint2() {
        return new Point2(this.m_x, this.m_y);
    }
    
    public Point3 multiplicateXY(final int factor) {
        this.m_x *= factor;
        this.m_y *= factor;
        return this;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Point3)) {
            return false;
        }
        final Point3 point3 = (Point3)o;
        return this.m_x == point3.m_x && this.m_y == point3.m_y && this.m_z == point3.m_z;
    }
    
    @Override
    public int hashCode() {
        long bits = 1L;
        bits = 31L * bits + (this.m_x & 0xFF);
        bits = 31L * bits + (this.m_x >> 8 & 0xFF);
        bits = 31L * bits + (this.m_x >> 16 & 0xFF);
        bits = 31L * bits + (this.m_x >> 24 & 0xFF);
        bits = 31L * bits + (this.m_y & 0xFF);
        bits = 31L * bits + (this.m_y >> 8 & 0xFF);
        bits = 31L * bits + (this.m_y >> 16 & 0xFF);
        bits = 31L * bits + (this.m_y >> 24 & 0xFF);
        bits = 31L * bits + (this.m_z & 0xFF);
        bits = 31L * bits + (this.m_z >> 8 & 0xFF);
        bits = 31L * bits + (this.m_z >> 16 & 0xFF);
        bits = 31L * bits + (this.m_z >> 24 & 0xFF);
        return (int)(bits ^ bits >> 32);
    }
    
    public int hashCodeIgnoringAltitude() {
        long bits = 1L;
        bits = 31L * bits + (this.m_x & 0xFF);
        bits = 31L * bits + (this.m_x >> 8 & 0xFF);
        bits = 31L * bits + (this.m_x >> 16 & 0xFF);
        bits = 31L * bits + (this.m_x >> 24 & 0xFF);
        bits = 31L * bits + (this.m_y & 0xFF);
        bits = 31L * bits + (this.m_y >> 8 & 0xFF);
        bits = 31L * bits + (this.m_y >> 16 & 0xFF);
        bits = 31L * bits + (this.m_y >> 24 & 0xFF);
        return (int)(bits ^ bits >> 32);
    }
    
    @Override
    public String toString() {
        return "{Point3 : (" + this.m_x + ", " + this.m_y + ", " + this.m_z + ") @" + Integer.toHexString(this.hashCode()) + "}";
    }
    
    public void add(final int x, final int y, final int z) {
        this.m_x += x;
        this.m_y += y;
        this.m_z += (short)z;
    }
    
    public void add(final Point3 p) {
        this.m_x += p.m_x;
        this.m_y += p.m_y;
        this.m_z += p.m_z;
    }
    
    public void add(final int x, final int y) {
        this.m_x += x;
        this.m_y += y;
    }
    
    public boolean equals(final int x, final int y) {
        return this.m_x == x && this.m_y == y;
    }
    
    public boolean equals(final int x, final int y, final int z) {
        return this.m_x == x && this.m_y == y && this.m_z == z;
    }
    
    public boolean equalsIgnoringAltitude(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Point3 point3 = (Point3)o;
        return this.m_x == point3.m_x && this.m_y == point3.m_y;
    }
    
    @Nullable
    public Direction8 getDirectionTo(final Point3 position) {
        int diffX = position.m_x - this.m_x;
        diffX = ((diffX == 0) ? 0 : ((diffX > 0) ? 1 : -1));
        int diffY = position.m_y - this.m_y;
        diffY = ((diffY == 0) ? 0 : ((diffY > 0) ? 1 : -1));
        return Direction8.getDirectionFromVector(diffX, diffY);
    }
    
    public Direction8 getDirectionTo(final int x, final int y, final short z) {
        int diffX = x - this.m_x;
        diffX = ((diffX == 0) ? 0 : ((diffX > 0) ? 1 : -1));
        int diffY = y - this.m_y;
        diffY = ((diffY == 0) ? 0 : ((diffY > 0) ? 1 : -1));
        return Direction8.getDirectionFromVector(diffX, diffY);
    }
    
    @Nullable
    public Direction8 getDirection4To(final Point3 position) {
        final int diffX = Math.abs(position.m_x - this.m_x);
        final int diffY = Math.abs(position.m_y - this.m_y);
        final Direction8 direction8 = this.getDirectionTo(position);
        return getDirection4To(diffX, diffY, direction8);
    }
    
    public static Direction8 getDirection4To(final int diffX, final int diffY, final Direction8 direction8) {
        if (direction8 == null) {
            return null;
        }
        if (direction8.isDirection4()) {
            return direction8;
        }
        switch (direction8) {
            case EAST: {
                return (diffX > diffY) ? Direction8.SOUTH_EAST : Direction8.NORTH_EAST;
            }
            case WEST: {
                return (diffX > diffY) ? Direction8.NORTH_WEST : Direction8.SOUTH_WEST;
            }
            case NORTH: {
                return (diffX > diffY) ? Direction8.NORTH_WEST : Direction8.NORTH_EAST;
            }
            case SOUTH: {
                return (diffX > diffY) ? Direction8.SOUTH_EAST : Direction8.SOUTH_WEST;
            }
            default: {
                return null;
            }
        }
    }
    
    public Direction8 expandedGetDirectionTo(final Point3 position) {
        int diffX = position.m_x - this.m_x;
        diffX = ((diffX == 0) ? diffX : (diffX / Math.abs(diffX)));
        int diffY = position.m_y - this.m_y;
        diffY = ((diffY == 0) ? diffY : (diffY / Math.abs(diffY)));
        return Direction8.getDirectionFromVector(diffX, diffY);
    }
    
    public int getDistance(final Point3 position) {
        return Math.abs(position.m_x - this.m_x) + Math.abs(position.m_y - this.m_y);
    }
    
    public int getDistance(final int x, final int y, final short z) {
        return Math.abs(x - this.m_x) + Math.abs(y - this.m_y);
    }
    
    public int getDistance(final int x, final int y) {
        return Math.abs(x - this.m_x) + Math.abs(y - this.m_y);
    }
    
    public int getDistance(final int[] position) {
        assert position != null && position.length >= 2;
        return Math.abs(position[0] - this.m_x) + Math.abs(position[1] - this.m_y);
    }
    
    public int getSpaceBetween(final Point3 position) {
        final int distance = this.getDistance(position);
        if (distance <= 1) {
            return 0;
        }
        return distance - 1;
    }
    
    public void reset() {
        this.m_x = 0;
        this.m_y = 0;
        this.m_z = 0;
    }
    
    public void set(final int[] coords) {
        if (coords.length > 1) {
            this.m_x = coords[0];
            this.m_y = coords[1];
            if (coords.length > 2) {
                this.m_z = (short)coords[2];
            }
            else {
                this.m_z = 0;
            }
            return;
        }
        throw new IllegalArgumentException("La longueur du tableau passe en parametre n'est pas adaptee : " + coords.length);
    }
    
    public void set(final int x, final int y, final short z) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public void set(final Point3 p) {
        this.m_x = p.m_x;
        this.m_y = p.m_y;
        this.m_z = p.m_z;
    }
    
    public void shift(final Direction8 direction) {
        this.m_x += direction.m_x;
        this.m_y += direction.m_y;
    }
    
    public void shift(final Vector3 v) {
        this.m_x += (int)v.getX();
        this.m_y += (int)v.getY();
        this.m_z += (short)v.getZ();
    }
    
    public void sub(final int x, final int y, final int z) {
        this.m_x -= x;
        this.m_y -= y;
        this.m_z -= (short)z;
    }
    
    public void sub(final Point3 p) {
        this.m_x -= p.m_x;
        this.m_y -= p.m_y;
        this.m_z -= p.m_z;
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public short getZ() {
        return this.m_z;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    public void setZ(final short z) {
        this.m_z = z;
    }
    
    public int[] toIntArray() {
        return new int[] { this.m_x, this.m_y, this.m_z };
    }
    
    public boolean isInVonNeumannNeighbourhood(final int x, final int y) {
        return (x == this.m_x && y == this.m_y) || (x == this.m_x - 1 && y == this.m_y) || (x == this.m_x && y == this.m_y - 1) || (x == this.m_x + 1 && y == this.m_y) || (x == this.m_x && y == this.m_y + 1);
    }
    
    public boolean isInMooreNeighborhood(final int x, final int y) {
        return this.isInVonNeumannNeighbourhood(x, y) || (x == this.m_x - 1 && y == this.m_y - 1) || (x == this.m_x + 1 && y == this.m_y + 1) || (x == this.m_x - 1 && y == this.m_y + 1) || (x == this.m_x + 1 && y == this.m_y - 1);
    }
    
    public boolean isAlignedWith(final Point3 p) {
        return p != null && !this.equals(p) && (this.m_x == p.m_x || this.m_y == p.m_y);
    }
    
    public boolean isAlignedWith(final Point3 p1, final Point3 p2) {
        return p1 != null && p2 != null && !this.equals(p1) && !this.equals(p2) && ((this.m_x == p1.m_x && this.m_x == p2.m_x) || (this.m_y == p1.m_y && this.m_y == p2.m_y));
    }
    
    static {
        m_logger = Logger.getLogger((Class)Point3.class);
    }
}
