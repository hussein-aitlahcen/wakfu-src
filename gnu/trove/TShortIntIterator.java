package gnu.trove;

public class TShortIntIterator extends TPrimitiveIterator
{
    private final TShortIntHashMap _map;
    
    public TShortIntIterator(final TShortIntHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public short key() {
        return this._map._set[this._index];
    }
    
    public int value() {
        return this._map._values[this._index];
    }
    
    public int setValue(final int val) {
        final int old = this.value();
        this._map._values[this._index] = val;
        return old;
    }
}
