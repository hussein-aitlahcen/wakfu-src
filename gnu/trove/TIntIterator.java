package gnu.trove;

public class TIntIterator extends TPrimitiveIterator
{
    private final TIntHash _hash;
    
    public TIntIterator(final TIntHash hash) {
        super(hash);
        this._hash = hash;
    }
    
    public int next() {
        this.moveToNextIndex();
        return this._hash._set[this._index];
    }
}
