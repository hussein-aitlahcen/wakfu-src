package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;

public class EmoteIconDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public EmoteIconDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setSize(new Dimension(-2, -2));
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final StaticLayout element2 = new StaticLayout();
        element2.onCheckOut();
        element2.setAdaptToContentSize(true);
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id = "image";
        final Image image = new Image();
        image.onCheckOut();
        image.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, image);
        }
        image.setNonBlocking(true);
        checkOut.addBasicElement(image);
        image.onAttributesInitialized();
        final StaticLayoutData element3 = new StaticLayoutData();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setSize(new Dimension(100.0f, 100.0f));
        image.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        image.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
