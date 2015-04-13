package gnu.trove;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class TIntLongHashMap extends TIntHash implements Externalizable
{
    static final long serialVersionUID = 1L;
    private final TIntLongProcedure PUT_ALL_PROC;
    protected transient long[] _values;
    
    public TIntLongHashMap() {
        super();
        this.PUT_ALL_PROC = new TIntLongProcedure() {
            public boolean execute(final int key, final long value) {
                TIntLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntLongHashMap(final int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TIntLongProcedure() {
            public boolean execute(final int key, final long value) {
                TIntLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntLongHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TIntLongProcedure() {
            public boolean execute(final int key, final long value) {
                TIntLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntLongHashMap(final TIntHashingStrategy strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TIntLongProcedure() {
            public boolean execute(final int key, final long value) {
                TIntLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntLongHashMap(final int initialCapacity, final TIntHashingStrategy strategy) {
        super(initialCapacity, strategy);
        this.PUT_ALL_PROC = new TIntLongProcedure() {
            public boolean execute(final int key, final long value) {
                TIntLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntLongHashMap(final int initialCapacity, final float loadFactor, final TIntHashingStrategy strategy) {
        super(initialCapacity, loadFactor, strategy);
        this.PUT_ALL_PROC = new TIntLongProcedure() {
            public boolean execute(final int key, final long value) {
                TIntLongHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public Object clone() {
        final TIntLongHashMap m = (TIntLongHashMap)super.clone();
        m._values = this._values.clone();
        return m;
    }
    
    public TIntLongIterator iterator() {
        return new TIntLongIterator(this);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._values = new long[capacity];
        return capacity;
    }
    
    public long put(final int key, final long value) {
        final int index = this.insertionIndex(key);
        return this.doPut(key, value, index);
    }
    
    public long putIfAbsent(final int key, final long value) {
        final int index = this.insertionIndex(key);
        if (index < 0) {
            return this._values[-index - 1];
        }
        return this.doPut(key, value, index);
    }
    
    private long doPut(final int key, final long value, int index) {
        long previous = 0L;
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
    
    public void putAll(final TIntLongHashMap map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final int[] oldKeys = this._set;
        final long[] oldVals = this._values;
        final byte[] oldStates = this._states;
        this._set = new int[newCapacity];
        this._values = new long[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldStates[i] == 1) {
                final int o = oldKeys[i];
                final int index = this.insertionIndex(o);
                this._set[index] = o;
                this._values[index] = oldVals[i];
                this._states[index] = 1;
            }
        }
    }
    
    public long get(final int key) {
        final int index = this.index(key);
        return (index < 0) ? 0L : this._values[index];
    }
    
    public void clear() {
        super.clear();
        final int[] keys = this._set;
        final long[] vals = this._values;
        final byte[] states = this._states;
        Arrays.fill(this._set, 0, this._set.length, 0);
        Arrays.fill(this._values, 0, this._values.length, 0L);
        Arrays.fill(this._states, 0, this._states.length, (byte)0);
    }
    
    public long remove(final int key) {
        long prev = 0L;
        final int index = this.index(key);
        if (index >= 0) {
            prev = this._values[index];
            this.removeAt(index);
        }
        return prev;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TIntLongHashMap)) {
            return false;
        }
        final TIntLongHashMap that = (TIntLongHashMap)other;
        return that.size() == this.size() && this.forEachEntry(new EqProcedure(that));
    }
    
    public int hashCode() {
        final HashProcedure p = new HashProcedure();
        this.forEachEntry(p);
        return p.getHashCode();
    }
    
    protected void removeAt(final int index) {
        this._values[index] = 0L;
        super.removeAt(index);
    }
    
    public long[] getValues() {
        final long[] vals = new long[this.size()];
        final long[] v = this._values;
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
    
    public int[] keys() {
        final int[] keys = new int[this.size()];
        final int[] k = this._set;
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
    
    public int[] keys(int[] a) {
        final int size = this.size();
        if (a.length < size) {
            a = (int[])Array.newInstance(a.getClass().getComponentType(), size);
        }
        final int[] k = this._set;
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
    
    public boolean containsValue(final long val) {
        final byte[] states = this._states;
        final long[] vals = this._values;
        int i = vals.length;
        while (i-- > 0) {
            if (states[i] == 1 && val == vals[i]) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsKey(final int key) {
        return this.contains(key);
    }
    
    public boolean forEachKey(final TIntProcedure procedure) {
        return this.forEach(procedure);
    }
    
    public boolean forEachValue(final TLongProcedure procedure) {
        final byte[] states = this._states;
        final long[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEntry(final TIntLongProcedure procedure) {
        final byte[] states = this._states;
        final int[] keys = this._set;
        final long[] values = this._values;
        int i = keys.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean retainEntries(final TIntLongProcedure procedure) {
        boolean modified = false;
        final byte[] states = this._states;
        final int[] keys = this._set;
        final long[] values = this._values;
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
    
    public void transformValues(final TLongFunction function) {
        final byte[] states = this._states;
        final long[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1) {
                values[i] = function.execute(values[i]);
            }
        }
    }
    
    public boolean increment(final int key) {
        return this.adjustValue(key, 1L);
    }
    
    public boolean adjustValue(final int key, final long amount) {
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        final long[] values = this._values;
        final int n = index;
        values[n] += amount;
        return true;
    }
    
    public long adjustOrPutValue(final int key, final long adjust_amount, final long put_amount) {
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
            final int key = in.readInt();
            final long val = in.readLong();
            this.put(key, val);
        }
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEachEntry(new TIntLongProcedure() {
            private boolean first = true;
            
            public boolean execute(final int key, final long value) {
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
    
    private final class HashProcedure implements TIntLongProcedure
    {
        private int h;
        
        private HashProcedure() {
            super();
            this.h = 0;
        }
        
        public int getHashCode() {
            return this.h;
        }
        
        public final boolean execute(final int key, final long value) {
            this.h += (TIntLongHashMap.this._hashingStrategy.computeHashCode(key) ^ HashFunctions.hash(value));
            return true;
        }
    }
    
    private static final class EqProcedure implements TIntLongProcedure
    {
        private final TIntLongHashMap _otherMap;
        
        EqProcedure(final TIntLongHashMap otherMap) {
            super();
            this._otherMap = otherMap;
        }
        
        public final boolean execute(final int key, final long value) {
            final int index = this._otherMap.index(key);
            return index >= 0 && this.eq(value, this._otherMap.get(key));
        }
        
        private final boolean eq(final long v1, final long v2) {
            return v1 == v2;
        }
    }
}
