package gnu.trove;

import java.util.*;

abstract class THashIterator<V> extends TIterator implements Iterator<V>
{
    private final TObjectHash _object_hash;
    
    public THashIterator(final TObjectHash hash) {
        super(hash);
        this._object_hash = hash;
    }
    
    public V next() {
        this.moveToNextIndex();
        return this.objectAtIndex(this._index);
    }
    
    protected final int nextIndex() {
        if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
        }
        final Object[] set = this._object_hash._set;
        int i = this._index;
        while (i-- > 0) {
            if (set[i] != TObjectHash.FREE) {
                if (set[i] == TObjectHash.REMOVED) {
                    continue;
                }
                break;
            }
        }
        return i;
    }
    
    protected abstract V objectAtIndex(final int p0);
}
