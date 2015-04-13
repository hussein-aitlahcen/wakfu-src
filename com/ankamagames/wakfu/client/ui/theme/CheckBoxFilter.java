package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class CheckBoxFilter implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public CheckBoxFilter() {
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
        ((TextWidgetAppearance)appearance).setAlignment(Alignment9.WEST);
        appearance.setState("default");
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final FontElement checkOut = FontElement.checkOut();
        checkOut.setRenderer(Xulor.getInstance().getDocumentParser().getFont("defaultFont"));
        checkOut.setElementMap(elementMap);
        appearance.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        final String id = "defaultLightColor";
        final ColorElement checkOut2 = ColorElement.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut2);
        }
        checkOut2.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final PixmapElement checkOut3 = PixmapElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setHeight(20);
        checkOut3.setPosition(Alignment17.CENTER);
        checkOut3.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut3.setWidth(20);
        checkOut3.setX(373);
        checkOut3.setY(261);
        appearance.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        appearance.onChildrenAdded();
        final DecoratorAppearance appearance2 = widget.getAppearance();
        appearance2.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance2).setAlignment(Alignment9.WEST);
        appearance2.setState("mouseHover");
        widget.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final FontElement checkOut4 = FontElement.checkOut();
        checkOut4.setRenderer(Xulor.getInstance().getDocumentParser().getFont("defaultFont"));
        checkOut4.setElementMap(elementMap);
        appearance2.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final String id2 = "defaultLightColor";
        final ColorElement checkOut5 = ColorElement.checkOut();
        checkOut5.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut5);
        }
        checkOut5.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        appearance2.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setHeight(20);
        checkOut6.setPosition(Alignment17.CENTER);
        checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut6.setWidth(20);
        checkOut6.setX(33);
        checkOut6.setY(283);
        appearance2.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        appearance2.onChildrenAdded();
        final DecoratorAppearance appearance3 = widget.getAppearance();
        appearance3.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance3).setAlignment(Alignment9.WEST);
        appearance3.setState("pressed");
        widget.addBasicElement(appearance3);
        appearance3.onAttributesInitialized();
        final FontElement checkOut7 = FontElement.checkOut();
        checkOut7.setRenderer(Xulor.getInstance().getDocumentParser().getFont("defaultFont"));
        checkOut7.setElementMap(elementMap);
        appearance3.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        final String id3 = "defaultLightColor";
        final ColorElement checkOut8 = ColorElement.checkOut();
        checkOut8.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut8);
        }
        checkOut8.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        appearance3.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        final PixmapElement checkOut9 = PixmapElement.checkOut();
        checkOut9.setElementMap(elementMap);
        checkOut9.setHeight(20);
        checkOut9.setPosition(Alignment17.CENTER);
        checkOut9.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut9.setWidth(20);
        checkOut9.setX(442);
        checkOut9.setY(261);
        appearance3.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        checkOut9.onChildrenAdded();
        appearance3.onChildrenAdded();
        final DecoratorAppearance appearance4 = widget.getAppearance();
        appearance4.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance4).setAlignment(Alignment9.WEST);
        appearance4.setState("selected");
        widget.addBasicElement(appearance4);
        appearance4.onAttributesInitialized();
        final FontElement checkOut10 = FontElement.checkOut();
        checkOut10.setRenderer(Xulor.getInstance().getDocumentParser().getFont("defaultFont"));
        checkOut10.setElementMap(elementMap);
        appearance4.addBasicElement(checkOut10);
        checkOut10.onAttributesInitialized();
        checkOut10.onChildrenAdded();
        final String id4 = "defaultLightColor";
        final ColorElement checkOut11 = ColorElement.checkOut();
        checkOut11.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, checkOut11);
        }
        checkOut11.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        appearance4.addBasicElement(checkOut11);
        checkOut11.onAttributesInitialized();
        checkOut11.onChildrenAdded();
        final PixmapElement checkOut12 = PixmapElement.checkOut();
        checkOut12.setElementMap(elementMap);
        checkOut12.setHeight(20);
        checkOut12.setPosition(Alignment17.CENTER);
        checkOut12.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut12.setWidth(20);
        checkOut12.setX(946);
        checkOut12.setY(283);
        appearance4.addBasicElement(checkOut12);
        checkOut12.onAttributesInitialized();
        checkOut12.onChildrenAdded();
        appearance4.onChildrenAdded();
        final DecoratorAppearance appearance5 = widget.getAppearance();
        appearance5.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance5).setAlignment(Alignment9.WEST);
        appearance5.setState("mouseHoverSelected");
        widget.addBasicElement(appearance5);
        appearance5.onAttributesInitialized();
        final FontElement checkOut13 = FontElement.checkOut();
        checkOut13.setRenderer(Xulor.getInstance().getDocumentParser().getFont("defaultFont"));
        checkOut13.setElementMap(elementMap);
        appearance5.addBasicElement(checkOut13);
        checkOut13.onAttributesInitialized();
        checkOut13.onChildrenAdded();
        final String id5 = "defaultLightColor";
        final ColorElement checkOut14 = ColorElement.checkOut();
        checkOut14.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, checkOut14);
        }
        checkOut14.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        appearance5.addBasicElement(checkOut14);
        checkOut14.onAttributesInitialized();
        checkOut14.onChildrenAdded();
        final PixmapElement checkOut15 = PixmapElement.checkOut();
        checkOut15.setElementMap(elementMap);
        checkOut15.setHeight(20);
        checkOut15.setPosition(Alignment17.CENTER);
        checkOut15.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut15.setWidth(20);
        checkOut15.setX(442);
        checkOut15.setY(261);
        appearance5.addBasicElement(checkOut15);
        checkOut15.onAttributesInitialized();
        checkOut15.onChildrenAdded();
        appearance5.onChildrenAdded();
        final DecoratorAppearance appearance6 = widget.getAppearance();
        appearance6.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance6).setAlignment(Alignment9.WEST);
        appearance6.setState("pressedSelected");
        widget.addBasicElement(appearance6);
        appearance6.onAttributesInitialized();
        final FontElement checkOut16 = FontElement.checkOut();
        checkOut16.setRenderer(Xulor.getInstance().getDocumentParser().getFont("defaultFont"));
        checkOut16.setElementMap(elementMap);
        appearance6.addBasicElement(checkOut16);
        checkOut16.onAttributesInitialized();
        checkOut16.onChildrenAdded();
        final String id6 = "defaultLightColor";
        final ColorElement checkOut17 = ColorElement.checkOut();
        checkOut17.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, checkOut17);
        }
        checkOut17.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        appearance6.addBasicElement(checkOut17);
        checkOut17.onAttributesInitialized();
        checkOut17.onChildrenAdded();
        final PixmapElement checkOut18 = PixmapElement.checkOut();
        checkOut18.setElementMap(elementMap);
        checkOut18.setHeight(20);
        checkOut18.setPosition(Alignment17.CENTER);
        checkOut18.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut18.setWidth(20);
        checkOut18.setX(33);
        checkOut18.setY(283);
        appearance6.addBasicElement(checkOut18);
        checkOut18.onAttributesInitialized();
        checkOut18.onChildrenAdded();
        appearance6.onChildrenAdded();
    }
}
