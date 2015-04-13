package gnu.trove;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class TShortObjectHashMap<V> extends TShortHash implements Externalizable
{
    static final long serialVersionUID = 1L;
    private final TShortObjectProcedure<V> PUT_ALL_PROC;
    protected transient V[] _values;
    
    public TShortObjectHashMap() {
        super();
        this.PUT_ALL_PROC = new TShortObjectProcedure<V>() {
            public boolean execute(final short key, final V value) {
                TShortObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TShortObjectHashMap(final int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TShortObjectProcedure<V>() {
            public boolean execute(final short key, final V value) {
                TShortObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TShortObjectHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TShortObjectProcedure<V>() {
            public boolean execute(final short key, final V value) {
                TShortObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TShortObjectHashMap(final TShortHashingStrategy strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TShortObjectProcedure<V>() {
            public boolean execute(final short key, final V value) {
                TShortObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TShortObjectHashMap(final int initialCapacity, final TShortHashingStrategy strategy) {
        super(initialCapacity, strategy);
        this.PUT_ALL_PROC = new TShortObjectProcedure<V>() {
            public boolean execute(final short key, final V value) {
                TShortObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TShortObjectHashMap(final int initialCapacity, final float loadFactor, final TShortHashingStrategy strategy) {
        super(initialCapacity, loadFactor, strategy);
        this.PUT_ALL_PROC = new TShortObjectProcedure<V>() {
            public boolean execute(final short key, final V value) {
                TShortObjectHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TShortObjectHashMap<V> clone() {
        final TShortObjectHashMap<V> m = (TShortObjectHashMap<V>)super.clone();
        m._values = this._values.clone();
        return m;
    }
    
    public TShortObjectIterator<V> iterator() {
        return new TShortObjectIterator<V>(this);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._values = new Object[capacity];
        return capacity;
    }
    
    public V put(final short key, final V value) {
        final int index = this.insertionIndex(key);
        return this.doPut(key, value, index);
    }
    
    public V putIfAbsent(final short key, final V value) {
        final int index = this.insertionIndex(key);
        if (index < 0) {
            return (V)this._values[-index - 1];
        }
        return this.doPut(key, value, index);
    }
    
    private V doPut(final short key, final V value, int index) {
        V previous = null;
        boolean isNewMapping = true;
        if (index < 0) {
            index = -index - 1;
            previous = (V)this._values[index];
            isNewMapping = false;
        }
        final byte previousState = this._states[index];
        this._set[index] = key;
        this._states[index] = 1;
        this._values[index] = value;
        if (isNewMapping) {
            this.postInsertHook(previousState == 0);
        }
        return previous;
    }
    
    public void putAll(final TShortObjectHashMap<V> map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final short[] oldKeys = this._set;
        final V[] oldVals = (V[])this._values;
        final byte[] oldStates = this._states;
        this._set = new short[newCapacity];
        this._values = new Object[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldStates[i] == 1) {
                final short o = oldKeys[i];
                final int index = this.insertionIndex(o);
                this._set[index] = o;
                this._values[index] = oldVals[i];
                this._states[index] = 1;
            }
        }
    }
    
    public V get(final short key) {
        final int index = this.index(key);
        return (V)((index < 0) ? null : this._values[index]);
    }
    
    public void clear() {
        super.clear();
        final short[] keys = this._set;
        final Object[] vals = this._values;
        final byte[] states = this._states;
        Arrays.fill(this._set, 0, this._set.length, (short)0);
        Arrays.fill(this._values, 0, this._values.length, null);
        Arrays.fill(this._states, 0, this._states.length, (byte)0);
    }
    
    public V remove(final short key) {
        V prev = null;
        final int index = this.index(key);
        if (index >= 0) {
            prev = (V)this._values[index];
            this.removeAt(index);
        }
        return prev;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TShortObjectHashMap)) {
            return false;
        }
        final TShortObjectHashMap that = (TShortObjectHashMap)other;
        return that.size() == this.size() && this.forEachEntry(new EqProcedure(that));
    }
    
    public int hashCode() {
        final HashProcedure p = new HashProcedure();
        this.forEachEntry(p);
        return p.getHashCode();
    }
    
    protected void removeAt(final int index) {
        this._values[index] = null;
        super.removeAt(index);
    }
    
    public Object[] getValues() {
        final Object[] vals = new Object[this.size()];
        final V[] v = (V[])this._values;
        final byte[] states = this._states;
        int i = v.length;
        int j = 0;
        while (i-- > 0) {
            if (states[i] == 1) {
                vals[j++] = v[i];
            }
        }
        return vals;
    }
    
    public <T> T[] getValues(T[] a) {
        if (a.length < this._size) {
            a = (T[])Array.newInstance(a.getClass().getComponentType(), this._size);
        }
        final V[] v = (V[])this._values;
        final byte[] states = this._states;
        int i = v.length;
        int j = 0;
        while (i-- > 0) {
            if (states[i] == 1) {
                a[j++] = (T)v[i];
            }
        }
        return a;
    }
    
    public short[] keys() {
        final short[] keys = new short[this.size()];
        final short[] k = this._set;
        final byte[] states = this._states;
        int i = k.length;
        int j = 0;
        while (i-- > 0) {
            if (states[i] == 1) {
                keys[j++] = k[i];
            }
        }
        return keys;
    }
    
    public short[] keys(short[] a) {
        final int size = this.size();
        if (a.length < size) {
            a = (short[])Array.newInstance(a.getClass().getComponentType(), size);
        }
        final short[] k = this._set;
        final byte[] states = this._states;
        int i = k.length;
        int j = 0;
        while (i-- > 0) {
            if (states[i] == 1) {
                a[j++] = k[i];
            }
        }
        return a;
    }
    
    public boolean containsValue(final V val) {
        final byte[] states = this._states;
        final V[] vals = (V[])this._values;
        if (null == val) {
            int i = vals.length;
            while (i-- > 0) {
                if (states[i] == 1 && val == vals[i]) {
                    return true;
                }
            }
        }
        else {
            int i = vals.length;
            while (i-- > 0) {
                if (states[i] == 1 && (val == vals[i] || val.equals(vals[i]))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean containsKey(final short key) {
        return this.contains(key);
    }
    
    public boolean forEachKey(final TShortProcedure procedure) {
        return this.forEach(procedure);
    }
    
    public boolean forEachValue(final TObjectProcedure<V> procedure) {
        final byte[] states = this._states;
        final V[] values = (V[])this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEntry(final TShortObjectProcedure<V> procedure) {
        final byte[] states = this._states;
        final short[] keys = this._set;
        final V[] values = (V[])this._values;
        int i = keys.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean retainEntries(final TShortObjectProcedure<V> procedure) {
        boolean modified = false;
        final byte[] states = this._states;
        final short[] keys = this._set;
        final V[] values = (V[])this._values;
        this.tempDisableAutoCompaction();
        try {
            int i = keys.length;
            while (i-- > 0) {
                if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
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
    
    public void transformValues(final TObjectFunction<V, V> function) {
        final byte[] states = this._states;
        final V[] values = (V[])this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1) {
                values[i] = function.execute(values[i]);
            }
        }
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
            final short key = in.readShort();
            final V val = (V)in.readObject();
            this.put(key, val);
        }
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEachEntry(new TShortObjectProcedure<V>() {
            private boolean first = true;
            
            public boolean execute(final short key, final Object value) {
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
    
    private final class HashProcedure implements TShortObjectProcedure
    {
        private int h;
        
        private HashProcedure() {
            super();
            this.h = 0;
        }
        
        public int getHashCode() {
            return this.h;
        }
        
        public final boolean execute(final short key, final Object value) {
            this.h += (TShortObjectHashMap.this._hashingStrategy.computeHashCode(key) ^ HashFunctions.hash(value));
            return true;
        }
    }
    
    private static final class EqProcedure implements TShortObjectProcedure
    {
        private final TShortObjectHashMap _otherMap;
        
        EqProcedure(final TShortObjectHashMap otherMap) {
            super();
            this._otherMap = otherMap;
        }
        
        public final boolean execute(final short key, final Object value) {
            final int index = this._otherMap.index(key);
            return index >= 0 && this.eq(value, this._otherMap.get(key));
        }
        
        private final boolean eq(final Object o1, final Object o2) {
            return o1 == o2 || (o1 != null && o1.equals(o2));
        }
    }
}
