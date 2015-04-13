package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;

public class ComboboxplusBlue2$List implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ComboboxplusBlue2$List() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public void applyStyle(final ElementMap item, final DocumentParser doc, final Widget widget) {
        this.doc = doc;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final Widget widgetByThemeElementName = widget.getWidgetByThemeElementName("list", false);
        if (widgetByThemeElementName != null) {
            final DecoratorAppearance appearance = widgetByThemeElementName.getAppearance();
            appearance.setElementMap(elementMap);
            widgetByThemeElementName.addBasicElement(appearance);
            appearance.onAttributesInitialized();
            final String id = "blueRadioDefaultBackground";
            final PixmapBackground checkOut = PixmapBackground.checkOut();
            checkOut.setElementMap(elementMap);
            if (elementMap != null && id != null) {
                elementMap.add(id, checkOut);
            }
            checkOut.setScaled(true);
            appearance.addBasicElement(checkOut);
            checkOut.onAttributesInitialized();
            final PixmapElement checkOut2 = PixmapElement.checkOut();
            checkOut2.setElementMap(elementMap);
            checkOut2.setHeight(7);
            checkOut2.setPosition(Alignment17.CENTER);
            checkOut2.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut2.setWidth(1);
            checkOut2.setX(88);
            checkOut2.setY(426);
            checkOut.addBasicElement(checkOut2);
            checkOut2.onAttributesInitialized();
            checkOut2.onChildrenAdded();
            checkOut.onChildrenAdded();
            final String id2 = "blueRadioDefaultBorder";
            final PixmapBorder pixmapBorder = new PixmapBorder();
            pixmapBorder.onCheckOut();
            pixmapBorder.setElementMap(elementMap);
            if (elementMap != null && id2 != null) {
                elementMap.add(id2, pixmapBorder);
            }
            appearance.addBasicElement(pixmapBorder);
            pixmapBorder.onAttributesInitialized();
            final PixmapElement checkOut3 = PixmapElement.checkOut();
            checkOut3.setElementMap(elementMap);
            checkOut3.setHeight(5);
            checkOut3.setPosition(Alignment17.NORTH_WEST);
            checkOut3.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut3.setWidth(5);
            checkOut3.setX(153);
            checkOut3.setY(356);
            pixmapBorder.addBasicElement(checkOut3);
            checkOut3.onAttributesInitialized();
            checkOut3.onChildrenAdded();
            final PixmapElement checkOut4 = PixmapElement.checkOut();
            checkOut4.setElementMap(elementMap);
            checkOut4.setHeight(5);
            checkOut4.setPosition(Alignment17.NORTH);
            checkOut4.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut4.setWidth(1);
            checkOut4.setX(594);
            checkOut4.setY(300);
            pixmapBorder.addBasicElement(checkOut4);
            checkOut4.onAttributesInitialized();
            checkOut4.onChildrenAdded();
            final PixmapElement checkOut5 = PixmapElement.checkOut();
            checkOut5.setElementMap(elementMap);
            checkOut5.setHeight(5);
            checkOut5.setPosition(Alignment17.NORTH_EAST);
            checkOut5.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut5.setWidth(5);
            checkOut5.setX(862);
            checkOut5.setY(231);
            pixmapBorder.addBasicElement(checkOut5);
            checkOut5.onAttributesInitialized();
            checkOut5.onChildrenAdded();
            final PixmapElement checkOut6 = PixmapElement.checkOut();
            checkOut6.setElementMap(elementMap);
            checkOut6.setHeight(8);
            checkOut6.setPosition(Alignment17.EAST);
            checkOut6.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut6.setWidth(5);
            checkOut6.setX(911);
            checkOut6.setY(405);
            pixmapBorder.addBasicElement(checkOut6);
            checkOut6.onAttributesInitialized();
            checkOut6.onChildrenAdded();
            final PixmapElement checkOut7 = PixmapElement.checkOut();
            checkOut7.setElementMap(elementMap);
            checkOut7.setHeight(7);
            checkOut7.setPosition(Alignment17.SOUTH_EAST);
            checkOut7.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut7.setWidth(5);
            checkOut7.setX(911);
            checkOut7.setY(353);
            pixmapBorder.addBasicElement(checkOut7);
            checkOut7.onAttributesInitialized();
            checkOut7.onChildrenAdded();
            final PixmapElement checkOut8 = PixmapElement.checkOut();
            checkOut8.setElementMap(elementMap);
            checkOut8.setHeight(7);
            checkOut8.setPosition(Alignment17.SOUTH);
            checkOut8.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut8.setWidth(1);
            checkOut8.setX(229);
            checkOut8.setY(452);
            pixmapBorder.addBasicElement(checkOut8);
            checkOut8.onAttributesInitialized();
            checkOut8.onChildrenAdded();
            final PixmapElement checkOut9 = PixmapElement.checkOut();
            checkOut9.setElementMap(elementMap);
            checkOut9.setHeight(7);
            checkOut9.setPosition(Alignment17.SOUTH_WEST);
            checkOut9.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut9.setWidth(5);
            checkOut9.setX(911);
            checkOut9.setY(363);
            pixmapBorder.addBasicElement(checkOut9);
            checkOut9.onAttributesInitialized();
            checkOut9.onChildrenAdded();
            final PixmapElement checkOut10 = PixmapElement.checkOut();
            checkOut10.setElementMap(elementMap);
            checkOut10.setHeight(8);
            checkOut10.setPosition(Alignment17.WEST);
            checkOut10.setTexture(this.doc.getTexture("default_1.tga"));
            checkOut10.setWidth(5);
            checkOut10.setX(226);
            checkOut10.setY(403);
            pixmapBorder.addBasicElement(checkOut10);
            checkOut10.onAttributesInitialized();
            checkOut10.onChildrenAdded();
            pixmapBorder.onChildrenAdded();
            final String id3 = "mecanicBlueColor";
            final ColorElement checkOut11 = ColorElement.checkOut();
            checkOut11.setElementMap(elementMap);
            if (elementMap != null && id3 != null) {
                elementMap.add(id3, checkOut11);
            }
            checkOut11.setColor(new Color(0.32f, 0.39f, 0.42f, 1.0f));
            appearance.addBasicElement(checkOut11);
            checkOut11.onAttributesInitialized();
            checkOut11.onChildrenAdded();
            appearance.onChildrenAdded();
        }
    }
}
