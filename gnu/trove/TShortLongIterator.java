package gnu.trove;

public class TShortLongIterator extends TPrimitiveIterator
{
    private final TShortLongHashMap _map;
    
    public TShortLongIterator(final TShortLongHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public short key() {
        return this._map._set[this._index];
    }
    
    public long value() {
        return this._map._values[this._index];
    }
    
    public long setValue(final long val) {
        final long old = this.value();
        this._map._values[this._index] = val;
        return old;
    }
}
