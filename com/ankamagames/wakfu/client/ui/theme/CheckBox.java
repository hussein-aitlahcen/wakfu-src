package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class CheckBox implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public CheckBox() {
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
        final String id = "mecanicBlueColor";
        final ColorElement checkOut2 = ColorElement.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut2);
        }
        checkOut2.setColor(new Color(0.32f, 0.39f, 0.42f, 1.0f));
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final PixmapElement checkOut3 = PixmapElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setHeight(10);
        checkOut3.setPosition(Alignment17.CENTER);
        checkOut3.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut3.setWidth(10);
        checkOut3.setX(443);
        checkOut3.setY(152);
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
        final String id2 = "mecanicBlueColor";
        final ColorElement checkOut4 = ColorElement.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut4);
        }
        checkOut4.setColor(new Color(0.32f, 0.39f, 0.42f, 1.0f));
        appearance2.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final PixmapElement checkOut5 = PixmapElement.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setHeight(10);
        checkOut5.setPosition(Alignment17.CENTER);
        checkOut5.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut5.setWidth(10);
        checkOut5.setX(443);
        checkOut5.setY(152);
        appearance2.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        appearance2.onChildrenAdded();
        final DecoratorAppearance appearance3 = widget.getAppearance();
        appearance3.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance3).setAlignment(Alignment9.WEST);
        appearance3.setState("disabled");
        widget.addBasicElement(appearance3);
        appearance3.onAttributesInitialized();
        final ColorElement checkOut6 = ColorElement.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setColor(new Color(0.6f, 0.6f, 0.6f, 1.0f));
        appearance3.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        final PixmapElement checkOut7 = PixmapElement.checkOut();
        checkOut7.setElementMap(elementMap);
        checkOut7.setHeight(10);
        checkOut7.setPosition(Alignment17.CENTER);
        checkOut7.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut7.setWidth(10);
        checkOut7.setX(154);
        checkOut7.setY(152);
        appearance3.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        appearance3.onChildrenAdded();
        final DecoratorAppearance appearance4 = widget.getAppearance();
        appearance4.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance4).setAlignment(Alignment9.WEST);
        appearance4.setState("pressed");
        widget.addBasicElement(appearance4);
        appearance4.onAttributesInitialized();
        final String id3 = "mecanicBlueColor";
        final ColorElement checkOut8 = ColorElement.checkOut();
        checkOut8.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut8);
        }
        checkOut8.setColor(new Color(0.32f, 0.39f, 0.42f, 1.0f));
        appearance4.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        final PixmapElement checkOut9 = PixmapElement.checkOut();
        checkOut9.setElementMap(elementMap);
        checkOut9.setHeight(10);
        checkOut9.setPosition(Alignment17.CENTER);
        checkOut9.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut9.setWidth(10);
        checkOut9.setX(889);
        checkOut9.setY(152);
        appearance4.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        checkOut9.onChildrenAdded();
        appearance4.onChildrenAdded();
        final DecoratorAppearance appearance5 = widget.getAppearance();
        appearance5.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance5).setAlignment(Alignment9.WEST);
        appearance5.setState("selected");
        widget.addBasicElement(appearance5);
        appearance5.onAttributesInitialized();
        final String id4 = "mecanicBlueColor";
        final ColorElement checkOut10 = ColorElement.checkOut();
        checkOut10.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, checkOut10);
        }
        checkOut10.setColor(new Color(0.32f, 0.39f, 0.42f, 1.0f));
        appearance5.addBasicElement(checkOut10);
        checkOut10.onAttributesInitialized();
        checkOut10.onChildrenAdded();
        final PixmapElement checkOut11 = PixmapElement.checkOut();
        checkOut11.setElementMap(elementMap);
        checkOut11.setHeight(10);
        checkOut11.setPosition(Alignment17.CENTER);
        checkOut11.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut11.setWidth(10);
        checkOut11.setX(889);
        checkOut11.setY(152);
        appearance5.addBasicElement(checkOut11);
        checkOut11.onAttributesInitialized();
        checkOut11.onChildrenAdded();
        appearance5.onChildrenAdded();
        final DecoratorAppearance appearance6 = widget.getAppearance();
        appearance6.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance6).setAlignment(Alignment9.WEST);
        appearance6.setState("mouseHoverSelected");
        widget.addBasicElement(appearance6);
        appearance6.onAttributesInitialized();
        final String id5 = "mecanicBlueColor";
        final ColorElement checkOut12 = ColorElement.checkOut();
        checkOut12.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, checkOut12);
        }
        checkOut12.setColor(new Color(0.32f, 0.39f, 0.42f, 1.0f));
        appearance6.addBasicElement(checkOut12);
        checkOut12.onAttributesInitialized();
        checkOut12.onChildrenAdded();
        final PixmapElement checkOut13 = PixmapElement.checkOut();
        checkOut13.setElementMap(elementMap);
        checkOut13.setHeight(10);
        checkOut13.setPosition(Alignment17.CENTER);
        checkOut13.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut13.setWidth(10);
        checkOut13.setX(889);
        checkOut13.setY(152);
        appearance6.addBasicElement(checkOut13);
        checkOut13.onAttributesInitialized();
        checkOut13.onChildrenAdded();
        appearance6.onChildrenAdded();
        final DecoratorAppearance appearance7 = widget.getAppearance();
        appearance7.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance7).setAlignment(Alignment9.WEST);
        appearance7.setState("disabledSelected");
        widget.addBasicElement(appearance7);
        appearance7.onAttributesInitialized();
        final ColorElement checkOut14 = ColorElement.checkOut();
        checkOut14.setElementMap(elementMap);
        checkOut14.setColor(new Color(0.6f, 0.6f, 0.6f, 1.0f));
        appearance7.addBasicElement(checkOut14);
        checkOut14.onAttributesInitialized();
        checkOut14.onChildrenAdded();
        final PixmapElement checkOut15 = PixmapElement.checkOut();
        checkOut15.setElementMap(elementMap);
        checkOut15.setHeight(10);
        checkOut15.setPosition(Alignment17.CENTER);
        checkOut15.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut15.setWidth(10);
        checkOut15.setX(988);
        checkOut15.setY(765);
        appearance7.addBasicElement(checkOut15);
        checkOut15.onAttributesInitialized();
        checkOut15.onChildrenAdded();
        appearance7.onChildrenAdded();
        final DecoratorAppearance appearance8 = widget.getAppearance();
        appearance8.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance8).setAlignment(Alignment9.WEST);
        appearance8.setState("pressedSelected");
        widget.addBasicElement(appearance8);
        appearance8.onAttributesInitialized();
        final String id6 = "mecanicBlueColor";
        final ColorElement checkOut16 = ColorElement.checkOut();
        checkOut16.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, checkOut16);
        }
        checkOut16.setColor(new Color(0.32f, 0.39f, 0.42f, 1.0f));
        appearance8.addBasicElement(checkOut16);
        checkOut16.onAttributesInitialized();
        checkOut16.onChildrenAdded();
        final PixmapElement checkOut17 = PixmapElement.checkOut();
        checkOut17.setElementMap(elementMap);
        checkOut17.setHeight(10);
        checkOut17.setPosition(Alignment17.CENTER);
        checkOut17.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut17.setWidth(10);
        checkOut17.setX(419);
        checkOut17.setY(152);
        appearance8.addBasicElement(checkOut17);
        checkOut17.onAttributesInitialized();
        checkOut17.onChildrenAdded();
        appearance8.onChildrenAdded();
    }
}
