package gnu.trove;

import java.util.*;
import java.io.*;

public class TIntHashSet extends TIntHash implements Externalizable
{
    static final long serialVersionUID = 1L;
    
    public TIntHashSet() {
        super();
    }
    
    public TIntHashSet(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public TIntHashSet(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    public TIntHashSet(final int[] array) {
        this(array.length);
        this.addAll(array);
    }
    
    public TIntHashSet(final TIntHashingStrategy strategy) {
        super(strategy);
    }
    
    public TIntHashSet(final int initialCapacity, final TIntHashingStrategy strategy) {
        super(initialCapacity, strategy);
    }
    
    public TIntHashSet(final int initialCapacity, final float loadFactor, final TIntHashingStrategy strategy) {
        super(initialCapacity, loadFactor, strategy);
    }
    
    public TIntHashSet(final int[] array, final TIntHashingStrategy strategy) {
        this(array.length, strategy);
        this.addAll(array);
    }
    
    public TIntIterator iterator() {
        return new TIntIterator(this);
    }
    
    public boolean add(final int val) {
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
        final int[] oldSet = this._set;
        final byte[] oldStates = this._states;
        this._set = new int[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldStates[i] == 1) {
                final int o = oldSet[i];
                final int index = this.insertionIndex(o);
                this._set[index] = o;
                this._states[index] = 1;
            }
        }
    }
    
    public int[] toArray() {
        final int[] result = new int[this.size()];
        final int[] set = this._set;
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
        final int[] set = this._set;
        final byte[] states = this._states;
        int i = set.length;
        while (i-- > 0) {
            set[i] = 0;
            states[i] = 0;
        }
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TIntHashSet)) {
            return false;
        }
        final TIntHashSet that = (TIntHashSet)other;
        return that.size() == this.size() && this.forEach(new TIntProcedure() {
            public final boolean execute(final int value) {
                return that.contains(value);
            }
        });
    }
    
    public int hashCode() {
        final HashProcedure p = new HashProcedure();
        this.forEach(p);
        return p.getHashCode();
    }
    
    public boolean remove(final int val) {
        final int index = this.index(val);
        if (index >= 0) {
            this.removeAt(index);
            return true;
        }
        return false;
    }
    
    public boolean containsAll(final int[] array) {
        int i = array.length;
        while (i-- > 0) {
            if (!this.contains(array[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean addAll(final int[] array) {
        boolean changed = false;
        int i = array.length;
        while (i-- > 0) {
            if (this.add(array[i])) {
                changed = true;
            }
        }
        return changed;
    }
    
    public boolean removeAll(final int[] array) {
        boolean changed = false;
        int i = array.length;
        while (i-- > 0) {
            if (this.remove(array[i])) {
                changed = true;
            }
        }
        return changed;
    }
    
    public boolean retainAll(final int[] array) {
        boolean changed = false;
        Arrays.sort(array);
        final int[] set = this._set;
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
            final int val = in.readInt();
            this.add(val);
        }
    }
    
    private final class HashProcedure implements TIntProcedure
    {
        private int h;
        
        private HashProcedure() {
            super();
            this.h = 0;
        }
        
        public int getHashCode() {
            return this.h;
        }
        
        public final boolean execute(final int key) {
            this.h += TIntHashSet.this._hashingStrategy.computeHashCode(key);
            return true;
        }
    }
}
