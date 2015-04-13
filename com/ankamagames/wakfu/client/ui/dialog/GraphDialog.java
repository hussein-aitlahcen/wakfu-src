package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import java.awt.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.appearance.*;

public class GraphDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public GraphDialog() {
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
        final DecoratorAppearance appearance = checkOut.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final PlainBorder element = new PlainBorder();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setInsets(new Insets(1, 1, 1, 1));
        element.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        appearance.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        appearance.onChildrenAdded();
        final StaticLayoutData element2 = new StaticLayoutData();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setAlign(Alignment17.CENTER);
        element2.setSize(new Dimension(-2, -2));
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id = "graph";
        final Graph graph = new Graph();
        graph.onCheckOut();
        graph.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, graph);
        }
        graph.setExpandable(false);
        graph.setCellSize(new Dimension(75, 250));
        checkOut.addBasicElement(graph);
        graph.onAttributesInitialized();
        final DecoratorAppearance appearance2 = graph.getAppearance();
        appearance2.setElementMap(elementMap);
        graph.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final PlainBackground element3 = new PlainBackground();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        appearance2.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        appearance2.onChildrenAdded();
        graph.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
