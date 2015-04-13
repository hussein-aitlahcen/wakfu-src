package gnu.trove;

public class TByteFloatIterator extends TPrimitiveIterator
{
    private final TByteFloatHashMap _map;
    
    public TByteFloatIterator(final TByteFloatHashMap map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public byte key() {
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
