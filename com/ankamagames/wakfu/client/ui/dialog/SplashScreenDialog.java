package com.ankamagames.wakfu.client.ui.dialog;

import java.util.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.layout.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.event.listener.*;
import com.ankamagames.xulor2.appearance.*;
import com.ankamagames.xulor2.property.*;

public class SplashScreenDialog implements BasicElementFactory
{
    private Stack<ElementMap> elementMaps;
    private Environment env;
    
    public SplashScreenDialog() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public BasicElement getElement(final Environment env, final ElementMap item) {
        this.env = env;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final Window window = new Window();
        window.onCheckOut();
        window.setElementMap(elementMap);
        window.onAttributesInitialized();
        final StaticLayoutData element = new StaticLayoutData();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlign(Alignment17.CENTER);
        element.setSize(new Dimension(100.0f, 100.0f));
        window.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        final Container checkOut = Container.checkOut();
        checkOut.setElementMap(elementMap);
        window.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        final Image element2 = new Image();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        final MouseClickedListener onClick = new MouseClickedListener();
        onClick.setCallBackFunc("unloadDialog");
        element2.setOnClick(onClick);
        checkOut.addBasicElement(element2);
        element2.onAttributesInitialized();
        final PixmapElement checkOut2 = PixmapElement.checkOut();
        checkOut2.setElementMap(elementMap);
        element2.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        final PropertyElement checkOut3 = PropertyElement.checkOut();
        checkOut3.setElementMap(elementMap);
        checkOut3.setAttribute("texture");
        checkOut3.setName("splashScreenIconUrl");
        checkOut2.addBasicElement(checkOut3);
        checkOut3.onAttributesInitialized();
        checkOut3.onChildrenAdded();
        checkOut2.onChildrenAdded();
        element2.onChildrenAdded();
        checkOut.onChildrenAdded();
        window.onChildrenAdded();
        return window;
    }
}
