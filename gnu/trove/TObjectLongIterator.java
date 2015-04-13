package gnu.trove;

import java.util.*;

public class TObjectLongIterator<K> extends TIterator
{
    private final TObjectLongHashMap<K> _map;
    
    public TObjectLongIterator(final TObjectLongHashMap<K> map) {
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
    
    public long value() {
        return this._map._values[this._index];
    }
    
    public long setValue(final long val) {
        final long old = this.value();
        this._map._values[this._index] = val;
        return old;
    }
}
