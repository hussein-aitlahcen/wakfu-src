package gnu.trove;

public abstract class TShortHash extends TPrimitiveHash implements TShortHashingStrategy
{
    protected transient short[] _set;
    protected TShortHashingStrategy _hashingStrategy;
    
    public TShortHash() {
        super();
        this._hashingStrategy = this;
    }
    
    public TShortHash(final int initialCapacity) {
        super(initialCapacity);
        this._hashingStrategy = this;
    }
    
    public TShortHash(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
        this._hashingStrategy = this;
    }
    
    public TShortHash(final TShortHashingStrategy strategy) {
        super();
        this._hashingStrategy = strategy;
    }
    
    public TShortHash(final int initialCapacity, final TShortHashingStrategy strategy) {
        super(initialCapacity);
        this._hashingStrategy = strategy;
    }
    
    public TShortHash(final int initialCapacity, final float loadFactor, final TShortHashingStrategy strategy) {
        super(initialCapacity, loadFactor);
        this._hashingStrategy = strategy;
    }
    
    public Object clone() {
        final TShortHash h = (TShortHash)super.clone();
        h._set = this._set.clone();
        return h;
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._set = new short[capacity];
        return capacity;
    }
    
    public boolean contains(final short val) {
        return this.index(val) >= 0;
    }
    
    public boolean forEach(final TShortProcedure procedure) {
        final byte[] states = this._states;
        final short[] set = this._set;
        int i = set.length;
        while (i-- > 0) {
            if (states[i] == 1 && !procedure.execute(set[i])) {
                return false;
            }
        }
        return true;
    }
    
    protected void removeAt(final int index) {
        this._set[index] = 0;
        super.removeAt(index);
    }
    
    protected int index(final short val) {
        final byte[] states = this._states;
        final short[] set = this._set;
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
    
    protected int insertionIndex(final short val) {
        final byte[] states = this._states;
        final short[] set = this._set;
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
    
    public final int computeHashCode(final short val) {
        return HashFunctions.hash(val);
    }
}
