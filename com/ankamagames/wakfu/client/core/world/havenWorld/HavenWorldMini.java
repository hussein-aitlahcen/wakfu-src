package com.ankamagames.wakfu.client.core.world.havenWorld;

import org.apache.log4j.*;
import java.util.*;
import com.ankamagames.wakfu.client.alea.graphics.havenWorldMini.*;
import com.ankamagames.baseImpl.graphics.alea.display.displayScreenworldHelpers.*;
import com.ankamagames.baseImpl.graphics.alea.display.*;
import com.ankamagames.framework.kernel.core.maths.*;

public class HavenWorldMini
{
    private static final Logger m_logger;
    private final HavenWorldTopology m_havenWorld;
    private final int m_patchByCellX;
    private final int m_patchByCellY;
    private final ArrayList<DisplayedScreenMultiElement> m_elements;
    
    public HavenWorldMini(final HavenWorldTopology havenWorld, final int patchByCellX, final int patchByCellY) {
        super();
        this.m_elements = new ArrayList<DisplayedScreenMultiElement>();
        this.m_havenWorld = havenWorld;
        this.m_patchByCellX = patchByCellX;
        this.m_patchByCellY = patchByCellY;
    }
    
    public void apply(final DisplayedScreenWorld displayedScreenWorld, final int posX, final int posY, final short posZ) {
        final Rect bounds = this.m_havenWorld.getEditableWorldBounds();
        final int cellCountW = bounds.width() / this.m_patchByCellX;
        final int cellCountH = bounds.height() / this.m_patchByCellY;
        final HavenWorldImagesLibrary library = HavenWorldImagesLibrary.INSTANCE;
        for (int y = 0; y < cellCountH; ++y) {
            for (int x = 0; x < cellCountW; ++x) {
                final DisplayedScreenMultiElement elt = DisplayedScreenMultiElement.Factory.newPooledInstance(this.m_havenWorld, this.m_patchByCellX * x + bounds.m_xMin, this.m_patchByCellY * y + bounds.m_yMin, this.m_patchByCellX, this.m_patchByCellY);
                elt.setImageOffsets(library);
                final int cellX = posX + x;
                final int cellY = posY + y;
                final DisplayedScreenElement elementAtTop = displayedScreenWorld.getElementAtTop(cellX, cellY, ElementFilter.ALL);
                final short cellZ = (elementAtTop == null) ? posZ : elementAtTop.getElement().getCellZ();
                final int altitudeOrder = (elementAtTop == null) ? 5 : elementAtTop.getElement().getAltitudeOrder();
                elt.setCoordinates(cellX, cellY, cellZ, altitudeOrder);
                displayedScreenWorld.addExternalElement(elt);
                this.m_elements.add(elt);
            }
        }
    }
    
    public void clear(final DisplayedScreenWorld displayedScreenWorld) {
        for (int i = 0, elementsSize = this.m_elements.size(); i < elementsSize; ++i) {
            final DisplayedScreenMultiElement element = this.m_elements.get(i);
            displayedScreenWorld.removeExternalElement(element);
            element.removeReference();
        }
        this.m_elements.clear();
    }
    
    static {
        m_logger = Logger.getLogger((Class)HavenWorldMini.class);
    }
}
