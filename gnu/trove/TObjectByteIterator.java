package gnu.trove;

import java.util.*;

public class TObjectByteIterator<K> extends TIterator
{
    private final TObjectByteHashMap<K> _map;
    
    public TObjectByteIterator(final TObjectByteHashMap<K> map) {
        super(map);
        this._map = map;
    }
    
    protected final int nextIndex() {
        if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
        }
        final Object[] set = this._map._set;
        int i = this._index;
        while (i-- > 0) {
            if (set[i] != null && set[i] != TObjectHash.REMOVED) {
                if (set[i] == TObjectHash.FREE) {
                    continue;
                }
                break;
            }
        }
        return i;
    }
    
    public void advance() {
        this.moveToNextIndex();
    }
    
    public K key() {
        return (K)this._map._set[this._index];
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
