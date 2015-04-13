package com.ankamagames.framework.kernel.core.common.collections.lightweight.byteKey;

import java.util.*;

public abstract class AbstractByteKeyLightWeightMap
{
    protected byte[] m_indexes;
    protected int m_size;
    
    protected AbstractByteKeyLightWeightMap(final int initialSize) {
        super();
        this.m_indexes = new byte[initialSize];
        this.m_size = 0;
    }
    
    public boolean ensureCapacity(final int newCapacity) {
        if (newCapacity > this.m_indexes.length) {
            final byte[] indexes = this.m_indexes;
            System.arraycopy(indexes, 0, this.m_indexes = new byte[newCapacity], 0, indexes.length);
            return true;
        }
        return false;
    }
    
    protected final int index(final byte key) {
        for (int i = this.m_size - 1; i >= 0; --i) {
            if (key == this.m_indexes[i]) {
                return i;
            }
        }
        return -1;
    }
    
    protected final int insertIndex(final byte key) {
        for (int i = this.m_size - 1; i >= 0; --i) {
            if (key == this.m_indexes[i]) {
                return -i - 1;
            }
        }
        return this.m_size;
    }
    
    protected final void checkCapacity() {
        if (this.m_size == this.m_indexes.length) {
            this.ensureCapacity(this.m_indexes.length * 2);
        }
    }
    
    public final boolean contains(final byte key) {
        return this.index(key) != -1;
    }
    
    public final void reset() {
        this.m_size = 0;
    }
    
    public void clear() {
        this.m_size = 0;
    }
    
    public final int size() {
        return this.m_size;
    }
    
    public final byte getQuickKey(final int index) {
        return this.m_indexes[index];
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AbstractByteKeyLightWeightMap that = (AbstractByteKeyLightWeightMap)o;
        return this.m_size == that.m_size && Arrays.equals(this.m_indexes, that.m_indexes);
    }
    
    @Override
    public int hashCode() {
        int result = (this.m_indexes != null) ? Arrays.hashCode(this.m_indexes) : 0;
        result = 31 * result + this.m_size;
        return result;
    }
}
