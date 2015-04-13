package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;

public class ContainerFighterControlsNotAllowed implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ContainerFighterControlsNotAllowed() {
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
        element.setScaled(false);
        appearance.addBasicElement(element);
        element.onAttributesInitialized();
        final ColorElement checkOut = ColorElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
        element.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        element.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
