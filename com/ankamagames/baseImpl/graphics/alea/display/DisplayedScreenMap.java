package com.ankamagames.baseImpl.graphics.alea.display;

import com.ankamagames.baseImpl.graphics.isometric.camera.*;
import com.ankamagames.baseImpl.graphics.isometric.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import java.util.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public class DisplayedScreenMap
{
    private static final int MIN_NUM_ELEMENTS_FOR_DICHOTOMY = 16;
    private static final ArrayList<DisplayedScreenElement> m_cellElements;
    private boolean m_isReady;
    private ScreenMap m_map;
    private DisplayedScreenElement[] m_elements;
    private final ArrayList<DisplayedScreenElement> m_animatedElements;
    private static final Comparator<DisplayedScreenElement> ELEMENT_HEIGHTS_COMPARATOR;
    
    public DisplayedScreenMap() {
        super();
        this.m_animatedElements = new ArrayList<DisplayedScreenElement>();
        this.m_isReady = false;
    }
    
    public void setMap(final ScreenMap map, final DisplayedScreenElementFactory factory, final boolean forceReload) {
        this.clear(factory);
        this.m_map = map;
        if (this.m_map == null) {
            return;
        }
        final int numElements = this.m_map.getElementsCountForVisibilityMask(DisplayedScreenWorld.getVisibilityMask());
        this.m_elements = new DisplayedScreenElement[numElements];
        int displayedElementsIndex = 0;
        for (int i = 0, size = this.m_map.m_elements.size(); i < size; ++i) {
            final ScreenElement screenElement = this.m_map.m_elements.get(i);
            final byte visibilityMask = screenElement.getCommonProperties().getVisibilityMask();
            if ((visibilityMask & DisplayedScreenWorld.getVisibilityMask()) == visibilityMask) {
                final DisplayedScreenElement displayedScreenElement = factory.createDisplayedScreenElement(screenElement.m_hashCode);
                if (forceReload || displayedScreenElement.m_element == null) {
                    displayedScreenElement.setElement(screenElement);
                }
                this.m_elements[displayedElementsIndex] = displayedScreenElement;
                ++displayedElementsIndex;
                if (screenElement.isAnimated()) {
                    this.m_animatedElements.add(displayedScreenElement);
                }
            }
        }
    }
    
    final boolean isVisible(final AbstractCamera camera) {
        return this.m_map != null && camera.isVisibleInScreen(this.m_map.m_maxY, this.m_map.m_minX, this.m_map.m_minY, this.m_map.m_maxX);
    }
    
    ArrayList<DisplayedScreenElement> getAnimatedElements() {
        return this.m_animatedElements;
    }
    
    public void update() {
        if (this.m_isReady) {
            return;
        }
        if (this.m_elements == null) {
            this.m_isReady = true;
            return;
        }
        for (int i = 0; i < this.m_elements.length; ++i) {
            final DisplayedScreenElement element = this.m_elements[i];
            if (element.m_sprite != null) {
                if (element.m_sprite.getTexture() != null) {
                    if (!element.m_sprite.getTexture().isReady()) {
                        return;
                    }
                }
            }
        }
        this.m_isReady = true;
    }
    
    public void addToScene(final IsoWorldScene scene, final ArrayList<DisplayedScreenElement> entities, final AbstractCamera camera) {
        if (!this.isVisible(camera)) {
            return;
        }
        for (int i = 0, count = this.m_elements.length; i < count; ++i) {
            this.m_elements[i].addToScene(scene, entities, camera);
        }
    }
    
    public final ScreenMap getMap() {
        return this.m_map;
    }
    
    public final DisplayedScreenElement[] getElements() {
        return this.m_elements;
    }
    
    public boolean isReady() {
        return this.m_isReady;
    }
    
    final void getElements(final int cellX, final int cellY, final ArrayList<DisplayedScreenElement> elements, final ElementFilter elementFilter) {
        if (this.m_elements == null) {
            return;
        }
        if (!this.containsCoord(cellX, cellY)) {
            return;
        }
        final int elementsCount = this.m_elements.length;
        if (elementsCount <= 16) {
            for (int i = 0; i < elementsCount; ++i) {
                final DisplayedScreenElement screenElement = this.m_elements[i];
                final ScreenElement element = screenElement.m_element;
                if (element.m_cellX == cellX && element.m_cellY == cellY && elementFilter.isValid(screenElement) && !elements.contains(screenElement)) {
                    elements.add(screenElement);
                }
            }
            return;
        }
        int minIndex = 0;
        int maxIndex = elementsCount - 1;
        int cellIndex = -1;
        do {
            final int midIndex = maxIndex + minIndex >>> 1;
            if (minIndex + 1 == maxIndex) {
                ScreenElement element2 = this.m_elements[minIndex].m_element;
                if (element2.m_cellX == cellX && element2.m_cellY == cellY) {
                    cellIndex = minIndex;
                    break;
                }
                element2 = this.m_elements[maxIndex].m_element;
                if (element2.m_cellX == cellX && element2.m_cellY == cellY) {
                    cellIndex = maxIndex;
                    break;
                }
                return;
            }
            else {
                final ScreenElement element2 = this.m_elements[midIndex].m_element;
                if (element2.m_cellY > cellY) {
                    maxIndex = midIndex;
                }
                else if (element2.m_cellY < cellY) {
                    minIndex = midIndex;
                }
                else if (element2.m_cellX > cellX) {
                    maxIndex = midIndex;
                }
                else if (element2.m_cellX < cellX) {
                    minIndex = midIndex;
                }
                else {
                    cellIndex = midIndex;
                }
            }
        } while (cellIndex == -1);
        int numElements;
        for (minIndex = cellIndex, numElements = 1; minIndex - numElements >= 0; ++numElements) {
            final ScreenElement element3 = this.m_elements[minIndex - numElements].m_element;
            if (element3.m_cellX != cellX) {
                break;
            }
            if (element3.m_cellY != cellY) {
                break;
            }
        }
        minIndex = minIndex + 1 - numElements;
        while (cellIndex + 1 < elementsCount) {
            final ScreenElement element3 = this.m_elements[++cellIndex].m_element;
            if (element3.m_cellX != cellX) {
                break;
            }
            if (element3.m_cellY != cellY) {
                break;
            }
            ++numElements;
        }
        for (int j = 0; j < numElements; ++j) {
            final DisplayedScreenElement element4 = this.m_elements[minIndex + j];
            if (elementFilter.isValid(element4) && !elements.contains(element4)) {
                elements.add(element4);
            }
        }
    }
    
    final DisplayedScreenElement getElementAtTop(final int x, final int y, final ElementFilter elementFilter) {
        if (!this.containsCoord(x, y)) {
            return null;
        }
        DisplayedScreenMap.m_cellElements.clear();
        this.getElements(x, y, DisplayedScreenMap.m_cellElements, elementFilter);
        DisplayedScreenElement elementAtTop = null;
        for (int numElements = DisplayedScreenMap.m_cellElements.size(), i = 0; i < numElements; ++i) {
            final DisplayedScreenElement screenElement = DisplayedScreenMap.m_cellElements.get(i);
            if (screenElement.m_element.m_cellX == x) {
                if (screenElement.m_element.m_cellY == y) {
                    if (elementAtTop == null || elementAtTop.m_element.m_altitudeOrder <= screenElement.m_element.m_altitudeOrder) {
                        elementAtTop = screenElement;
                    }
                }
            }
        }
        return elementAtTop;
    }
    
    final DisplayedScreenElement getElementFromTop(final int from, final int x, final int y, final ElementFilter elementFilter) {
        if (!this.containsCoord(x, y)) {
            return null;
        }
        DisplayedScreenMap.m_cellElements.clear();
        this.getElements(x, y, DisplayedScreenMap.m_cellElements, elementFilter);
        for (int i = DisplayedScreenMap.m_cellElements.size() - 1; i >= 0; --i) {
            final DisplayedScreenElement screenElement = DisplayedScreenMap.m_cellElements.get(i);
            if (screenElement.m_element.m_cellX != x || screenElement.m_element.m_cellY != y) {
                DisplayedScreenMap.m_cellElements.remove(i);
            }
        }
        if (DisplayedScreenMap.m_cellElements.size() <= from) {
            return null;
        }
        Collections.sort(DisplayedScreenMap.m_cellElements, DisplayedScreenMap.ELEMENT_HEIGHTS_COMPARATOR);
        return DisplayedScreenMap.m_cellElements.get(DisplayedScreenMap.m_cellElements.size() - 1 - from);
    }
    
    DisplayedScreenElement getElementAtTop(final int x, final int y, final int z, final ElementFilter elementFilter) {
        if (!this.containsCoord(x, y, (short)z)) {
            return null;
        }
        DisplayedScreenMap.m_cellElements.clear();
        this.getElements(x, y, DisplayedScreenMap.m_cellElements, elementFilter);
        DisplayedScreenElement elementAtTop = null;
        for (int numElements = DisplayedScreenMap.m_cellElements.size(), i = 0; i < numElements; ++i) {
            final DisplayedScreenElement screenElement = DisplayedScreenMap.m_cellElements.get(i);
            final ScreenElement element = screenElement.m_element;
            if (element.m_cellX == x) {
                if (element.m_cellY == y) {
                    if (element.m_cellZ != z) {
                        if (!element.m_commonData.isMoveTop()) {
                            continue;
                        }
                        if (element.m_cellZ - element.m_height != z) {
                            continue;
                        }
                    }
                    if (elementAtTop == null || elementAtTop.m_element.m_altitudeOrder <= element.m_altitudeOrder) {
                        elementAtTop = screenElement;
                    }
                }
            }
        }
        return elementAtTop;
    }
    
    final DisplayedScreenElement getWalkableElementAtTop(final int x, final int y, final int z, final ElementFilter elementFilter) {
        if (!this.containsCoord(x, y, (short)z)) {
            return null;
        }
        DisplayedScreenMap.m_cellElements.clear();
        this.getElements(x, y, DisplayedScreenMap.m_cellElements, elementFilter);
        DisplayedScreenElement elementAtTop = null;
        for (int numElements = DisplayedScreenMap.m_cellElements.size(), i = 0; i < numElements; ++i) {
            final DisplayedScreenElement screenElement = DisplayedScreenMap.m_cellElements.get(i);
            assert screenElement.m_element.m_cellX == x;
            assert screenElement.m_element.m_cellY == y;
            if (screenElement.m_element.m_cellZ == z) {
                if (elementAtTop == null || elementAtTop.m_element.m_altitudeOrder <= screenElement.m_element.m_altitudeOrder) {
                    if (screenElement.m_element.m_commonData.isWalkable()) {
                        elementAtTop = screenElement;
                    }
                }
            }
        }
        return elementAtTop;
    }
    
    void getDisplayedElements(final int x, final int y, final ArrayList<DisplayedScreenElement> elements) {
        if (!this.contains(x, y)) {
            return;
        }
        for (int i = 0; i < this.m_elements.length; ++i) {
            final DisplayedScreenElement element = this.m_elements[i];
            final Entity sprite = element.m_sprite;
            if (y >= sprite.m_minY) {
                if (y < sprite.m_maxY) {
                    if (x >= sprite.m_minX) {
                        if (x < sprite.m_maxX) {
                            if (!element.m_element.m_commonData.isMoveTop()) {
                                if (element.isVisible()) {
                                    if (element.fineHitTest(x, y)) {
                                        if (!elements.contains(element)) {
                                            elements.add(element);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean containsCoord(final int x, final int y) {
        return this.m_map == null || this.m_map.isInMap(x, y);
    }
    
    private boolean containsCoord(final int x, final int y, final short z) {
        return this.m_map == null || this.m_map.isInMap(x, y, z);
    }
    
    private boolean contains(final int x, final int y) {
        return this.m_map != null && x <= this.m_map.m_maxX && x >= this.m_map.m_minX && y <= this.m_map.m_maxY && y >= this.m_map.m_minY;
    }
    
    public void clear(final DisplayedScreenElementFactory factory) {
        if (this.m_map == null) {
            return;
        }
        for (int i = 0; i < this.m_elements.length; ++i) {
            final DisplayedScreenElement element = this.m_elements[i];
            element.clear(factory);
        }
        this.m_animatedElements.clear();
        this.m_elements = null;
        this.m_map = null;
        this.m_isReady = false;
    }
    
    @Override
    public final String toString() {
        return "DisplayedScreenMap {" + this.m_map + "}";
    }
    
    static {
        m_cellElements = new ArrayList<DisplayedScreenElement>(64);
        ELEMENT_HEIGHTS_COMPARATOR = new Comparator<DisplayedScreenElement>() {
            @Override
            public int compare(final DisplayedScreenElement o1, final DisplayedScreenElement o2) {
                if (o1 == o2) {
                    return 0;
                }
                return o1.m_element.m_altitudeOrder - o2.m_element.m_altitudeOrder;
            }
        };
    }
}
