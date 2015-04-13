package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.appearance.*;

public class ContainerVerticalBigProgressBar implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ContainerVerticalBigProgressBar() {
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
        final String id = "verticalProgressBigContainerBorder";
        final PixmapBorder pixmapBorder = new PixmapBorder();
        pixmapBorder.onCheckOut();
        pixmapBorder.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, pixmapBorder);
        }
        appearance.addBasicElement(pixmapBorder);
        pixmapBorder.onAttributesInitialized();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(16);
        checkOut.setPosition(Alignment17.NORTH_EAST);
        checkOut.setRotation(GeometrySprite.SpriteRotation.QUARTER_CLOCKWISE);
        checkOut.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut.setWidth(4);
        checkOut.setX(690);
        checkOut.setY(219);
        pixmapBorder.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setHeight(16);
        checkOut2.setPosition(Alignment17.EAST);
        checkOut2.setRotation(GeometrySprite.SpriteRotation.QUARTER_CLOCKWISE);
        checkOut2.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut2.setWidth(1);
        checkOut2.setX(829);
        checkOut2.setY(220);
        pixmapBorder.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final PixmapElement checkOut3 = PixmapElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setFlipHorizontaly(true);
        checkOut3.setHeight(16);
        checkOut3.setPosition(Alignment17.SOUTH_EAST);
        checkOut3.setRotation(GeometrySprite.SpriteRotation.QUARTER_CLOCKWISE);
        checkOut3.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut3.setWidth(4);
        checkOut3.setX(690);
        checkOut3.setY(219);
        pixmapBorder.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final PixmapElement checkOut4 = PixmapElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setFlipHorizontaly(true);
        checkOut4.setHeight(1);
        checkOut4.setPosition(Alignment17.SOUTH);
        checkOut4.setRotation(GeometrySprite.SpriteRotation.QUARTER_CLOCKWISE);
        checkOut4.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut4.setWidth(4);
        checkOut4.setX(977);
        checkOut4.setY(200);
        pixmapBorder.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final PixmapElement checkOut5 = PixmapElement.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setFlipHorizontaly(true);
        checkOut5.setHeight(9);
        checkOut5.setPosition(Alignment17.SOUTH_WEST);
        checkOut5.setRotation(GeometrySprite.SpriteRotation.QUARTER_CLOCKWISE);
        checkOut5.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut5.setWidth(5);
        checkOut5.setX(86);
        checkOut5.setY(487);
        pixmapBorder.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setHeight(9);
        checkOut6.setPosition(Alignment17.WEST);
        checkOut6.setRotation(GeometrySprite.SpriteRotation.QUARTER_CLOCKWISE);
        checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut6.setWidth(1);
        checkOut6.setX(89);
        checkOut6.setY(574);
        pixmapBorder.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        final PixmapElement checkOut7 = PixmapElement.checkOut();
        checkOut7.setElementMap(elementMap);
        checkOut7.setHeight(9);
        checkOut7.setPosition(Alignment17.NORTH_WEST);
        checkOut7.setRotation(GeometrySprite.SpriteRotation.QUARTER_CLOCKWISE);
        checkOut7.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut7.setWidth(5);
        checkOut7.setX(86);
        checkOut7.setY(487);
        pixmapBorder.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        final PixmapElement checkOut8 = PixmapElement.checkOut();
        checkOut8.setElementMap(elementMap);
        checkOut8.setHeight(1);
        checkOut8.setPosition(Alignment17.NORTH);
        checkOut8.setRotation(GeometrySprite.SpriteRotation.QUARTER_CLOCKWISE);
        checkOut8.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut8.setWidth(4);
        checkOut8.setX(977);
        checkOut8.setY(200);
        pixmapBorder.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        pixmapBorder.onChildrenAdded();
        final String id2 = "verticalProgressBigContainerBackground";
        final PixmapBackground checkOut9 = PixmapBackground.checkOut();
        checkOut9.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut9);
        }
        checkOut9.setScaled(true);
        appearance.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        final PixmapElement checkOut10 = PixmapElement.checkOut();
        checkOut10.setElementMap(elementMap);
        checkOut10.setHeight(1);
        checkOut10.setPosition(Alignment17.CENTER);
        checkOut10.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut10.setWidth(1);
        checkOut10.setX(649);
        checkOut10.setY(195);
        checkOut9.addBasicElement(checkOut10);
        checkOut10.onAttributesInitialized();
        checkOut10.onChildrenAdded();
        checkOut9.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
