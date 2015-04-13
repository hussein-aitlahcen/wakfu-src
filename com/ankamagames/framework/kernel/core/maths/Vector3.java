package com.ankamagames.framework.kernel.core.maths;

public class Vector3
{
    private static final float PI = 3.1415927f;
    private static final float PI_8 = 0.3926991f;
    private static final float PI_8_3 = 1.1780972f;
    private static final float PI_8_5 = 1.9634955f;
    private static final float PI_8_7 = 2.7488937f;
    private static final float PI_4 = 0.7853982f;
    private static final float PI_4_3 = 2.3561945f;
    public float m_x;
    public float m_y;
    public float m_z;
    public static final Vector3 AXIS_X;
    public static final Vector3 AXIS_Y;
    public static final Vector3 AXIS_Z;
    public static final Vector3 ZERO;
    
    public Vector3() {
        this(0.0f, 0.0f, 0.0f);
    }
    
    public Vector3(final Vector3 v) {
        this(v.m_x, v.m_y, v.m_z);
    }
    
    public Vector3(final Point3 start, final Point3 end) {
        this(end.getX() - start.getX(), end.getY() - start.getY(), end.getZ() - start.getZ());
    }
    
    public Vector3(final float[] v) {
        this(v[0], v[1], v[2]);
    }
    
    public Vector3(final int[] v) {
        this(v[0], v[1], v[2]);
    }
    
