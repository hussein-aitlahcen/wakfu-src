package gnu.trove;

import java.util.*;

abstract class TIterator
{
    protected final THash _hash;
    protected int _expectedSize;
    protected int _index;
    
    public TIterator(final THash hash) {
        super();
        this._hash = hash;
        this._expectedSize = this._hash.size();
        this._index = this._hash.capacity();
    }
    
    public boolean hasNext() {
        return this.nextIndex() >= 0;
    }
    
    public void remove() {
        if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
        }
        try {
            this._hash.tempDisableAutoCompaction();
            this._hash.removeAt(this._index);
        }
        finally {
            this._hash.reenableAutoCompaction(false);
        }
        --this._expectedSize;
    }
    
    protected final void moveToNextIndex() {
        final int nextIndex = this.nextIndex();
        this._index = nextIndex;
        if (nextIndex < 0) {
            throw new NoSuchElementException();
        }
    }
    
    protected abstract int nextIndex();
}
