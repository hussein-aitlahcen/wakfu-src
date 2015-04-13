package com.ankamagames.framework.kernel.core.common.collections;

import org.apache.log4j.*;
import java.util.*;

public class SortedList<E> extends ArrayList<E>
{
    protected static final Logger m_logger;
    private static final int INITIAL_CAPACITY_DEFAULT_VALUE = 10;
    public static final Comparator<Long> LONG_COMPARATOR;
    public static final Comparator<Integer> INTEGER_COMPARATOR;
    public static final Comparator<Short> SHORT_COMPARATOR;
    public static final Comparator<Byte> BYTE_COMPARATOR;
    public static final Comparator<String> STRING_COMPARATOR;
    private final AbstractSearchMethod<E> m_abstractSearchMethod;
    
    public SortedList(final int initialCapacity, final AbstractSearchMethod<E> abstractSearchMethod) {
        super(initialCapacity);
        this.m_abstractSearchMethod = abstractSearchMethod;
    }
    
    public SortedList(final int initialCapacity, final Comparator<E> comparator) {
        super(initialCapacity);
        this.m_abstractSearchMethod = new DefaultSearchMethod<E>((Comparator)comparator);
    }
    
    public SortedList(final AbstractSearchMethod<E> abstractSearchMethod) {
        this(10, abstractSearchMethod);
    }
    
    public SortedList(final Comparator<E> comparator) {
        this(10, comparator);
    }
    
    @Override
    public boolean add(final E element) {
        final int index = this.indexOf(element);
        if (index < 0) {
            super.add(-(index + 1), element);
        }
        else {
            super.add(index, element);
        }
        return true;
    }
    
    @Deprecated
    @Override
    public void add(final int index, final E element) {
        SortedList.m_logger.warn((Object)"Impossible d'inserer un \u00e9l\u00e9ment \u00e0 un index donn\u00e9.");
        this.add(element);
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        if (collection instanceof SortedList && this.size() == 0) {
            super.addAll(collection);
        }
        else {
            for (final E element : collection) {
                this.add(element);
            }
        }
        return 0 < collection.size();
    }
    
    @Deprecated
    @Override
    public boolean addAll(final int index, final Collection<? extends E> collection) {
        SortedList.m_logger.warn((Object)"Impossible d'inserer des \u00e9l\u00e9ments \u00e0 un index donn\u00e9.");
        return this.addAll(collection);
    }
    
    private void addAllQuick(final Collection<? extends E> collection) {
        super.addAll(collection);
    }
    
    public boolean addFirst(final E element) {
        final boolean added = this.size() == 0 || this.compare(element, this.get(0)) <= 0;
        if (added) {
            super.add(0, element);
        }
        return added;
    }
    
    public boolean addLast(final E element) {
        final int size = this.size();
        final boolean added = size == 0 || this.compare(this.get(size - 1), element) <= 0;
        if (added) {
            super.add(size, element);
        }
        return added;
    }
    
    @Override
    public Object clone() {
        final SortedList<E> sortedList = new SortedList<E>(this.size(), this.m_abstractSearchMethod);
        sortedList.addAllQuick((Collection<? extends E>)this);
        return sortedList;
    }
    
    protected final int compare(final E element1, final E element2) {
        return this.m_abstractSearchMethod.compare(element1, element2);
    }
    
