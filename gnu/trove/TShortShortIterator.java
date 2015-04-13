package gnu.trove;

public class TShortShortIterator extends TPrimitiveIterator
{
    private final TShortShortHashMap _map;
    
    public TShortShortIterator(final TShortShortHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public short key() {
        return this._map._set[this._index];
    }
    
    public short value() {
        return this._map._values[this._index];
    }
    
    public short setValue(final short val) {
        final short old = this.value();
        this._map._values[this._index] = val;
        return old;
    }
}
