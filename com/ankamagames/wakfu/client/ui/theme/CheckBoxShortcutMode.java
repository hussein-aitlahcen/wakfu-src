package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class CheckBoxShortcutMode implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public CheckBoxShortcutMode() {
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
        ((TextWidgetAppearance)appearance).setAlignment(Alignment9.CENTER);
        appearance.setState("default");
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final String id = "shortcutBarButtonDefaultPixmap";
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut);
        }
        checkOut.setHeight(18);
        checkOut.setPosition(Alignment17.CENTER);
        checkOut.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut.setWidth(34);
        checkOut.setX(224);
        checkOut.setY(218);
        appearance.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        appearance.onChildrenAdded();
        final DecoratorAppearance appearance2 = widget.getAppearance();
        appearance2.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance2).setAlignment(Alignment9.CENTER);
        appearance2.setState("mouseHover");
        widget.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final String id2 = "shortcutBarButtonOverPixmap";
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut2);
        }
        checkOut2.setHeight(18);
        checkOut2.setPosition(Alignment17.CENTER);
        checkOut2.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut2.setWidth(34);
        checkOut2.setX(984);
        checkOut2.setY(220);
        appearance2.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        appearance2.onChildrenAdded();
        final DecoratorAppearance appearance3 = widget.getAppearance();
        appearance3.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance3).setAlignment(Alignment9.CENTER);
        appearance3.setState("pressed");
        widget.addBasicElement(appearance3);
        appearance3.onAttributesInitialized();
        final String id3 = "shortcutBarButtonPressedPixmap";
        final PixmapElement checkOut3 = PixmapElement.checkOut();
        checkOut3.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut3);
        }
        checkOut3.setHeight(18);
        checkOut3.setPosition(Alignment17.CENTER);
        checkOut3.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut3.setWidth(34);
        checkOut3.setX(760);
        checkOut3.setY(239);
        appearance3.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        appearance3.onChildrenAdded();
        final DecoratorAppearance appearance4 = widget.getAppearance();
        appearance4.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance4).setAlignment(Alignment9.CENTER);
        appearance4.setState("selected");
        widget.addBasicElement(appearance4);
        appearance4.onAttributesInitialized();
        final String id4 = "shortcutBarButtonSelectedPixmap";
        final PixmapElement checkOut4 = PixmapElement.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, checkOut4);
        }
        checkOut4.setHeight(18);
        checkOut4.setPosition(Alignment17.CENTER);
        checkOut4.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut4.setWidth(34);
        checkOut4.setX(515);
        checkOut4.setY(218);
        appearance4.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        appearance4.onChildrenAdded();
        final DecoratorAppearance appearance5 = widget.getAppearance();
        appearance5.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance5).setAlignment(Alignment9.CENTER);
        appearance5.setState("mouseHoverSelected");
        widget.addBasicElement(appearance5);
        appearance5.onAttributesInitialized();
        final String id5 = "shortcutBarButtonSelectedOverPixmap";
        final PixmapElement checkOut5 = PixmapElement.checkOut();
        checkOut5.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, checkOut5);
        }
        checkOut5.setHeight(18);
        checkOut5.setPosition(Alignment17.CENTER);
        checkOut5.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut5.setWidth(34);
        checkOut5.setX(569);
        checkOut5.setY(218);
        appearance5.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        appearance5.onChildrenAdded();
        final DecoratorAppearance appearance6 = widget.getAppearance();
        appearance6.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance6).setAlignment(Alignment9.CENTER);
        appearance6.setState("pressedSelected");
        widget.addBasicElement(appearance6);
        appearance6.onAttributesInitialized();
        final String id6 = "shortcutBarButtonSelectedPressedPixmap";
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, checkOut6);
        }
        checkOut6.setHeight(18);
        checkOut6.setPosition(Alignment17.CENTER);
        checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut6.setWidth(34);
        checkOut6.setX(350);
        checkOut6.setY(218);
        appearance6.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        appearance6.onChildrenAdded();
    }
}
