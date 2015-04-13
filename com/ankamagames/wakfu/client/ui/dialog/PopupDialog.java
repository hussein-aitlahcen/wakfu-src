package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;

public class PopupDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public PopupDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "popupMenu";
        final PopupMenu element = new PopupMenu();
        element.onCheckOut();
        element.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, element);
        }
        element.setHotSpotPosition(Alignment9.NORTH);
        element.onAttributesInitialized();
        final Button element2 = new Button();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setStyle("popupMenu");
        element.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final Label element3 = new Label();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setStyle("popupMenu");
        element3.setMaxWidth(250);
        element.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final Separator element4 = new Separator();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setStyle("popupMenu");
        element.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        element.onChildrenAdded();
        return element;
    }
}
