package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.nongraphical.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;

public class MinimapPopup implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public MinimapPopup() {
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
        element.setAlign(Alignment9.NORTH);
        element.setHotSpotPosition(Alignment9.SOUTH);
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
        checkOut.setStyle("popup");
        element.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final TextView element3 = new TextView();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setStyle("white");
        element3.setMinWidth(1);
        element3.setMaxWidth(200);
        checkOut.addBasicElement(element3);
        element3.onAttributesInitialized();
        final PropertyElement checkOut2 = PropertyElement.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setName("minimapPopupDescription");
        checkOut2.setAttribute("text");
        element3.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        element3.onChildrenAdded();
        checkOut.onChildrenAdded();
        element.onChildrenAdded();
        return element;
    }
}
