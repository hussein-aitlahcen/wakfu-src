package gnu.trove;

public class TShortByteIterator extends TPrimitiveIterator
{
    private final TShortByteHashMap _map;
    
    public TShortByteIterator(final TShortByteHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public short key() {
        return this._map._set[this._index];
    }
    
    public byte value() {
        return this._map._values[this._index];
    }
    
    public byte setValue(final byte val) {
        final byte old = this.value();
        this._map._values[this._index] = val;
        return old;
    }
}
