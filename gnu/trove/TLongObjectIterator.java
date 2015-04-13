package gnu.trove;

public class TLongObjectIterator<V> extends TPrimitiveIterator
{
    private final TLongObjectHashMap<V> _map;
    
    public TLongObjectIterator(final TLongObjectHashMap<V> map) {
        super(map);
        this._map = map;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public long key() {
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
