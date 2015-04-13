package com.ankamagames.framework.kernel.core.maths;

import com.ankamagames.framework.kernel.core.common.*;
import org.apache.log4j.*;
import java.io.*;
import com.ankamagames.framework.fileFormat.io.*;

public final class Matrix44 extends MemoryObject
{
    public static final ObjectFactory Factory;
    private static final Logger m_logger;
    private static final float[] m_zeroFilledBuffer;
    public static final Matrix44 IDENTITY;
    private final float[] m_buffer;
    private boolean m_isIdentity;
    
    private Matrix44() {
        super();
        this.m_buffer = new float[16];
    }
    
    public Matrix44(final Matrix44 m) {
        super();
        this.m_buffer = new float[16];
        this.set(m);
    }
    
    public Matrix44(final float[] buffer) {
        super();
        this.m_buffer = new float[16];
        this.set(buffer);
    }
    
    public Matrix44(final Quaternion q) {
        super();
        this.m_buffer = new float[16];
        this.set(q);
    }
    
    public Matrix44(final Quaternion q, final Vector4 v) {
        super();
        this.m_buffer = new float[16];
        this.set(q, v);
    }
    
    public Matrix44(final Vector4 x, final Vector4 y, final Vector4 z, final Vector4 t) {
        super();
        this.m_buffer = new float[16];
        this.set(x, y, z, t);
    }
    
    public static Matrix44 getIdentity() {
        final Matrix44 m = new Matrix44();
        m.setIdentity();
        return m;
    }
    
    public void load(final ExtendedDataInputStream bitStream) throws IOException {
        for (int i = 0; i < this.m_buffer.length; ++i) {
            this.m_buffer[i] = bitStream.readFloat();
        }
        this.m_isIdentity = false;
    }
    
    public void save(final OutputBitStream bitStream) throws IOException {
        for (int i = 0; i < this.m_buffer.length; ++i) {
            bitStream.writeFloat(this.m_buffer[i]);
        }
    }
    
    public void set(final Matrix44 m) {
        this.m_isIdentity = m.m_isIdentity;
        System.arraycopy(m.m_buffer, 0, this.m_buffer, 0, this.m_buffer.length);
    }
    
    public void set(final float[] buffer) {
        assert buffer.length >= 16 : "Buffer length must be at least of 16 float";
        System.arraycopy(buffer, 0, this.m_buffer, 0, 16);
        this.m_isIdentity = false;
    }
    
    public void set(final Quaternion Q) {
        final float x2 = Q.getX() + Q.getX();
        final float y2 = Q.getY() + Q.getY();
        final float z2 = Q.getZ() + Q.getZ();
        final float xx = Q.getX() * x2;
        final float xy = Q.getX() * y2;
        final float xz = Q.getX() * z2;
        final float yy = Q.getY() * y2;
        final float yz = Q.getY() * z2;
        final float zz = Q.getZ() * z2;
        final float wx = Q.getW() * x2;
        final float wy = Q.getW() * y2;
        final float wz = Q.getW() * z2;
        this.m_buffer[0] = 1.0f - (yy + zz);
        this.m_buffer[1] = xy + wz;
        this.m_buffer[2] = xz - wy;
        this.m_buffer[3] = 0.0f;
        this.m_buffer[4] = xy - wz;
        this.m_buffer[5] = 1.0f - (xx + zz);
        this.m_buffer[6] = yz + wx;
        this.m_buffer[7] = 0.0f;
        this.m_buffer[8] = xz + wy;
        this.m_buffer[9] = yz - wx;
        this.m_buffer[10] = 1.0f - (xx + yy);
        this.m_buffer[11] = 0.0f;
        this.m_buffer[12] = 0.0f;
        this.m_buffer[13] = 0.0f;
        this.m_buffer[14] = 0.0f;
        this.m_buffer[15] = 1.0f;
        this.m_isIdentity = false;
    }
    
