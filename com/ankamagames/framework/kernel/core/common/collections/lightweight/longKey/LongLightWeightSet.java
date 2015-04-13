package com.ankamagames.framework.kernel.core.common.collections.lightweight.longKey;

public class LongLightWeightSet extends AbstractLongKeyLightWeightMap
{
    public LongLightWeightSet(final int initialSize) {
        super(initialSize);
    }
    
    public LongLightWeightSet() {
        super(10);
    }
    
    public void add(final long key) {
        this.checkCapacity();
        final int index = this.insertIndex(key);
        if (index >= 0) {
            this.m_indexes[index] = key;
            ++this.m_size;
        }
    }
    
    public boolean remove(final long key) {
        if (this.m_size == 0) {
            return false;
        }
        final int index = this.index(key);
        return index >= 0 && this.removeQuick(index);
    }
    
    public boolean removeQuick(final int index) {
        this.m_indexes[index] = this.m_indexes[this.m_size - 1];
        --this.m_size;
        return true;
    }
}
