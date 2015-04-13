package gnu.trove;

public class TShortIterator extends TPrimitiveIterator
{
    private final TShortHash _hash;
    
    public TShortIterator(final TShortHash hash) {
        super(hash);
        this._hash = hash;
    }
    
    public short next() {
        this.moveToNextIndex();
        return this._hash._set[this._index];
    }
}