    public void set(final Quaternion q, final Vector4 v) {
        this.set(q);
        this.setTranslation(v);
        this.setHomogeneCoordinates();
        this.m_isIdentity = false;
    }
    
    public void set(final Quaternion q, final Vector4 v, final Vector4 scale) {
        this.set(q);
        this.setTranslation(v);
        this.setHomogeneCoordinates();
        final float scaleX = scale.getX();
        final float scaleY = scale.getY();
        final float scaleZ = scale.getZ();
        if (scaleX != 1.0f || scaleY != 1.0f || scaleZ != 1.0f) {
            final float[] buffer = this.m_buffer;
            final int n = 0;
            buffer[n] *= scaleX;
            final float[] buffer2 = this.m_buffer;
            final int n2 = 1;
            buffer2[n2] *= scaleX;
            final float[] buffer3 = this.m_buffer;
            final int n3 = 2;
            buffer3[n3] *= scaleX;
            final float[] buffer4 = this.m_buffer;
            final int n4 = 4;
            buffer4[n4] *= scaleY;
            final float[] buffer5 = this.m_buffer;
            final int n5 = 5;
            buffer5[n5] *= scaleY;
            final float[] buffer6 = this.m_buffer;
            final int n6 = 6;
            buffer6[n6] *= scaleY;
            final float[] buffer7 = this.m_buffer;
            final int n7 = 8;
            buffer7[n7] *= scaleZ;
            final float[] buffer8 = this.m_buffer;
            final int n8 = 9;
            buffer8[n8] *= scaleZ;
            final float[] buffer9 = this.m_buffer;
            final int n9 = 10;
            buffer9[n9] *= scaleZ;
        }
        this.m_isIdentity = false;
    }
    
    public void set(final Vector4 x, final Vector4 y, final Vector4 z, final Vector4 t) {
        this.setXAxis(x);
        this.setYAxis(y);
        this.setZAxis(z);
        this.setTranslation(t);
        this.m_isIdentity = false;
    }
    
    public void setXAxis(final Vector4 x) {
        this.m_buffer[0] = x.getX();
        this.m_buffer[1] = x.getY();
        this.m_buffer[2] = x.getZ();
        this.m_isIdentity = false;
    }
    
    public void setYAxis(final Vector4 y) {
        this.m_buffer[4] = y.getX();
        this.m_buffer[5] = y.getY();
        this.m_buffer[6] = y.getZ();
        this.m_isIdentity = false;
    }
    
    public void setZAxis(final Vector4 z) {
        this.m_buffer[8] = z.getX();
        this.m_buffer[9] = z.getY();
        this.m_buffer[10] = z.getZ();
        this.m_isIdentity = false;
    }
    
    public void setTranslation(final Vector4 t) {
        this.m_buffer[12] = t.getX();
        this.m_buffer[13] = t.getY();
        this.m_buffer[14] = t.getZ();
        this.m_isIdentity = false;
    }
    
    public void setTranslation(final float x, final float y, final float z) {
        this.m_buffer[12] = x;
        this.m_buffer[13] = y;
        this.m_buffer[14] = z;
        this.m_isIdentity = false;
    }
    
    public void translate(final Vector4 t) {
        final float[] buffer = this.m_buffer;
        final int n = 12;
        buffer[n] += t.getX();
        final float[] buffer2 = this.m_buffer;
        final int n2 = 13;
        buffer2[n2] += t.getY();
        final float[] buffer3 = this.m_buffer;
        final int n3 = 14;
        buffer3[n3] += t.getZ();
        this.m_isIdentity = false;
    }
    
