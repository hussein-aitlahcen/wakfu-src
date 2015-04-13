package gnu.trove;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class TLongFloatHashMap extends TLongHash implements Externalizable
{
    static final long serialVersionUID = 1L;
    private final TLongFloatProcedure PUT_ALL_PROC;
    protected transient float[] _values;
    
    public TLongFloatHashMap() {
        super();
        this.PUT_ALL_PROC = new TLongFloatProcedure() {
            public boolean execute(final long key, final float value) {
                TLongFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TLongFloatHashMap(final int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TLongFloatProcedure() {
            public boolean execute(final long key, final float value) {
                TLongFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TLongFloatHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TLongFloatProcedure() {
            public boolean execute(final long key, final float value) {
                TLongFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TLongFloatHashMap(final TLongHashingStrategy strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TLongFloatProcedure() {
            public boolean execute(final long key, final float value) {
                TLongFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TLongFloatHashMap(final int initialCapacity, final TLongHashingStrategy strategy) {
        super(initialCapacity, strategy);
        this.PUT_ALL_PROC = new TLongFloatProcedure() {
            public boolean execute(final long key, final float value) {
                TLongFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TLongFloatHashMap(final int initialCapacity, final float loadFactor, final TLongHashingStrategy strategy) {
        super(initialCapacity, loadFactor, strategy);
        this.PUT_ALL_PROC = new TLongFloatProcedure() {
            public boolean execute(final long key, final float value) {
                TLongFloatHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public Object clone() {
        final TLongFloatHashMap m = (TLongFloatHashMap)super.clone();
        m._values = this._values.clone();
        return m;
    }
    
    public TLongFloatIterator iterator() {
        return new TLongFloatIterator(this);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._values = new float[capacity];
        return capacity;
    }
    
    public float put(final long key, final float value) {
        final int index = this.insertionIndex(key);
        return this.doPut(key, value, index);
    }
    
    public float putIfAbsent(final long key, final float value) {
        final int index = this.insertionIndex(key);
        if (index < 0) {
            return this._values[-index - 1];
        }
        return this.doPut(key, value, index);
    }
    
    private float doPut(final long key, final float value, int index) {
        float previous = 0.0f;
        boolean isNewMapping = true;
        if (index < 0) {
            index = -index - 1;
            previous = this._values[index];
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
    
    public void putAll(final TLongFloatHashMap map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final long[] oldKeys = this._set;
        final float[] oldVals = this._values;
        final byte[] oldStates = this._states;
        this._set = new long[newCapacity];
        this._values = new float[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldStates[i] == 1) {
                final long o = oldKeys[i];
                final int index = this.insertionIndex(o);
                this._set[index] = o;
                this._values[index] = oldVals[i];
                this._states[index] = 1;
            }
        }
    }
    
    public float get(final long key) {
        final int index = this.index(key);
        return (index < 0) ? 0.0f : this._values[index];
    }
    
    public void clear() {
        super.clear();
        final long[] keys = this._set;
        final float[] vals = this._values;
        final byte[] states = this._states;
        Arrays.fill(this._set, 0, this._set.length, 0L);
        Arrays.fill(this._values, 0, this._values.length, 0.0f);
        Arrays.fill(this._states, 0, this._states.length, (byte)0);
    }
    
    public float remove(final long key) {
        float prev = 0.0f;
        final int index = this.index(key);
        if (index >= 0) {
            prev = this._values[index];
            this.removeAt(index);
        }
        return prev;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TLongFloatHashMap)) {
            return false;
        }
        final TLongFloatHashMap that = (TLongFloatHashMap)other;
        return that.size() == this.size() && this.forEachEntry(new EqProcedure(that));
    }
    
    public int hashCode() {
        final HashProcedure p = new HashProcedure();
        this.forEachEntry(p);
        return p.getHashCode();
    }
    
    protected void removeAt(final int index) {
        this._values[index] = 0.0f;
        super.removeAt(index);
    }
    
    public float[] getValues() {
        final float[] vals = new float[this.size()];
        final float[] v = this._values;
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
    
    public long[] keys() {
        final long[] keys = new long[this.size()];
        final long[] k = this._set;
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
    
    public long[] keys(long[] a) {
        final int size = this.size();
        if (a.length < size) {
            a = (long[])Array.newInstance(a.getClass().getComponentType(), size);
        }
        final long[] k = this._set;
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
    
    public boolean containsValue(final float val) {
        final byte[] states = this._states;
        final float[] vals = this._values;
        int i = vals.length;
        while (i-- > 0) {
            if (states[i] == 1 && val == vals[i]) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsKey(final long key) {
        return this.contains(key);
    }
    
    public boolean forEachKey(final TLongProcedure procedure) {
        return this.forEach(procedure);
    }
    
    public boolean forEachValue(final TFloatProcedure procedure) {
        final byte[] states = this._states;
        final float[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEntry(final TLongFloatProcedure procedure) {
        final byte[] states = this._states;
        final long[] keys = this._set;
        final float[] values = this._values;
        int i = keys.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean retainEntries(final TLongFloatProcedure procedure) {
        boolean modified = false;
        final byte[] states = this._states;
        final long[] keys = this._set;
        final float[] values = this._values;
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
    
    public void transformValues(final TFloatFunction function) {
        final byte[] states = this._states;
        final float[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1) {
                values[i] = function.execute(values[i]);
            }
        }
    }
    
    public boolean increment(final long key) {
        return this.adjustValue(key, 1.0f);
    }
    
    public boolean adjustValue(final long key, final float amount) {
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        final float[] values = this._values;
        final int n = index;
        values[n] += amount;
        return true;
    }
    
    public float adjustOrPutValue(final long key, final float adjust_amount, final float put_amount) {
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
        final byte previousState = this._states[index];
        this._set[index] = key;
        this._states[index] = 1;
        if (isNewMapping) {
            this.postInsertHook(previousState == 0);
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
            final long key = in.readLong();
            final float val = in.readFloat();
            this.put(key, val);
        }
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEachEntry(new TLongFloatProcedure() {
            private boolean first = true;
            
            public boolean execute(final long key, final float value) {
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
    
    private final class HashProcedure implements TLongFloatProcedure
    {
        private int h;
        
        private HashProcedure() {
            super();
            this.h = 0;
        }
        
        public int getHashCode() {
            return this.h;
        }
        
        public final boolean execute(final long key, final float value) {
            this.h += (TLongFloatHashMap.this._hashingStrategy.computeHashCode(key) ^ HashFunctions.hash(value));
            return true;
        }
    }
    
    private static final class EqProcedure implements TLongFloatProcedure
    {
        private final TLongFloatHashMap _otherMap;
        
        EqProcedure(final TLongFloatHashMap otherMap) {
            super();
            this._otherMap = otherMap;
        }
        
        public final boolean execute(final long key, final float value) {
            final int index = this._otherMap.index(key);
            return index >= 0 && this.eq(value, this._otherMap.get(key));
        }
        
        private final boolean eq(final float v1, final float v2) {
            return v1 == v2;
        }
    }
}
