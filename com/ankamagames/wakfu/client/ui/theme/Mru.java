package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class Mru implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public Mru() {
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
        final Widget widgetByThemeElementName = appearance.getParentOfType(Widget.class).getWidgetByThemeElementName("decreaseButton", false);
        if (widgetByThemeElementName != null) {
            final DecoratorAppearance appearance2 = widgetByThemeElementName.getAppearance();
            appearance2.setElementMap(elementMap);
            ((TextWidgetAppearance)appearance2).setAlignment(Alignment9.CENTER);
            appearance2.setState("default");
            widgetByThemeElementName.addBasicElement(appearance2);
            appearance2.onAttributesInitialized();
            final PixmapElement checkOut = PixmapElement.checkOut();
            checkOut.setElementMap(elementMap);
            checkOut.setHeight(30);
            checkOut.setPosition(Alignment17.CENTER);
            checkOut.setTexture(this.doc.getTexture("mru_0.tga"));
            checkOut.setWidth(28);
            checkOut.setX(2);
            checkOut.setY(480);
            appearance2.addBasicElement(checkOut);
            checkOut.onAttributesInitialized();
            checkOut.onChildrenAdded();
            appearance2.onChildrenAdded();
            final DecoratorAppearance appearance3 = widgetByThemeElementName.getAppearance();
            appearance3.setElementMap(elementMap);
            ((TextWidgetAppearance)appearance3).setAlignment(Alignment9.CENTER);
            appearance3.setState("mouseHover");
            widgetByThemeElementName.addBasicElement(appearance3);
            appearance3.onAttributesInitialized();
            final PixmapElement checkOut2 = PixmapElement.checkOut();
            checkOut2.setElementMap(elementMap);
            checkOut2.setHeight(30);
            checkOut2.setPosition(Alignment17.CENTER);
            checkOut2.setTexture(this.doc.getTexture("mru_0.tga"));
            checkOut2.setWidth(28);
            checkOut2.setX(95);
            checkOut2.setY(480);
            appearance3.addBasicElement(checkOut2);
            checkOut2.onAttributesInitialized();
            checkOut2.onChildrenAdded();
            appearance3.onChildrenAdded();
            final DecoratorAppearance appearance4 = widgetByThemeElementName.getAppearance();
            appearance4.setElementMap(elementMap);
            ((TextWidgetAppearance)appearance4).setAlignment(Alignment9.CENTER);
            appearance4.setState("pressed");
            widgetByThemeElementName.addBasicElement(appearance4);
            appearance4.onAttributesInitialized();
            final PixmapElement checkOut3 = PixmapElement.checkOut();
            checkOut3.setElementMap(elementMap);
            checkOut3.setHeight(30);
            checkOut3.setPosition(Alignment17.CENTER);
            checkOut3.setTexture(this.doc.getTexture("mru_0.tga"));
            checkOut3.setWidth(28);
            checkOut3.setX(126);
            checkOut3.setY(480);
            appearance4.addBasicElement(checkOut3);
            checkOut3.onAttributesInitialized();
            checkOut3.onChildrenAdded();
            appearance4.onChildrenAdded();
        }
        final Widget widgetByThemeElementName2 = appearance.getParentOfType(Widget.class).getWidgetByThemeElementName("increaseButton", false);
        if (widgetByThemeElementName2 != null) {
            final DecoratorAppearance appearance5 = widgetByThemeElementName2.getAppearance();
            appearance5.setElementMap(elementMap);
            ((TextWidgetAppearance)appearance5).setAlignment(Alignment9.CENTER);
            appearance5.setState("default");
            widgetByThemeElementName2.addBasicElement(appearance5);
            appearance5.onAttributesInitialized();
            final PixmapElement checkOut4 = PixmapElement.checkOut();
            checkOut4.setElementMap(elementMap);
            checkOut4.setHeight(30);
            checkOut4.setPosition(Alignment17.CENTER);
            checkOut4.setTexture(this.doc.getTexture("mru_0.tga"));
            checkOut4.setWidth(28);
            checkOut4.setX(33);
            checkOut4.setY(480);
            appearance5.addBasicElement(checkOut4);
            checkOut4.onAttributesInitialized();
            checkOut4.onChildrenAdded();
            appearance5.onChildrenAdded();
            final DecoratorAppearance appearance6 = widgetByThemeElementName2.getAppearance();
            appearance6.setElementMap(elementMap);
            ((TextWidgetAppearance)appearance6).setAlignment(Alignment9.CENTER);
            appearance6.setState("mouseHover");
            widgetByThemeElementName2.addBasicElement(appearance6);
            appearance6.onAttributesInitialized();
            final PixmapElement checkOut5 = PixmapElement.checkOut();
            checkOut5.setElementMap(elementMap);
            checkOut5.setHeight(30);
            checkOut5.setPosition(Alignment17.CENTER);
            checkOut5.setTexture(this.doc.getTexture("mru_0.tga"));
            checkOut5.setWidth(28);
            checkOut5.setX(64);
            checkOut5.setY(480);
            appearance6.addBasicElement(checkOut5);
            checkOut5.onAttributesInitialized();
            checkOut5.onChildrenAdded();
            appearance6.onChildrenAdded();
            final DecoratorAppearance appearance7 = widgetByThemeElementName2.getAppearance();
            appearance7.setElementMap(elementMap);
            ((TextWidgetAppearance)appearance7).setAlignment(Alignment9.CENTER);
            appearance7.setState("pressed");
            widgetByThemeElementName2.addBasicElement(appearance7);
            appearance7.onAttributesInitialized();
            final PixmapElement checkOut6 = PixmapElement.checkOut();
            checkOut6.setElementMap(elementMap);
            checkOut6.setHeight(30);
            checkOut6.setPosition(Alignment17.CENTER);
            checkOut6.setTexture(this.doc.getTexture("mru_0.tga"));
            checkOut6.setWidth(28);
            checkOut6.setX(157);
            checkOut6.setY(480);
            appearance7.addBasicElement(checkOut6);
            checkOut6.onAttributesInitialized();
            checkOut6.onChildrenAdded();
            appearance7.onChildrenAdded();
        }
        appearance.onChildrenAdded();
    }
}
