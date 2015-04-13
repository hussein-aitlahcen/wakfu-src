package com.ankamagames.xulor2.util;

import com.ankamagames.xulor2.core.*;
import org.jetbrains.annotations.*;
import java.util.*;

public class ElementMapIterator implements Iterator<EventDispatcher>
{
    private final ElementMap m_map;
    private Iterator<EventDispatcher> m_iterator;
    private EventDispatcher m_next;
    private boolean m_nextHasBeenComputed;
    private int m_currentChildElementMapIndex;
    
    public ElementMapIterator(final ElementMap map) {
        super();
        this.m_map = map;
        if (this.m_map == null) {
            throw new IllegalArgumentException("Impossible d'initialiser avec une map nulle");
        }
        this.m_iterator = this.m_map.iterator();
        this.m_currentChildElementMapIndex = -1;
        this.m_next = this.computeNext();
    }
    
    @Nullable
    private EventDispatcher computeNext() {
        this.m_nextHasBeenComputed = true;
        if (this.m_iterator.hasNext()) {
            return this.m_iterator.next();
        }
        this.m_iterator = this.getNextIterator();
        if (this.m_iterator == null) {
            return null;
        }
        return this.m_iterator.next();
    }
    
    @Nullable
    private Iterator<EventDispatcher> getNextIterator() {
        ++this.m_currentChildElementMapIndex;
        final ArrayList<ElementMap> childrenElementMaps = this.m_map.getChildrenElementMaps();
        if (childrenElementMaps == null || childrenElementMaps.size() >= this.m_currentChildElementMapIndex) {
            return null;
        }
        final ElementMap map = childrenElementMaps.get(this.m_currentChildElementMapIndex);
        final Iterator<EventDispatcher> it = new ElementMapIterator(map);
        return it.hasNext() ? it : this.getNextIterator();
    }
    
    @Override
    public boolean hasNext() {
        if (!this.m_nextHasBeenComputed) {
            this.m_next = this.computeNext();
        }
        return this.m_next != null;
    }
    
    @Override
    public EventDispatcher next() {
        if (this.m_next == null) {
            throw new NoSuchElementException();
        }
        this.m_nextHasBeenComputed = false;
        return this.m_next;
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
