package com.ankamagames.framework.kernel.core.common.collections;

import java.util.*;

public class ByteArray
{
    private static final int DEFAULT_GROWTH = 10;
    protected byte[] m_baseArray;
    protected int m_capacity;
    protected int m_size;
    protected int m_growth;
    
    public ByteArray() {
        super();
        this.m_baseArray = new byte[10];
        this.m_capacity = 10;
        this.m_size = 0;
        this.m_growth = 10;
    }
    
    public ByteArray(final ByteArray array) {
        super();
        final int size = array.m_size;
        this.m_size = size;
        this.m_capacity = size;
        this.m_baseArray = new byte[array.m_size];
        this.m_growth = array.m_growth;
    }
    
    public ByteArray(final int size) {
        super();
        if (size < 0) {
            throw new IllegalArgumentException("La taille du tableau doit \u00eatre >= 0");
        }
        this.m_baseArray = new byte[size];
        this.m_capacity = size;
        this.m_size = 0;
        this.m_growth = 10;
    }
    
    public ByteArray(final int size, final int growth) {
        super();
        if (size < 0) {
            throw new IllegalArgumentException("La taille du tableau doit \u00eatre >= 0");
        }
        if (growth < 1) {
            throw new IllegalArgumentException("L'incr\u00e9ment de taille growth doit \u00eatre >= 1");
        }
        this.m_baseArray = new byte[size];
        this.m_capacity = size;
        this.m_size = 0;
        this.m_growth = growth;
    }
    
    public static ByteArray wrap(final byte[] byteArray) {
        if (byteArray == null) {
            throw new IllegalArgumentException("Le tableau ne peut \u00eatre nul");
        }
        final ByteArray a = new ByteArray();
        a.m_baseArray = byteArray;
        a.m_capacity = byteArray.length;
        a.m_growth = 10;
        a.m_size = a.m_capacity;
        return a;
    }
    
    public final void put(final byte value) {
        this.ensureCapacity(this.m_size + 1);
        this.m_baseArray[this.m_size] = value;
        ++this.m_size;
    }
    
    public final void put(final byte[] values) {
        final int len = values.length;
        this.ensureCapacity(this.m_size + len);
        System.arraycopy(values, 0, this.m_baseArray, this.m_size, len);
        this.m_size += len;
    }
    
    public final void put(final byte[] values, final int len) {
        this.ensureCapacity(this.m_size + len);
        System.arraycopy(values, 0, this.m_baseArray, this.m_size, len);
        this.m_size += len;
    }
    
    public final void put(final byte[] values, final int from, final int len) {
        this.ensureCapacity(this.m_size + len);
        System.arraycopy(values, from, this.m_baseArray, this.m_size, len);
        this.m_size += len;
    }
    
    public final void put(final ByteArray array) {
        this.put(array.m_baseArray, 0, array.m_size);
    }
    
    public final void putBoolean(final boolean v) {
        this.put((byte)(v ? 1 : 0));
    }
    
    public final void putChar(final char v) {
        this.ensureCapacity(this.m_size + 2);
        this.m_baseArray[this.m_size] = (byte)('\u00ff' & v >> 8);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)('\u00ff' & v);
        ++this.m_size;
    }
    
    public final void putShort(final short v) {
        this.ensureCapacity(this.m_size + 2);
        this.m_baseArray[this.m_size] = (byte)(0xFF & v >> 8);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFF & v);
        ++this.m_size;
    }
    
    public final void putInt(final int v) {
        this.ensureCapacity(this.m_size + 4);
        this.m_baseArray[this.m_size] = (byte)(0xFF & v >> 24);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFF & v >> 16);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFF & v >> 8);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFF & v);
        ++this.m_size;
    }
    
    public final void putLong(final long v) {
        this.ensureCapacity(this.m_size + 8);
        this.m_baseArray[this.m_size] = (byte)(0xFFL & v >> 56);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFFL & v >> 48);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFFL & v >> 40);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFFL & v >> 32);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFFL & v >> 24);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFFL & v >> 16);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFFL & v >> 8);
        ++this.m_size;
        this.m_baseArray[this.m_size] = (byte)(0xFFL & v);
        ++this.m_size;
    }
    
    public final boolean contains(final byte b) {
        for (int i = 0; i < this.m_size; ++i) {
            if (this.m_baseArray[i] == b) {
                return true;
            }
        }
        return false;
    }
    
    public final void putFloat(final float v) {
        this.putInt(Float.floatToIntBits(v));
    }
    
    public final void putDouble(final double v) {
        this.putLong(Double.doubleToLongBits(v));
    }
    
    public final byte get(final int index) {
        if (index >= this.m_size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.m_baseArray[index];
    }
    
    public final int size() {
        return this.m_size;
    }
    
    public final byte[] internalArray() {
        return this.m_baseArray;
    }
    
    public final byte[] toArray() {
        final byte[] tmp = new byte[this.m_size];
        System.arraycopy(this.m_baseArray, 0, tmp, 0, this.m_size);
        return tmp;
    }
    
    private void ensureCapacity(final int capacity) {
        if (capacity > this.m_capacity) {
            this.m_capacity = capacity + this.m_growth;
            final byte[] tmp = new byte[this.m_capacity];
            System.arraycopy(this.m_baseArray, 0, tmp, 0, this.m_size);
            this.m_baseArray = tmp;
        }
    }
    
    public final void clear() {
        Arrays.fill(this.m_baseArray, (byte)0);
        this.m_size = 0;
    }
}