    public void setScale(final float scaleX, final float scaleY, final float scaleZ) {
        this.setHomogeneCoordinates();
        if (scaleX != 1.0f || scaleY != 1.0f || scaleZ != 1.0f) {
            final float[] buffer = this.m_buffer;
            final int n = 0;
            buffer[n] *= scaleX;
            final float[] buffer2 = this.m_buffer;
            final int n2 = 1;
            buffer2[n2] *= scaleX;
            final float[] buffer3 = this.m_buffer;
            final int n3 = 2;
            buffer3[n3] *= scaleX;
            final float[] buffer4 = this.m_buffer;
            final int n4 = 4;
            buffer4[n4] *= scaleY;
            final float[] buffer5 = this.m_buffer;
            final int n5 = 5;
            buffer5[n5] *= scaleY;
            final float[] buffer6 = this.m_buffer;
            final int n6 = 6;
            buffer6[n6] *= scaleY;
            final float[] buffer7 = this.m_buffer;
            final int n7 = 8;
            buffer7[n7] *= scaleZ;
            final float[] buffer8 = this.m_buffer;
            final int n8 = 9;
            buffer8[n8] *= scaleZ;
            final float[] buffer9 = this.m_buffer;
            final int n9 = 10;
            buffer9[n9] *= scaleZ;
        }
    }
    
    public void setScale(final Vector4 scale) {
        this.setScale(scale.getX(), scale.getY(), scale.getZ());
    }
    
    public void setIdentity() {
        try {
            System.arraycopy(Matrix44.m_zeroFilledBuffer, 0, this.m_buffer, 0, 16);
        }
        catch (Exception e) {
            Matrix44.m_logger.error((Object)"Exception", (Throwable)e);
        }
        final float[] buffer = this.m_buffer;
        final int n = 0;
        final float[] buffer2 = this.m_buffer;
        final int n2 = 5;
        final float[] buffer3 = this.m_buffer;
        final int n3 = 10;
        final float[] buffer4 = this.m_buffer;
        final int n4 = 15;
        final float n5 = 1.0f;
        buffer3[n3] = (buffer4[n4] = n5);
        buffer[n] = (buffer2[n2] = n5);
        this.m_isIdentity = true;
    }
    
    public void transformVector(final Vector4 vector, final Vector4 transformedVector) {
        transformedVector.set(vector.getX() * this.m_buffer[0] + vector.getY() * this.m_buffer[4] + vector.getZ() * this.m_buffer[8], vector.getX() * this.m_buffer[1] + vector.getY() * this.m_buffer[5] + vector.getZ() * this.m_buffer[9], vector.getX() * this.m_buffer[2] + vector.getY() * this.m_buffer[6] + vector.getZ() * this.m_buffer[10], 0.0f);
    }
    
    public void transformPoint(final Vector4 vector, final Vector4 transformedVector) {
        transformedVector.set(vector.getX() * this.m_buffer[0] + vector.getY() * this.m_buffer[4] + vector.getZ() * this.m_buffer[8] + vector.getW() * this.m_buffer[12], vector.getX() * this.m_buffer[1] + vector.getY() * this.m_buffer[5] + vector.getZ() * this.m_buffer[9] + vector.getW() * this.m_buffer[13], vector.getX() * this.m_buffer[2] + vector.getY() * this.m_buffer[6] + vector.getZ() * this.m_buffer[10] + vector.getW() * this.m_buffer[14], vector.getX() * this.m_buffer[3] + vector.getY() * this.m_buffer[7] + vector.getZ() * this.m_buffer[11] + vector.getW() * this.m_buffer[15]);
    }
    
