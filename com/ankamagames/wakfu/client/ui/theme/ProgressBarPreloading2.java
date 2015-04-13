package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class ProgressBarPreloading2 implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ProgressBarPreloading2() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public void applyStyle(final ElementMap item, final DocumentParser doc, final Widget widget) {
        this.doc = doc;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final DecoratorAppearance appearance = widget.getAppearance();
        appearance.setElementMap(elementMap);
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(0);
        checkOut.setPosition(Alignment17.NORTH_WEST);
        checkOut.setWidth(0);
        checkOut.setX(5);
        checkOut.setY(209);
        appearance.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setHeight(0);
        checkOut2.setPosition(Alignment17.NORTH);
        checkOut2.setWidth(0);
        checkOut2.setX(20);
        checkOut2.setY(209);
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final PixmapElement checkOut3 = PixmapElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setHeight(0);
        checkOut3.setPosition(Alignment17.NORTH_EAST);
        checkOut3.setWidth(0);
        checkOut3.setX(25);
        checkOut3.setY(209);
        appearance.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final PixmapElement checkOut4 = PixmapElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setHeight(0);
        checkOut4.setPosition(Alignment17.WEST);
        checkOut4.setWidth(0);
        checkOut4.setX(5);
        checkOut4.setY(209);
        appearance.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final PixmapElement checkOut5 = PixmapElement.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setHeight(249);
        checkOut5.setPosition(Alignment17.CENTER);
        checkOut5.setTexture(this.doc.getTexture("preWorld_1.tga"));
        checkOut5.setWidth(209);
        checkOut5.setX(725);
        checkOut5.setY(224);
        appearance.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setHeight(0);
        checkOut6.setPosition(Alignment17.EAST);
        checkOut6.setWidth(0);
        checkOut6.setX(25);
        checkOut6.setY(209);
        appearance.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        final PixmapElement checkOut7 = PixmapElement.checkOut();
        checkOut7.setElementMap(elementMap);
        checkOut7.setHeight(0);
        checkOut7.setPosition(Alignment17.SOUTH_WEST);
        checkOut7.setWidth(0);
        checkOut7.setX(5);
        checkOut7.setY(209);
        appearance.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        final PixmapElement checkOut8 = PixmapElement.checkOut();
        checkOut8.setElementMap(elementMap);
        checkOut8.setHeight(0);
        checkOut8.setPosition(Alignment17.SOUTH);
        checkOut8.setWidth(0);
        checkOut8.setX(20);
        checkOut8.setY(209);
        appearance.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        final PixmapElement checkOut9 = PixmapElement.checkOut();
        checkOut9.setElementMap(elementMap);
        checkOut9.setHeight(0);
        checkOut9.setPosition(Alignment17.SOUTH_EAST);
        checkOut9.setWidth(0);
        checkOut9.setX(25);
        checkOut9.setY(209);
        appearance.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        checkOut9.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
