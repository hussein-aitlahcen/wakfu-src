package gnu.trove;

public class TLongLongIterator extends TPrimitiveIterator
{
    private final TLongLongHashMap _map;
    
    public TLongLongIterator(final TLongLongHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public long key() {
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
