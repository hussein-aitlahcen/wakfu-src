package gnu.trove;

import java.util.*;
import java.io.*;

public abstract class TObjectHash<T> extends THash implements TObjectHashingStrategy<T>
{
    static final long serialVersionUID = -3461112548087185871L;
    protected transient Object[] _set;
    protected TObjectHashingStrategy<T> _hashingStrategy;
    protected static final Object REMOVED;
    protected static final Object FREE;
    
    public TObjectHash() {
        super();
        this._hashingStrategy = this;
    }
    
    public TObjectHash(final TObjectHashingStrategy<T> strategy) {
        super();
        this._hashingStrategy = strategy;
    }
    
    public TObjectHash(final int initialCapacity) {
        super(initialCapacity);
        this._hashingStrategy = this;
    }
    
    public TObjectHash(final int initialCapacity, final TObjectHashingStrategy<T> strategy) {
        super(initialCapacity);
        this._hashingStrategy = strategy;
    }
    
    public TObjectHash(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this._hashingStrategy = this;
    }
    
    public TObjectHash(final int initialCapacity, final float loadFactor, final TObjectHashingStrategy<T> strategy) {
        super(initialCapacity, loadFactor);
        this._hashingStrategy = strategy;
    }
    
    public TObjectHash<T> clone() {
        final TObjectHash<T> h = (TObjectHash<T>)super.clone();
        h._set = this._set.clone();
        return h;
    }
    
    protected int capacity() {
        return this._set.length;
    }
    
    protected void removeAt(final int index) {
        this._set[index] = TObjectHash.REMOVED;
        super.removeAt(index);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        Arrays.fill(this._set = new Object[capacity], TObjectHash.FREE);
        return capacity;
    }
    
    public boolean forEach(final TObjectProcedure<T> procedure) {
        final Object[] set = this._set;
        int i = set.length;
        while (i-- > 0) {
            if (set[i] != TObjectHash.FREE && set[i] != TObjectHash.REMOVED && !procedure.execute((T)set[i])) {
                return false;
            }
        }
        return true;
    }
    
    public boolean contains(final Object obj) {
        return this.index(obj) >= 0;
    }
    
    protected int index(final T obj) {
        final TObjectHashingStrategy<T> hashing_strategy = this._hashingStrategy;
        final Object[] set = this._set;
        final int length = set.length;
        final int hash = hashing_strategy.computeHashCode(obj) & Integer.MAX_VALUE;
        int index = hash % length;
        Object cur = set[index];
        if (cur == TObjectHash.FREE) {
            return -1;
        }
        if (cur == TObjectHash.REMOVED || !hashing_strategy.equals((T)cur, obj)) {
            final int probe = 1 + hash % (length - 2);
            do {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
                cur = set[index];
            } while (cur != TObjectHash.FREE && (cur == TObjectHash.REMOVED || !this._hashingStrategy.equals((T)cur, obj)));
        }
        return (cur == TObjectHash.FREE) ? -1 : index;
    }
    
    protected int insertionIndex(final T obj) {
        final TObjectHashingStrategy<T> hashing_strategy = this._hashingStrategy;
        final Object[] set = this._set;
        final int length = set.length;
        final int hash = hashing_strategy.computeHashCode(obj) & Integer.MAX_VALUE;
        int index = hash % length;
        Object cur = set[index];
        if (cur == TObjectHash.FREE) {
            return index;
        }
        if (cur != TObjectHash.REMOVED && hashing_strategy.equals((T)cur, obj)) {
            return -index - 1;
        }
        final int probe = 1 + hash % (length - 2);
        if (cur != TObjectHash.REMOVED) {
            do {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
                cur = set[index];
            } while (cur != TObjectHash.FREE && cur != TObjectHash.REMOVED && !hashing_strategy.equals((T)cur, obj));
        }
        if (cur == TObjectHash.REMOVED) {
            final int firstRemoved = index;
            while (cur != TObjectHash.FREE && (cur == TObjectHash.REMOVED || !hashing_strategy.equals((T)cur, obj))) {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
                cur = set[index];
            }
            return (cur != TObjectHash.FREE) ? (-index - 1) : firstRemoved;
        }
        return (cur != TObjectHash.FREE) ? (-index - 1) : index;
    }
    
    public final int computeHashCode(final T o) {
        return (o == null) ? 0 : o.hashCode();
    }
    
    public final boolean equals(final T o1, final T o2) {
        return (o1 == null) ? (o2 == null) : o1.equals(o2);
    }
    
    protected final void throwObjectContractViolation(final Object o1, final Object o2) throws IllegalArgumentException {
        throw new IllegalArgumentException("Equal objects must have equal hashcodes. During rehashing, Trove discovered that the following two objects claim to be equal (as in java.lang.Object.equals()) but their hashCodes (or those calculated by your TObjectHashingStrategy) are not equal.This violates the general contract of java.lang.Object.hashCode().  See bullet point two in that method's documentation. object #1 =" + o1 + "; object #2 =" + o2);
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeByte(0);
        if (this._hashingStrategy == this) {
            out.writeObject(null);
        }
        else {
            out.writeObject(this._hashingStrategy);
        }
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        in.readByte();
        this._hashingStrategy = (TObjectHashingStrategy<T>)in.readObject();
        if (this._hashingStrategy == null) {
            this._hashingStrategy = this;
        }
    }
    
    static {
        REMOVED = new Object();
        FREE = new Object();
    }
}
