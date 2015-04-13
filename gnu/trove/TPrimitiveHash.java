package gnu.trove;

public abstract class TPrimitiveHash extends THash
{
    protected transient byte[] _states;
    protected static final byte FREE = 0;
    protected static final byte FULL = 1;
    protected static final byte REMOVED = 2;
    
    public TPrimitiveHash() {
        super();
    }
    
    public TPrimitiveHash(final int initialCapacity) {
        this(initialCapacity, 0.5f);
    }
    
    public TPrimitiveHash(final int initialCapacity, final float loadFactor) {
        super();
        this._loadFactor = loadFactor;
        this.setUp(HashFunctions.fastCeil(initialCapacity / loadFactor));
    }
    
    public Object clone() {
        final TPrimitiveHash h = (TPrimitiveHash)super.clone();
        h._states = this._states.clone();
        return h;
    }
    
    protected int capacity() {
        return this._states.length;
    }
    
    protected void removeAt(final int index) {
        this._states[index] = 2;
        super.removeAt(index);
    }
    
    protected int setUp(final int initialCapacity) {
        final int capacity = super.setUp(initialCapacity);
        this._states = new byte[capacity];
        return capacity;
    }
}
