package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class CheckBoxFightBar2 implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public CheckBoxFightBar2() {
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
        final PixmapBackground checkOut = PixmapBackground.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setScaled(true);
        appearance.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final String id = "fightBarButtonDefaultPixmap";
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut2);
        }
        checkOut2.setHeight(35);
        checkOut2.setPosition(Alignment17.CENTER);
        checkOut2.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut2.setWidth(33);
        checkOut2.setX(53);
        checkOut2.setY(548);
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        checkOut.onChildrenAdded();
        appearance.onChildrenAdded();
        final DecoratorAppearance appearance2 = widget.getAppearance();
        appearance2.setElementMap(elementMap);
        appearance2.setState("pressed");
        widget.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final PixmapBackground checkOut3 = PixmapBackground.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setScaled(true);
        appearance2.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        final String id2 = "fightBarButtonPressedPixmap";
        final PixmapElement checkOut4 = PixmapElement.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut4);
        }
        checkOut4.setHeight(35);
        checkOut4.setPosition(Alignment17.CENTER);
        checkOut4.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut4.setWidth(33);
        checkOut4.setX(656);
        checkOut4.setY(548);
        checkOut3.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        checkOut3.onChildrenAdded();
        appearance2.onChildrenAdded();
        final DecoratorAppearance appearance3 = widget.getAppearance();
        appearance3.setElementMap(elementMap);
        appearance3.setState("mouseHover");
        widget.addBasicElement(appearance3);
        appearance3.onAttributesInitialized();
        final PixmapBackground checkOut5 = PixmapBackground.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setScaled(true);
        appearance3.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        final String id3 = "fightBarButtonOverPixmap";
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut6);
        }
        checkOut6.setHeight(35);
        checkOut6.setPosition(Alignment17.CENTER);
        checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut6.setWidth(33);
        checkOut6.setX(2);
        checkOut6.setY(548);
        checkOut5.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        checkOut5.onChildrenAdded();
        appearance3.onChildrenAdded();
        final DecoratorAppearance appearance4 = widget.getAppearance();
        appearance4.setElementMap(elementMap);
        appearance4.setState("disabled");
        widget.addBasicElement(appearance4);
        appearance4.onAttributesInitialized();
        final PixmapBackground checkOut7 = PixmapBackground.checkOut();
        checkOut7.setElementMap(elementMap);
        checkOut7.setScaled(true);
        appearance4.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        final String id4 = "fightBarButtonDisabledPixmap";
        final PixmapElement checkOut8 = PixmapElement.checkOut();
        checkOut8.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, checkOut8);
        }
        checkOut8.setHeight(35);
        checkOut8.setPosition(Alignment17.CENTER);
        checkOut8.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut8.setWidth(33);
        checkOut8.setX(288);
        checkOut8.setY(548);
        checkOut7.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        checkOut7.onChildrenAdded();
        appearance4.onChildrenAdded();
        final DecoratorAppearance appearance5 = widget.getAppearance();
        appearance5.setElementMap(elementMap);
        appearance5.setState("selected");
        widget.addBasicElement(appearance5);
        appearance5.onAttributesInitialized();
        final PixmapBackground checkOut9 = PixmapBackground.checkOut();
        checkOut9.setElementMap(elementMap);
        checkOut9.setScaled(true);
        appearance5.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        final String id5 = "fightBarButtonSelectedPixmap";
        final PixmapElement checkOut10 = PixmapElement.checkOut();
        checkOut10.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, checkOut10);
        }
        checkOut10.setHeight(35);
        checkOut10.setPosition(Alignment17.CENTER);
        checkOut10.setTexture(this.doc.getTexture("default_0.tga"));
        checkOut10.setWidth(33);
        checkOut10.setX(269);
        checkOut10.setY(3);
        checkOut9.addBasicElement(checkOut10);
        checkOut10.onAttributesInitialized();
        checkOut10.onChildrenAdded();
        checkOut9.onChildrenAdded();
        appearance5.onChildrenAdded();
        final DecoratorAppearance appearance6 = widget.getAppearance();
        appearance6.setElementMap(elementMap);
        appearance6.setState("pressedSelected");
        widget.addBasicElement(appearance6);
        appearance6.onAttributesInitialized();
        final PixmapBackground checkOut11 = PixmapBackground.checkOut();
        checkOut11.setElementMap(elementMap);
        checkOut11.setScaled(true);
        appearance6.addBasicElement(checkOut11);
        checkOut11.onAttributesInitialized();
        final String id6 = "fightBarButtonSelectedPressedPixmap";
        final PixmapElement checkOut12 = PixmapElement.checkOut();
        checkOut12.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, checkOut12);
        }
        checkOut12.setHeight(35);
        checkOut12.setPosition(Alignment17.CENTER);
        checkOut12.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut12.setWidth(33);
        checkOut12.setX(656);
        checkOut12.setY(548);
        checkOut11.addBasicElement(checkOut12);
        checkOut12.onAttributesInitialized();
        checkOut12.onChildrenAdded();
        checkOut11.onChildrenAdded();
        appearance6.onChildrenAdded();
        final DecoratorAppearance appearance7 = widget.getAppearance();
        appearance7.setElementMap(elementMap);
        appearance7.setState("mouseHoverSelected");
        widget.addBasicElement(appearance7);
        appearance7.onAttributesInitialized();
        final PixmapBackground checkOut13 = PixmapBackground.checkOut();
        checkOut13.setElementMap(elementMap);
        checkOut13.setScaled(true);
        appearance7.addBasicElement(checkOut13);
        checkOut13.onAttributesInitialized();
        final String id7 = "fightBarButtonSelectedOverPixmap";
        final PixmapElement checkOut14 = PixmapElement.checkOut();
        checkOut14.setElementMap(elementMap);
        if (elementMap != null && id7 != null) {
            elementMap.add(id7, checkOut14);
        }
        checkOut14.setHeight(35);
        checkOut14.setPosition(Alignment17.CENTER);
        checkOut14.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut14.setWidth(33);
        checkOut14.setX(94);
        checkOut14.setY(549);
        checkOut13.addBasicElement(checkOut14);
        checkOut14.onAttributesInitialized();
        checkOut14.onChildrenAdded();
        checkOut13.onChildrenAdded();
        appearance7.onChildrenAdded();
        final DecoratorAppearance appearance8 = widget.getAppearance();
        appearance8.setElementMap(elementMap);
        appearance8.setState("disabledSelected");
        widget.addBasicElement(appearance8);
        appearance8.onAttributesInitialized();
        final PixmapBackground checkOut15 = PixmapBackground.checkOut();
        checkOut15.setElementMap(elementMap);
        checkOut15.setScaled(true);
        appearance8.addBasicElement(checkOut15);
        checkOut15.onAttributesInitialized();
        final String id8 = "fightBarButtonSelectedDisabledPixmap";
        final PixmapElement checkOut16 = PixmapElement.checkOut();
        checkOut16.setElementMap(elementMap);
        if (elementMap != null && id8 != null) {
            elementMap.add(id8, checkOut16);
        }
        checkOut16.setHeight(35);
        checkOut16.setPosition(Alignment17.CENTER);
        checkOut16.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut16.setWidth(33);
        checkOut16.setX(235);
        checkOut16.setY(548);
        checkOut15.addBasicElement(checkOut16);
        checkOut16.onAttributesInitialized();
        checkOut16.onChildrenAdded();
        checkOut15.onChildrenAdded();
        appearance8.onChildrenAdded();
    }
}
