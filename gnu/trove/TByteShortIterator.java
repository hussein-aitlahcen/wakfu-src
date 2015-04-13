package gnu.trove;

public class TByteShortIterator extends TPrimitiveIterator
{
    private final TByteShortHashMap _map;
    
    public TByteShortIterator(final TByteShortHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public byte key() {
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
