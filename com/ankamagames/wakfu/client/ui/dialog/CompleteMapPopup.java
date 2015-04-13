package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import java.awt.*;
import com.ankamagames.xulor2.core.renderer.condition.*;
import com.ankamagames.xulor2.appearance.*;

public class CompleteMapPopup implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public CompleteMapPopup() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "popup";
        final PopupElement element = new PopupElement();
        element.onCheckOut();
        element.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, element);
        }
        element.setAlign(Alignment9.NORTH_EAST);
        element.setHotSpotPosition(Alignment9.SOUTH_WEST);
        element.setHideOnClick(false);
        element.onAttributesInitialized();
        final StaticLayoutData element2 = new StaticLayoutData();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setSize(new Dimension(-2, -2));
        element.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id2 = "container";
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut);
        }
        element.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final StaticLayout element3 = new StaticLayout();
        element3.onCheckOut();
        element3.setAdaptToContentSize(true);
        checkOut.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final Image element4 = new Image();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setNonBlocking(true);
        checkOut.addBasicElement(element4);
        element4.onAttributesInitialized();
        final StaticLayoutData element5 = new StaticLayoutData();
        element5.onCheckOut();
        element5.setElementMap(elementMap);
        element5.setSize(new Dimension(-2, -2));
        element5.setAlign(Alignment17.CENTER);
        element4.addBasicElement(element5);
        element5.onAttributesInitialized();
        element5.onChildrenAdded();
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        element4.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        final PropertyElement checkOut3 = PropertyElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setName("currentMapScrollDecoratorPath");
        checkOut3.setAttribute("texture");
        checkOut2.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        checkOut2.onChildrenAdded();
        element4.onChildrenAdded();
        final TextView element6 = new TextView();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setMultiline(false);
        element6.setStyle("backgroundBold");
        element6.setNonBlocking(true);
        element6.setMinWidth(1);
        element6.setMaxWidth(150);
        element6.setExpandable(false);
        element6.setEnableAutoZoomShrink(true);
        checkOut.addBasicElement(element6);
        element6.onAttributesInitialized();
        final StaticLayoutData element7 = new StaticLayoutData();
        element7.onCheckOut();
        element7.setElementMap(elementMap);
        element7.setSize(new Dimension(-2, -2));
        element7.setAlign(Alignment17.CENTER);
        element6.addBasicElement(element7);
        element7.onAttributesInitialized();
        element7.onChildrenAdded();
        final DecoratorAppearance appearance = element6.getAppearance();
        appearance.setElementMap(elementMap);
        element6.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final ColorElement checkOut4 = ColorElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        appearance.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final Margin checkOut5 = Margin.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setInsets(new Insets(30, 15, 15, 15));
        appearance.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        appearance.onChildrenAdded();
        final PropertyElement checkOut6 = PropertyElement.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setName("mapPopupDescription");
        checkOut6.setAttribute("text");
        element6.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        final PropertyElement checkOut7 = PropertyElement.checkOut();
        checkOut7.setElementMap(elementMap);
        checkOut7.setName("mapPopupIsEditing");
        checkOut7.setAttribute("visible");
        element6.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        final ConditionResult element8 = new ConditionResult();
        element8.onCheckOut();
        element8.setElementMap(elementMap);
        checkOut7.addBasicElement(element8);
        element8.onAttributesInitialized();
        final FalseCondition element9 = new FalseCondition();
        element9.onCheckOut();
        element9.setElementMap(elementMap);
        element8.addBasicElement(element9);
        element9.onAttributesInitialized();
        element9.onChildrenAdded();
        element8.onChildrenAdded();
        checkOut7.onChildrenAdded();
        element6.onChildrenAdded();
        checkOut.onChildrenAdded();
        element.onChildrenAdded();
        return element;
    }
}
