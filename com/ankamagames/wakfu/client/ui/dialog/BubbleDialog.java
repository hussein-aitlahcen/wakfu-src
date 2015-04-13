package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.*;
import java.awt.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.*;

public class BubbleDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public BubbleDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "test";
        final WakfuBubbleWidget element = new WakfuBubbleWidget();
        element.onCheckOut();
        element.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, element);
        }
        element.setPack(true);
        element.onAttributesInitialized();
        final StaticLayoutData element2 = new StaticLayoutData();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setSize(new Dimension(-2, -2));
        element.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final StaticLayout element3 = new StaticLayout();
        element3.onCheckOut();
        element3.setAdaptToContentSize(true);
        element.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final String id2 = "coloredContainer";
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut);
        }
        checkOut.setStyle("ChatBubbleBackground");
        checkOut.setVisible(false);
        checkOut.setPrefSize(new Dimension(0, 80));
        element.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final StaticLayoutData element4 = new StaticLayoutData();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setSize(new Dimension(100.0f, 100.0f));
        checkOut.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        final DecoratorAppearance appearance = checkOut.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Margin checkOut2 = Margin.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setInsets(new Insets(5, 10, 10, 10));
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final Padding element5 = new Padding();
        element5.onCheckOut();
        element5.setElementMap(elementMap);
        element5.setInsets(new Insets(5, 5, 5, 5));
        appearance.addBasicElement(element5);
        element5.onAttributesInitialized();
        element5.onChildrenAdded();
        appearance.onChildrenAdded();
        checkOut.onChildrenAdded();
        final String id3 = "container";
        final Container checkOut3 = Container.checkOut();
        checkOut3.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut3);
        }
        checkOut3.setStyle("chatBubble");
        element.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        final StaticLayoutData element6 = new StaticLayoutData();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setAlign(Alignment17.SOUTH_WEST);
        checkOut3.addBasicElement(element6);
        element6.onAttributesInitialized();
        element6.onChildrenAdded();
        final DecoratorAppearance appearance2 = checkOut3.getAppearance();
        appearance2.setElementMap(elementMap);
        checkOut3.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final Margin checkOut4 = Margin.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setInsets(new Insets(10, 15, 15, 15));
        appearance2.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        final Padding element7 = new Padding();
        element7.onCheckOut();
        element7.setElementMap(elementMap);
        element7.setInsets(new Insets(10, 15, 10, 15));
        appearance2.addBasicElement(element7);
        element7.onAttributesInitialized();
        element7.onChildrenAdded();
        appearance2.onChildrenAdded();
        final String id4 = "text";
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, textView);
        }
        textView.setMinWidth(10);
        textView.setMaxWidth(250);
        textView.setStyle("dark16");
        textView.setNonBlocking(true);
        checkOut3.addBasicElement(textView);
        textView.onAttributesInitialized();
        final RowLayoutData element8 = new RowLayoutData();
        element8.onCheckOut();
        element8.setElementMap(elementMap);
        element8.setAlign(Alignment9.CENTER);
        textView.addBasicElement(element8);
        element8.onAttributesInitialized();
        element8.onChildrenAdded();
        textView.onChildrenAdded();
        checkOut3.onChildrenAdded();
        final String id5 = "image";
        final Image image = new Image();
        image.onCheckOut();
        image.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, image);
        }
        image.setStyle("BubbleArrowLeft");
        image.setNonBlocking(true);
        element.addBasicElement(image);
        image.onAttributesInitialized();
        final StaticLayoutData element9 = new StaticLayoutData();
        element9.onCheckOut();
        element9.setElementMap(elementMap);
        element9.setAlign(Alignment17.SOUTH_WEST);
        element9.setSize(new Dimension(-2, -2));
        image.addBasicElement(element9);
        element9.onAttributesInitialized();
        element9.onChildrenAdded();
        image.onChildrenAdded();
        element.onChildrenAdded();
        return element;
    }
}
