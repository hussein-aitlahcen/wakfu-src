package org.jdom;

import java.io.*;
import org.jdom.filter.*;
import java.util.*;

final class ContentList extends AbstractList implements Serializable
{
    private static final String CVS_ID = "@(#) $RCSfile: ContentList.java,v $ $Revision: 1.39 $ $Date: 2004/02/28 03:30:27 $ $Name: jdom_1_0 $";
    private static final int INITIAL_ARRAY_SIZE = 5;
    private static final int CREATE = 0;
    private static final int HASPREV = 1;
    private static final int HASNEXT = 2;
    private static final int PREV = 3;
    private static final int NEXT = 4;
    private static final int ADD = 5;
    private static final int REMOVE = 6;
    private Content[] elementData;
    private int size;
    private Parent parent;
    
    ContentList(final Parent parent) {
        super();
        this.parent = parent;
    }
    
    public void add(final int index, final Object obj) {
        if (obj == null) {
            throw new IllegalAddException("Cannot add null object");
        }
        if (obj instanceof Content) {
            this.add(index, (Content)obj);
            return;
        }
        throw new IllegalAddException("Class " + obj.getClass().getName() + " is of unrecognized type and cannot be added");
    }
    
    void add(final int index, final Content child) {
        if (child == null) {
            throw new IllegalAddException("Cannot add null object");
        }
        if (this.parent instanceof Document) {
            this.documentCanContain(index, child);
        }
        else {
            elementCanContain(index, child);
        }
        if (child.getParent() != null) {
            final Parent p = child.getParent();
            if (p instanceof Document) {
                throw new IllegalAddException((Element)child, "The Content already has an existing parent document");
            }
            throw new IllegalAddException("The Content already has an existing parent \"" + ((Element)p).getQualifiedName() + "\"");
        }
        else {
            if (child == this.parent) {
                throw new IllegalAddException("The Element cannot be added to itself");
            }
            if (this.parent instanceof Element && child instanceof Element && ((Element)child).isAncestor((Element)this.parent)) {
                throw new IllegalAddException("The Element cannot be added as a descendent of itself");
            }
            if (index < 0 || index > this.size) {
                throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size());
            }
            child.setParent(this.parent);
            this.ensureCapacity(this.size + 1);
            if (index == this.size) {
                this.elementData[this.size++] = child;
            }
            else {
                System.arraycopy(this.elementData, index, this.elementData, index + 1, this.size - index);
                this.elementData[index] = child;
                ++this.size;
            }
            ++super.modCount;
        }
    }
    
    public boolean addAll(final int index, final Collection collection) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size());
        }
        if (collection == null || collection.size() == 0) {
            return false;
        }
        this.ensureCapacity(this.size() + collection.size());
        int count = 0;
        try {
            for (final Object obj : collection) {
                this.add(index + count, obj);
                ++count;
            }
        }
        catch (RuntimeException ex) {
            for (int j = 0; j < count; ++j) {
                this.remove(index);
            }
            throw ex;
        }
        return true;
    }
    
    public boolean addAll(final Collection collection) {
        return this.addAll(this.size(), collection);
    }
    
    public void clear() {
        if (this.elementData != null) {
            for (int i = 0; i < this.size; ++i) {
                final Content obj = this.elementData[i];
                removeParent(obj);
            }
            this.elementData = null;
            this.size = 0;
        }
        ++super.modCount;
    }
    
    void clearAndSet(final Collection collection) {
        final Content[] old = this.elementData;
        final int oldSize = this.size;
        this.elementData = null;
        this.size = 0;
        if (collection != null && collection.size() != 0) {
            this.ensureCapacity(collection.size());
            try {
                this.addAll(0, collection);
            }
            catch (RuntimeException exception) {
                this.elementData = old;
                this.size = oldSize;
                throw exception;
            }
        }
        if (old != null) {
            for (int i = 0; i < oldSize; ++i) {
                removeParent(old[i]);
            }
        }
        ++super.modCount;
    }
    
    private void documentCanContain(final int index, final Content child) throws IllegalAddException {
        if (child instanceof Element) {
            if (this.indexOfFirstElement() >= 0) {
                throw new IllegalAddException("Cannot add a second root element, only one is allowed");
            }
            if (this.indexOfDocType() > index) {
                throw new IllegalAddException("A root element cannot be added before the DocType");
            }
        }
        if (child instanceof DocType) {
            if (this.indexOfDocType() >= 0) {
                throw new IllegalAddException("Cannot add a second doctype, only one is allowed");
            }
            final int firstElt = this.indexOfFirstElement();
            if (firstElt != -1 && firstElt < index) {
                throw new IllegalAddException("A DocType cannot be added after the root element");
            }
        }
        if (child instanceof CDATA) {
            throw new IllegalAddException("A CDATA is not allowed at the document root");
        }
        if (child instanceof Text) {
            throw new IllegalAddException("A Text is not allowed at the document root");
        }
        if (child instanceof EntityRef) {
            throw new IllegalAddException("An EntityRef is not allowed at the document root");
        }
    }
    
    private static void elementCanContain(final int index, final Content child) throws IllegalAddException {
        if (child instanceof DocType) {
            throw new IllegalAddException("A DocType is not allowed except at the document level");
        }
    }
    
    void ensureCapacity(final int minCapacity) {
        if (this.elementData == null) {
            this.elementData = new Content[Math.max(minCapacity, 5)];
        }
        else {
            final int oldCapacity = this.elementData.length;
            if (minCapacity > oldCapacity) {
                final Object[] oldData = this.elementData;
                int newCapacity = oldCapacity * 3 / 2 + 1;
                if (newCapacity < minCapacity) {
                    newCapacity = minCapacity;
                }
                System.arraycopy(oldData, 0, this.elementData = new Content[newCapacity], 0, this.size);
            }
        }
    }
    
    public Object get(final int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size());
        }
        return this.elementData[index];
    }
    
    private int getModCount() {
        return super.modCount;
    }
    
    List getView(final Filter filter) {
        return new FilterList(filter);
    }
    
    int indexOfDocType() {
        if (this.elementData != null) {
            for (int i = 0; i < this.size; ++i) {
                if (this.elementData[i] instanceof DocType) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    int indexOfFirstElement() {
        if (this.elementData != null) {
            for (int i = 0; i < this.size; ++i) {
                if (this.elementData[i] instanceof Element) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    public Object remove(final int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size());
        }
        final Content old = this.elementData[index];
        removeParent(old);
        final int numMoved = this.size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(this.elementData, index + 1, this.elementData, index, numMoved);
        }
        this.elementData[--this.size] = null;
        ++super.modCount;
        return old;
    }
    
    private static void removeParent(final Content c) {
        c.setParent(null);
    }
    
    public Object set(final int index, final Object obj) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size());
        }
        if (obj instanceof Element && this.parent instanceof Document) {
            final int root = this.indexOfFirstElement();
            if (root >= 0 && root != index) {
                throw new IllegalAddException("Cannot add a second root element, only one is allowed");
            }
        }
        if (obj instanceof DocType && this.parent instanceof Document) {
            final int docTypeIndex = this.indexOfDocType();
            if (docTypeIndex >= 0 && docTypeIndex != index) {
                throw new IllegalAddException("Cannot add a second doctype, only one is allowed");
            }
        }
        final Object old = this.remove(index);
        try {
            this.add(index, obj);
        }
        catch (RuntimeException exception) {
            this.add(index, old);
            throw exception;
        }
        return old;
    }
    
    public int size() {
        return this.size;
    }
    
    public String toString() {
        return super.toString();
    }
    
    final void uncheckedAddContent(final Content c) {
        c.parent = this.parent;
        this.ensureCapacity(this.size + 1);
        this.elementData[this.size++] = c;
        ++super.modCount;
    }
    
    class FilterList extends AbstractList implements Serializable
    {
        Filter filter;
        int count;
        int expected;
        
        FilterList(final Filter filter) {
            super();
            this.count = 0;
            this.expected = -1;
            this.filter = filter;
        }
        
        public void add(final int index, final Object obj) {
            if (this.filter.matches(obj)) {
                final int adjusted = this.getAdjustedIndex(index);
                ContentList.this.add(adjusted, obj);
                ++this.expected;
                ++this.count;
                return;
            }
            throw new IllegalAddException("Filter won't allow the " + obj.getClass().getName() + " '" + obj + "' to be added to the list");
        }
        
        public Object get(final int index) {
            final int adjusted = this.getAdjustedIndex(index);
            return ContentList.this.get(adjusted);
        }
        
        private final int getAdjustedIndex(final int index) {
            int adjusted = 0;
            for (int i = 0; i < ContentList.this.size; ++i) {
                final Object obj = ContentList.this.elementData[i];
                if (this.filter.matches(obj)) {
                    if (index == adjusted) {
                        return i;
                    }
                    ++adjusted;
                }
            }
            if (index == adjusted) {
                return ContentList.this.size;
            }
            return ContentList.this.size + 1;
        }
        
        public Iterator iterator() {
            return new FilterListIterator(this.filter, 0);
        }
        
        public ListIterator listIterator() {
            return new FilterListIterator(this.filter, 0);
        }
        
        public ListIterator listIterator(final int index) {
            return new FilterListIterator(this.filter, index);
        }
        
        public Object remove(final int index) {
            final int adjusted = this.getAdjustedIndex(index);
            Object old = ContentList.this.get(adjusted);
            if (this.filter.matches(old)) {
                old = ContentList.this.remove(adjusted);
                ++this.expected;
                --this.count;
                return old;
            }
            throw new IllegalAddException("Filter won't allow the " + old.getClass().getName() + " '" + old + "' (index " + index + ") to be removed");
        }
        
        public Object set(final int index, final Object obj) {
            Object old = null;
            if (!this.filter.matches(obj)) {
                throw new IllegalAddException("Filter won't allow index " + index + " to be set to " + obj.getClass().getName());
            }
            final int adjusted = this.getAdjustedIndex(index);
            old = ContentList.this.get(adjusted);
            if (!this.filter.matches(old)) {
                throw new IllegalAddException("Filter won't allow the " + old.getClass().getName() + " '" + old + "' (index " + index + ") to be removed");
            }
            old = ContentList.this.set(adjusted, obj);
            this.expected += 2;
            return old;
        }
        
        public int size() {
            if (this.expected == ContentList.this.getModCount()) {
                return this.count;
            }
            this.count = 0;
            for (int i = 0; i < ContentList.this.size(); ++i) {
                final Object obj = ContentList.this.elementData[i];
                if (this.filter.matches(obj)) {
                    ++this.count;
                }
            }
            this.expected = ContentList.this.getModCount();
            return this.count;
        }
    }
    
    class FilterListIterator implements ListIterator
    {
        Filter filter;
        int lastOperation;
        int initialCursor;
        int cursor;
        int last;
        int expected;
        
        FilterListIterator(final Filter filter, final int start) {
            super();
            this.filter = filter;
            this.initialCursor = this.initializeCursor(start);
            this.last = -1;
            this.expected = ContentList.this.getModCount();
            this.lastOperation = 0;
        }
        
        public void add(final Object obj) {
            this.checkConcurrentModification();
            if (this.filter.matches(obj)) {
                this.last = this.cursor + 1;
                ContentList.this.add(this.last, obj);
                this.expected = ContentList.this.getModCount();
                this.lastOperation = 5;
                return;
            }
            throw new IllegalAddException("Filter won't allow add of " + obj.getClass().getName());
        }
        
        private void checkConcurrentModification() {
            if (this.expected != ContentList.this.getModCount()) {
                throw new ConcurrentModificationException();
            }
        }
        
        public boolean hasNext() {
            this.checkConcurrentModification();
            while (true) {
                switch (this.lastOperation) {
                    default: {
                        throw new IllegalStateException("Unknown operation");
                    }
                    case 2: {
                        if (this.lastOperation != 0) {
                            this.lastOperation = 2;
                        }
                        return this.cursor < ContentList.this.size();
                    }
                    case 0: {
                        this.cursor = this.initialCursor;
                        continue;
                    }
                    case 3: {
                        this.cursor = this.last;
                        continue;
                    }
                    case 4:
                    case 5: {
                        this.cursor = this.moveForward(this.last + 1);
                        continue;
                    }
                    case 6: {
                        this.cursor = this.moveForward(this.last);
                        continue;
                    }
                    case 1: {
                        this.cursor = this.moveForward(this.cursor + 1);
                        continue;
                    }
                }
                break;
            }
        }
        
        public boolean hasPrevious() {
            this.checkConcurrentModification();
            while (true) {
                switch (this.lastOperation) {
                    case 0: {
                        this.cursor = this.initialCursor;
                        final int size = ContentList.this.size();
                        if (this.cursor >= size) {
                            this.cursor = this.moveBackward(size - 1);
                        }
                        break Label_0145;
                    }
                    default: {
                        throw new IllegalStateException("Unknown operation");
                    }
                    case 1: {
                        if (this.lastOperation != 0) {
                            this.lastOperation = 1;
                        }
                        return this.cursor >= 0;
                    }
                    case 3:
                    case 6: {
                        this.cursor = this.moveBackward(this.last - 1);
                        continue;
                    }
                    case 2: {
                        this.cursor = this.moveBackward(this.cursor - 1);
                        continue;
                    }
                    case 4:
                    case 5: {
                        this.cursor = this.last;
                        continue;
                    }
                }
                break;
            }
        }
        
        private int initializeCursor(final int start) {
            if (start < 0) {
                throw new IndexOutOfBoundsException("Index: " + start);
            }
            int count = 0;
            for (int i = 0; i < ContentList.this.size(); ++i) {
                final Object obj = ContentList.this.get(i);
                if (this.filter.matches(obj)) {
                    if (start == count) {
                        return i;
                    }
                    ++count;
                }
            }
            if (start > count) {
                throw new IndexOutOfBoundsException("Index: " + start + " Size: " + count);
            }
            return ContentList.this.size();
        }
        
        private int moveBackward(int start) {
            if (start >= ContentList.this.size()) {
                start = ContentList.this.size() - 1;
            }
            for (int i = start; i >= 0; --i) {
                final Object obj = ContentList.this.get(i);
                if (this.filter.matches(obj)) {
                    return i;
                }
            }
            return -1;
        }
        
        private int moveForward(int start) {
            if (start < 0) {
                start = 0;
            }
            for (int i = start; i < ContentList.this.size(); ++i) {
                final Object obj = ContentList.this.get(i);
                if (this.filter.matches(obj)) {
                    return i;
                }
            }
            return ContentList.this.size();
        }
        
        public Object next() {
            this.checkConcurrentModification();
            if (this.hasNext()) {
                this.last = this.cursor;
                this.lastOperation = 4;
                return ContentList.this.get(this.last);
            }
            this.last = ContentList.this.size();
            throw new NoSuchElementException();
        }
        
        public int nextIndex() {
            this.checkConcurrentModification();
            this.hasNext();
            int count = 0;
            for (int i = 0; i < ContentList.this.size(); ++i) {
                if (this.filter.matches(ContentList.this.get(i))) {
                    if (i == this.cursor) {
                        return count;
                    }
                    ++count;
                }
            }
            this.expected = ContentList.this.getModCount();
            return count;
        }
        
        public Object previous() {
            this.checkConcurrentModification();
            if (this.hasPrevious()) {
                this.last = this.cursor;
                this.lastOperation = 3;
                return ContentList.this.get(this.last);
            }
            this.last = -1;
            throw new NoSuchElementException();
        }
        
        public int previousIndex() {
            this.checkConcurrentModification();
            if (this.hasPrevious()) {
                int count = 0;
                for (int i = 0; i < ContentList.this.size(); ++i) {
                    if (this.filter.matches(ContentList.this.get(i))) {
                        if (i == this.cursor) {
                            return count;
                        }
                        ++count;
                    }
                }
            }
            return -1;
        }
        
        public void remove() {
            this.checkConcurrentModification();
            if (this.last < 0 || this.lastOperation == 6) {
                throw new IllegalStateException("no preceeding call to prev() or next()");
            }
            if (this.lastOperation == 5) {
                throw new IllegalStateException("cannot call remove() after add()");
            }
            final Object old = ContentList.this.get(this.last);
            if (this.filter.matches(old)) {
                ContentList.this.remove(this.last);
                this.expected = ContentList.this.getModCount();
                this.lastOperation = 6;
                return;
            }
            throw new IllegalAddException("Filter won't allow " + old.getClass().getName() + " (index " + this.last + ") to be removed");
        }
        
        public void set(final Object obj) {
            this.checkConcurrentModification();
            if (this.lastOperation == 5 || this.lastOperation == 6) {
                throw new IllegalStateException("cannot call set() after add() or remove()");
            }
            if (this.last < 0) {
                throw new IllegalStateException("no preceeding call to prev() or next()");
            }
            if (!this.filter.matches(obj)) {
                throw new IllegalAddException("Filter won't allow index " + this.last + " to be set to " + obj.getClass().getName());
            }
            final Object old = ContentList.this.get(this.last);
            if (!this.filter.matches(old)) {
                throw new IllegalAddException("Filter won't allow " + old.getClass().getName() + " (index " + this.last + ") to be removed");
            }
            ContentList.this.set(this.last, obj);
            this.expected = ContentList.this.getModCount();
        }
    }
}
