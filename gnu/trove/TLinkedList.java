package gnu.trove;

import java.io.*;
import java.util.*;

public class TLinkedList<T extends TLinkable> extends AbstractSequentialList<T> implements Externalizable
{
    static final long serialVersionUID = 1L;
    protected T _head;
    protected T _tail;
    protected int _size;
    
    public TLinkedList() {
        super();
        this._size = 0;
    }
    
    public ListIterator<T> listIterator(final int index) {
        return new IteratorImpl(index);
    }
    
    public int size() {
        return this._size;
    }
    
    public void add(final int index, final T linkable) {
        if (index < 0 || index > this.size()) {
            throw new IndexOutOfBoundsException("index:" + index);
        }
        this.insert(index, linkable);
    }
    
    public boolean add(final T linkable) {
        this.insert(this._size, linkable);
        return true;
    }
    
    public void addFirst(final T linkable) {
        this.insert(0, linkable);
    }
    
    public void addLast(final T linkable) {
        this.insert(this.size(), linkable);
    }
    
    public void clear() {
        if (null != this._head) {
            for (TLinkable link = this._head.getNext(); link != null; link = link.getNext()) {
                final TLinkable prev = link.getPrevious();
                prev.setNext(null);
                link.setPrevious(null);
            }
            final TLinkable tLinkable = null;
            this._tail = (T)tLinkable;
            this._head = (T)tLinkable;
        }
        this._size = 0;
    }
    
    public Object[] toArray() {
        final Object[] o = new Object[this._size];
        int i = 0;
        for (TLinkable link = this._head; link != null; link = link.getNext()) {
            o[i++] = link;
        }
        return o;
    }
    
    public Object[] toUnlinkedArray() {
        final Object[] o = new Object[this._size];
        int i = 0;
        T link = this._head;
        T tmp = null;
        while (link != null) {
            o[i] = link;
            tmp = link;
            link = (T)link.getNext();
            tmp.setNext(null);
            tmp.setPrevious(null);
            ++i;
        }
        this._size = 0;
        final TLinkable tLinkable = null;
        this._tail = (T)tLinkable;
        this._head = (T)tLinkable;
        return o;
    }
    
    public boolean contains(final Object o) {
        for (TLinkable link = this._head; link != null; link = link.getNext()) {
            if (o.equals(link)) {
                return true;
            }
        }
        return false;
    }
    