    public void setMultiply(final float f) {
        this.m_isIdentity = false;
        final float[] buffer = this.m_buffer;
        final int n = 0;
        buffer[n] *= f;
        final float[] buffer2 = this.m_buffer;
        final int n2 = 1;
        buffer2[n2] *= f;
        final float[] buffer3 = this.m_buffer;
        final int n3 = 2;
        buffer3[n3] *= f;
        final float[] buffer4 = this.m_buffer;
        final int n4 = 3;
        buffer4[n4] *= f;
        final float[] buffer5 = this.m_buffer;
        final int n5 = 4;
        buffer5[n5] *= f;
        final float[] buffer6 = this.m_buffer;
        final int n6 = 5;
        buffer6[n6] *= f;
        final float[] buffer7 = this.m_buffer;
        final int n7 = 6;
        buffer7[n7] *= f;
        final float[] buffer8 = this.m_buffer;
        final int n8 = 7;
        buffer8[n8] *= f;
        final float[] buffer9 = this.m_buffer;
        final int n9 = 8;
        buffer9[n9] *= f;
        final float[] buffer10 = this.m_buffer;
        final int n10 = 9;
        buffer10[n10] *= f;
        final float[] buffer11 = this.m_buffer;
        final int n11 = 10;
        buffer11[n11] *= f;
        final float[] buffer12 = this.m_buffer;
        final int n12 = 11;
        buffer12[n12] *= f;
        final float[] buffer13 = this.m_buffer;
        final int n13 = 12;
        buffer13[n13] *= f;
        final float[] buffer14 = this.m_buffer;
        final int n14 = 13;
        buffer14[n14] *= f;
        final float[] buffer15 = this.m_buffer;
        final int n15 = 14;
        buffer15[n15] *= f;
        final float[] buffer16 = this.m_buffer;
        final int n16 = 15;
        buffer16[n16] *= f;
    }
    
    public void getTranspose(final Matrix44 transpose) {
        if (this.m_isIdentity) {
            transpose.set(this);
        }
        else {
            transpose.m_buffer[0] = this.m_buffer[0];
            transpose.m_buffer[1] = this.m_buffer[4];
            transpose.m_buffer[2] = this.m_buffer[8];
            transpose.m_buffer[3] = this.m_buffer[12];
            transpose.m_buffer[4] = this.m_buffer[1];
            transpose.m_buffer[5] = this.m_buffer[5];
            transpose.m_buffer[6] = this.m_buffer[9];
            transpose.m_buffer[7] = this.m_buffer[13];
            transpose.m_buffer[8] = this.m_buffer[2];
            transpose.m_buffer[9] = this.m_buffer[6];
            transpose.m_buffer[10] = this.m_buffer[10];
            transpose.m_buffer[11] = this.m_buffer[14];
            transpose.m_buffer[12] = this.m_buffer[3];
            transpose.m_buffer[13] = this.m_buffer[7];
            transpose.m_buffer[14] = this.m_buffer[11];
            transpose.m_buffer[15] = this.m_buffer[15];
            transpose.m_isIdentity = false;
        }
    }
    
