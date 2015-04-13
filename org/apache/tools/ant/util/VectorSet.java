package org.apache.tools.ant.util;

import java.util.*;

public final class VectorSet<E> extends Vector<E>
{
    private static final long serialVersionUID = 1L;
    private final HashSet<E> set;
    
    public VectorSet() {
        super();
        this.set = new HashSet<E>();
    }
    
    public VectorSet(final int initialCapacity) {
        super(initialCapacity);
        this.set = new HashSet<E>();
    }
    
    public VectorSet(final int initialCapacity, final int capacityIncrement) {
        super(initialCapacity, capacityIncrement);
        this.set = new HashSet<E>();
    }
    
    public VectorSet(final Collection<? extends E> c) {
        super();
        this.set = new HashSet<E>();
        if (c != null) {
            for (final E e : c) {
                this.add(e);
            }
        }
    }
    
    public synchronized boolean add(final E o) {
        if (!this.set.contains(o)) {
            this.doAdd(this.size(), o);
            return true;
        }
        return false;
    }
    
    public void add(final int index, final E o) {
        this.doAdd(index, o);
    }
    
    private synchronized void doAdd(final int index, final E o) {
        if (this.set.add(o)) {
            final int count = this.size();
            this.ensureCapacity(count + 1);
            if (index != count) {
                System.arraycopy(this.elementData, index, this.elementData, index + 1, count - index);
            }
            this.elementData[index] = o;
            ++this.elementCount;
        }
    }
    
    public synchronized void addElement(final E o) {
        this.doAdd(this.size(), o);
    }
    
    public synchronized boolean addAll(final Collection<? extends E> c) {
        boolean changed = false;
        for (final E e : c) {
            changed |= this.add(e);
        }
        return changed;
    }
    
    public synchronized boolean addAll(int index, final Collection<? extends E> c) {
        boolean changed = false;
        for (final E e : c) {
            if (!this.set.contains(e)) {
                this.doAdd(index++, e);
                changed = true;
            }
        }
        return changed;
    }
    
    public synchronized void clear() {
        super.clear();
        this.set.clear();
    }
    
    public Object clone() {
        final VectorSet<E> vs = (VectorSet<E>)super.clone();
        vs.set.addAll((Collection<?>)this.set);
        return vs;
    }
    
    public synchronized boolean contains(final Object o) {
        return this.set.contains(o);
    }
    
    public synchronized boolean containsAll(final Collection<?> c) {
        return this.set.containsAll(c);
    }
    
    public void insertElementAt(final E o, final int index) {
        this.doAdd(index, o);
    }
    
    public synchronized E remove(final int index) {
        final E o = this.get(index);
        this.remove(o);
        return o;
    }
    
    public boolean remove(final Object o) {
        return this.doRemove(o);
    }
    
    private synchronized boolean doRemove(final Object o) {
        if (this.set.remove(o)) {
            final int index = this.indexOf(o);
            if (index < this.elementData.length - 1) {
                System.arraycopy(this.elementData, index + 1, this.elementData, index, this.elementData.length - index - 1);
            }
            --this.elementCount;
            return true;
        }
        return false;
    }
    
    public synchronized boolean removeAll(final Collection<?> c) {
        boolean changed = false;
        for (final Object o : c) {
            changed |= this.remove(o);
        }
        return changed;
    }
    
    public synchronized void removeAllElements() {
        this.set.clear();
        super.removeAllElements();
    }
    
    public boolean removeElement(final Object o) {
        return this.doRemove(o);
    }
    
    public synchronized void removeElementAt(final int index) {
        this.remove(this.get(index));
    }
    
    public synchronized void removeRange(final int fromIndex, int toIndex) {
        while (toIndex > fromIndex) {
            this.remove(--toIndex);
        }
    }
    
    public synchronized boolean retainAll(Collection<?> c) {
        if (!(c instanceof Set)) {
            c = new HashSet<Object>(c);
        }
        final LinkedList<E> l = new LinkedList<E>();
        for (final E o : this) {
            if (!c.contains(o)) {
                l.addLast(o);
            }
        }
        if (!l.isEmpty()) {
            this.removeAll(l);
            return true;
        }
        return false;
    }
    
    public synchronized E set(final int index, final E o) {
        final E orig = this.get(index);
        if (this.set.add(o)) {
            this.elementData[index] = o;
            this.set.remove(orig);
        }
        else {
            final int oldIndexOfO = this.indexOf(o);
            this.remove(o);
            this.remove(orig);
            this.add((oldIndexOfO > index) ? index : (index - 1), o);
        }
        return orig;
    }
    
    public void setElementAt(final E o, final int index) {
        this.set(index, o);
    }
}
