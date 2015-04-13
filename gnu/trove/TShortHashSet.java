package gnu.trove;

import java.util.*;
import java.io.*;

public class TShortHashSet extends TShortHash implements Externalizable
{
    static final long serialVersionUID = 1L;
    
    public TShortHashSet() {
        super();
    }
    
    public TShortHashSet(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public TShortHashSet(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    public TShortHashSet(final short[] array) {
        this(array.length);
        this.addAll(array);
    }
    
    public TShortHashSet(final TShortHashingStrategy strategy) {
        super(strategy);
    }
    
    public TShortHashSet(final int initialCapacity, final TShortHashingStrategy strategy) {
        super(initialCapacity, strategy);
    }
    
    public TShortHashSet(final int initialCapacity, final float loadFactor, final TShortHashingStrategy strategy) {
        super(initialCapacity, loadFactor, strategy);
    }
    
    public TShortHashSet(final short[] array, final TShortHashingStrategy strategy) {
        this(array.length, strategy);
        this.addAll(array);
    }
    
    public TShortIterator iterator() {
        return new TShortIterator(this);
    }
    
    public boolean add(final short val) {
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
        final short[] oldSet = this._set;
        final byte[] oldStates = this._states;
        this._set = new short[newCapacity];
        this._states = new byte[newCapacity];
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldStates[i] == 1) {
                final short o = oldSet[i];
                final int index = this.insertionIndex(o);
                this._set[index] = o;
                this._states[index] = 1;
            }
        }
    }
    
    public short[] toArray() {
        final short[] result = new short[this.size()];
        final short[] set = this._set;
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
        final short[] set = this._set;
        final byte[] states = this._states;
        int i = set.length;
        while (i-- > 0) {
            set[i] = 0;
            states[i] = 0;
        }
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof TShortHashSet)) {
            return false;
        }
        final TShortHashSet that = (TShortHashSet)other;
        return that.size() == this.size() && this.forEach(new TShortProcedure() {
            public final boolean execute(final short value) {
                return that.contains(value);
            }
        });
    }
    
    public int hashCode() {
        final HashProcedure p = new HashProcedure();
        this.forEach(p);
        return p.getHashCode();
    }
    
    public boolean remove(final short val) {
        final int index = this.index(val);
        if (index >= 0) {
            this.removeAt(index);
            return true;
        }
        return false;
    }
    
    public boolean containsAll(final short[] array) {
        int i = array.length;
        while (i-- > 0) {
            if (!this.contains(array[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean addAll(final short[] array) {
        boolean changed = false;
        int i = array.length;
        while (i-- > 0) {
            if (this.add(array[i])) {
                changed = true;
            }
        }
        return changed;
    }
    
    public boolean removeAll(final short[] array) {
        boolean changed = false;
        int i = array.length;
        while (i-- > 0) {
            if (this.remove(array[i])) {
                changed = true;
            }
        }
        return changed;
    }
    
    public boolean retainAll(final short[] array) {
        boolean changed = false;
        Arrays.sort(array);
        final short[] set = this._set;
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
            final short val = in.readShort();
            this.add(val);
        }
    }
    
    private final class HashProcedure implements TShortProcedure
    {
        private int h;
        
        private HashProcedure() {
            super();
            this.h = 0;
        }
        
        public int getHashCode() {
            return this.h;
        }
        
        public final boolean execute(final short key) {
            this.h += TShortHashSet.this._hashingStrategy.computeHashCode(key);
            return true;
        }
    }
}