    public void setMultiply(final Matrix44 m1) {
        this.m_isIdentity = false;
        final float a0 = this.m_buffer[0] * m1.m_buffer[0] + this.m_buffer[1] * m1.m_buffer[4] + this.m_buffer[2] * m1.m_buffer[8] + this.m_buffer[3] * m1.m_buffer[12];
        final float a = this.m_buffer[0] * m1.m_buffer[1] + this.m_buffer[1] * m1.m_buffer[5] + this.m_buffer[2] * m1.m_buffer[9] + this.m_buffer[3] * m1.m_buffer[13];
        final float a2 = this.m_buffer[0] * m1.m_buffer[2] + this.m_buffer[1] * m1.m_buffer[6] + this.m_buffer[2] * m1.m_buffer[10] + this.m_buffer[3] * m1.m_buffer[14];
        final float a3 = this.m_buffer[0] * m1.m_buffer[3] + this.m_buffer[1] * m1.m_buffer[7] + this.m_buffer[2] * m1.m_buffer[11] + this.m_buffer[3] * m1.m_buffer[15];
        final float a4 = this.m_buffer[4] * m1.m_buffer[0] + this.m_buffer[5] * m1.m_buffer[4] + this.m_buffer[6] * m1.m_buffer[8] + this.m_buffer[7] * m1.m_buffer[12];
        final float a5 = this.m_buffer[4] * m1.m_buffer[1] + this.m_buffer[5] * m1.m_buffer[5] + this.m_buffer[6] * m1.m_buffer[9] + this.m_buffer[7] * m1.m_buffer[13];
        final float a6 = this.m_buffer[4] * m1.m_buffer[2] + this.m_buffer[5] * m1.m_buffer[6] + this.m_buffer[6] * m1.m_buffer[10] + this.m_buffer[7] * m1.m_buffer[14];
        final float a7 = this.m_buffer[4] * m1.m_buffer[3] + this.m_buffer[5] * m1.m_buffer[7] + this.m_buffer[6] * m1.m_buffer[11] + this.m_buffer[7] * m1.m_buffer[15];
        final float a8 = this.m_buffer[8] * m1.m_buffer[0] + this.m_buffer[9] * m1.m_buffer[4] + this.m_buffer[10] * m1.m_buffer[8] + this.m_buffer[11] * m1.m_buffer[12];
        final float a9 = this.m_buffer[8] * m1.m_buffer[1] + this.m_buffer[9] * m1.m_buffer[5] + this.m_buffer[10] * m1.m_buffer[9] + this.m_buffer[11] * m1.m_buffer[13];
        final float a10 = this.m_buffer[8] * m1.m_buffer[2] + this.m_buffer[9] * m1.m_buffer[6] + this.m_buffer[10] * m1.m_buffer[10] + this.m_buffer[11] * m1.m_buffer[14];
        final float a11 = this.m_buffer[8] * m1.m_buffer[3] + this.m_buffer[9] * m1.m_buffer[7] + this.m_buffer[10] * m1.m_buffer[11] + this.m_buffer[11] * m1.m_buffer[15];
        final float a12 = this.m_buffer[12] * m1.m_buffer[0] + this.m_buffer[13] * m1.m_buffer[4] + this.m_buffer[14] * m1.m_buffer[8] + this.m_buffer[15] * m1.m_buffer[12];
        final float a13 = this.m_buffer[12] * m1.m_buffer[1] + this.m_buffer[13] * m1.m_buffer[5] + this.m_buffer[14] * m1.m_buffer[9] + this.m_buffer[15] * m1.m_buffer[13];
        final float a14 = this.m_buffer[12] * m1.m_buffer[2] + this.m_buffer[13] * m1.m_buffer[6] + this.m_buffer[14] * m1.m_buffer[10] + this.m_buffer[15] * m1.m_buffer[14];
        final float a15 = this.m_buffer[12] * m1.m_buffer[3] + this.m_buffer[13] * m1.m_buffer[7] + this.m_buffer[14] * m1.m_buffer[11] + this.m_buffer[15] * m1.m_buffer[15];
        this.m_buffer[0] = a0;
        this.m_buffer[1] = a;
        this.m_buffer[2] = a2;
        this.m_buffer[3] = a3;
        this.m_buffer[4] = a4;
        this.m_buffer[5] = a5;
        this.m_buffer[6] = a6;
        this.m_buffer[7] = a7;
        this.m_buffer[8] = a8;
        this.m_buffer[9] = a9;
        this.m_buffer[10] = a10;
        this.m_buffer[11] = a11;
        this.m_buffer[12] = a12;
        this.m_buffer[13] = a13;
        this.m_buffer[14] = a14;
        this.m_buffer[15] = a15;
    }
    
