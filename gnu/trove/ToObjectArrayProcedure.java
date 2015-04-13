package gnu.trove;

final class ToObjectArrayProcedure<T> implements TObjectProcedure<T>
{
    private final T[] target;
    private int pos;
    
    public ToObjectArrayProcedure(final T[] target) {
        super();
        this.pos = 0;
        this.target = target;
    }
    
    public final boolean execute(final T value) {
        this.target[this.pos++] = value;
        return true;
    }
}
