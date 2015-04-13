package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class RadioButton implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public RadioButton() {
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
        final String id = "defaultDarkColor";
        final ColorElement checkOut2 = ColorElement.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut2);
        }
        checkOut2.setColor(new Color(0.29f, 0.17f, 0.07f, 1.0f));
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final String id2 = "pmRadioButtonDefault";
        final PixmapElement checkOut3 = PixmapElement.checkOut();
        checkOut3.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut3);
        }
        checkOut3.setHeight(14);
        checkOut3.setPosition(Alignment17.CENTER);
        checkOut3.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut3.setWidth(14);
        checkOut3.setX(830);
        checkOut3.setY(338);
        appearance.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        appearance.onChildrenAdded();
        final DecoratorAppearance appearance2 = widget.getAppearance();
        appearance2.setElementMap(elementMap);
        appearance2.setState("mouseHover");
        widget.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final String id3 = "pmRadioButtonOver";
        final PixmapElement checkOut4 = PixmapElement.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut4);
        }
        checkOut4.setHeight(14);
        checkOut4.setPosition(Alignment17.CENTER);
        checkOut4.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut4.setWidth(14);
        checkOut4.setX(379);
        checkOut4.setY(244);
        appearance2.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        appearance2.onChildrenAdded();
        final DecoratorAppearance appearance3 = widget.getAppearance();
        appearance3.setElementMap(elementMap);
        appearance3.setState("pressed");
        widget.addBasicElement(appearance3);
        appearance3.onAttributesInitialized();
        final String id4 = "pmRadioButtonPressed";
        final PixmapElement checkOut5 = PixmapElement.checkOut();
        checkOut5.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, checkOut5);
        }
        checkOut5.setHeight(14);
        checkOut5.setPosition(Alignment17.CENTER);
        checkOut5.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut5.setWidth(14);
        checkOut5.setX(281);
        checkOut5.setY(244);
        appearance3.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        appearance3.onChildrenAdded();
        final DecoratorAppearance appearance4 = widget.getAppearance();
        appearance4.setElementMap(elementMap);
        appearance4.setState("disabled");
        widget.addBasicElement(appearance4);
        appearance4.onAttributesInitialized();
        final String id5 = "pmRadioButtonDisabled";
        final PixmapElement checkOut6 = PixmapElement.checkOut();
        checkOut6.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, checkOut6);
        }
        checkOut6.setHeight(14);
        checkOut6.setPosition(Alignment17.CENTER);
        checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut6.setWidth(14);
        checkOut6.setX(36);
        checkOut6.setY(480);
        appearance4.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        appearance4.onChildrenAdded();
        final DecoratorAppearance appearance5 = widget.getAppearance();
        appearance5.setElementMap(elementMap);
        appearance5.setState("selected");
        widget.addBasicElement(appearance5);
        appearance5.onAttributesInitialized();
        final String id6 = "pmRadioButtonDefaultSelected";
        final PixmapElement checkOut7 = PixmapElement.checkOut();
        checkOut7.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, checkOut7);
        }
        checkOut7.setHeight(14);
        checkOut7.setPosition(Alignment17.CENTER);
        checkOut7.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut7.setWidth(14);
        checkOut7.setX(281);
        checkOut7.setY(210);
        appearance5.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        checkOut7.onChildrenAdded();
        final FontElement checkOut8 = FontElement.checkOut();
        checkOut8.setRenderer(Xulor.getInstance().getDocumentParser().getFont("defaultBoldFont"));
        checkOut8.setElementMap(elementMap);
        appearance5.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        appearance5.onChildrenAdded();
        final DecoratorAppearance appearance6 = widget.getAppearance();
        appearance6.setElementMap(elementMap);
        appearance6.setState("mouseHoverSelected");
        widget.addBasicElement(appearance6);
        appearance6.onAttributesInitialized();
        final String id7 = "pmRadioButtonOverSelected";
        final PixmapElement checkOut9 = PixmapElement.checkOut();
        checkOut9.setElementMap(elementMap);
        if (elementMap != null && id7 != null) {
            elementMap.add(id7, checkOut9);
        }
        checkOut9.setHeight(14);
        checkOut9.setPosition(Alignment17.CENTER);
        checkOut9.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut9.setWidth(14);
        checkOut9.setX(595);
        checkOut9.setY(610);
        appearance6.addBasicElement(checkOut9);
        checkOut9.onAttributesInitialized();
        checkOut9.onChildrenAdded();
        appearance6.onChildrenAdded();
        final DecoratorAppearance appearance7 = widget.getAppearance();
        appearance7.setElementMap(elementMap);
        appearance7.setState("pressedSelected");
        widget.addBasicElement(appearance7);
        appearance7.onAttributesInitialized();
        final String id8 = "pmRadioButtonPressedSelected";
        final PixmapElement checkOut10 = PixmapElement.checkOut();
        checkOut10.setElementMap(elementMap);
        if (elementMap != null && id8 != null) {
            elementMap.add(id8, checkOut10);
        }
        checkOut10.setHeight(14);
        checkOut10.setPosition(Alignment17.CENTER);
        checkOut10.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut10.setWidth(14);
        checkOut10.setX(308);
        checkOut10.setY(514);
        appearance7.addBasicElement(checkOut10);
        checkOut10.onAttributesInitialized();
        checkOut10.onChildrenAdded();
        appearance7.onChildrenAdded();
        final DecoratorAppearance appearance8 = widget.getAppearance();
        appearance8.setElementMap(elementMap);
        appearance8.setState("disabledSelected");
        widget.addBasicElement(appearance8);
        appearance8.onAttributesInitialized();
        final String id9 = "pmRadioButtonDisabledSelected";
        final PixmapElement checkOut11 = PixmapElement.checkOut();
        checkOut11.setElementMap(elementMap);
        if (elementMap != null && id9 != null) {
            elementMap.add(id9, checkOut11);
        }
        checkOut11.setHeight(14);
        checkOut11.setPosition(Alignment17.CENTER);
        checkOut11.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut11.setWidth(14);
        checkOut11.setX(360);
        checkOut11.setY(340);
        appearance8.addBasicElement(checkOut11);
        checkOut11.onAttributesInitialized();
        checkOut11.onChildrenAdded();
        appearance8.onChildrenAdded();
    }
}
