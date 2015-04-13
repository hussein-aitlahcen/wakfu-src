package gnu.trove;

public class TByteLongIterator extends TPrimitiveIterator
{
    private final TByteLongHashMap _map;
    
    public TByteLongIterator(final TByteLongHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public byte key() {
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
