package gnu.trove;

import java.util.*;
import java.lang.reflect.*;
import java.io.*;

public class TIntByteHashMap extends TIntHash implements Externalizable
{
    static final long serialVersionUID = 1L;
    private final TIntByteProcedure PUT_ALL_PROC;
    protected transient byte[] _values;
    
    public TIntByteHashMap() {
        super();
        this.PUT_ALL_PROC = new TIntByteProcedure() {
            public boolean execute(final int key, final byte value) {
                TIntByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntByteHashMap(final int initialCapacity) {
        super(initialCapacity);
        this.PUT_ALL_PROC = new TIntByteProcedure() {
            public boolean execute(final int key, final byte value) {
                TIntByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntByteHashMap(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this.PUT_ALL_PROC = new TIntByteProcedure() {
            public boolean execute(final int key, final byte value) {
                TIntByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntByteHashMap(final TIntHashingStrategy strategy) {
        super(strategy);
        this.PUT_ALL_PROC = new TIntByteProcedure() {
            public boolean execute(final int key, final byte value) {
                TIntByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntByteHashMap(final int initialCapacity, final TIntHashingStrategy strategy) {
        super(initialCapacity, strategy);
        this.PUT_ALL_PROC = new TIntByteProcedure() {
            public boolean execute(final int key, final byte value) {
                TIntByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public TIntByteHashMap(final int initialCapacity, final float loadFactor, final TIntHashingStrategy strategy) {
        super(initialCapacity, loadFactor, strategy);
        this.PUT_ALL_PROC = new TIntByteProcedure() {
            public boolean execute(final int key, final byte value) {
                TIntByteHashMap.this.put(key, value);
                return true;
            }
        };
    }
    
    public Object clone() {
        final TIntByteHashMap m = (TIntByteHashMap)super.clone();
        m._values = this._values.clone();
        return m;
    }
    
    public TIntByteIterator iterator() {
        return new TIntByteIterator(this);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._values = new byte[capacity];
        return capacity;
    }
    
    public byte put(final int key, final byte value) {
        final int index = this.insertionIndex(key);
        return this.doPut(key, value, index);
    }
    
    public byte putIfAbsent(final int key, final byte value) {
        final int index = this.insertionIndex(key);
        if (index < 0) {
            return this._values[-index - 1];
        }
        return this.doPut(key, value, index);
    }
    
    private byte doPut(final int key, final byte value, int index) {
        byte previous = 0;
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
    
    public void putAll(final TIntByteHashMap map) {
        map.forEachEntry(this.PUT_ALL_PROC);
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final int[] oldKeys = this._set;
        final byte[] oldVals = this._values;
        final byte[] oldStates = this._states;
        this._set = new int[newCapacity];
        this._values = new byte[newCapacity];
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
    
    public byte get(final int key) {
        final int index = this.index(key);
        return (byte)((index < 0) ? 0 : this._values[index]);
    }
    
    public void clear() {
        super.clear();
        final int[] keys = this._set;
        final byte[] vals = this._values;
        final byte[] states = this._states;
        Arrays.fill(this._set, 0, this._set.length, 0);
        Arrays.fill(this._values, 0, this._values.length, (byte)0);
        Arrays.fill(this._states, 0, this._states.length, (byte)0);
    }
    
    public byte remove(final int key) {
        byte prev = 0;
        final int index = this.index(key);
        if (index >= 0) {
            prev = this._values[index];
            this.removeAt(index);
        }
        return prev;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TIntByteHashMap)) {
            return false;
        }
        final TIntByteHashMap that = (TIntByteHashMap)other;
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
    
    public byte[] getValues() {
        final byte[] vals = new byte[this.size()];
        final byte[] v = this._values;
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
    
    public boolean containsValue(final byte val) {
        final byte[] states = this._states;
        final byte[] vals = this._values;
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
    
    public boolean forEachValue(final TByteProcedure procedure) {
        final byte[] states = this._states;
        final byte[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean forEachEntry(final TIntByteProcedure procedure) {
        final byte[] states = this._states;
        final int[] keys = this._set;
        final byte[] values = this._values;
        int i = keys.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean retainEntries(final TIntByteProcedure procedure) {
        boolean modified = false;
        final byte[] states = this._states;
        final int[] keys = this._set;
        final byte[] values = this._values;
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
    
    public void transformValues(final TByteFunction function) {
        final byte[] states = this._states;
        final byte[] values = this._values;
        int i = values.length;
        while (i-- > 0) {
            if (states[i] == 1) {
                values[i] = function.execute(values[i]);
            }
        }
    }
    
    public boolean increment(final int key) {
        return this.adjustValue(key, (byte)1);
    }
    
    public boolean adjustValue(final int key, final byte amount) {
        final int index = this.index(key);
        if (index < 0) {
            return false;
        }
        final byte[] values = this._values;
        final int n = index;
        values[n] += amount;
        return true;
    }
    
    public byte adjustOrPutValue(final int key, final byte adjust_amount, final byte put_amount) {
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
            final byte val = in.readByte();
            this.put(key, val);
        }
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEachEntry(new TIntByteProcedure() {
            private boolean first = true;
            
            public boolean execute(final int key, final byte value) {
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
    
    private final class HashProcedure implements TIntByteProcedure
    {
        private int h;
        
        private HashProcedure() {
            super();
            this.h = 0;
        }
        
        public int getHashCode() {
            return this.h;
        }
        
        public final boolean execute(final int key, final byte value) {
            this.h += (TIntByteHashMap.this._hashingStrategy.computeHashCode(key) ^ HashFunctions.hash(value));
            return true;
        }
    }
    
    private static final class EqProcedure implements TIntByteProcedure
    {
        private final TIntByteHashMap _otherMap;
        
        EqProcedure(final TIntByteHashMap otherMap) {
            super();
            this._otherMap = otherMap;
        }
        
        public final boolean execute(final int key, final byte value) {
            final int index = this._otherMap.index(key);
            return index >= 0 && this.eq(value, this._otherMap.get(key));
        }
        
        private final boolean eq(final byte v1, final byte v2) {
            return v1 == v2;
        }
    }
}
