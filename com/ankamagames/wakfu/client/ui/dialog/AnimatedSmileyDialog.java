package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;

public class AnimatedSmileyDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public AnimatedSmileyDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final SmileyWidget smileyWidget = new SmileyWidget();
        smileyWidget.onCheckOut();
        smileyWidget.setElementMap(elementMap);
        smileyWidget.setNonBlocking(true);
        smileyWidget.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.CENTER);
        element.setSize(new Dimension(32, 32));
        smileyWidget.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final String id = "animatedElementViewer";
        final AnimatedElementViewer animatedElementViewer = new AnimatedElementViewer();
        animatedElementViewer.onCheckOut();
        animatedElementViewer.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, animatedElementViewer);
        }
        animatedElementViewer.setScale(1.5f);
        animatedElementViewer.setPrefSize(new Dimension(32, 32));
        animatedElementViewer.setNonBlocking(true);
        animatedElementViewer.setDirection(2);
        smileyWidget.addBasicElement(animatedElementViewer);
        animatedElementViewer.onAttributesInitialized();
        animatedElementViewer.onChildrenAdded();
        smileyWidget.onChildrenAdded();
        return smileyWidget;
    }
}
