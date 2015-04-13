package gnu.trove;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class TObjectLongHashMap<K> extends TObjectHash<K> implements Externalizable
{
    static final long serialVersionUID = 1L;
    private final TObjectLongProcedure<K> PUT_ALL_PROC;
    protected transient long[] _values;
    
    public TObjectLongHashMap() {
        super();
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() {
            public boolean execute(final K key, final long value) {
                TObjectLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectLongHashMap(final int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() {
            public boolean execute(final K key, final long value) {
                TObjectLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectLongHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() {
            public boolean execute(final K key, final long value) {
                TObjectLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectLongHashMap(final TObjectHashingStrategy<K> strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() {
            public boolean execute(final K key, final long value) {
                TObjectLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectLongHashMap(final int initialCapacity, final TObjectHashingStrategy<K> strategy) {
        super(initialCapacity, strategy);
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() {
            public boolean execute(final K key, final long value) {
                TObjectLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectLongHashMap(final int initialCapacity, final float loadFactor, final TObjectHashingStrategy<K> strategy) {
        super(initialCapacity, loadFactor, strategy);
        this.PUT_ALL_PROC = new TObjectLongProcedure<K>() {
            public boolean execute(final K key, final long value) {
                TObjectLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectLongIterator<K> iterator() {
        return new TObjectLongIterator<K>(this);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._values = new long[capacity];
        return capacity;
    }
    
    public long put(final K key, final long value) {
        final int index = this.insertionIndex(key);
        return this.doPut(key, value, index);
    }
    
    public long putIfAbsent(final K key, final long value) {
        final int index = this.insertionIndex(key);
        if (index < 0) {
            return this._values[-index - 1];
        }
        return this.doPut(key, value, index);
    }
    
    private long doPut(final K key, final long value, int index) {
        long previous = 0L;
        boolean isNewMapping = true;
        if (index < 0) {
            index = -index - 1;
            previous = this._values[index];
            isNewMapping = false;
        }
        final K oldKey = (K)this._set[index];
        this._set[index] = key;
        this._values[index] = value;
        if (isNewMapping) {
            this.postInsertHook(oldKey == TObjectLongHashMap.FREE);
        }
        return previous;
    }
    
    public void putAll(final TObjectLongHashMap<K> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final K[] oldKeys = (K[])this._set;
        final long[] oldVals = this._values;
        Arrays.fill(this._set = new Object[newCapacity], TObjectLongHashMap.FREE);
        this._values = new long[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldKeys[i] != TObjectLongHashMap.FREE && oldKeys[i] != TObjectLongHashMap.REMOVED) {
                final K o = oldKeys[i];
                final int index = this.insertionIndex(o);
                if (index < 0) {
                    this.throwObjectContractViolation(this._set[-index - 1], o);
                }
                this._set[index] = o;
                this._values[index] = oldVals[i];
            }
        }
    }
    
    public long get(final K key) {
        final int index = this.index(key);
        return (index < 0) ? 0L : this._values[index];
    }
    
    public void clear() {
        super.clear();
        final Object[] keys = this._set;
        final long[] vals = this._values;
        Arrays.fill(this._set, 0, this._set.length, TObjectLongHashMap.FREE);
        Arrays.fill(this._values, 0, this._values.length, 0L);
    }
    
    public long remove(final K key) {
        long prev = 0L;
        final int index = this.index(key);
        if (index >= 0) {
            prev = this._values[index];
            this.removeAt(index);
        }
        return prev;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TObjectLongHashMap)) {
            return false;
        }
        final TObjectLongHashMap that = (TObjectLongHashMap)other;
        return that.size() == this.size() && this.forEachEntry(new EqProcedure(that));
    }
    
    public TObjectLongHashMap<K> clone() {
        final TObjectLongHashMap<K> clone = (TObjectLongHashMap)super.clone();
        clone._values = new long[this._values.length];
        System.arraycopy(this._values, 0, clone._values, 0, clone._values.length);
        return clone;
    }
    
    protected void removeAt(final int index) {
        this._values[index] = 0L;
        super.removeAt(index);
    }
    
    public long[] getValues() {
        final long[] vals = new long[this.size()];
        final long[] v = this._values;
        final Object[] keys = this._set;
        int i = v.length;
        int j = 0;
        while (i-- > 0) {
            if (keys[i] != TObjectLongHashMap.FREE && keys[i] != TObjectLongHashMap.REMOVED) {
                vals[j++] = v[i];
            }
        }
        return vals;
    }
    
    public Object[] keys() {
        final Object[] keys = new Object[this.size()];
        final K[] k = (K[])this._set;
        int i = k.length;
        int j = 0;
        while (i-- > 0) {
            if (k[i] != TObjectLongHashMap.FREE && k[i] != TObjectLongHashMap.REMOVED) {
                keys[j++] = k[i];
            }
        }
        return keys;
    }
    
    public K[] keys(K[] a) {
        final int size = this.size();
        if (a.length < size) {
            a = (K[])Array.newInstance(a.getClass().getComponentType(), size);
        }
        final K[] k = (K[])this._set;
        int i = k.length;
        int j = 0;
        while (i-- > 0) {
            if (k[i] != TObjectLongHashMap.FREE && k[i] != TObjectLongHashMap.REMOVED) {
                a[j++] = k[i];
            }
        }
        return a;
    }
    
    public boolean containsValue(final long val) {
        final Object[] keys = this._set;
        final long[] vals = this._values;
        int i = vals.length;
        while (i-- > 0) {
            if (keys[i] != TObjectLongHashMap.FREE && keys[i] != TObjectLongHashMap.REMOVED && val == vals[i]) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsKey(final K key) {
        return this.contains(key);
    }
    
    public boolean forEachKey(final TObjectProcedure<K> procedure) {
        return this.forEach(procedure);
    }
    
    public boolean forEachValue(final TLongProcedure procedure) {
        final Object[] keys = this._set;
        final long[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (keys[i] != TObjectLongHashMap.FREE && keys[i] != TObjectLongHashMap.REMOVED && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEntry(final TObjectLongProcedure<K> procedure) {
        final K[] keys = (K[])this._set;
        final long[] values = this._values;
        int i = keys.length;
        while (i-- > 0) {
            if (keys[i] != TObjectLongHashMap.FREE && keys[i] != TObjectLongHashMap.REMOVED && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean retainEntries(final TObjectLongProcedure<K> procedure) {
        boolean modified = false;
        final K[] keys = (K[])this._set;
        final long[] values = this._values;
        this.tempDisableAutoCompaction();
        try {
            int i = keys.length;
            while (i-- > 0) {
                if (keys[i] != TObjectLongHashMap.FREE && keys[i] != TObjectLongHashMap.REMOVED && !procedure.execute(keys[i], values[i])) {
                    this.removeAt(i);
                    modified = true;
                }
            }
        }
        finally {
            this.reenableAutoCompaction(true);
        }
        return modified;
    }
    
    public void transformValues(final TLongFunction function) {
        final Object[] keys = this._set;
        final long[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (keys[i] != null && keys[i] != TObjectLongHashMap.REMOVED) {
                values[i] = function.execute(values[i]);
            }
        }
    }
    
    public boolean increment(final K key) {
        return this.adjustValue(key, 1L);
    }
    
    public boolean adjustValue(final K key, final long amount) {
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        final long[] values = this._values;
        final int n = index;
        values[n] += amount;
        return true;
    }
    
    public long adjustOrPutValue(final K key, final long adjust_amount, final long put_amount) {
        int index = this.insertionIndex(key);
        long newValue;
        boolean isNewMapping;
        if (index < 0) {
            index = -index - 1;
            final long[] values = this._values;
            final int n = index;
            final long n2 = values[n] + adjust_amount;
            values[n] = n2;
            newValue = n2;
            isNewMapping = false;
        }
        else {
            this._values[index] = put_amount;
            newValue = put_amount;
            isNewMapping = true;
        }
        final K oldKey = (K)this._set[index];
        this._set[index] = key;
        if (isNewMapping) {
            this.postInsertHook(oldKey == TObjectLongHashMap.FREE);
        }
        return newValue;
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeInt(this._size);
        final SerializationProcedure writeProcedure = new SerializationProcedure(out);
        if (!this.forEachEntry(writeProcedure)) {
            throw writeProcedure.exception;
        }
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        int size = in.readInt();
        this.setUp(size);
        while (size-- > 0) {
            final K key = (K)in.readObject();
            final long val = in.readLong();
            this.put(key, val);
        }
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEachEntry(new TObjectLongProcedure<K>() {
            private boolean first = true;
            
            public boolean execute(final K key, final long value) {
                if (this.first) {
                    this.first = false;
                }
                else {
                    buf.append(",");
                }
                buf.append(key);
                buf.append("=");
                buf.append(value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }
    
    private static final class EqProcedure implements TObjectLongProcedure
    {
        private final TObjectLongHashMap _otherMap;
        
        EqProcedure(final TObjectLongHashMap otherMap) {
            super();
            this._otherMap = otherMap;
        }
        
        public final boolean execute(final Object key, final long value) {
            final int index = this._otherMap.index(key);
            return index >= 0 && this.eq(value, this._otherMap.get(key));
        }
        
        private final boolean eq(final long v1, final long v2) {
            return v1 == v2;
        }
    }
}
