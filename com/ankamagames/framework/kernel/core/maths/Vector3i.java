package com.ankamagames.framework.kernel.core.maths;

public class Vector3i
{
    private int m_x;
    private int m_y;
    private int m_z;
    
    public Vector3i() {
        this(0, 0, 0);
    }
    
    public Vector3i(final Vector3i v) {
        this(v.m_x, v.m_y, v.m_z);
    }
    
    public Vector3i(final int[] v) {
        this(v[0], v[1], v[2]);
    }
    
    public Vector3i(final Point3 startPoint, final Point3 endPoint) {
        super();
        this.m_x = endPoint.getX() - startPoint.getX();
        this.m_y = endPoint.getY() - startPoint.getY();
        this.m_z = endPoint.getZ() - startPoint.getZ();
    }
    
    public Vector3i(final int x, final int y, final int z) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public Vector3i(final int startX, final int startY, final int startZ, final int endX, final int endY, final int endZ) {
        super();
        this.m_x = endX - startX;
        this.m_y = endY - startY;
        this.m_z = endZ - startZ;
    }
    
    public void set(final int[] coords) {
        this.m_x = coords[0];
        this.m_y = coords[1];
        this.m_z = coords[2];
    }
    
    public int getX() {
        return this.m_x;
    }
    
    public void setX(final int x) {
        this.m_x = x;
    }
    
    public int getY() {
        return this.m_y;
    }
    
    public void setY(final int y) {
        this.m_y = y;
    }
    
    public int getZ() {
        return this.m_z;
    }
    
    public void setZ(final int z) {
        this.m_z = z;
    }
    
    public Vector3i add(final Vector3i v) {
        return new Vector3i(v.m_x + this.m_x, v.m_y + this.m_y, v.m_z + this.m_z);
    }
    
    public Vector3i sub(final Vector3i v) {
        return new Vector3i(this.m_x - v.m_x, this.m_y - v.m_y, this.m_z - v.m_z);
    }
    
    public Vector3i mul(final Vector3i v) {
        return new Vector3i(this.m_x * v.m_x + this.m_x * v.m_y + this.m_x * v.m_z, this.m_y * v.m_x + this.m_y * v.m_y + this.m_y * v.m_z, this.m_z * v.m_x + this.m_z * v.m_y + this.m_z * v.m_z);
    }
    
    public Vector3i mul(final int s) {
        return new Vector3i(s * this.m_x, s * this.m_y, s * this.m_z);
    }
    
    public Vector3i mulD(final float d) {
        return new Vector3i((int)(d * this.m_x), (int)(d * this.m_y), (int)(d * this.m_z));
    }
    
    public float det(final Vector3i v) {
        return this.m_x * v.m_y + this.m_y * v.m_z + this.m_z * v.m_x - v.m_x * this.m_y - v.m_y * this.m_z - v.m_z * this.m_x;
    }
    
    public float dot(final Vector3i v) {
        return this.m_x * v.m_x + this.m_y * v.m_y + this.m_z * v.m_z;
    }
    
    public int sqrLength() {
        return this.m_x * this.m_x + this.m_y * this.m_y + this.m_z * this.m_z;
    }
    
    public int length() {
        final int l = this.m_x * this.m_x + this.m_y * this.m_y + this.m_z * this.m_z;
        return MathHelper.isqrt(l);
    }
    
    public Vector3i normalize() {
        final int l = this.length();
        return this.mul(1 / l);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Vector3i)) {
            return false;
        }
        final Vector3i v = (Vector3i)obj;
        return v.m_x == this.m_x && v.m_y == this.m_y && v.m_z == this.m_z;
    }
    
    @Override
    public String toString() {
        return "[" + this.m_x + " ; " + this.m_y + " ; " + this.m_z + "]";
    }
    
    public static Direction8 getDirection8FromVector(final float vx, final float vy) {
        return Vector3.getDirection8FromVector(vx, vy);
    }
    
    public Direction8 toDirection8() {
        return Vector3.getDirection8FromVector(this.m_x, this.m_y);
    }
    
    public static Direction8 getDirection4FromVector(final float vx, final float vy) {
        return Vector3.getDirection4FromVector(vx, vy);
    }
    
    public Direction8 toDirection4() {
        return Vector3.getDirection4FromVector(this.m_x, this.m_y);
    }
    
    public Direction8 toDirection4Prefering(final Direction8 preferedDirection4) {
        int xPlusY = this.m_x + this.m_y;
        int xMinusY = this.m_x - this.m_y;
        final int dx = preferedDirection4.m_x;
        final int dy = preferedDirection4.m_y;
        if (xPlusY == 0) {
            xPlusY = dx + dy;
        }
        if (xMinusY == 0) {
            xMinusY = dx - dy;
        }
        if (xPlusY > 0) {
            if (xMinusY > 0) {
                return Direction8.SOUTH_EAST;
            }
            return Direction8.SOUTH_WEST;
        }
        else {
            if (xMinusY > 0) {
                return Direction8.NORTH_EAST;
            }
            return Direction8.NORTH_WEST;
        }
    }
    
    public Direction8 toDirection4PreferingIfNulVector(final Direction8 preferedDirection4) {
        if (this.m_x == 0 && this.m_y == 0) {
            return Direction8.getDirectionFromIndex(preferedDirection4.m_index);
        }
        return this.toDirection4();
    }
    
    @Override
    public int hashCode() {
        assert false : "Il n'est pas pr\u00e9vu que ces objets comparables servent de clef dans une HashTable/HashMap.";
        return super.hashCode();
    }
}
