package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.decorator.*;
import java.awt.*;
import com.ankamagames.xulor2.appearance.*;

public class ButtonPopupMenu implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ButtonPopupMenu() {
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
        final PlainBackground element = new PlainBackground();
        element.onCheckOut();
        element.setElementMap(elementMap);
        appearance.addBasicElement(element);
        element.onAttributesInitialized();
        final String id2 = "defaultDarkGreyColor";
        final ColorElement checkOut3 = ColorElement.checkOut();
        checkOut3.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut3);
        }
        checkOut3.setColor(new Color(0.38f, 0.33f, 0.34f, 1.0f));
        element.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        element.onChildrenAdded();
        final PlainBorder element2 = new PlainBorder();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setColor(new Color(1.0f, 1.0f, 1.0f, 0.1f));
        element2.setInsets(new Insets(1, 1, 1, 1));
        appearance.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        appearance.onChildrenAdded();
        final DecoratorAppearance appearance2 = widget.getAppearance();
        appearance2.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance2).setAlignment(Alignment9.CENTER);
        appearance2.setState("disabled");
        widget.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final String id3 = "defaultDisabledColor";
        final ColorElement checkOut4 = ColorElement.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut4);
        }
        checkOut4.setColor(new Color(0.7f, 0.7f, 0.6f, 1.0f));
        appearance2.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final PlainBackground element3 = new PlainBackground();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        appearance2.addBasicElement(element3);
        element3.onAttributesInitialized();
        final String id4 = "defaultDarkGreyColor";
        final ColorElement checkOut5 = ColorElement.checkOut();
        checkOut5.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, checkOut5);
        }
        checkOut5.setColor(new Color(0.38f, 0.33f, 0.34f, 1.0f));
        element3.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        element3.onChildrenAdded();
        final PlainBorder element4 = new PlainBorder();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setColor(new Color(1.0f, 1.0f, 1.0f, 0.1f));
        element4.setInsets(new Insets(1, 1, 1, 1));
        appearance2.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        appearance2.onChildrenAdded();
        final DecoratorAppearance appearance3 = widget.getAppearance();
        appearance3.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance3).setAlignment(Alignment9.CENTER);
        appearance3.setState("mouseHover");
        widget.addBasicElement(appearance3);
        appearance3.onAttributesInitialized();
        final String id5 = "blackColor";
        final ColorElement checkOut6 = ColorElement.checkOut();
        checkOut6.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, checkOut6);
        }
        checkOut6.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        appearance3.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        final PlainBackground element5 = new PlainBackground();
        element5.onCheckOut();
        element5.setElementMap(elementMap);
        appearance3.addBasicElement(element5);
        element5.onAttributesInitialized();
        final String id6 = "defaultLightColor";
        final ColorElement checkOut7 = ColorElement.checkOut();
        checkOut7.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, checkOut7);
        }
        checkOut7.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        element5.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        element5.onChildrenAdded();
        final PlainBorder element6 = new PlainBorder();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setColor(new Color(0.8f, 0.7f, 0.7f, 1.0f));
        element6.setInsets(new Insets(1, 1, 1, 1));
        appearance3.addBasicElement(element6);
        element6.onAttributesInitialized();
        element6.onChildrenAdded();
        appearance3.onChildrenAdded();
        final DecoratorAppearance appearance4 = widget.getAppearance();
        appearance4.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance4).setAlignment(Alignment9.CENTER);
        appearance4.setState("pressed");
        widget.addBasicElement(appearance4);
        appearance4.onAttributesInitialized();
        final String id7 = "blackColor";
        final ColorElement checkOut8 = ColorElement.checkOut();
        checkOut8.setElementMap(elementMap);
        if (elementMap != null && id7 != null) {
            elementMap.add(id7, checkOut8);
        }
        checkOut8.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        appearance4.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        final PlainBackground element7 = new PlainBackground();
        element7.onCheckOut();
        element7.setElementMap(elementMap);
        appearance4.addBasicElement(element7);
        element7.onAttributesInitialized();
        final String id8 = "pressedBlackColor";
        final ColorElement checkOut9 = ColorElement.checkOut();
        checkOut9.setElementMap(elementMap);
        if (elementMap != null && id8 != null) {
            elementMap.add(id8, checkOut9);
        }
        checkOut9.setColor(new Color(0.7f, 0.7f, 0.7f, 1.0f));
        element7.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        checkOut9.onChildrenAdded();
        element7.onChildrenAdded();
        final PlainBorder element8 = new PlainBorder();
        element8.onCheckOut();
        element8.setElementMap(elementMap);
        element8.setColor(new Color(1.0f, 1.0f, 1.0f, 0.1f));
        element8.setInsets(new Insets(1, 1, 1, 1));
        appearance4.addBasicElement(element8);
        element8.onAttributesInitialized();
        element8.onChildrenAdded();
        appearance4.onChildrenAdded();
    }
}
