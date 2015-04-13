package com.ankamagames.framework.kernel.core.common.collections;

public final class IntArray
{
    private static final int DEFAULT_GROWTH = 10;
    private int[] m_baseArray;
    private int m_capacity;
    private int m_size;
    private int m_growth;
    
    public IntArray() {
        super();
        this.m_baseArray = new int[10];
        this.m_capacity = 10;
        this.m_size = 0;
        this.m_growth = 10;
    }
    
    public IntArray(final IntArray array) {
        super();
        final int size = array.m_size;
        this.m_size = size;
        this.m_capacity = size;
        this.m_baseArray = new int[array.m_size];
        this.m_growth = array.m_growth;
    }
    
    public IntArray(final int size) {
        super();
        if (size < 0) {
            throw new IllegalArgumentException("La taille du tableau doit \u00eatre >= 0");
        }
        this.m_baseArray = new int[size];
        this.m_capacity = size;
        this.m_size = 0;
        this.m_growth = 10;
    }
    
    public IntArray(final int size, final int growth) {
        super();
        if (size < 0) {
            throw new IllegalArgumentException("La taille du tableau doit \u00eatre >= 0");
        }
        if (growth < 1) {
            throw new IllegalArgumentException("L'incr\u00e9ment de taille growth doit \u00eatre >= 1");
        }
        this.m_baseArray = new int[size];
        this.m_capacity = size;
        this.m_size = 0;
        this.m_growth = growth;
    }
    
    public void put(final int value) {
        this.ensureCapacity(this.m_size + 1);
        this.m_baseArray[this.m_size] = value;
        ++this.m_size;
    }
    
    public void put(final int[] values) {
        final int len = values.length;
        this.ensureCapacity(this.m_size + len);
        System.arraycopy(values, 0, this.m_baseArray, this.m_size, len);
        this.m_size += len;
    }
    
    public void put(final int[] values, final int len) {
        this.ensureCapacity(this.m_size + len);
        System.arraycopy(values, 0, this.m_baseArray, this.m_size, len);
        this.m_size += len;
    }
    
    public void put(final int[] values, final int from, final int len) {
        this.ensureCapacity(this.m_size + len);
        System.arraycopy(values, from, this.m_baseArray, this.m_size, len);
        this.m_size += len;
    }
    
    public void put(final IntArray array) {
        this.put(array.m_baseArray, 0, array.m_size);
    }
    
    public void remove(final int pos) {
        if (pos < 0 || pos >= this.m_size) {
            throw new ArrayIndexOutOfBoundsException("Can't remove value outside of range (0, size). Size =" + this.m_size + " index : " + pos);
        }
        if (pos == this.m_size - 1) {
            --this.m_size;
            return;
        }
        System.arraycopy(this.m_baseArray, pos + 1, this.m_baseArray, pos, this.m_size - pos - 1);
        --this.m_size;
    }
    
    public int getQuick(final int index) {
        return this.m_baseArray[index];
    }
    
    public int get(final int index) {
        if (index >= this.m_size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.m_baseArray[index];
    }
    
    public int size() {
        return this.m_size;
    }
    
    public void clear() {
        this.m_size = 0;
    }
    
    public int[] internalArray() {
        return this.m_baseArray;
    }
    
    public int[] toArray() {
        final int[] tmp = new int[this.m_size];
        System.arraycopy(this.m_baseArray, 0, tmp, 0, this.m_size);
        return tmp;
    }
    
    private void ensureCapacity(final int capacity) {
        if (capacity > this.m_capacity) {
            this.m_capacity = capacity + this.m_growth;
            final int[] tmp = new int[this.m_capacity];
            System.arraycopy(this.m_baseArray, 0, tmp, 0, this.m_size);
            this.m_baseArray = tmp;
        }
    }
}
