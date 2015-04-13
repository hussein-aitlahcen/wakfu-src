package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class ContainerGreen implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ContainerGreen() {
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
        final String id = "greenBackground";
        final PixmapBackground checkOut = PixmapBackground.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut);
        }
        checkOut.setScaled(true);
        appearance.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final String id2 = "greenBackgroundNorthWest";
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut2);
        }
        checkOut2.setHeight(10);
        checkOut2.setPosition(Alignment17.NORTH_WEST);
        checkOut2.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut2.setWidth(10);
        checkOut2.setX(723);
        checkOut2.setY(152);
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final String id3 = "greenBackgroundNorth";
        final PixmapElement checkOut3 = PixmapElement.checkOut();
        checkOut3.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut3);
        }
        checkOut3.setHeight(10);
        checkOut3.setPosition(Alignment17.NORTH);
        checkOut3.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut3.setWidth(1);
        checkOut3.setX(393);
        checkOut3.setY(816);
        checkOut.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final String id4 = "greenBackgroundNorthEast";
        final PixmapElement checkOut4 = PixmapElement.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, checkOut4);
        }
        checkOut4.setFlipHorizontaly(true);
        checkOut4.setHeight(10);
        checkOut4.setPosition(Alignment17.NORTH_EAST);
        checkOut4.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut4.setWidth(10);
        checkOut4.setX(723);
        checkOut4.setY(152);
        checkOut.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final String id5 = "greenBackgroundWest";
        final PixmapElement checkOut5 = PixmapElement.checkOut();
        checkOut5.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, checkOut5);
        }
        checkOut5.setHeight(1);
        checkOut5.setPosition(Alignment17.WEST);
        checkOut5.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut5.setWidth(10);
        checkOut5.setX(328);
        checkOut5.setY(136);
        checkOut.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        final String id6 = "greenBackgroundCenter";
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, checkOut6);
        }
        checkOut6.setHeight(1);
        checkOut6.setPosition(Alignment17.CENTER);
        checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut6.setWidth(1);
        checkOut6.setX(602);
        checkOut6.setY(198);
        checkOut.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        final String id7 = "greenBackgroundEast";
        final PixmapElement checkOut7 = PixmapElement.checkOut();
        checkOut7.setElementMap(elementMap);
        if (elementMap != null && id7 != null) {
            elementMap.add(id7, checkOut7);
        }
        checkOut7.setFlipHorizontaly(true);
        checkOut7.setHeight(1);
        checkOut7.setPosition(Alignment17.EAST);
        checkOut7.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut7.setWidth(10);
        checkOut7.setX(328);
        checkOut7.setY(136);
        checkOut.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        final String id8 = "greenBackgroundSouthWest";
        final PixmapElement checkOut8 = PixmapElement.checkOut();
        checkOut8.setElementMap(elementMap);
        if (elementMap != null && id8 != null) {
            elementMap.add(id8, checkOut8);
        }
        checkOut8.setFlipVerticaly(true);
        checkOut8.setHeight(10);
        checkOut8.setPosition(Alignment17.SOUTH_WEST);
        checkOut8.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut8.setWidth(10);
        checkOut8.setX(723);
        checkOut8.setY(152);
        checkOut.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        final String id9 = "greenBackgroundSouth";
        final PixmapElement checkOut9 = PixmapElement.checkOut();
        checkOut9.setElementMap(elementMap);
        if (elementMap != null && id9 != null) {
            elementMap.add(id9, checkOut9);
        }
        checkOut9.setFlipVerticaly(true);
        checkOut9.setHeight(10);
        checkOut9.setPosition(Alignment17.SOUTH);
        checkOut9.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut9.setWidth(1);
        checkOut9.setX(393);
        checkOut9.setY(816);
        checkOut.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        checkOut9.onChildrenAdded();
        final String id10 = "greenBackgroundSouthEast";
        final PixmapElement checkOut10 = PixmapElement.checkOut();
        checkOut10.setElementMap(elementMap);
        if (elementMap != null && id10 != null) {
            elementMap.add(id10, checkOut10);
        }
        checkOut10.setFlipHorizontaly(true);
        checkOut10.setFlipVerticaly(true);
        checkOut10.setHeight(10);
        checkOut10.setPosition(Alignment17.SOUTH_EAST);
        checkOut10.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut10.setWidth(10);
        checkOut10.setX(723);
        checkOut10.setY(152);
        checkOut.addBasicElement(checkOut10);
        checkOut10.onAttributesInitialized();
        checkOut10.onChildrenAdded();
        checkOut.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
