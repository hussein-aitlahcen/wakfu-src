package gnu.trove;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class TObjectByteHashMap<K> extends TObjectHash<K> implements Externalizable
{
    static final long serialVersionUID = 1L;
    private final TObjectByteProcedure<K> PUT_ALL_PROC;
    protected transient byte[] _values;
    
    public TObjectByteHashMap() {
        super();
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() {
            public boolean execute(final K key, final byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectByteHashMap(final int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() {
            public boolean execute(final K key, final byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectByteHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() {
            public boolean execute(final K key, final byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectByteHashMap(final TObjectHashingStrategy<K> strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() {
            public boolean execute(final K key, final byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectByteHashMap(final int initialCapacity, final TObjectHashingStrategy<K> strategy) {
        super(initialCapacity, strategy);
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() {
            public boolean execute(final K key, final byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectByteHashMap(final int initialCapacity, final float loadFactor, final TObjectHashingStrategy<K> strategy) {
        super(initialCapacity, loadFactor, strategy);
        this.PUT_ALL_PROC = new TObjectByteProcedure<K>() {
            public boolean execute(final K key, final byte value) {
                TObjectByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TObjectByteIterator<K> iterator() {
        return new TObjectByteIterator<K>(this);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._values = new byte[capacity];
        return capacity;
    }
    
    public byte put(final K key, final byte value) {
        final int index = this.insertionIndex(key);
        return this.doPut(key, value, index);
    }
    
    public byte putIfAbsent(final K key, final byte value) {
        final int index = this.insertionIndex(key);
        if (index < 0) {
            return this._values[-index - 1];
        }
        return this.doPut(key, value, index);
    }
    
    private byte doPut(final K key, final byte value, int index) {
        byte previous = 0;
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
            this.postInsertHook(oldKey == TObjectByteHashMap.FREE);
        }
        return previous;
    }
    
    public void putAll(final TObjectByteHashMap<K> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final K[] oldKeys = (K[])this._set;
        final byte[] oldVals = this._values;
        Arrays.fill(this._set = new Object[newCapacity], TObjectByteHashMap.FREE);
        this._values = new byte[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldKeys[i] != TObjectByteHashMap.FREE && oldKeys[i] != TObjectByteHashMap.REMOVED) {
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
    
    public byte get(final K key) {
        final int index = this.index(key);
        return (byte)((index < 0) ? 0 : this._values[index]);
    }
    
    public void clear() {
        super.clear();
        final Object[] keys = this._set;
        final byte[] vals = this._values;
        Arrays.fill(this._set, 0, this._set.length, TObjectByteHashMap.FREE);
        Arrays.fill(this._values, 0, this._values.length, (byte)0);
    }
    
    public byte remove(final K key) {
        byte prev = 0;
        final int index = this.index(key);
        if (index >= 0) {
            prev = this._values[index];
            this.removeAt(index);
        }
        return prev;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TObjectByteHashMap)) {
            return false;
        }
        final TObjectByteHashMap that = (TObjectByteHashMap)other;
        return that.size() == this.size() && this.forEachEntry(new EqProcedure(that));
    }
    
    public TObjectByteHashMap<K> clone() {
        final TObjectByteHashMap<K> clone = (TObjectByteHashMap)super.clone();
        clone._values = new byte[this._values.length];
        System.arraycopy(this._values, 0, clone._values, 0, clone._values.length);
        return clone;
    }
    
    protected void removeAt(final int index) {
        this._values[index] = 0;
        super.removeAt(index);
    }
    
    public byte[] getValues() {
        final byte[] vals = new byte[this.size()];
        final byte[] v = this._values;
        final Object[] keys = this._set;
        int i = v.length;
        int j = 0;
        while (i-- > 0) {
            if (keys[i] != TObjectByteHashMap.FREE && keys[i] != TObjectByteHashMap.REMOVED) {
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
            if (k[i] != TObjectByteHashMap.FREE && k[i] != TObjectByteHashMap.REMOVED) {
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
            if (k[i] != TObjectByteHashMap.FREE && k[i] != TObjectByteHashMap.REMOVED) {
                a[j++] = k[i];
            }
        }
        return a;
    }
    
    public boolean containsValue(final byte val) {
        final Object[] keys = this._set;
        final byte[] vals = this._values;
        int i = vals.length;
        while (i-- > 0) {
            if (keys[i] != TObjectByteHashMap.FREE && keys[i] != TObjectByteHashMap.REMOVED && val == vals[i]) {
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
    
    public boolean forEachValue(final TByteProcedure procedure) {
        final Object[] keys = this._set;
        final byte[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (keys[i] != TObjectByteHashMap.FREE && keys[i] != TObjectByteHashMap.REMOVED && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEntry(final TObjectByteProcedure<K> procedure) {
        final K[] keys = (K[])this._set;
        final byte[] values = this._values;
        int i = keys.length;
        while (i-- > 0) {
            if (keys[i] != TObjectByteHashMap.FREE && keys[i] != TObjectByteHashMap.REMOVED && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean retainEntries(final TObjectByteProcedure<K> procedure) {
        boolean modified = false;
        final K[] keys = (K[])this._set;
        final byte[] values = this._values;
        this.tempDisableAutoCompaction();
        try {
            int i = keys.length;
            while (i-- > 0) {
                if (keys[i] != TObjectByteHashMap.FREE && keys[i] != TObjectByteHashMap.REMOVED && !procedure.execute(keys[i], values[i])) {
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
    
    public void transformValues(final TByteFunction function) {
        final Object[] keys = this._set;
        final byte[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (keys[i] != null && keys[i] != TObjectByteHashMap.REMOVED) {
                values[i] = function.execute(values[i]);
            }
        }
    }
    
    public boolean increment(final K key) {
        return this.adjustValue(key, (byte)1);
    }
    
    public boolean adjustValue(final K key, final byte amount) {
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        final byte[] values = this._values;
        final int n = index;
        values[n] += amount;
        return true;
    }
    
    public byte adjustOrPutValue(final K key, final byte adjust_amount, final byte put_amount) {
        int index = this.insertionIndex(key);
        byte newValue;
        boolean isNewMapping;
        if (index < 0) {
            index = -index - 1;
            final byte[] values = this._values;
            final int n = index;
            final byte b = (byte)(values[n] + adjust_amount);
            values[n] = b;
            newValue = b;
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
            this.postInsertHook(oldKey == TObjectByteHashMap.FREE);
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
            final byte val = in.readByte();
            this.put(key, val);
        }
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEachEntry(new TObjectByteProcedure<K>() {
            private boolean first = true;
            
            public boolean execute(final K key, final byte value) {
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
    
    private static final class EqProcedure implements TObjectByteProcedure
    {
        private final TObjectByteHashMap _otherMap;
        
        EqProcedure(final TObjectByteHashMap otherMap) {
            super();
            this._otherMap = otherMap;
        }
        
        public final boolean execute(final Object key, final byte value) {
            final int index = this._otherMap.index(key);
            return index >= 0 && this.eq(value, this._otherMap.get(key));
        }
        
        private final boolean eq(final byte v1, final byte v2) {
            return v1 == v2;
        }
    }
}
