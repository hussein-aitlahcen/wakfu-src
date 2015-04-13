package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import java.awt.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.framework.graphics.image.*;
import com.ankamagames.xulor2.appearance.*;

public class ProgressBarTimePoint implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ProgressBarTimePoint() {
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
        ((ProgressBarAppearance)appearance).setInnerBorder(new Insets(2, 0, 0, 0));
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        final ColorElement checkOut = ColorElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setColor(new Color(0.0f, 0.0f, 0.0f, 1.0f));
        checkOut.setName("progressBarBorder");
        appearance.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        final ColorElement checkOut2 = ColorElement.checkOut();
        checkOut2.setElementMap(elementMap);
        checkOut2.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        checkOut2.setName("progressBar");
        appearance.addBasicElement(checkOut2);
        checkOut2.onAttributesInitialized();
        checkOut2.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
