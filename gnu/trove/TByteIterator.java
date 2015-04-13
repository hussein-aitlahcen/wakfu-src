package gnu.trove;

public class TByteIterator extends TPrimitiveIterator
{
    private final TByteHash _hash;
    
    public TByteIterator(final TByteHash hash) {
        super(hash);
        this._hash = hash;
    }
    
    public byte next() {
        this.moveToNextIndex();
        return this._hash._set[this._index];
    }
}
