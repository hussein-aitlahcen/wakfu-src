package gnu.trove;

public class TLongIterator extends TPrimitiveIterator
{
    private final TLongHash _hash;
    
    public TLongIterator(final TLongHash hash) {
        super(hash);
        this._hash = hash;
    }
    
    public long next() {
        this.moveToNextIndex();
        return this._hash._set[this._index];
    }
}
