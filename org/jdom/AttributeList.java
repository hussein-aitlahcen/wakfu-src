package org.jdom;

import java.io.*;
import java.util.*;

class AttributeList extends AbstractList implements List, Serializable
{
    private static final String CVS_ID = "@(#) $RCSfile: AttributeList.java,v $ $Revision: 1.23 $ $Date: 2004/02/28 03:30:27 $ $Name: jdom_1_0 $";
    private static final int INITIAL_ARRAY_SIZE = 5;
    private Attribute[] elementData;
    private int size;
    private Element parent;
    
    private AttributeList() {
        super();
    }
    
    AttributeList(final Element parent) {
        super();
        this.parent = parent;
    }
    
    public void add(final int index, final Object obj) {
        if (obj instanceof Attribute) {
            final Attribute attribute = (Attribute)obj;
            final int duplicate = this.indexOfDuplicate(attribute);
            if (duplicate >= 0) {
                throw new IllegalAddException("Cannot add duplicate attribute");
            }
            this.add(index, attribute);
            ++super.modCount;
        }
        else {
            if (obj == null) {
                throw new IllegalAddException("Cannot add null attribute");
            }
            throw new IllegalAddException("Class " + obj.getClass().getName() + " is not an attribute");
        }
    }
    
    void add(final int index, final Attribute attribute) {
        if (attribute.getParent() != null) {
            throw new IllegalAddException("The attribute already has an existing parent \"" + attribute.getParent().getQualifiedName() + "\"");
        }
        final String reason = Verifier.checkNamespaceCollision(attribute, this.parent);
        if (reason != null) {
            throw new IllegalAddException(this.parent, attribute, reason);
        }
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size());
        }
        attribute.setParent(this.parent);
        this.ensureCapacity(this.size + 1);
        if (index == this.size) {
            this.elementData[this.size++] = attribute;
        }
        else {
            System.arraycopy(this.elementData, index, this.elementData, index + 1, this.size - index);
            this.elementData[index] = attribute;
            ++this.size;
        }
        ++super.modCount;
    }
    
    public boolean add(final Object obj) {
        if (obj instanceof Attribute) {
            final Attribute attribute = (Attribute)obj;
            final int duplicate = this.indexOfDuplicate(attribute);
            if (duplicate < 0) {
                this.add(this.size(), attribute);
            }
            else {
                this.set(duplicate, attribute);
            }
            return true;
        }
        if (obj == null) {
            throw new IllegalAddException("Cannot add null attribute");
        }
        throw new IllegalAddException("Class " + obj.getClass().getName() + " is not an attribute");
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
                final Attribute attribute = this.elementData[i];
                attribute.setParent(null);
            }
            this.elementData = null;
            this.size = 0;
        }
        ++super.modCount;
    }
    
    void clearAndSet(final Collection collection) {
        final Attribute[] old = this.elementData;
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
            for (final Attribute attribute : old) {
                attribute.setParent(null);
            }
        }
        ++super.modCount;
    }
    
    private void ensureCapacity(final int minCapacity) {
        if (this.elementData == null) {
            this.elementData = new Attribute[Math.max(minCapacity, 5)];
        }
        else {
            final int oldCapacity = this.elementData.length;
            if (minCapacity > oldCapacity) {
                final Attribute[] oldData = this.elementData;
                int newCapacity = oldCapacity * 3 / 2 + 1;
                if (newCapacity < minCapacity) {
                    newCapacity = minCapacity;
                }
                System.arraycopy(oldData, 0, this.elementData = new Attribute[newCapacity], 0, this.size);
            }
        }
    }
    
    public Object get(final int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size());
        }
        return this.elementData[index];
    }
    
    Object get(final String name, final Namespace namespace) {
        final int index = this.indexOf(name, namespace);
        if (index < 0) {
            return null;
        }
        return this.elementData[index];
    }
    
    int indexOf(final String name, final Namespace namespace) {
        final String uri = namespace.getURI();
        if (this.elementData != null) {
            for (int i = 0; i < this.size; ++i) {
                final Attribute old = this.elementData[i];
                final String oldURI = old.getNamespaceURI();
                final String oldName = old.getName();
                if (oldURI.equals(uri) && oldName.equals(name)) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private int indexOfDuplicate(final Attribute attribute) {
        int duplicate = -1;
        final String name = attribute.getName();
        final Namespace namespace = attribute.getNamespace();
        duplicate = this.indexOf(name, namespace);
        return duplicate;
    }
    
    public Object remove(final int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size());
        }
        final Attribute old = this.elementData[index];
        old.setParent(null);
        final int numMoved = this.size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(this.elementData, index + 1, this.elementData, index, numMoved);
        }
        this.elementData[--this.size] = null;
        ++super.modCount;
        return old;
    }
    
    boolean remove(final String name, final Namespace namespace) {
        final int index = this.indexOf(name, namespace);
        if (index < 0) {
            return false;
        }
        this.remove(index);
        return true;
    }
    
    public Object set(final int index, final Object obj) {
        if (obj instanceof Attribute) {
            final Attribute attribute = (Attribute)obj;
            final int duplicate = this.indexOfDuplicate(attribute);
            if (duplicate >= 0 && duplicate != index) {
                throw new IllegalAddException("Cannot set duplicate attribute");
            }
            return this.set(index, attribute);
        }
        else {
            if (obj == null) {
                throw new IllegalAddException("Cannot add null attribute");
            }
            throw new IllegalAddException("Class " + obj.getClass().getName() + " is not an attribute");
        }
    }
    
    Object set(final int index, final Attribute attribute) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + " Size: " + this.size());
        }
        if (attribute.getParent() != null) {
            throw new IllegalAddException("The attribute already has an existing parent \"" + attribute.getParent().getQualifiedName() + "\"");
        }
        final String reason = Verifier.checkNamespaceCollision(attribute, this.parent);
        if (reason != null) {
            throw new IllegalAddException(this.parent, attribute, reason);
        }
        final Attribute old = this.elementData[index];
        old.setParent(null);
        (this.elementData[index] = attribute).setParent(this.parent);
        return old;
    }
    
    public int size() {
        return this.size;
    }
    
    public String toString() {
        return super.toString();
    }
    
    final void uncheckedAddAttribute(final Attribute a) {
        a.parent = this.parent;
        this.ensureCapacity(this.size + 1);
        this.elementData[this.size++] = a;
        ++super.modCount;
    }
}
