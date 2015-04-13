package com.ankamagames.framework.kernel.core.common.collections;

public final class FreeList
{
    public static final int NO_FREE_DATA = -1;
    private int m_firstFree;
    private int[] m_freeList;
    public int m_numFree;
    public int m_numUsed;
    
    public FreeList(final int size) {
        super();
        this.m_freeList = new int[size];
        this.freeAll();
    }
    
    public final int checkout() {
        if (this.m_firstFree >= this.m_freeList.length) {
            return -1;
        }
        final int index = this.m_firstFree;
        this.m_firstFree = this.m_freeList[this.m_firstFree];
        --this.m_numFree;
        ++this.m_numUsed;
        return index;
    }
    
    public final void checkin(final int id) {
        this.m_freeList[id] = this.m_firstFree;
        this.m_firstFree = id;
        ++this.m_numFree;
        --this.m_numUsed;
    }
    
    public final void freeAll() {
        this.m_firstFree = 0;
        final int length = this.m_freeList.length;
        for (int i = 0; i < length; ++i) {
            this.m_freeList[i] = i + 1;
        }
        this.m_numFree = length;
        this.m_numUsed = 0;
    }
    
    public final int getNumFree() {
        return this.m_numFree;
    }
    
    public final int getNumUsed() {
        return this.m_numUsed;
    }
    
    public final int getSize() {
        return this.m_freeList.length;
    }
    
    public final void resize(final int size) {
        assert size > this.m_freeList.length;
        final int[] freeList = new int[size];
        for (int i = this.m_freeList.length; i < freeList.length; ++i) {
            freeList[i] = i + 1;
        }
        System.arraycopy(this.m_freeList, 0, freeList, 0, this.m_freeList.length);
        this.m_freeList = freeList;
        this.m_numFree = size - this.m_numUsed;
    }
}