    public Vector3(final float x, final float y, final float z) {
        super();
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public Vector3(final int startX, final int startY, final short startZ, final int endX, final int endY, final short endZ) {
        super();
        this.m_x = endX - startX;
        this.m_y = endY - startY;
        this.m_z = endZ - startZ;
    }
    
    public float getX() {
        return this.m_x;
    }
    
    public void setX(final float x) {
        this.m_x = x;
    }
    
    public float getY() {
        return this.m_y;
    }
    
    public void setY(final float y) {
        this.m_y = y;
    }
    
    public float getZ() {
        return this.m_z;
    }
    
    public void setZ(final float z) {
        this.m_z = z;
    }
    
    public void set(final float x, final float y, final float z) {
        this.m_x = x;
        this.m_y = y;
        this.m_z = z;
    }
    
    public void set(final int[] v) {
        this.m_x = v[0];
        this.m_y = v[1];
        this.m_z = v[2];
    }
    
    public void setCurrent(final Vector3 v) {
        if (v == null) {
            return;
        }
        this.m_x = v.m_x;
        this.m_y = v.m_y;
        this.m_z = v.m_z;
    }
    
    public void addCurrent(final Vector3 v) {
        if (v == null) {
            return;
        }
        this.m_x += v.m_x;
        this.m_y += v.m_y;
        this.m_z += v.m_z;
    }
    
    public void subCurrent(final Vector3 v) {
        if (v == null) {
            return;
        }
        this.m_x -= v.m_x;
        this.m_y -= v.m_y;
        this.m_z -= v.m_z;
    }
    
    public void mulCurrent(final double scale) {
        this.m_x *= (float)scale;
        this.m_y *= (float)scale;
        this.m_z *= (float)scale;
    }
    
    public boolean cropSymCurrent(final float xMax, final float yMax, final float zMax) {
        final boolean x = this.cropSymXCurrent(xMax);
        final boolean y = this.cropSymYCurrent(yMax);
        final boolean z = this.cropSymZCurrent(zMax);
        return x || y || z;
    }
    
    public boolean cropSymXCurrent(final float xMax) {
        if (-xMax <= this.m_x && this.m_x <= xMax) {
            return false;
        }
        final float absXMax = Math.abs(xMax);
        final float absX = Math.abs(this.m_x);
        this.m_z = this.m_z * absXMax / absX;
        this.m_y = this.m_y * absXMax / absX;
        this.m_x = ((this.m_x > xMax) ? xMax : (-xMax));
        return true;
    }
    
    public boolean cropSymYCurrent(final float yMax) {
        if (-yMax <= this.m_y && this.m_y <= yMax) {
            return false;
        }
        final float absYMax = Math.abs(yMax);
        final float absY = Math.abs(this.m_y);
        this.m_z = this.m_z * absYMax / absY;
        this.m_x = this.m_x * absYMax / absY;
        this.m_y = ((this.m_y > yMax) ? yMax : (-yMax));
        return true;
    }
    
    public boolean cropSymZCurrent(final float zMax) {
        if (-zMax <= this.m_z && this.m_z <= zMax) {
            return false;
        }
        final float absZMax = Math.abs(zMax);
        final float absZ = Math.abs(this.m_z);
        this.m_y = this.m_y * absZMax / absZ;
        this.m_x = this.m_x * absZMax / absZ;
        this.m_z = ((this.m_z > zMax) ? zMax : (-zMax));
        return true;
    }
    
    public Vector3 add(final Vector3 v) {
        return new Vector3(v.m_x + this.m_x, v.m_y + this.m_y, v.m_z + this.m_z);
    }
    
    public Vector3 sub(final Vector3 v) {
        return new Vector3(this.m_x - v.m_x, this.m_y - v.m_y, this.m_z - v.m_z);
    }
    
    public Vector3 mul(final Vector3 v) {
        return new Vector3(this.m_x * v.m_x + this.m_x * v.m_y + this.m_x * v.m_z, this.m_y * v.m_x + this.m_y * v.m_y + this.m_y * v.m_z, this.m_z * v.m_x + this.m_z * v.m_y + this.m_z * v.m_z);
    }
    
    public Vector3 mul(final float s) {
        return new Vector3(s * this.m_x, s * this.m_y, s * this.m_z);
    }
    
    public float dot(final Vector3 v) {
        return this.m_x * v.m_x + this.m_y * v.m_y + this.m_z * v.m_z;
    }
    
    public float dot(final Vector3i v) {
        return this.m_x * v.getX() + this.m_y * v.getY() + this.m_z * v.getZ();
    }
    
    public float det(final Vector3 v) {
        return this.m_x * v.m_y + this.m_y * v.m_z + this.m_z * v.m_x - v.m_x * this.m_y - v.m_y * this.m_z - v.m_z * this.m_x;
    }
    
    public float sqrLength() {
        return this.m_x * this.m_x + this.m_y * this.m_y + this.m_z * this.m_z;
    }
    
    public float length() {
        return (float)Math.sqrt(this.m_x * this.m_x + this.m_y * this.m_y + this.m_z * this.m_z);
    }
    
    public float length2D() {
        return Vector2.length(this.m_x, this.m_y);
    }
    
    public Vector3 normalize() {
        final float l = this.length();
        if (l == 0.0f) {
            return new Vector3(this.m_x, this.m_y, this.m_z);
        }
        return this.mul(1.0f / l);
    }
    
    public void normalizeCurrent() {
        final float l = this.length();
        if (l == 0.0f) {
            return;
        }
        this.mulCurrent(1.0f / l);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof Vector3)) {
            return false;
        }
        final Vector3 v = (Vector3)obj;
        return v.m_x == this.m_x && v.m_y == this.m_y && v.m_z == this.m_z;
    }
    
    @Override
    public String toString() {
        return "[" + this.m_x + " ; " + this.m_y + " ; " + this.m_z + "]";
    }
    
    public static Direction8 getDirection8FromVector(final float vx, final float vy) {
        final float a = -(float)Math.atan2(vy, vx);
        Direction8 direction;
        if (a < 2.7488937f && a >= 1.9634955f) {
            direction = Direction8.NORTH;
        }
        else if (a < 1.9634955f && a >= 1.1780972f) {
            direction = Direction8.NORTH_EAST;
        }
        else if (a < 1.1780972f && a >= 0.3926991f) {
            direction = Direction8.EAST;
        }
        else if (a < 0.3926991f && a >= -0.3926991f) {
            direction = Direction8.SOUTH_EAST;
        }
        else if (a < -0.3926991f && a >= -1.1780972f) {
            direction = Direction8.SOUTH;
        }
        else if (a < -1.1780972f && a >= -1.9634955f) {
            direction = Direction8.SOUTH_WEST;
        }
        else if (a < -1.9634955f && a >= -2.7488937f) {
            direction = Direction8.WEST;
        }
        else {
            direction = Direction8.NORTH_WEST;
        }
        return direction;
    }
    
    public Direction8 toDirection8() {
        return getDirection8FromVector(this.m_x, this.m_y);
    }
    
    public static Direction8 getDirection4FromVector(final float vx, final float vy) {
        if (vx == 0.0f && vy == 0.0f) {
            return Direction8.NONE;
        }
        final float a = -(float)Math.atan2(vy, vx);
        Direction8 direction;
        if (a < 2.3561945f && a >= 0.7853982f) {
            direction = Direction8.NORTH_EAST;
        }
        else if (a < 0.7853982f && a >= -0.7853982f) {
            direction = Direction8.SOUTH_EAST;
        }
        else if (a < -0.7853982f && a >= -2.3561945f) {
            direction = Direction8.SOUTH_WEST;
        }
        else {
            direction = Direction8.NORTH_WEST;
        }
        return direction;
    }
    
    public Direction8 toDirection4() {
        return getDirection4FromVector(this.m_x, this.m_y);
    }
    
    public Vector3 opposite() {
        return new Vector3(-this.m_x, -this.m_y, -this.m_z);
    }
    
    @Override
    public int hashCode() {
        assert false : "Il n'est pas pr\u00e9vu que ces objets comparables servent de clef dans une HashTable/HashMap.";
        return super.hashCode();
    }
    
    static {
        AXIS_X = new Vector3(1.0f, 0.0f, 0.0f);
        AXIS_Y = new Vector3(0.0f, 1.0f, 0.0f);
        AXIS_Z = new Vector3(0.0f, 0.0f, 1.0f);
        ZERO = new Vector3();
    }
}
