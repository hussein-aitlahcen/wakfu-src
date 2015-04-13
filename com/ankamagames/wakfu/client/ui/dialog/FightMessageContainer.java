package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import java.awt.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.appearance.*;

public class FightMessageContainer implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public FightMessageContainer() {
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
        checkOut.setStyle("popup");
        checkOut.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.CENTER);
        element.setYOffset(200);
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final DecoratorAppearance appearance = checkOut.getAppearance();
        appearance.setElementMap(elementMap);
        checkOut.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Margin checkOut2 = Margin.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setInsets(new Insets(0, 0, 0, 0));
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        appearance.onChildrenAdded();
        final Container checkOut3 = Container.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setExpandable(false);
        checkOut.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        final String id2 = "image1";
        final Image image = new Image();
        image.onCheckOut();
        image.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, image);
        }
        image.setStyle("leftLeafDecorator");
        checkOut3.addBasicElement(image);
        image.onAttributesInitialized();
        image.onChildrenAdded();
        final String id3 = "container2";
        final Container checkOut4 = Container.checkOut();
        checkOut4.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, checkOut4);
        }
        checkOut4.setStyle("leafDecorator");
        checkOut3.addBasicElement(checkOut4);
        checkOut4.onAttributesInitialized();
        final String id4 = "text";
        final Label label = new Label();
        label.onCheckOut();
        label.setElementMap(elementMap);
        if (elementMap != null && id4 != null) {
            elementMap.add(id4, label);
        }
        label.setStyle("systemMessage");
        checkOut4.addBasicElement(label);
        label.onAttributesInitialized();
        label.onChildrenAdded();
        checkOut4.onChildrenAdded();
        final String id5 = "image2";
        final Image image2 = new Image();
        image2.onCheckOut();
        image2.setElementMap(elementMap);
        if (elementMap != null && id5 != null) {
            elementMap.add(id5, image2);
        }
        image2.setStyle("rightLeafDecorator");
        checkOut3.addBasicElement(image2);
        image2.onAttributesInitialized();
        image2.onChildrenAdded();
        checkOut3.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
