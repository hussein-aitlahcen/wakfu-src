package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class SliderCurrentWakfu implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public SliderCurrentWakfu() {
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
        final Widget widgetByThemeElementName = appearance.getParentOfType(Widget.class).getWidgetByThemeElementName("horizontalButton", false);
        if (widgetByThemeElementName != null) {
            final DecoratorAppearance appearance2 = widgetByThemeElementName.getAppearance();
            appearance2.setElementMap(elementMap);
            appearance2.setState("default");
            widgetByThemeElementName.addBasicElement(appearance2);
            appearance2.onAttributesInitialized();
            final String id = "pmCurrentWakfuSlider";
            final PixmapElement checkOut = PixmapElement.checkOut();
            checkOut.setElementMap(elementMap);
            if (elementMap != null && id != null) {
                elementMap.add(id, checkOut);
            }
            checkOut.setHeight(12);
            checkOut.setPosition(Alignment17.CENTER);
            checkOut.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut.setWidth(23);
            checkOut.setX(857);
            checkOut.setY(587);
            appearance2.addBasicElement(checkOut);
            checkOut.onAttributesInitialized();
            checkOut.onChildrenAdded();
            appearance2.onChildrenAdded();
        }
        appearance.onChildrenAdded();
    }
}
