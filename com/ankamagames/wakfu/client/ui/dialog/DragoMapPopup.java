package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import java.awt.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class DragoMapPopup implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public DragoMapPopup() {
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
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        element.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final StaticLayout element3 = new StaticLayout();
        element3.onCheckOut();
        element3.setAdaptToContentSize(true);
        checkOut.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final String id2 = "container";
        final Container checkOut2 = Container.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut2);
        }
        checkOut2.setStyle("chatBubble");
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        final StaticLayoutData element4 = new StaticLayoutData();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setSize(new Dimension(100.0f, 100.0f));
        checkOut2.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        final DecoratorAppearance appearance = checkOut2.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut2.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Margin checkOut3 = Margin.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setInsets(new Insets(0, 0, 15, 0));
        appearance.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final Padding element5 = new Padding();
        element5.onCheckOut();
        element5.setElementMap(elementMap);
        element5.setInsets(new Insets(10, 15, 10, 15));
        appearance.addBasicElement(element5);
        element5.onAttributesInitialized();
        element5.onChildrenAdded();
        appearance.onChildrenAdded();
        final TextView element6 = new TextView();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setStyle("smallboldMap");
        checkOut2.addBasicElement(element6);
        element6.onAttributesInitialized();
        final PropertyElement checkOut4 = PropertyElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setName("mapPopupDescription");
        checkOut4.setAttribute("text");
        element6.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        element6.onChildrenAdded();
        checkOut2.onChildrenAdded();
        final String id3 = "image";
        final Image image = new Image();
        image.onCheckOut();
        image.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, image);
        }
        image.setStyle("BubbleArrowLeft");
        image.setNonBlocking(true);
        checkOut.addBasicElement(image);
        image.onAttributesInitialized();
        final StaticLayoutData element7 = new StaticLayoutData();
        element7.onCheckOut();
        element7.setElementMap(elementMap);
        element7.setAlign(Alignment17.SOUTH_WEST);
        element7.setSize(new Dimension(-2, -2));
        image.addBasicElement(element7);
        element7.onAttributesInitialized();
        element7.onChildrenAdded();
        image.onChildrenAdded();
        checkOut.onChildrenAdded();
        element.onChildrenAdded();
        return element;
    }
}
