package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import java.awt.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class PetBubbleDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public PetBubbleDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final ImmutableWakfuBubbleWidget immutableWakfuBubbleWidget = new ImmutableWakfuBubbleWidget();
        immutableWakfuBubbleWidget.onCheckOut();
        immutableWakfuBubbleWidget.setElementMap(elementMap);
        immutableWakfuBubbleWidget.setPack(true);
        final MouseClickedListener onClick = new MouseClickedListener();
        onClick.setCallBackFunc("wakfu.petDialog:validOrSetNextMessage");
        immutableWakfuBubbleWidget.setOnClick(onClick);
        immutableWakfuBubbleWidget.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setSize(new Dimension(-2, -2));
        immutableWakfuBubbleWidget.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final StaticLayout element2 = new StaticLayout();
        element2.onCheckOut();
        element2.setAdaptToContentSize(true);
        immutableWakfuBubbleWidget.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id = "container";
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut);
        }
        checkOut.setStyle("petBubble");
        immutableWakfuBubbleWidget.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final StaticLayoutData element3 = new StaticLayoutData();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setSize(new Dimension(100.0f, 100.0f));
        checkOut.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final DecoratorAppearance appearance = checkOut.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Margin checkOut2 = Margin.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setInsets(new Insets(23, 0, 20, 0));
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        appearance.onChildrenAdded();
        final String id2 = "text";
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, textView);
        }
        textView.setMinWidth(10);
        textView.setMaxWidth(250);
        textView.setStyle("DefaultBold14");
        checkOut.addBasicElement(textView);
        textView.onAttributesInitialized();
        textView.onChildrenAdded();
        checkOut.onChildrenAdded();
        final String id3 = "image";
        final Image image = new Image();
        image.onCheckOut();
        image.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, image);
        }
        image.setStyle("petBubble");
        immutableWakfuBubbleWidget.addBasicElement(image);
        image.onAttributesInitialized();
        final StaticLayoutData element4 = new StaticLayoutData();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setAlign(Alignment17.NORTH_WEST);
        element4.setSize(new Dimension(-2, -2));
        element4.setXOffset(20);
        image.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        image.onChildrenAdded();
        immutableWakfuBubbleWidget.onChildrenAdded();
        return immutableWakfuBubbleWidget;
    }
}
