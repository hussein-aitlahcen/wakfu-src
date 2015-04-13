package gnu.trove;

public class TIntFloatIterator extends TPrimitiveIterator
{
    private final TIntFloatHashMap _map;
    
    public TIntFloatIterator(final TIntFloatHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public int key() {
        return this._map._set[this._index];
    }
    
    public float value() {
        return this._map._values[this._index];
    }
    
    public float setValue(final float val) {
        final float old = this.value();
        this._map._values[this._index] = val;
        return old;
    }
}
