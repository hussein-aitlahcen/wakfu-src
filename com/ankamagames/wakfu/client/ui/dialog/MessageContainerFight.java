package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import java.awt.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class MessageContainerFight implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public MessageContainerFight() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "container1";
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut);
        }
        checkOut.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.CENTER);
        element.setYOffset(150);
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final StaticLayout element2 = new StaticLayout();
        element2.onCheckOut();
        element2.setAdaptToContentSize(true);
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id2 = "container2";
        final Container checkOut2 = Container.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut2);
        }
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        final StaticLayoutData element3 = new StaticLayoutData();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setSize(new Dimension(100.0f, 100.0f));
        checkOut2.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final StaticLayout element4 = new StaticLayout();
        element4.onCheckOut();
        element4.setAdaptToContentSize(true);
        checkOut2.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        final String id3 = "animatedElementLeft";
        final AnimatedElementViewer animatedElementViewer = new AnimatedElementViewer();
        animatedElementViewer.onCheckOut();
        animatedElementViewer.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, animatedElementViewer);
        }
        animatedElementViewer.setNonBlocking(true);
        animatedElementViewer.setStyle("fightDecoratorLeft");
        animatedElementViewer.setExpandable(false);
        animatedElementViewer.setPrefSize(new Dimension(220, 84));
        animatedElementViewer.setOffsetX(238.0f);
        animatedElementViewer.setOffsetY(-13.0f);
        checkOut2.addBasicElement(animatedElementViewer);
        animatedElementViewer.onAttributesInitialized();
        final StaticLayoutData element5 = new StaticLayoutData();
        element5.onCheckOut();
        element5.setElementMap(elementMap);
        element5.setAlign(Alignment17.WEST);
        element5.setSize(new Dimension(-2, 100.0f));
        animatedElementViewer.addBasicElement(element5);
        element5.onAttributesInitialized();
        element5.onChildrenAdded();
        animatedElementViewer.onChildrenAdded();
        final String id4 = "animatedElementRight";
        final AnimatedElementViewer animatedElementViewer2 = new AnimatedElementViewer();
        animatedElementViewer2.onCheckOut();
        animatedElementViewer2.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, animatedElementViewer2);
        }
        animatedElementViewer2.setNonBlocking(true);
        animatedElementViewer2.setStyle("fightDecoratorRight");
        animatedElementViewer2.setExpandable(false);
        animatedElementViewer2.setPrefSize(new Dimension(220, 84));
        animatedElementViewer2.setOffsetX(-238.0f);
        animatedElementViewer2.setOffsetY(-13.0f);
        checkOut2.addBasicElement(animatedElementViewer2);
        animatedElementViewer2.onAttributesInitialized();
        final StaticLayoutData element6 = new StaticLayoutData();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setAlign(Alignment17.EAST);
        element6.setSize(new Dimension(-2, -2));
        animatedElementViewer2.addBasicElement(element6);
        element6.onAttributesInitialized();
        element6.onChildrenAdded();
        animatedElementViewer2.onChildrenAdded();
        checkOut2.onChildrenAdded();
        final Container checkOut3 = Container.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        final DecoratorAppearance appearance = checkOut3.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut3.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Margin checkOut4 = Margin.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setInsets(new Insets(0, 165, 5, 165));
        appearance.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        checkOut4.onChildrenAdded();
        appearance.onChildrenAdded();
        final StaticLayoutData element7 = new StaticLayoutData();
        element7.onCheckOut();
        element7.setElementMap(elementMap);
        element7.setSize(new Dimension(100.0f, -2));
        checkOut3.addBasicElement(element7);
        element7.onAttributesInitialized();
        element7.onChildrenAdded();
        checkOut3.onChildrenAdded();
        final String id5 = "text";
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, textView);
        }
        textView.setStyle("bigBordered");
        textView.setMaxWidth(700);
        textView.setNonBlocking(true);
        checkOut.addBasicElement(textView);
        textView.onAttributesInitialized();
        final StaticLayoutData element8 = new StaticLayoutData();
        element8.onCheckOut();
        element8.setElementMap(elementMap);
        element8.setSize(new Dimension(100.0f, 100.0f));
        textView.addBasicElement(element8);
        element8.onAttributesInitialized();
        element8.onChildrenAdded();
        final DecoratorAppearance appearance2 = textView.getAppearance();
        appearance2.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance2).setAlign(Alignment9.SOUTH);
        textView.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final Margin checkOut5 = Margin.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setInsets(new Insets(0, 275, 20, 275));
        appearance2.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        appearance2.onChildrenAdded();
        textView.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
