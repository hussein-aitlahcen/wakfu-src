package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.util.*;

public class AdminMessageContainer implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public AdminMessageContainer() {
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
        element.setYOffset(200);
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final String id2 = "container2";
        final Container checkOut2 = Container.checkOut();
        checkOut2.setElementMap(elementMap);
        if (elementMap != null && id2 != null) {
            elementMap.add(id2, checkOut2);
        }
        checkOut.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        final String id3 = "text";
        final TextView textView = new TextView();
        textView.onCheckOut();
        textView.setElementMap(elementMap);
        if (elementMap != null && id3 != null) {
            elementMap.add(id3, textView);
        }
        textView.setStyle("adminMessage");
        textView.setMaxWidth(800);
        textView.setPrefSize(new Dimension(0, 600));
        textView.setNonBlocking(true);
        checkOut2.addBasicElement(textView);
        textView.onAttributesInitialized();
        textView.onChildrenAdded();
        checkOut2.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