    public T get(final int index) {
        if (index < 0 || index >= this._size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this._size);
        }
        if (index > this._size >> 1) {
            int position = this._size - 1;
            T node = this._tail;
            while (position > index) {
                node = (T)node.getPrevious();
                --position;
            }
            return node;
        }
        int position = 0;
        T node = this._head;
        while (position < index) {
            node = (T)node.getNext();
            ++position;
        }
        return node;
    }
    
    public T getFirst() {
        return this._head;
    }
    
    public T getLast() {
        return this._tail;
    }
    
    public T getNext(final T current) {
        return (T)current.getNext();
    }
    
    public T getPrevious(final T current) {
        return (T)current.getPrevious();
    }
    
    public T removeFirst() {
        final T o = this._head;
        if (o == null) {
            return null;
        }
        final T n = (T)o.getNext();
        o.setNext(null);
        if (null != n) {
            n.setPrevious(null);
        }
        this._head = n;
        if (--this._size == 0) {
            this._tail = null;
        }
        return o;
    }
    
    public T removeLast() {
        final T o = this._tail;
        if (o == null) {
            return null;
        }
        final T prev = (T)o.getPrevious();
        o.setPrevious(null);
        if (null != prev) {
            prev.setNext(null);
        }
        this._tail = prev;
        if (--this._size == 0) {
            this._head = null;
        }
        return o;
    }
    
    protected void insert(final int index, final T linkable) {
        if (this._size == 0) {
            this._tail = linkable;
            this._head = linkable;
        }
        else if (index == 0) {
            linkable.setNext(this._head);
            this._head.setPrevious(linkable);
            this._head = linkable;
        }
        else if (index == this._size) {
            this._tail.setNext(linkable);
            linkable.setPrevious(this._tail);
            this._tail = linkable;
        }
        else {
            final T node = this.get(index);
            final T before = (T)node.getPrevious();
            if (before != null) {
                before.setNext(linkable);
            }
            linkable.setPrevious(before);
            linkable.setNext(node);
            node.setPrevious(linkable);
        }
        ++this._size;
    }
    
    public boolean remove(final Object o) {
        if (o instanceof TLinkable) {
            final TLinkable link = (TLinkable)o;
            final T p = (T)link.getPrevious();
            final T n = (T)link.getNext();
            if (n == null && p == null) {
                if (o != this._head) {
                    return false;
                }
                final TLinkable tLinkable = null;
                this._tail = (T)tLinkable;
                this._head = (T)tLinkable;
            }
            else if (n == null) {
                link.setPrevious(null);
                p.setNext(null);
                this._tail = p;
            }
            else if (p == null) {
                link.setNext(null);
                n.setPrevious(null);
                this._head = n;
            }
            else {
                p.setNext(n);
                n.setPrevious(p);
                link.setNext(null);
                link.setPrevious(null);
            }
            --this._size;
            return true;
        }
        return false;
    }
    
    public void addBefore(final T current, final T newElement) {
        if (current == this._head) {
            this.addFirst(newElement);
        }
        else if (current == null) {
            this.addLast(newElement);
        }
        else {
            final TLinkable p = current.getPrevious();
            newElement.setNext(current);
            p.setNext(newElement);
            newElement.setPrevious(p);
            current.setPrevious(newElement);
            ++this._size;
        }
    }
    
    public void addAfter(final T current, final T newElement) {
        if (current == this._tail) {
            this.addLast(newElement);
        }
        else if (current == null) {
            this.addFirst(newElement);
        }
        else {
            final TLinkable n = current.getNext();
            newElement.setPrevious(current);
            newElement.setNext(n);
            current.setNext(newElement);
            n.setPrevious(newElement);
            ++this._size;
        }
    }
    
    public boolean forEachValue(final TObjectProcedure<T> procedure) {
        for (T node = this._head; node != null; node = (T)node.getNext()) {
            final boolean keep_going = procedure.execute(node);
            if (!keep_going) {
                return false;
            }
        }
        return true;
    }
    
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeByte(0);
        out.writeInt(this._size);
        out.writeObject(this._head);
        out.writeObject(this._tail);
    }
    
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        in.readByte();
        this._size = in.readInt();
        this._head = (T)in.readObject();
        this._tail = (T)in.readObject();
    }
    
    protected final class IteratorImpl implements ListIterator<T>
    {
        private int _nextIndex;
        private T _next;
        private T _lastReturned;
        
        IteratorImpl(final int position) {
            super();
            this._nextIndex = 0;
            if (position < 0 || position > TLinkedList.this._size) {
                throw new IndexOutOfBoundsException();
            }
            if ((this._nextIndex = position) == 0) {
                this._next = TLinkedList.this._head;
            }
            else if (position == TLinkedList.this._size) {
                this._next = null;
            }
            else if (position < TLinkedList.this._size >> 1) {
                int pos = 0;
                this._next = TLinkedList.this._head;
                while (pos < position) {
                    this._next = (T)this._next.getNext();
                    ++pos;
                }
            }
            else {
                int pos = TLinkedList.this._size - 1;
                this._next = TLinkedList.this._tail;
                while (pos > position) {
                    this._next = (T)this._next.getPrevious();
                    --pos;
                }
            }
        }
        
        public final void add(final T linkable) {
            this._lastReturned = null;
            ++this._nextIndex;
            if (TLinkedList.this._size == 0) {
                TLinkedList.this.add(linkable);
            }
            else {
                TLinkedList.this.addBefore(this._next, linkable);
            }
        }
        
        public final boolean hasNext() {
            return this._nextIndex != TLinkedList.this._size;
        }
        
        public final boolean hasPrevious() {
            return this._nextIndex != 0;
        }
        
        public final T next() {
            if (this._nextIndex == TLinkedList.this._size) {
                throw new NoSuchElementException();
            }
            this._lastReturned = this._next;
            this._next = (T)this._next.getNext();
            ++this._nextIndex;
            return this._lastReturned;
        }
        
        public final int nextIndex() {
            return this._nextIndex;
        }
        
        public final T previous() {
            if (this._nextIndex == 0) {
                throw new NoSuchElementException();
            }
            if (this._nextIndex == TLinkedList.this._size) {
                final TLinkable tail = TLinkedList.this._tail;
                this._next = (T)tail;
                this._lastReturned = (T)tail;
            }
            else {
                final TLinkable previous = this._next.getPrevious();
                this._next = (T)previous;
                this._lastReturned = (T)previous;
            }
            --this._nextIndex;
            return this._lastReturned;
        }
        
        public final int previousIndex() {
            return this._nextIndex - 1;
        }
        
        public final void remove() {
            if (this._lastReturned == null) {
                throw new IllegalStateException("must invoke next or previous before invoking remove");
            }
            if (this._lastReturned != this._next) {
                --this._nextIndex;
            }
            this._next = (T)this._lastReturned.getNext();
            TLinkedList.this.remove(this._lastReturned);
            this._lastReturned = null;
        }
        
        public final void set(final T linkable) {
            if (this._lastReturned == null) {
                throw new IllegalStateException();
            }
            if (this._lastReturned == TLinkedList.this._head) {
                TLinkedList.this._head = linkable;
            }
            if (this._lastReturned == TLinkedList.this._tail) {
                TLinkedList.this._tail = linkable;
            }
            this.swap(this._lastReturned, linkable);
            this._lastReturned = linkable;
        }
        
        private void swap(final T from, final T to) {
            final T p = (T)from.getPrevious();
            final T n = (T)from.getNext();
            if (null != p) {
                to.setPrevious(p);
                p.setNext(to);
            }
            if (null != n) {
                to.setNext(n);
                n.setPrevious(to);
            }
            from.setNext(null);
            from.setPrevious(null);
        }
    }
}
