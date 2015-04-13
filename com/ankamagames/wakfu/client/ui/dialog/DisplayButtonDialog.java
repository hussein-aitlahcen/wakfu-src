package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.property.*;

public class DisplayButtonDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public DisplayButtonDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final Button button = new Button();
        button.onCheckOut();
        button.setElementMap(elementMap);
        button.setNonBlocking(false);
        final MouseClickedListener onClick = new MouseClickedListener();
        onClick.setCallBackFunc("wakfu.displayButton:nextStep()");
        button.setOnClick(onClick);
        button.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.SOUTH_EAST);
        element.setSize(new Dimension(-2, -2));
        element.setXOffset(-10);
        element.setYOffset(10);
        button.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final PropertyElement checkOut = PropertyElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setAttribute("text");
        checkOut.setName("displayButton");
        checkOut.setField("text");
        button.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        button.onChildrenAdded();
        return button;
    }
}