    public void setMultiply(final Matrix44 m0, final Matrix44 m1) {
        this.m_isIdentity = false;
        this.m_buffer[0] = m0.m_buffer[0] * m1.m_buffer[0] + m0.m_buffer[1] * m1.m_buffer[4] + m0.m_buffer[2] * m1.m_buffer[8] + m0.m_buffer[3] * m1.m_buffer[12];
        this.m_buffer[1] = m0.m_buffer[0] * m1.m_buffer[1] + m0.m_buffer[1] * m1.m_buffer[5] + m0.m_buffer[2] * m1.m_buffer[9] + m0.m_buffer[3] * m1.m_buffer[13];
        this.m_buffer[2] = m0.m_buffer[0] * m1.m_buffer[2] + m0.m_buffer[1] * m1.m_buffer[6] + m0.m_buffer[2] * m1.m_buffer[10] + m0.m_buffer[3] * m1.m_buffer[14];
        this.m_buffer[3] = m0.m_buffer[0] * m1.m_buffer[3] + m0.m_buffer[1] * m1.m_buffer[7] + m0.m_buffer[2] * m1.m_buffer[11] + m0.m_buffer[3] * m1.m_buffer[15];
        this.m_buffer[4] = m0.m_buffer[4] * m1.m_buffer[0] + m0.m_buffer[5] * m1.m_buffer[4] + m0.m_buffer[6] * m1.m_buffer[8] + m0.m_buffer[7] * m1.m_buffer[12];
        this.m_buffer[5] = m0.m_buffer[4] * m1.m_buffer[1] + m0.m_buffer[5] * m1.m_buffer[5] + m0.m_buffer[6] * m1.m_buffer[9] + m0.m_buffer[7] * m1.m_buffer[13];
        this.m_buffer[6] = m0.m_buffer[4] * m1.m_buffer[2] + m0.m_buffer[5] * m1.m_buffer[6] + m0.m_buffer[6] * m1.m_buffer[10] + m0.m_buffer[7] * m1.m_buffer[14];
        this.m_buffer[7] = m0.m_buffer[4] * m1.m_buffer[3] + m0.m_buffer[5] * m1.m_buffer[7] + m0.m_buffer[6] * m1.m_buffer[11] + m0.m_buffer[7] * m1.m_buffer[15];
        this.m_buffer[8] = m0.m_buffer[8] * m1.m_buffer[0] + m0.m_buffer[9] * m1.m_buffer[4] + m0.m_buffer[10] * m1.m_buffer[8] + m0.m_buffer[11] * m1.m_buffer[12];
        this.m_buffer[9] = m0.m_buffer[8] * m1.m_buffer[1] + m0.m_buffer[9] * m1.m_buffer[5] + m0.m_buffer[10] * m1.m_buffer[9] + m0.m_buffer[11] * m1.m_buffer[13];
        this.m_buffer[10] = m0.m_buffer[8] * m1.m_buffer[2] + m0.m_buffer[9] * m1.m_buffer[6] + m0.m_buffer[10] * m1.m_buffer[10] + m0.m_buffer[11] * m1.m_buffer[14];
        this.m_buffer[11] = m0.m_buffer[8] * m1.m_buffer[3] + m0.m_buffer[9] * m1.m_buffer[7] + m0.m_buffer[10] * m1.m_buffer[11] + m0.m_buffer[11] * m1.m_buffer[15];
        this.m_buffer[12] = m0.m_buffer[12] * m1.m_buffer[0] + m0.m_buffer[13] * m1.m_buffer[4] + m0.m_buffer[14] * m1.m_buffer[8] + m0.m_buffer[15] * m1.m_buffer[12];
        this.m_buffer[13] = m0.m_buffer[12] * m1.m_buffer[1] + m0.m_buffer[13] * m1.m_buffer[5] + m0.m_buffer[14] * m1.m_buffer[9] + m0.m_buffer[15] * m1.m_buffer[13];
        this.m_buffer[14] = m0.m_buffer[12] * m1.m_buffer[2] + m0.m_buffer[13] * m1.m_buffer[6] + m0.m_buffer[14] * m1.m_buffer[10] + m0.m_buffer[15] * m1.m_buffer[14];
        this.m_buffer[15] = m0.m_buffer[12] * m1.m_buffer[3] + m0.m_buffer[13] * m1.m_buffer[7] + m0.m_buffer[14] * m1.m_buffer[11] + m0.m_buffer[15] * m1.m_buffer[15];
    }
    
    public void setMultiply(final Matrix44 m0, final Matrix44 m1, final Matrix44 m2) {
        final Matrix44 i = new Matrix44();
        i.setMultiply(m1, m2);
        this.setMultiply(m0, i);
        this.m_isIdentity = false;
    }
    
