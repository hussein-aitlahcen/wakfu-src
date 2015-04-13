package gnu.trove;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class TObjectFloatHashMap<K> extends TObjectHash<K> implements Externalizable
{
    static final long serialVersionUID = 1L;
    private final TObjectFloatProcedure<K> PUT_ALL_PROC;
    protected transient float[] _values;
    
    public TObjectFloatHashMap() {
        super();
        this.PUT_ALL_PROC = new TObjectFloatProcedure<K>() {
            public boolean execute(final K key, final float value) {
                TObjectFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectFloatHashMap(final int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TObjectFloatProcedure<K>() {
            public boolean execute(final K key, final float value) {
                TObjectFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectFloatHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectFloatProcedure<K>() {
            public boolean execute(final K key, final float value) {
                TObjectFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectFloatHashMap(final TObjectHashingStrategy<K> strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TObjectFloatProcedure<K>() {
            public boolean execute(final K key, final float value) {
                TObjectFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectFloatHashMap(final int initialCapacity, final TObjectHashingStrategy<K> strategy) {
        super(initialCapacity, strategy);
        this.PUT_ALL_PROC = new TObjectFloatProcedure<K>() {
            public boolean execute(final K key, final float value) {
                TObjectFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectFloatHashMap(final int initialCapacity, final float loadFactor, final TObjectHashingStrategy<K> strategy) {
        super(initialCapacity, loadFactor, strategy);
        this.PUT_ALL_PROC = new TObjectFloatProcedure<K>() {
            public boolean execute(final K key, final float value) {
                TObjectFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectFloatIterator<K> iterator() {
        return new TObjectFloatIterator<K>(this);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._values = new float[capacity];
        return capacity;
    }
    
    public float put(final K key, final float value) {
        final int index = this.insertionIndex(key);
        return this.doPut(key, value, index);
    }
    
    public float putIfAbsent(final K key, final float value) {
        final int index = this.insertionIndex(key);
        if (index < 0) {
            return this._values[-index - 1];
        }
        return this.doPut(key, value, index);
    }
    
    private float doPut(final K key, final float value, int index) {
        float previous = 0.0f;
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
            this.postInsertHook(oldKey == TObjectFloatHashMap.FREE);
        }
        return previous;
    }
    
    public void putAll(final TObjectFloatHashMap<K> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final K[] oldKeys = (K[])this._set;
        final float[] oldVals = this._values;
        Arrays.fill(this._set = new Object[newCapacity], TObjectFloatHashMap.FREE);
        this._values = new float[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldKeys[i] != TObjectFloatHashMap.FREE && oldKeys[i] != TObjectFloatHashMap.REMOVED) {
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
    
    public float get(final K key) {
        final int index = this.index(key);
        return (index < 0) ? 0.0f : this._values[index];
    }
    
    public void clear() {
        super.clear();
        final Object[] keys = this._set;
        final float[] vals = this._values;
        Arrays.fill(this._set, 0, this._set.length, TObjectFloatHashMap.FREE);
        Arrays.fill(this._values, 0, this._values.length, 0.0f);
    }
    
    public float remove(final K key) {
        float prev = 0.0f;
        final int index = this.index(key);
        if (index >= 0) {
            prev = this._values[index];
            this.removeAt(index);
        }
        return prev;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TObjectFloatHashMap)) {
            return false;
        }
        final TObjectFloatHashMap that = (TObjectFloatHashMap)other;
        return that.size() == this.size() && this.forEachEntry(new EqProcedure(that));
    }
    
    public TObjectFloatHashMap<K> clone() {
        final TObjectFloatHashMap<K> clone = (TObjectFloatHashMap)super.clone();
        clone._values = new float[this._values.length];
        System.arraycopy(this._values, 0, clone._values, 0, clone._values.length);
        return clone;
    }
    
    protected void removeAt(final int index) {
        this._values[index] = 0.0f;
        super.removeAt(index);
    }
    
    public float[] getValues() {
        final float[] vals = new float[this.size()];
        final float[] v = this._values;
        final Object[] keys = this._set;
        int i = v.length;
        int j = 0;
        while (i-- > 0) {
            if (keys[i] != TObjectFloatHashMap.FREE && keys[i] != TObjectFloatHashMap.REMOVED) {
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
            if (k[i] != TObjectFloatHashMap.FREE && k[i] != TObjectFloatHashMap.REMOVED) {
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
            if (k[i] != TObjectFloatHashMap.FREE && k[i] != TObjectFloatHashMap.REMOVED) {
                a[j++] = k[i];
            }
        }
        return a;
    }
    
    public boolean containsValue(final float val) {
        final Object[] keys = this._set;
        final float[] vals = this._values;
        int i = vals.length;
        while (i-- > 0) {
            if (keys[i] != TObjectFloatHashMap.FREE && keys[i] != TObjectFloatHashMap.REMOVED && val == vals[i]) {
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
    
    public boolean forEachValue(final TFloatProcedure procedure) {
        final Object[] keys = this._set;
        final float[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (keys[i] != TObjectFloatHashMap.FREE && keys[i] != TObjectFloatHashMap.REMOVED && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEntry(final TObjectFloatProcedure<K> procedure) {
        final K[] keys = (K[])this._set;
        final float[] values = this._values;
        int i = keys.length;
        while (i-- > 0) {
            if (keys[i] != TObjectFloatHashMap.FREE && keys[i] != TObjectFloatHashMap.REMOVED && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean retainEntries(final TObjectFloatProcedure<K> procedure) {
        boolean modified = false;
        final K[] keys = (K[])this._set;
        final float[] values = this._values;
        this.tempDisableAutoCompaction();
        try {
            int i = keys.length;
            while (i-- > 0) {
                if (keys[i] != TObjectFloatHashMap.FREE && keys[i] != TObjectFloatHashMap.REMOVED && !procedure.execute(keys[i], values[i])) {
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
    
    public void transformValues(final TFloatFunction function) {
        final Object[] keys = this._set;
        final float[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (keys[i] != null && keys[i] != TObjectFloatHashMap.REMOVED) {
                values[i] = function.execute(values[i]);
            }
        }
    }
    
    public boolean increment(final K key) {
        return this.adjustValue(key, 1.0f);
    }
    
    public boolean adjustValue(final K key, final float amount) {
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        final float[] values = this._values;
        final int n = index;
        values[n] += amount;
        return true;
    }
    
    public float adjustOrPutValue(final K key, final float adjust_amount, final float put_amount) {
        int index = this.insertionIndex(key);
        float newValue;
        boolean isNewMapping;
        if (index < 0) {
            index = -index - 1;
            final float[] values = this._values;
            final int n = index;
            final float n2 = values[n] + adjust_amount;
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
            this.postInsertHook(oldKey == TObjectFloatHashMap.FREE);
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
            final float val = in.readFloat();
            this.put(key, val);
        }
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEachEntry(new TObjectFloatProcedure<K>() {
            private boolean first = true;
            
            public boolean execute(final K key, final float value) {
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
    
    private static final class EqProcedure implements TObjectFloatProcedure
    {
        private final TObjectFloatHashMap _otherMap;
        
        EqProcedure(final TObjectFloatHashMap otherMap) {
            super();
            this._otherMap = otherMap;
        }
        
        public final boolean execute(final Object key, final float value) {
            final int index = this._otherMap.index(key);
            return index >= 0 && this.eq(value, this._otherMap.get(key));
        }
        
        private final boolean eq(final float v1, final float v2) {
            return v1 == v2;
        }
    }
}
