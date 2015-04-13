package gnu.trove;

public class TByteObjectIterator<V> extends TPrimitiveIterator
{
    private final TByteObjectHashMap<V> _map;
    
    public TByteObjectIterator(final TByteObjectHashMap<V> map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public byte key() {
        return this._map._set[this._index];
    }
    
    public V value() {
        return (V)this._map._values[this._index];
    }
    
    public V setValue(final V val) {
        final V old = this.value();
        this._map._values[this._index] = val;
        return old;
    }
}
