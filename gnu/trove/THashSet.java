package gnu.trove;

import java.lang.reflect.*;
import java.util.*;
import java.io.*;

public class THashSet<E> extends TObjectHash<E> implements Set<E>, Iterable<E>, Externalizable
{
    static final long serialVersionUID = 1L;
    
    public THashSet() {
        super();
    }
    
    public THashSet(final TObjectHashingStrategy<E> strategy) {
        super(strategy);
    }
    
    public THashSet(final int initialCapacity) {
        super(initialCapacity);
    }
    
    public THashSet(final int initialCapacity, final TObjectHashingStrategy<E> strategy) {
        super(initialCapacity, strategy);
    }
    
    public THashSet(final int initialCapacity, final float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    public THashSet(final int initialCapacity, final float loadFactor, final TObjectHashingStrategy<E> strategy) {
        super(initialCapacity, loadFactor, strategy);
    }
    
    public THashSet(final Collection<? extends E> collection) {
        this(collection.size());
        this.addAll(collection);
    }
    
    public THashSet(final Collection<? extends E> collection, final TObjectHashingStrategy<E> strategy) {
        this(collection.size(), strategy);
        this.addAll(collection);
    }
    
    public boolean add(final E obj) {
        final int index = this.insertionIndex(obj);
        if (index < 0) {
            return false;
        }
        final Object old = this._set[index];
        this._set[index] = obj;
        this.postInsertHook(old == THashSet.FREE);
        return true;
    }
    
    public boolean equals(final Object other) {
        if (!(other instanceof Set)) {
            return false;
        }
        final Set that = (Set)other;
        return that.size() == this.size() && this.containsAll(that);
    }
    
    public int hashCode() {
        final HashProcedure p = new HashProcedure();
        this.forEach(p);
        return p.getHashCode();
    }
    
    protected void rehash(final int newCapacity) {
        final int oldCapacity = this._set.length;
        final Object[] oldSet = this._set;
        Arrays.fill(this._set = new Object[newCapacity], THashSet.FREE);
        int i = oldCapacity;
        while (i-- > 0) {
            if (oldSet[i] != THashSet.FREE && oldSet[i] != THashSet.REMOVED) {
                final E o = (E)oldSet[i];
                final int index = this.insertionIndex(o);
                if (index < 0) {
                    this.throwObjectContractViolation(this._set[-index - 1], o);
                }
                this._set[index] = o;
            }
        }
    }
    
    public Object[] toArray() {
        final Object[] result = new Object[this.size()];
        this.forEach(new ToObjectArrayProcedure<E>((E[])result));
        return result;
    }
    
    public <T> T[] toArray(T[] a) {
        final int size = this.size();
        if (a.length < size) {
            a = (T[])Array.newInstance(a.getClass().getComponentType(), size);
        }
        this.forEach(new ToObjectArrayProcedure<E>((E[])a));
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
    
    public void clear() {
        super.clear();
        Arrays.fill(this._set, 0, this._set.length, THashSet.FREE);
    }
    
    public boolean remove(final Object obj) {
        final int index = this.index((E)obj);
        if (index >= 0) {
            this.removeAt(index);
            return true;
        }
        return false;
    }
    
    public Iterator<E> iterator() {
        return new TObjectHashIterator<E>(this);
    }
    
    public boolean containsAll(final Collection<?> collection) {
        final Iterator i = collection.iterator();
        while (i.hasNext()) {
            if (!this.contains(i.next())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean addAll(final Collection<? extends E> collection) {
        boolean changed = false;
        int size = collection.size();
        this.ensureCapacity(size);
        final Iterator<? extends E> it = collection.iterator();
        while (size-- > 0) {
            if (this.add(it.next())) {
                changed = true;
            }
        }
        return changed;
    }
    
    public boolean removeAll(final Collection<?> collection) {
        boolean changed = false;
        int size = collection.size();
        final Iterator it = collection.iterator();
        while (size-- > 0) {
            if (this.remove(it.next())) {
                changed = true;
            }
        }
        return changed;
    }
    
    public boolean retainAll(final Collection<?> collection) {
        boolean changed = false;
        int size = this.size();
        final Iterator it = this.iterator();
        while (size-- > 0) {
            if (!collection.contains(it.next())) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }
    
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        this.forEach(new TObjectProcedure() {
            private boolean first = true;
            
            public boolean execute(final Object value) {
                if (this.first) {
                    this.first = false;
                }
                else {
                    buf.append(",");
                }
                buf.append(value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeByte(1);
        super.writeExternal(out);
        out.writeInt(this._size);
        final SerializationProcedure writeProcedure = new SerializationProcedure(out);
        if (!this.forEach(writeProcedure)) {
            throw writeProcedure.exception;
        }
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        final byte version = in.readByte();
        if (version != 0) {
            super.readExternal(in);
        }
        int size = in.readInt();
        this.setUp(size);
        while (size-- > 0) {
            final E val = (E)in.readObject();
            this.add(val);
        }
    }
    
    private final class HashProcedure implements TObjectProcedure<E>
    {
        private int h;
        
        private HashProcedure() {
            super();
            this.h = 0;
        }
        
        public int getHashCode() {
            return this.h;
        }
        
        public final boolean execute(final E key) {
            this.h += THashSet.this._hashingStrategy.computeHashCode((T)key);
            return true;
        }
    }
}
