package gnu.trove;

public abstract class TLongHash extends TPrimitiveHash implements TLongHashingStrategy
{
    protected transient long[] _set;
    protected TLongHashingStrategy _hashingStrategy;
    
    public TLongHash() {
        super();
        this._hashingStrategy = this;
    }
    
    public TLongHash(final int initialCapacity) {
        super(initialCapacity);
        this._hashingStrategy = this;
    }
    
    public TLongHash(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this._hashingStrategy = this;
    }
    
    public TLongHash(final TLongHashingStrategy strategy) {
        super();
        this._hashingStrategy = strategy;
    }
    
    public TLongHash(final int initialCapacity, final TLongHashingStrategy strategy) {
        super(initialCapacity);
        this._hashingStrategy = strategy;
    }
    
    public TLongHash(final int initialCapacity, final float loadFactor, final TLongHashingStrategy strategy) {
        super(initialCapacity, loadFactor);
        this._hashingStrategy = strategy;
    }
    
    public Object clone() {
        final TLongHash h = (TLongHash)super.clone();
        h._set = this._set.clone();
        return h;
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._set = new long[capacity];
        return capacity;
    }
    
    public boolean contains(final long val) {
        return this.index(val) >= 0;
    }
    
    public boolean forEach(final TLongProcedure procedure) {
        final byte[] states = this._states;
        final long[] set = this._set;
        int i = set.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(set[i])) {
                return false;
            }
        }
        return true;
    }
    
    protected void removeAt(final int index) {
        this._set[index] = 0L;
        super.removeAt(index);
    }
    
    protected int index(final long val) {
        final byte[] states = this._states;
        final long[] set = this._set;
        final int length = states.length;
        final int hash = this._hashingStrategy.computeHashCode(val) & Integer.MAX_VALUE;
        int index = hash % length;
        if (states[index] != 0 && (states[index] == 2 || set[index] != val)) {
            final int probe = 1 + hash % (length - 2);
            do {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
            } while (states[index] != 0 && (states[index] == 2 || set[index] != val));
        }
        return (states[index] == 0) ? -1 : index;
    }
    
    protected int insertionIndex(final long val) {
        final byte[] states = this._states;
        final long[] set = this._set;
        final int length = states.length;
        final int hash = this._hashingStrategy.computeHashCode(val) & Integer.MAX_VALUE;
        int index = hash % length;
        if (states[index] == 0) {
            return index;
        }
        if (states[index] == 1 && set[index] == val) {
            return -index - 1;
        }
        final int probe = 1 + hash % (length - 2);
        if (states[index] != 2) {
            do {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
            } while (states[index] == 1 && set[index] != val);
        }
        if (states[index] == 2) {
            final int firstRemoved = index;
            while (states[index] != 0 && (states[index] == 2 || set[index] != val)) {
                index -= probe;
                if (index < 0) {
                    index += length;
                }
            }
            return (states[index] == 1) ? (-index - 1) : firstRemoved;
        }
        return (states[index] == 1) ? (-index - 1) : index;
    }
    
    public final int computeHashCode(final long val) {
        return HashFunctions.hash(val);
    }
}
