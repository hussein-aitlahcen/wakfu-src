package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;

public class TestBoussole implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public TestBoussole() {
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
        element.setSize(new Dimension(200, 200));
        element.setAlign(Alignment17.CENTER);
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final CompassWidget element2 = new CompassWidget();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setProximity(0.5f);
        element2.setAngle(14.5f);
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