    public void setMultiply(final Matrix44 m0, final Matrix44 m1, final Matrix44 m2, final Matrix44 m3) {
        final Matrix44 i = new Matrix44();
        i.setMultiply(m1, m2, m3);
        this.setMultiply(m0, i);
        this.m_isIdentity = false;
    }
    
    public float getDeterminant() {
        if (this.m_isIdentity) {
            return 1.0f;
        }
        final float A0 = this.m_buffer[0] * this.m_buffer[5] - this.m_buffer[1] * this.m_buffer[4];
        final float A = this.m_buffer[0] * this.m_buffer[6] - this.m_buffer[2] * this.m_buffer[4];
        final float A2 = this.m_buffer[0] * this.m_buffer[7] - this.m_buffer[3] * this.m_buffer[4];
        final float A3 = this.m_buffer[1] * this.m_buffer[6] - this.m_buffer[2] * this.m_buffer[5];
        final float A4 = this.m_buffer[1] * this.m_buffer[7] - this.m_buffer[3] * this.m_buffer[5];
        final float A5 = this.m_buffer[2] * this.m_buffer[7] - this.m_buffer[3] * this.m_buffer[6];
        final float B0 = this.m_buffer[8] * this.m_buffer[13] - this.m_buffer[9] * this.m_buffer[12];
        final float B = this.m_buffer[8] * this.m_buffer[14] - this.m_buffer[10] * this.m_buffer[12];
        final float B2 = this.m_buffer[8] * this.m_buffer[15] - this.m_buffer[11] * this.m_buffer[12];
        final float B3 = this.m_buffer[9] * this.m_buffer[14] - this.m_buffer[10] * this.m_buffer[13];
        final float B4 = this.m_buffer[9] * this.m_buffer[15] - this.m_buffer[11] * this.m_buffer[13];
        final float B5 = this.m_buffer[10] * this.m_buffer[15] - this.m_buffer[11] * this.m_buffer[14];
        return A0 * B5 - A * B4 + A2 * B3 + A3 * B2 - A4 * B + A5 * B0;
    }
    
