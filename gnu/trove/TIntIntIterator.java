package gnu.trove;

public class TIntIntIterator extends TPrimitiveIterator
{
    private final TIntIntHashMap _map;
    
    public TIntIntIterator(final TIntIntHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public int key() {
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
