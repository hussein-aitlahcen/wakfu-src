package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;

public class MruDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public MruDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "mru";
        final UniversalRadialMenu element = new UniversalRadialMenu();
        element.onCheckOut();
        element.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, element);
        }
        element.onAttributesInitialized();
        final String id2 = "popup";
        final PopupElement popupElement = new PopupElement();
        popupElement.onCheckOut();
        popupElement.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, popupElement);
        }
        popupElement.setAlign(Alignment9.NORTH);
        popupElement.setHotSpotPosition(Alignment9.SOUTH);
        element.addBasicElement(popupElement);
        popupElement.onAttributesInitialized();
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setStyle("popup");
        popupElement.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final String id3 = "popupText";
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, textView);
        }
        textView.setStyle("white");
        checkOut.addBasicElement(textView);
        textView.onAttributesInitialized();
        textView.onChildrenAdded();
        checkOut.onChildrenAdded();
        popupElement.onChildrenAdded();
        final Button element2 = new Button();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        element.onChildrenAdded();
        return element;
    }
}
