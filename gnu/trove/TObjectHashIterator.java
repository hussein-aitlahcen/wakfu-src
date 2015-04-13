package gnu.trove;

class TObjectHashIterator<E> extends THashIterator<E>
{
    protected final TObjectHash<E> _objectHash;
    
    public TObjectHashIterator(final TObjectHash<E> hash) {
        super(hash);
        this._objectHash = hash;
    }
    
    protected E objectAtIndex(final int index) {
        return (E)this._objectHash._set[index];
    }
}
