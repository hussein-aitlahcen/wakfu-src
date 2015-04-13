package gnu.trove;

import java.util.*;
import java.io.*;

public class TLongHashSet extends TLongHash implements Externalizable
{
    static final long serialVersionUID = 1L;
    
    public TLongHashSet() {
        super();
    }
    
    public TLongHashSet(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public TLongHashSet(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    public TLongHashSet(final long[] array) {
        this(array.length);
        this.addAll(array);
    }
    
    public TLongHashSet(final TLongHashingStrategy strategy) {
        super(strategy);
    }
    
    public TLongHashSet(final int initialCapacity, final TLongHashingStrategy strategy) {
        super(initialCapacity, strategy);
    }
    
    public TLongHashSet(final int initialCapacity, final float loadFactor, final TLongHashingStrategy strategy) {
        super(initialCapacity, loadFactor, strategy);
    }
    
    public TLongHashSet(final long[] array, final TLongHashingStrategy strategy) {
        this(array.length, strategy);
        this.addAll(array);
    }
    
    public TLongIterator iterator() {
        return new TLongIterator(this);
    }
    
    public boolean add(final long val) {
        final int index = this.insertionIndex(val);
        if (index < 0) {
            return false;
        }
        final byte previousState = this._states[index];
        this._set[index] = val;
        this._states[index] = 1;
        this.postInsertHook(previousState == 0);
        return true;
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final long[] oldSet = this._set;
        final byte[] oldStates = this._states;
        this._set = new long[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldStates[i] == 1) {
                final long o = oldSet[i];
                final int index = this.insertionIndex(o);
                this._set[index] = o;
                this._states[index] = 1;
            }
        }
    }
    
    public long[] toArray() {
        final long[] result = new long[this.size()];
        final long[] set = this._set;
        final byte[] states = this._states;
        int i = states.length;
        int j = 0;
        while (i-- > 0) {
            if (states[i] == 1) {
                result[j++] = set[i];
            }
        }
        return result;
    }
    
    public void clear() {
        super.clear();
        final long[] set = this._set;
        final byte[] states = this._states;
        int i = set.length;
        while (i-- > 0) {
            set[i] = 0L;
            states[i] = 0;
        }
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TLongHashSet)) {
            return false;
        }
        final TLongHashSet that = (TLongHashSet)other;
        return that.size() == this.size() && this.forEach(new TLongProcedure() {
            public final boolean execute(final long value) {
                return that.contains(value);
            }
        });
    }
    
    public int hashCode() {
        final HashProcedure p = new HashProcedure();
        this.forEach(p);
        return p.getHashCode();
    }
    
    public boolean remove(final long val) {
        final int index = this.index(val);
        if (index >= 0) {
            this.removeAt(index);
            return true;
        }
        return false;
    }
    
    public boolean containsAll(final long[] array) {
        int i = array.length;
        while (i-- > 0) {
            if (!this.contains(array[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean addAll(final long[] array) {
        boolean changed = false;
        int i = array.length;
        while (i-- > 0) {
            if (this.add(array[i])) {
                changed = true;
            }
        }
        return changed;
    }
    
    public boolean removeAll(final long[] array) {
        boolean changed = false;
        int i = array.length;
        while (i-- > 0) {
            if (this.remove(array[i])) {
                changed = true;
            }
        }
        return changed;
    }
    
    public boolean retainAll(final long[] array) {
        boolean changed = false;
        Arrays.sort(array);
        final long[] set = this._set;
        final byte[] states = this._states;
        int i = set.length;
        while (i-- > 0) {
            if (states[i] == 1 && Arrays.binarySearch(array, set[i]) < 0) {
                this.remove(set[i]);
                changed = true;
            }
        }
        return changed;
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeInt(this._size);
        final SerializationProcedure writeProcedure = new SerializationProcedure(out);
        if (!this.forEach(writeProcedure)) {
            throw writeProcedure.exception;
        }
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        int size = in.readInt();
        this.setUp(size);
        while (size-- > 0) {
            final long val = in.readLong();
            this.add(val);
        }
    }
    
    private final class HashProcedure implements TLongProcedure
    {
        private int h;
        
        private HashProcedure() {
            super();
            this.h = 0;
        }
        
        public int getHashCode() {
            return this.h;
        }
        
        public final boolean execute(final long key) {
            this.h += TLongHashSet.this._hashingStrategy.computeHashCode(key);
            return true;
        }
    }
}
