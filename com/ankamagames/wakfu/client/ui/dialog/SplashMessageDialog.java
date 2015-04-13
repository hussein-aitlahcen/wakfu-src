package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.property.*;

public class SplashMessageDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public SplashMessageDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final String id = "mainContainer";
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, checkOut);
        }
        checkOut.setStyle("genericSplash");
        checkOut.setNonBlocking(true);
        checkOut.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.CENTER);
        element.setYOffset(160);
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final RowLayout checkOut2 = RowLayout.checkOut();
        checkOut2.setHorizontal(false);
        checkOut2.setAlign(Alignment9.SOUTH);
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        final String id2 = "text";
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, textView);
        }
        textView.setStyle("BigBordered");
        textView.setNonBlocking(true);
        textView.setMinWidth(1);
        textView.setMaxWidth(350);
        checkOut.addBasicElement(textView);
        textView.onAttributesInitialized();
        final PropertyElement checkOut3 = PropertyElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setAttribute("text");
        checkOut3.setName("splashText");
        textView.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        textView.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
