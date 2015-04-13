package com.ankamagames.framework.kernel.core.common.collections.lightweight;

import gnu.trove.*;

public class LightWeightMap<K, V>
{
    protected K[] m_indexes;
    private V[] m_values;
    protected int m_size;
    
    public LightWeightMap() {
        this(10);
    }
    
    public LightWeightMap(final int initialCapacity) {
        super();
        this.m_indexes = new Object[initialCapacity];
        this.m_values = new Object[initialCapacity];
        this.m_size = 0;
    }
    
    public boolean ensureCapacity(final int newCapacity) {
        if (newCapacity > this.m_indexes.length) {
            final K[] indexes = (K[])this.m_indexes;
            System.arraycopy(indexes, 0, this.m_indexes = new Object[newCapacity], 0, indexes.length);
            final V[] values = (V[])this.m_values;
            System.arraycopy(values, 0, this.m_values = new Object[newCapacity], 0, values.length);
            return true;
        }
        return false;
    }
    
    protected final int index(final K key) {
        for (int i = this.m_size - 1; i >= 0; --i) {
            if (key.equals(this.m_indexes[i])) {
                return i;
            }
        }
        return -1;
    }
    
    protected final int insertIndex(final K key) {
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
    
    public final boolean contains(final K key) {
        return this.index(key) != -1;
    }
    
    public final void reset() {
        this.m_size = 0;
    }
    
    public void clear() {
        this.m_size = 0;
        for (int i = 0, size = this.m_values.length; i < size; ++i) {
            this.m_values[i] = null;
        }
    }
    
    public final int size() {
        return this.m_size;
    }
    
    public final K getQuickKey(final int index) {
        return (K)this.m_indexes[index];
    }
    
    public void put(final K key, final V value) {
        this.checkCapacity();
        int index = this.insertIndex(key);
        if (index < 0) {
            index = -index - 1;
        }
        else {
            ++this.m_size;
            this.m_indexes[index] = key;
        }
        this.m_values[index] = value;
    }
    
    public V remove(final K key) {
        if (this.m_size == 0) {
            return null;
        }
        final int index = this.index(key);
        if (index < 0) {
            return null;
        }
        final V removed = (V)this.m_values[index];
        if (index < this.m_size - 1) {
            this.m_indexes[index] = this.m_indexes[this.m_size - 1];
            this.m_values[index] = this.m_values[this.m_size - 1];
            this.m_indexes[this.m_size - 1] = null;
            this.m_values[this.m_size - 1] = null;
        }
        else {
            this.m_indexes[index] = null;
            this.m_values[index] = null;
        }
        --this.m_size;
        return removed;
    }
    
    public V get(final K key) {
        final int index = this.index(key);
        if (index < 0) {
            return null;
        }
        return (V)this.m_values[index];
    }
    
    public final V getQuickValue(final int index) {
        return (V)this.m_values[index];
    }
    
    public final void foreachValue(final TObjectProcedure<V> procedure) {
        for (int i = 0; i < this.m_size; ++i) {
            procedure.execute((V)this.m_values[i]);
        }
    }
    
    public static void main(final String[] args) {
        final LightWeightMap<String, Integer> map = new LightWeightMap<String, Integer>();
        map.put("id", 10);
        map.put("craft_id", 20);
        final int i = map.get("craft_id");
        System.out.println(i);
    }
}
