package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.layout.*;
import java.awt.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;

public class XpGainDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public XpGainDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "container";
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut);
        }
        checkOut.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.NORTH);
        element.setSize(new Dimension(-2, -2));
        element.setYOffset(-200);
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final StaticLayout element2 = new StaticLayout();
        element2.onCheckOut();
        element2.setAdaptToContentSize(true);
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id2 = "backgroundContainer";
        final Container checkOut2 = Container.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut2);
        }
        checkOut2.setStyle("white");
        checkOut2.setPrefSize(new Dimension(350, 16));
        checkOut2.setExpandable(false);
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        final StaticLayoutData element3 = new StaticLayoutData();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setAlign(Alignment17.CENTER);
        element3.setSize(new Dimension(-2, -2));
        checkOut2.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final DecoratorAppearance appearance = checkOut2.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut2.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Margin checkOut3 = Margin.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setInsets(new Insets(0, 65, 10, 65));
        appearance.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        final Padding element4 = new Padding();
        element4.onCheckOut();
        element4.setElementMap(elementMap);
        element4.setInsets(new Insets(3, 6, 3, 6));
        appearance.addBasicElement(element4);
        element4.onAttributesInitialized();
        element4.onChildrenAdded();
        appearance.onChildrenAdded();
        final String id3 = "xpImage";
        final Image image = new Image();
        image.onCheckOut();
        image.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, image);
        }
        image.setStyle("xpIcon");
        image.setNonBlocking(true);
        image.setExpandable(false);
        checkOut2.addBasicElement(image);
        image.onAttributesInitialized();
        image.onChildrenAdded();
        final Container checkOut4 = Container.checkOut();
        checkOut4.setElementMap(elementMap);
        checkOut4.setPrefSize(new Dimension(0, 20));
        checkOut2.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        final DecoratorAppearance appearance2 = checkOut4.getAppearance();
        appearance2.setElementMap(elementMap);
        checkOut4.addBasicElement(appearance2);
        appearance2.onAttributesInitialized();
        final Margin checkOut5 = Margin.checkOut();
        checkOut5.setElementMap(elementMap);
        checkOut5.setInsets(new Insets(0, 2, 0, 0));
        appearance2.addBasicElement(checkOut5);
        checkOut5.onAttributesInitialized();
        checkOut5.onChildrenAdded();
        appearance2.onChildrenAdded();
        final StaticLayout element5 = new StaticLayout();
        element5.onCheckOut();
        element5.setAdaptToContentSize(true);
        checkOut4.addBasicElement(element5);
        element5.onAttributesInitialized();
        element5.onChildrenAdded();
        final String id4 = "progressBar";
        final ProgressBar progressBar = new ProgressBar();
        progressBar.onCheckOut();
        progressBar.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, progressBar);
        }
        progressBar.setStyle("XP2");
        progressBar.setNonBlocking(true);
        checkOut4.addBasicElement(progressBar);
        progressBar.onAttributesInitialized();
        final DecoratorAppearance appearance3 = progressBar.getAppearance();
        appearance3.setElementMap(elementMap);
        progressBar.addBasicElement(appearance3);
        appearance3.onAttributesInitialized();
        final Margin checkOut6 = Margin.checkOut();
        checkOut6.setElementMap(elementMap);
        checkOut6.setInsets(new Insets(0, 1, 0, 1));
        appearance3.addBasicElement(checkOut6);
        checkOut6.onAttributesInitialized();
        checkOut6.onChildrenAdded();
        appearance3.onChildrenAdded();
        final StaticLayoutData element6 = new StaticLayoutData();
        element6.onCheckOut();
        element6.setElementMap(elementMap);
        element6.setSize(new Dimension(100.0f, 14));
        element6.setAlign(Alignment17.CENTER);
        progressBar.addBasicElement(element6);
        element6.onAttributesInitialized();
        element6.onChildrenAdded();
        progressBar.onChildrenAdded();
        final String id5 = "progressBarContainer";
        final Container checkOut7 = Container.checkOut();
        checkOut7.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, checkOut7);
        }
        checkOut7.setStyle("ProgressBar");
        checkOut7.setNonBlocking(true);
        checkOut4.addBasicElement(checkOut7);
        checkOut7.onAttributesInitialized();
        final StaticLayoutData element7 = new StaticLayoutData();
        element7.onCheckOut();
        element7.setElementMap(elementMap);
        element7.setSize(new Dimension(100.0f, 14));
        element7.setAlign(Alignment17.CENTER);
        checkOut7.addBasicElement(element7);
        element7.onAttributesInitialized();
        element7.onChildrenAdded();
        checkOut7.onChildrenAdded();
        checkOut4.onChildrenAdded();
        checkOut2.onChildrenAdded();
        final String id6 = "leftDecorator";
        final Image image2 = new Image();
        image2.onCheckOut();
        image2.setElementMap(elementMap);
        if (elementMap != null && id6 != null) {
            elementMap.add(id6, image2);
        }
        image2.setStyle("xpBarDecoratorLeft");
        image2.setNonBlocking(true);
        checkOut.addBasicElement(image2);
        image2.onAttributesInitialized();
        final StaticLayoutData element8 = new StaticLayoutData();
        element8.onCheckOut();
        element8.setElementMap(elementMap);
        element8.setAlign(Alignment17.WEST);
        element8.setSize(new Dimension(-2, -2));
        image2.addBasicElement(element8);
        element8.onAttributesInitialized();
        element8.onChildrenAdded();
        image2.onChildrenAdded();
        final String id7 = "rightDecorator";
        final Image image3 = new Image();
        image3.onCheckOut();
        image3.setElementMap(elementMap);
        if (elementMap != null && id7 != null) {
            elementMap.add(id7, image3);
        }
        image3.setStyle("xpBarDecoratorRight");
        image3.setNonBlocking(true);
        checkOut.addBasicElement(image3);
        image3.onAttributesInitialized();
        final StaticLayoutData element9 = new StaticLayoutData();
        element9.onCheckOut();
        element9.setElementMap(elementMap);
        element9.setAlign(Alignment17.EAST);
        element9.setSize(new Dimension(-2, -2));
        image3.addBasicElement(element9);
        element9.onAttributesInitialized();
        element9.onChildrenAdded();
        image3.onChildrenAdded();
        final String id8 = "levelText";
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(elementMap);
        if (elementMap != null && id8 != null) {
            elementMap.add(id8, textView);
        }
        textView.setStyle("bigBordered");
        textView.setExpandable(false);
        textView.setNonBlocking(true);
        checkOut.addBasicElement(textView);
        textView.onAttributesInitialized();
        final StaticLayoutData element10 = new StaticLayoutData();
        element10.onCheckOut();
        element10.setElementMap(elementMap);
        element10.setSize(new Dimension(-2, -2));
        element10.setAlign(Alignment17.CENTER);
        element10.setYOffset(25);
        textView.addBasicElement(element10);
        element10.onAttributesInitialized();
        element10.onChildrenAdded();
        final DecoratorAppearance appearance4 = textView.getAppearance();
        appearance4.setElementMap(elementMap);
        ((TextWidgetAppearance)appearance4).setTextColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        textView.addBasicElement(appearance4);
        appearance4.onAttributesInitialized();
        final Margin checkOut8 = Margin.checkOut();
        checkOut8.setElementMap(elementMap);
        checkOut8.setInsets(new Insets(0, 0, 0, 0));
        appearance4.addBasicElement(checkOut8);
        checkOut8.onAttributesInitialized();
        checkOut8.onChildrenAdded();
        appearance4.onChildrenAdded();
        textView.onChildrenAdded();
        final String id9 = "xpGainText";
        final TextView textView2 = new TextView();
        textView2.onCheckOut();
        textView2.setElementMap(elementMap);
        if (elementMap != null && id9 != null) {
            elementMap.add(id9, textView2);
        }
        textView2.setStyle("SmallBoldBordered12");
        textView2.setMinWidth(1);
        textView2.setMaxWidth(200);
        textView2.setNonBlocking(true);
        checkOut.addBasicElement(textView2);
        textView2.onAttributesInitialized();
        final StaticLayoutData element11 = new StaticLayoutData();
        element11.onCheckOut();
        element11.setElementMap(elementMap);
        element11.setSize(new Dimension(-2, -2));
        element11.setAlign(Alignment17.CENTER);
        element11.setYOffset(-13);
        textView2.addBasicElement(element11);
        element11.onAttributesInitialized();
        element11.onChildrenAdded();
        textView2.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
