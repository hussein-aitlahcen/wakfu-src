package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.decorator.*;
import java.awt.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;

public class WindowConsole implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public WindowConsole() {
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
        final PlainBorder element = new PlainBorder();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setInsets(new Insets(1, 1, 1, 1));
        appearance.addBasicElement(element);
        element.onAttributesInitialized();
        final ColorElement checkOut = ColorElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        element.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        element.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
