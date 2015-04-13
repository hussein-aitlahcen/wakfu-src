package com.ankamagames.baseImpl.graphics.alea.display;

import org.apache.log4j.*;
import gnu.trove.*;

public final class DisplayedScreenElementFactory
{
    private static final Logger m_logger;
    private final TLongObjectHashMap<DisplayedScreenElement> m_elements;
    
    public DisplayedScreenElementFactory() {
        super();
        (this.m_elements = new TLongObjectHashMap<DisplayedScreenElement>(8192)).setAutoCompactionFactor(0.0f);
    }
    
    public DisplayedScreenElement createDisplayedScreenElement(final long hashCode) {
        assert hashCode != 0L;
        final DisplayedScreenElement element = this.m_elements.get(hashCode);
        if (element != null) {
            element.addReference();
            return element;
        }
        final DisplayedScreenElement newElement = DisplayedScreenElement.Factory.newPooledInstance();
        this.m_elements.put(hashCode, newElement);
        return newElement;
    }
    
    public void deleteDisplayedScreenElement(final DisplayedScreenElement element) {
        if (element.getNumReferences() <= 0) {
            final long hashCode = element.m_element.m_hashCode;
            assert hashCode != 0L;
            this.m_elements.remove(hashCode);
        }
        element.removeReference();
    }
    
    public void createSpriteForLOD(final byte visibilityMask) {
        final TLongObjectIterator<DisplayedScreenElement> iterator = this.m_elements.iterator();
        while (iterator.hasNext()) {
            iterator.advance();
            final DisplayedScreenElement screenElement = iterator.value();
            if (screenElement.m_sprite != null) {
                continue;
            }
            if (!screenElement.checkVisibilityMask(visibilityMask)) {
                continue;
            }
            screenElement.createSprite();
        }
    }
    
    public void clear() {
        if (!this.m_elements.isEmpty()) {
            DisplayedScreenElementFactory.m_logger.error((Object)"la factory contient encore des \u00e9l\u00e9ments!!!");
        }
        this.m_elements.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)DisplayedScreenElementFactory.class);
    }
}
