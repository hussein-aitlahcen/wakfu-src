package gnu.trove;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class TObjectIntHashMap<K> extends TObjectHash<K> implements Externalizable
{
    static final long serialVersionUID = 1L;
    private final TObjectIntProcedure<K> PUT_ALL_PROC;
    protected transient int[] _values;
    
    public TObjectIntHashMap() {
        super();
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() {
            public boolean execute(final K key, final int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectIntHashMap(final int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() {
            public boolean execute(final K key, final int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectIntHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() {
            public boolean execute(final K key, final int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectIntHashMap(final TObjectHashingStrategy<K> strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() {
            public boolean execute(final K key, final int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectIntHashMap(final int initialCapacity, final TObjectHashingStrategy<K> strategy) {
        super(initialCapacity, strategy);
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() {
            public boolean execute(final K key, final int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectIntHashMap(final int initialCapacity, final float loadFactor, final TObjectHashingStrategy<K> strategy) {
        super(initialCapacity, loadFactor, strategy);
        this.PUT_ALL_PROC = new TObjectIntProcedure<K>() {
            public boolean execute(final K key, final int value) {
                TObjectIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectIntIterator<K> iterator() {
        return new TObjectIntIterator<K>(this);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._values = new int[capacity];
        return capacity;
    }
    
    public int put(final K key, final int value) {
        final int index = this.insertionIndex(key);
        return this.doPut(key, value, index);
    }
    
    public int putIfAbsent(final K key, final int value) {
        final int index = this.insertionIndex(key);
        if (index < 0) {
            return this._values[-index - 1];
        }
        return this.doPut(key, value, index);
    }
    
    private int doPut(final K key, final int value, int index) {
        int previous = 0;
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
            this.postInsertHook(oldKey == TObjectIntHashMap.FREE);
        }
        return previous;
    }
    
    public void putAll(final TObjectIntHashMap<K> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final K[] oldKeys = (K[])this._set;
        final int[] oldVals = this._values;
        Arrays.fill(this._set = new Object[newCapacity], TObjectIntHashMap.FREE);
        this._values = new int[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldKeys[i] != TObjectIntHashMap.FREE && oldKeys[i] != TObjectIntHashMap.REMOVED) {
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
    
    public int get(final K key) {
        final int index = this.index(key);
        return (index < 0) ? 0 : this._values[index];
    }
    
    public void clear() {
        super.clear();
        final Object[] keys = this._set;
        final int[] vals = this._values;
        Arrays.fill(this._set, 0, this._set.length, TObjectIntHashMap.FREE);
        Arrays.fill(this._values, 0, this._values.length, 0);
    }
    
    public int remove(final K key) {
        int prev = 0;
        final int index = this.index(key);
        if (index >= 0) {
            prev = this._values[index];
            this.removeAt(index);
        }
        return prev;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TObjectIntHashMap)) {
            return false;
        }
        final TObjectIntHashMap that = (TObjectIntHashMap)other;
        return that.size() == this.size() && this.forEachEntry(new EqProcedure(that));
    }
    
    public TObjectIntHashMap<K> clone() {
        final TObjectIntHashMap<K> clone = (TObjectIntHashMap)super.clone();
        clone._values = new int[this._values.length];
        System.arraycopy(this._values, 0, clone._values, 0, clone._values.length);
        return clone;
    }
    
    protected void removeAt(final int index) {
        this._values[index] = 0;
        super.removeAt(index);
    }
    
    public int[] getValues() {
        final int[] vals = new int[this.size()];
        final int[] v = this._values;
        final Object[] keys = this._set;
        int i = v.length;
        int j = 0;
        while (i-- > 0) {
            if (keys[i] != TObjectIntHashMap.FREE && keys[i] != TObjectIntHashMap.REMOVED) {
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
            if (k[i] != TObjectIntHashMap.FREE && k[i] != TObjectIntHashMap.REMOVED) {
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
            if (k[i] != TObjectIntHashMap.FREE && k[i] != TObjectIntHashMap.REMOVED) {
                a[j++] = k[i];
            }
        }
        return a;
    }
    
    public boolean containsValue(final int val) {
        final Object[] keys = this._set;
        final int[] vals = this._values;
        int i = vals.length;
        while (i-- > 0) {
            if (keys[i] != TObjectIntHashMap.FREE && keys[i] != TObjectIntHashMap.REMOVED && val == vals[i]) {
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
    
    public boolean forEachValue(final TIntProcedure procedure) {
        final Object[] keys = this._set;
        final int[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (keys[i] != TObjectIntHashMap.FREE && keys[i] != TObjectIntHashMap.REMOVED && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEntry(final TObjectIntProcedure<K> procedure) {
        final K[] keys = (K[])this._set;
        final int[] values = this._values;
        int i = keys.length;
        while (i-- > 0) {
            if (keys[i] != TObjectIntHashMap.FREE && keys[i] != TObjectIntHashMap.REMOVED && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean retainEntries(final TObjectIntProcedure<K> procedure) {
        boolean modified = false;
        final K[] keys = (K[])this._set;
        final int[] values = this._values;
        this.tempDisableAutoCompaction();
        try {
            int i = keys.length;
            while (i-- > 0) {
                if (keys[i] != TObjectIntHashMap.FREE && keys[i] != TObjectIntHashMap.REMOVED && !procedure.execute(keys[i], values[i])) {
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
    
    public void transformValues(final TIntFunction function) {
        final Object[] keys = this._set;
        final int[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (keys[i] != null && keys[i] != TObjectIntHashMap.REMOVED) {
                values[i] = function.execute(values[i]);
            }
        }
    }
    
    public boolean increment(final K key) {
        return this.adjustValue(key, 1);
    }
    
    public boolean adjustValue(final K key, final int amount) {
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        final int[] values = this._values;
        final int n = index;
        values[n] += amount;
        return true;
    }
    
    public int adjustOrPutValue(final K key, final int adjust_amount, final int put_amount) {
        int index = this.insertionIndex(key);
        int newValue;
        boolean isNewMapping;
        if (index < 0) {
            index = -index - 1;
            final int[] values = this._values;
            final int n = index;
            final int n2 = values[n] + adjust_amount;
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
            this.postInsertHook(oldKey == TObjectIntHashMap.FREE);
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
            final int val = in.readInt();
            this.put(key, val);
        }
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEachEntry(new TObjectIntProcedure<K>() {
            private boolean first = true;
            
            public boolean execute(final K key, final int value) {
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
    
    private static final class EqProcedure implements TObjectIntProcedure
    {
        private final TObjectIntHashMap _otherMap;
        
        EqProcedure(final TObjectIntHashMap otherMap) {
            super();
            this._otherMap = otherMap;
        }
        
        public final boolean execute(final Object key, final int value) {
            final int index = this._otherMap.index(key);
            return index >= 0 && this.eq(value, this._otherMap.get(key));
        }
        
        private final boolean eq(final int v1, final int v2) {
            return v1 == v2;
        }
    }
}
