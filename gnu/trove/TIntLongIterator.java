package gnu.trove;

public class TIntLongIterator extends TPrimitiveIterator
{
    private final TIntLongHashMap _map;
    
    public TIntLongIterator(final TIntLongHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public int key() {
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
