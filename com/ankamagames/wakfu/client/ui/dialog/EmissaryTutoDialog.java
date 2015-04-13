package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;

public class EmissaryTutoDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public EmissaryTutoDialog() {
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
        checkOut.setNonBlocking(false);
        checkOut.setPrefSize(new Dimension(200, 200));
        checkOut.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setSize(new Dimension(200, 200));
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final StaticLayout element2 = new StaticLayout();
        element2.onCheckOut();
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        final AnimatedElementViewer element3 = new AnimatedElementViewer();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setOffsetY(-80.0f);
        element3.setNonBlocking(true);
        element3.setPrefSize(new Dimension(200, 200));
        element3.setScale(2.5f);
        element2.addBasicElement(element3);
        element3.onAttributesInitialized();
        final StaticLayoutData element4 = new StaticLayoutData();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setSize(new Dimension(200, 200));
        element3.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        final PropertyElement checkOut2 = PropertyElement.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setLocal(true);
        checkOut2.setName("filePath");
        checkOut2.setAttribute("filePath");
        element3.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final PropertyElement checkOut3 = PropertyElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setLocal(true);
        checkOut3.setName("animName");
        checkOut3.setAttribute("animName");
        element3.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final PropertyElement checkOut4 = PropertyElement.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setLocal(true);
        checkOut4.setName("direction");
        checkOut4.setAttribute("direction");
        element3.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        element3.onChildrenAdded();
        element2.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
