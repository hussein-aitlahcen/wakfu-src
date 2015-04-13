package gnu.trove;

public class TLongByteIterator extends TPrimitiveIterator
{
    private final TLongByteHashMap _map;
    
    public TLongByteIterator(final TLongByteHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public long key() {
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
