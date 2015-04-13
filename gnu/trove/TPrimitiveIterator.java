package gnu.trove;

import java.util.*;

abstract class TPrimitiveIterator extends TIterator
{
    protected final TPrimitiveHash _hash;
    
    public TPrimitiveIterator(final TPrimitiveHash hash) {
        super(hash);
        this._hash = hash;
    }
    
    protected final int nextIndex() {
        if (this._expectedSize != this._hash.size()) {
            throw new ConcurrentModificationException();
        }
        final byte[] states = this._hash._states;
        int i = this._index;
        while (i-- > 0 && states[i] != 1) {}
        return i;
    }
}
