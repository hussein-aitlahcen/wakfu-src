package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.decorator.*;
import java.awt.*;
import com.ankamagames.xulor2.appearance.*;

public class WindowConsole$Titlebar implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public WindowConsole$Titlebar() {
        super();
        this.elementMaps = new Stack<ElementMap>();
    }
    
    @Override
    public void applyStyle(final ElementMap item, final DocumentParser doc, final Widget widget) {
        this.doc = doc;
        this.elementMaps.push(item);
        final ElementMap elementMap = this.elementMaps.peek();
        final DecoratorAppearance appearance = widget.getAppearance();
        appearance.setElementMap(elementMap);
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final PlainBackground element = new PlainBackground();
        element.onCheckOut();
        element.setElementMap(elementMap);
        appearance.addBasicElement(element);
        element.onAttributesInitialized();
        final ColorElement checkOut = ColorElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        element.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        element.onChildrenAdded();
        final PlainBorder element2 = new PlainBorder();
        element2.onCheckOut();
        element2.setElementMap(elementMap);
        element2.setInsets(new Insets(0, 0, 0, 2));
        appearance.addBasicElement(element2);
        element2.onAttributesInitialized();
        final ColorElement checkOut2 = ColorElement.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        element2.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        element2.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