    public boolean getInverse(final Matrix44 inverse) {
        if (this.m_isIdentity) {
            inverse.setIdentity();
            return true;
        }
        final float A0 = this.m_buffer[0] * this.m_buffer[5] - this.m_buffer[1] * this.m_buffer[4];
        final float A = this.m_buffer[0] * this.m_buffer[6] - this.m_buffer[2] * this.m_buffer[4];
        final float A2 = this.m_buffer[0] * this.m_buffer[7] - this.m_buffer[3] * this.m_buffer[4];
        final float A3 = this.m_buffer[1] * this.m_buffer[6] - this.m_buffer[2] * this.m_buffer[5];
        final float A4 = this.m_buffer[1] * this.m_buffer[7] - this.m_buffer[3] * this.m_buffer[5];
        final float A5 = this.m_buffer[2] * this.m_buffer[7] - this.m_buffer[3] * this.m_buffer[6];
        final float B0 = this.m_buffer[8] * this.m_buffer[13] - this.m_buffer[9] * this.m_buffer[12];
        final float B = this.m_buffer[8] * this.m_buffer[14] - this.m_buffer[10] * this.m_buffer[12];
        final float B2 = this.m_buffer[8] * this.m_buffer[15] - this.m_buffer[11] * this.m_buffer[12];
        final float B3 = this.m_buffer[9] * this.m_buffer[14] - this.m_buffer[10] * this.m_buffer[13];
        final float B4 = this.m_buffer[9] * this.m_buffer[15] - this.m_buffer[11] * this.m_buffer[13];
        final float B5 = this.m_buffer[10] * this.m_buffer[15] - this.m_buffer[11] * this.m_buffer[14];
        final float det = A0 * B5 - A * B4 + A2 * B3 + A3 * B2 - A4 * B + A5 * B0;
        if (Math.abs(det) < 1.0E-5f) {
            return false;
        }
        inverse.m_buffer[0] = this.m_buffer[5] * B5 - this.m_buffer[6] * B4 + this.m_buffer[7] * B3;
        inverse.m_buffer[4] = -this.m_buffer[4] * B5 + this.m_buffer[6] * B2 - this.m_buffer[7] * B;
        inverse.m_buffer[8] = this.m_buffer[4] * B4 - this.m_buffer[5] * B2 + this.m_buffer[7] * B0;
        inverse.m_buffer[12] = -this.m_buffer[4] * B3 + this.m_buffer[5] * B - this.m_buffer[6] * B0;
        inverse.m_buffer[1] = -this.m_buffer[1] * B5 + this.m_buffer[2] * B4 - this.m_buffer[3] * B3;
        inverse.m_buffer[5] = this.m_buffer[0] * B5 - this.m_buffer[2] * B2 + this.m_buffer[3] * B;
        inverse.m_buffer[9] = -this.m_buffer[0] * B4 + this.m_buffer[1] * B2 - this.m_buffer[3] * B0;
        inverse.m_buffer[13] = this.m_buffer[0] * B3 - this.m_buffer[1] * B + this.m_buffer[2] * B0;
        inverse.m_buffer[2] = this.m_buffer[13] * A5 - this.m_buffer[14] * A4 + this.m_buffer[15] * A3;
        inverse.m_buffer[6] = -this.m_buffer[12] * A5 + this.m_buffer[14] * A2 - this.m_buffer[15] * A;
        inverse.m_buffer[10] = this.m_buffer[12] * A4 - this.m_buffer[13] * A2 + this.m_buffer[15] * A0;
        inverse.m_buffer[14] = -this.m_buffer[12] * A3 + this.m_buffer[13] * A - this.m_buffer[14] * A0;
        inverse.m_buffer[3] = -this.m_buffer[9] * A5 + this.m_buffer[10] * A4 - this.m_buffer[11] * A3;
        inverse.m_buffer[7] = this.m_buffer[8] * A5 - this.m_buffer[10] * A2 + this.m_buffer[11] * A;
        inverse.m_buffer[11] = -this.m_buffer[8] * A4 + this.m_buffer[9] * A2 - this.m_buffer[11] * A0;
        inverse.m_buffer[15] = this.m_buffer[8] * A3 - this.m_buffer[9] * A + this.m_buffer[10] * A0;
        inverse.setMultiply(1.0f / det);
        return true;
    }
    
    public float[] getBuffer() {
        return this.m_buffer;
    }
    
    public void set(final int index, final float value) {
        this.m_isIdentity = false;
        this.m_buffer[index] = value;
    }
    
    public boolean isIdentity() {
        return this.m_isIdentity;
    }
    
    public boolean equals(final Matrix44 matrix) {
        if (matrix == this) {
            return true;
        }
        for (int i = 0; i < this.m_buffer.length; ++i) {
            if (this.m_buffer[i] != matrix.m_buffer[i]) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    protected void checkout() {
        this.m_isIdentity = false;
    }
    
    @Override
    protected void checkin() {
    }
    
    private void setHomogeneCoordinates() {
        this.m_buffer[3] = 0.0f;
        this.m_buffer[7] = 0.0f;
        this.m_buffer[11] = 0.0f;
        this.m_buffer[15] = 1.0f;
    }
    
    static {
        Factory = new ObjectFactory();
        m_logger = Logger.getLogger((Class)Matrix44.class);
        m_zeroFilledBuffer = new float[16];
        IDENTITY = getIdentity();
        for (int i = 0; i < 16; ++i) {
            Matrix44.m_zeroFilledBuffer[i] = 0.0f;
        }
    }
    
    public static final class ObjectFactory extends MemoryObject.ObjectFactory<Matrix44>
    {
        public ObjectFactory() {
            super(Matrix44.class);
        }
        
        @Override
        public Matrix44 create() {
            return new Matrix44((Matrix44$1)null);
        }
    }
}
