package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class MessageContainer3 implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public MessageContainer3() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "container1";
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut);
        }
        checkOut.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.CENTER);
        element.setYOffset(200);
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final StaticLayout element2 = new StaticLayout();
        element2.onCheckOut();
        element2.setAdaptToContentSize(true);
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id2 = "bannerImage";
        final Image image = new Image();
        image.onCheckOut();
        image.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, image);
        }
        checkOut.addBasicElement(image);
        image.onAttributesInitialized();
        final StaticLayoutData element3 = new StaticLayoutData();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setSize(new Dimension(100.0f, 100.0f));
        image.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        image.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        final PropertyElement checkOut3 = PropertyElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setLocal(true);
        checkOut3.setName("bannerImageIconUrl");
        checkOut3.setAttribute("texture");
        checkOut2.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        checkOut2.onChildrenAdded();
        image.onChildrenAdded();
        final String id3 = "text";
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, textView);
        }
        textView.setStyle("bigBordered");
        textView.setMaxWidth(700);
        textView.setNonBlocking(true);
        checkOut.addBasicElement(textView);
        textView.onAttributesInitialized();
        final StaticLayoutData element4 = new StaticLayoutData();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setSize(new Dimension(100.0f, 100.0f));
        textView.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        final DecoratorAppearance appearance = textView.getAppearance();
        appearance.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance).setAlign(Alignment9.SOUTH);
        textView.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        appearance.onChildrenAdded();
        textView.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
