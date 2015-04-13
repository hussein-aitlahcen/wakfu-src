package gnu.trove;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class TByteIntHashMap extends TByteHash implements Externalizable
{
    static final long serialVersionUID = 1L;
    private final TByteIntProcedure PUT_ALL_PROC;
    protected transient int[] _values;
    
    public TByteIntHashMap() {
        super();
        this.PUT_ALL_PROC = new TByteIntProcedure() {
            public boolean execute(final byte key, final int value) {
                TByteIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TByteIntHashMap(final int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TByteIntProcedure() {
            public boolean execute(final byte key, final int value) {
                TByteIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TByteIntHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TByteIntProcedure() {
            public boolean execute(final byte key, final int value) {
                TByteIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TByteIntHashMap(final TByteHashingStrategy strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TByteIntProcedure() {
            public boolean execute(final byte key, final int value) {
                TByteIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TByteIntHashMap(final int initialCapacity, final TByteHashingStrategy strategy) {
        super(initialCapacity, strategy);
        this.PUT_ALL_PROC = new TByteIntProcedure() {
            public boolean execute(final byte key, final int value) {
                TByteIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TByteIntHashMap(final int initialCapacity, final float loadFactor, final TByteHashingStrategy strategy) {
        super(initialCapacity, loadFactor, strategy);
        this.PUT_ALL_PROC = new TByteIntProcedure() {
            public boolean execute(final byte key, final int value) {
                TByteIntHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public Object clone() {
        final TByteIntHashMap m = (TByteIntHashMap)super.clone();
        m._values = this._values.clone();
        return m;
    }
    
    public TByteIntIterator iterator() {
        return new TByteIntIterator(this);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._values = new int[capacity];
        return capacity;
    }
    
    public int put(final byte key, final int value) {
        final int index = this.insertionIndex(key);
        return this.doPut(key, value, index);
    }
    
    public int putIfAbsent(final byte key, final int value) {
        final int index = this.insertionIndex(key);
        if (index < 0) {
            return this._values[-index - 1];
        }
        return this.doPut(key, value, index);
    }
    
    private int doPut(final byte key, final int value, int index) {
        int previous = 0;
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
    
    public void putAll(final TByteIntHashMap map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final byte[] oldKeys = this._set;
        final int[] oldVals = this._values;
        final byte[] oldStates = this._states;
        this._set = new byte[newCapacity];
        this._values = new int[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldStates[i] == 1) {
                final byte o = oldKeys[i];
                final int index = this.insertionIndex(o);
                this._set[index] = o;
                this._values[index] = oldVals[i];
                this._states[index] = 1;
            }
        }
    }
    
    public int get(final byte key) {
        final int index = this.index(key);
        return (index < 0) ? 0 : this._values[index];
    }
    
    public void clear() {
        super.clear();
        final byte[] keys = this._set;
        final int[] vals = this._values;
        final byte[] states = this._states;
        Arrays.fill(this._set, 0, this._set.length, (byte)0);
        Arrays.fill(this._values, 0, this._values.length, 0);
        Arrays.fill(this._states, 0, this._states.length, (byte)0);
    }
    
    public int remove(final byte key) {
        int prev = 0;
        final int index = this.index(key);
        if (index >= 0) {
            prev = this._values[index];
            this.removeAt(index);
        }
        return prev;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TByteIntHashMap)) {
            return false;
        }
        final TByteIntHashMap that = (TByteIntHashMap)other;
        return that.size() == this.size() && this.forEachEntry(new EqProcedure(that));
    }
    
    public int hashCode() {
        final HashProcedure p = new HashProcedure();
        this.forEachEntry(p);
        return p.getHashCode();
    }
    
    protected void removeAt(final int index) {
        this._values[index] = 0;
        super.removeAt(index);
    }
    
    public int[] getValues() {
        final int[] vals = new int[this.size()];
        final int[] v = this._values;
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
    
    public byte[] keys() {
        final byte[] keys = new byte[this.size()];
        final byte[] k = this._set;
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
    
    public byte[] keys(byte[] a) {
        final int size = this.size();
        if (a.length < size) {
            a = (byte[])Array.newInstance(a.getClass().getComponentType(), size);
        }
        final byte[] k = this._set;
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
    
    public boolean containsValue(final int val) {
        final byte[] states = this._states;
        final int[] vals = this._values;
        int i = vals.length;
        while (i-- > 0) {
            if (states[i] == 1 && val == vals[i]) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsKey(final byte key) {
        return this.contains(key);
    }
    
    public boolean forEachKey(final TByteProcedure procedure) {
        return this.forEach(procedure);
    }
    
    public boolean forEachValue(final TIntProcedure procedure) {
        final byte[] states = this._states;
        final int[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEntry(final TByteIntProcedure procedure) {
        final byte[] states = this._states;
        final byte[] keys = this._set;
        final int[] values = this._values;
        int i = keys.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean retainEntries(final TByteIntProcedure procedure) {
        boolean modified = false;
        final byte[] states = this._states;
        final byte[] keys = this._set;
        final int[] values = this._values;
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
    
    public void transformValues(final TIntFunction function) {
        final byte[] states = this._states;
        final int[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1) {
                values[i] = function.execute(values[i]);
            }
        }
    }
    
    public boolean increment(final byte key) {
        return this.adjustValue(key, 1);
    }
    
    public boolean adjustValue(final byte key, final int amount) {
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        final int[] values = this._values;
        final int n = index;
        values[n] += amount;
        return true;
    }
    
    public int adjustOrPutValue(final byte key, final int adjust_amount, final int put_amount) {
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
            final byte key = in.readByte();
            final int val = in.readInt();
            this.put(key, val);
        }
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEachEntry(new TByteIntProcedure() {
            private boolean first = true;
            
            public boolean execute(final byte key, final int value) {
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
    
    private final class HashProcedure implements TByteIntProcedure
    {
        private int h;
        
        private HashProcedure() {
            super();
            this.h = 0;
        }
        
        public int getHashCode() {
            return this.h;
        }
        
        public final boolean execute(final byte key, final int value) {
            this.h += (TByteIntHashMap.this._hashingStrategy.computeHashCode(key) ^ HashFunctions.hash(value));
            return true;
        }
    }
    
    private static final class EqProcedure implements TByteIntProcedure
    {
        private final TByteIntHashMap _otherMap;
        
        EqProcedure(final TByteIntHashMap otherMap) {
            super();
            this._otherMap = otherMap;
        }
        
        public final boolean execute(final byte key, final int value) {
            final int index = this._otherMap.index(key);
            return index >= 0 && this.eq(value, this._otherMap.get(key));
        }
        
        private final boolean eq(final int v1, final int v2) {
            return v1 == v2;
        }
    }
}
