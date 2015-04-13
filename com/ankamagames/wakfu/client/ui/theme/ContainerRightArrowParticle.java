package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.decorator.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.xulor2.appearance.*;

public class ContainerRightArrowParticle implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ContainerRightArrowParticle() {
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
        final ParticleDecorator element = new ParticleDecorator();
        element.onCheckOut();
        element.setElementMap(elementMap);
        element.setAlignment(Alignment9.CENTER);
        element.setFile("800247.xps");
        element.setLevel(1);
        appearance.addBasicElement(element);
        element.onAttributesInitialized();
        element.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
