package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.appearance.spacing.*;
import java.awt.*;
import com.ankamagames.xulor2.appearance.*;

public class SlotMachineDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public SlotMachineDialog() {
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
        element.setAlign(Alignment17.CENTER);
        checkOut.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final StaticLayout element2 = new StaticLayout();
        element2.onCheckOut();
        element2.setAdaptToContentSize(true);
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        element2.onChildrenAdded();
        final String id = "closeButton";
        final Button button = new Button();
        button.onCheckOut();
        button.setElementMap(elementMap);
        if (elementMap != null && id != null) {
            elementMap.add(id, button);
        }
        button.setStyle("backgroundDisplayClose");
        final MouseClickedListener onClick = new MouseClickedListener();
        onClick.setCallBackFunc("unloadDialog");
        button.setOnClick(onClick);
        checkOut.addBasicElement(button);
        button.onAttributesInitialized();
        final StaticLayoutData element3 = new StaticLayoutData();
        element3.onCheckOut();
        element3.setElementMap(elementMap);
        element3.setAlign(Alignment17.NORTH_EAST);
        button.addBasicElement(element3);
        element3.onAttributesInitialized();
        element3.onChildrenAdded();
        final DecoratorAppearance appearance = button.getAppearance();
        appearance.setElementMap(elementMap);
        button.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final Margin checkOut2 = Margin.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setInsets(new Insets(10, 0, 0, 20));
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        appearance.onChildrenAdded();
        button.onChildrenAdded();
        checkOut.onChildrenAdded();
        return checkOut;
    }
}
