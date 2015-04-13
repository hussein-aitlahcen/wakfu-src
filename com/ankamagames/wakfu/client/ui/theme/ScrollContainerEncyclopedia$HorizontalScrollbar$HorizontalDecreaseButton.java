package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.xulor2.appearance.*;

public class ScrollContainerEncyclopedia$HorizontalScrollbar$HorizontalDecreaseButton implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ScrollContainerEncyclopedia$HorizontalScrollbar$HorizontalDecreaseButton() {
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
        appearance.setState("default");
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final String id = "pmSmallLeftSideArrow2Default";
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut);
        }
        checkOut.setHeight(16);
        checkOut.setPosition(Alignment17.CENTER);
        checkOut.setRotation(GeometrySprite.SpriteRotation.QUARTER_COUNTER_CLOCKWISE);
        checkOut.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut.setWidth(28);
        checkOut.setX(33);
        checkOut.setY(199);
        appearance.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        appearance.onChildrenAdded();
        final DecoratorAppearance appearance2 = widget.getAppearance();
        appearance2.setElementMap(elementMap);
        appearance2.setState("mouseHover");
        widget.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final String id2 = "pmSmallLeftSideArrow2Over";
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut2);
        }
        checkOut2.setHeight(16);
        checkOut2.setPosition(Alignment17.CENTER);
        checkOut2.setRotation(GeometrySprite.SpriteRotation.QUARTER_COUNTER_CLOCKWISE);
        checkOut2.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut2.setWidth(28);
        checkOut2.setX(946);
        checkOut2.setY(199);
        appearance2.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        appearance2.onChildrenAdded();
        final DecoratorAppearance appearance3 = widget.getAppearance();
        appearance3.setElementMap(elementMap);
        appearance3.setState("pressed");
        widget.addBasicElement(appearance3);
        appearance3.onAttributesInitialized();
        final String id3 = "pmSmallLeftSideArrow2Pressed";
        final PixmapElement checkOut3 = PixmapElement.checkOut();
        checkOut3.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut3);
        }
        checkOut3.setHeight(16);
        checkOut3.setPosition(Alignment17.CENTER);
        checkOut3.setRotation(GeometrySprite.SpriteRotation.QUARTER_COUNTER_CLOCKWISE);
        checkOut3.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut3.setWidth(28);
        checkOut3.setX(378);
        checkOut3.setY(37);
        appearance3.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        appearance3.onChildrenAdded();
        final DecoratorAppearance appearance4 = widget.getAppearance();
        appearance4.setElementMap(elementMap);
        appearance4.setState("disabled");
        widget.addBasicElement(appearance4);
        appearance4.onAttributesInitialized();
        final String id4 = "pmSmallLeftSideArrow2Disabled";
        final PixmapElement checkOut4 = PixmapElement.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, checkOut4);
        }
        checkOut4.setHeight(16);
        checkOut4.setPosition(Alignment17.CENTER);
        checkOut4.setRotation(GeometrySprite.SpriteRotation.QUARTER_COUNTER_CLOCKWISE);
        checkOut4.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut4.setWidth(28);
        checkOut4.setX(899);
        checkOut4.setY(180);
        appearance4.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        appearance4.onChildrenAdded();
        final DecoratorAppearance appearance5 = widget.getAppearance();
        appearance5.setElementMap(elementMap);
        appearance5.setState("selected");
        widget.addBasicElement(appearance5);
        appearance5.onAttributesInitialized();
        final String id5 = "pmSmallLeftSideArrow2DefaultSelected";
        final PixmapElement checkOut5 = PixmapElement.checkOut();
        checkOut5.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, checkOut5);
        }
        checkOut5.setHeight(16);
        checkOut5.setPosition(Alignment17.CENTER);
        checkOut5.setRotation(GeometrySprite.SpriteRotation.QUARTER_COUNTER_CLOCKWISE);
        checkOut5.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut5.setWidth(28);
        checkOut5.setX(33);
        checkOut5.setY(199);
        appearance5.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        appearance5.onChildrenAdded();
        final DecoratorAppearance appearance6 = widget.getAppearance();
        appearance6.setElementMap(elementMap);
        appearance6.setState("mouseHoverSelected");
        widget.addBasicElement(appearance6);
        appearance6.onAttributesInitialized();
        final String id6 = "pmSmallLeftSideArrow2OverSelected";
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, checkOut6);
        }
        checkOut6.setHeight(16);
        checkOut6.setPosition(Alignment17.CENTER);
        checkOut6.setRotation(GeometrySprite.SpriteRotation.QUARTER_COUNTER_CLOCKWISE);
        checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut6.setWidth(28);
        checkOut6.setX(33);
        checkOut6.setY(199);
        appearance6.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        appearance6.onChildrenAdded();
        final DecoratorAppearance appearance7 = widget.getAppearance();
        appearance7.setElementMap(elementMap);
        appearance7.setState("pressedSelected");
        widget.addBasicElement(appearance7);
        appearance7.onAttributesInitialized();
        final String id7 = "pmSmallLeftSideArrow2PressedSelected";
        final PixmapElement checkOut7 = PixmapElement.checkOut();
        checkOut7.setElementMap(elementMap);
        if (elementMap != null && id7 != null) {
            elementMap.add(id7, checkOut7);
        }
        checkOut7.setHeight(16);
        checkOut7.setPosition(Alignment17.CENTER);
        checkOut7.setRotation(GeometrySprite.SpriteRotation.QUARTER_COUNTER_CLOCKWISE);
        checkOut7.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut7.setWidth(28);
        checkOut7.setX(33);
        checkOut7.setY(199);
        appearance7.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        appearance7.onChildrenAdded();
        final DecoratorAppearance appearance8 = widget.getAppearance();
        appearance8.setElementMap(elementMap);
        appearance8.setState("disabledSelected");
        widget.addBasicElement(appearance8);
        appearance8.onAttributesInitialized();
        final String id8 = "pmSmallLeftSideArrow2DisabledSelected";
        final PixmapElement checkOut8 = PixmapElement.checkOut();
        checkOut8.setElementMap(elementMap);
        if (elementMap != null && id8 != null) {
            elementMap.add(id8, checkOut8);
        }
        checkOut8.setHeight(16);
        checkOut8.setPosition(Alignment17.CENTER);
        checkOut8.setRotation(GeometrySprite.SpriteRotation.QUARTER_COUNTER_CLOCKWISE);
        checkOut8.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut8.setWidth(28);
        checkOut8.setX(369);
        checkOut8.setY(199);
        appearance8.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        appearance8.onChildrenAdded();
    }
}