    @Override
    public boolean contains(final Object object) {
        return 0 <= this.indexOf(object);
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!this.contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    public E getFirst() {
        return (this.size() == 0) ? null : this.get(0);
    }
    
    public E getLast() {
        int size = this.size();
        return (size == 0) ? null : this.get(--size);
    }
    
    @Override
    public int indexOf(final Object object) {
        final int middleIndex = this.search(object);
        if (middleIndex < 0 || this.get(middleIndex).equals(object)) {
            return middleIndex;
        }
        int index = middleIndex;
        do {
            --index;
        } while (0 <= index && this.compare(this.get(index), object) == 0 && !this.get(index).equals(object));
        if (0 <= index && this.compare(this.get(index), object) == 0 && this.get(index).equals(object)) {
            return index;
        }
        final int minIndex = Math.max(0, index + 1);
        index = middleIndex;
        final int size = this.size();
        while (++index < size && this.compare(this.get(index), object) == 0 && !this.get(index).equals(object)) {}
        return (index < size && this.compare(this.get(index), object) == 0 && this.get(index).equals(object)) ? index : (-(minIndex + 1));
    }
    
    public int firstIndexOf(final Object object) {
        int index = this.indexOf(object);
        if (index < 0) {
            return -1;
        }
        final int size = this.size();
        do {
            --index;
        } while (0 <= index && this.compare(this.get(index), object) == 0);
        while (++index < size && this.compare(this.get(index), object) == 0 && !this.get(index).equals(object)) {}
        return (index < size && this.compare(this.get(index), object) == 0 && this.get(index).equals(object)) ? index : -1;
    }
    
    @Override
    public int lastIndexOf(final Object object) {
        int index = this.indexOf(object);
        if (index < 0) {
            return -1;
        }
        final int size = this.size();
        while (++index < size && this.compare(this.get(index), object) == 0) {}
        do {
            --index;
        } while (0 <= index && this.compare(this.get(index), object) == 0 && !this.get(index).equals(object));
        return (0 <= index && this.compare(this.get(index), object) == 0 && this.get(index).equals(object)) ? index : -1;
    }
    
    public boolean onElementChanged(final E element) {
        int lastIndex = super.lastIndexOf(element);
        final boolean elementChanged = 0 <= lastIndex;
        if (elementChanged) {
            int firstIndex;
            for (firstIndex = lastIndex - 1; 0 <= firstIndex && this.get(firstIndex).equals(element); --firstIndex) {}
            ++firstIndex;
            ++lastIndex;
            super.removeRange(firstIndex, lastIndex);
            final int count = lastIndex - firstIndex;
            if (count == 1) {
                super.add(-(this.indexOf(element) + 1), element);
            }
            else {
                final ArrayList<E> arrayList = new ArrayList<E>(count + 1);
                for (int index = 0; index < count; ++index) {
                    arrayList.add(index, element);
                }
                super.addAll(-(this.indexOf(element) + 1), (Collection<? extends E>)arrayList);
            }
        }
        return elementChanged;
    }
    
    @Override
    public boolean remove(final Object object) {
        final int index = this.indexOf(object);
        final boolean removed = 0 <= index;
        if (removed) {
            super.remove(index);
        }
        return removed;
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        final int size = this.size();
        for (final Object object : collection) {
            final int firstIndex = this.firstIndexOf(object);
            if (0 <= firstIndex) {
                super.removeRange(firstIndex, this.lastIndexOf(object) + 1);
            }
        }
        return this.size() < size;
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        final int size = this.size();
        int index = size - 1;
        while (0 <= index) {
            if (collection.contains(this.get(index))) {
                --index;
            }
            else {
                final int lastIndex = index;
                do {
                    --index;
                } while (0 <= index && !collection.contains(this.get(index)));
                super.removeRange(index + 1, lastIndex + 1);
            }
        }
        return this.size() < size;
    }
    
    protected final int search(final E element) {
        return this.m_abstractSearchMethod.search(this, element);
    }
    
    @Deprecated
    @Override
    public E set(final int index, final E element) {
        final E oldElement = super.remove(index);
        super.add(-(this.indexOf(element) + 1), element);
        return oldElement;
    }
    
    static {
        m_logger = Logger.getLogger((Class)SortedList.class);
        LONG_COMPARATOR = new Comparator<Long>() {
            @Override
            public final int compare(final Long long1, final Long long2) {
                return long1.compareTo(long2);
            }
        };
        INTEGER_COMPARATOR = new Comparator<Integer>() {
            @Override
            public final int compare(final Integer integer1, final Integer integer2) {
                return integer1.compareTo(integer2);
            }
        };
        SHORT_COMPARATOR = new Comparator<Short>() {
            @Override
            public final int compare(final Short short1, final Short short2) {
                return short1.compareTo(short2);
            }
        };
        BYTE_COMPARATOR = new Comparator<Byte>() {
            @Override
            public final int compare(final Byte byte1, final Byte byte2) {
                return byte1.compareTo(byte2);
            }
        };
        STRING_COMPARATOR = new Comparator<String>() {
            @Override
            public final int compare(final String string1, final String string2) {
                return string1.compareTo(string2);
            }
        };
    }
    
    private abstract class AbstractSearchMethod<E>
    {
        private final Comparator<E> m_comparator;
        
        private AbstractSearchMethod(final Comparator<E> comparator) {
            super();
            this.m_comparator = comparator;
        }
        
        public final int compare(final E element1, final E element2) {
            return this.m_comparator.compare(element1, element2);
        }
        
        public abstract int search(final SortedList<E> p0, final E p1);
    }
    
    private final class DefaultSearchMethod<E> extends AbstractSearchMethod<E>
    {
        private DefaultSearchMethod(final Comparator<E> comparator) {
            super((Comparator)comparator);
        }
        
        @Override
        public final int search(final SortedList<E> sortedList, final E element) {
            return Collections.binarySearch((List<? extends E>)sortedList, element, ((AbstractSearchMethod<Object>)this).m_comparator);
        }
    }
}
