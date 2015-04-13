package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.util.alignment.*;
import com.ankamagames.framework.graphics.engine.geometry.*;
import com.ankamagames.xulor2.appearance.*;

public class ImageVerticalSeparator2 implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public ImageVerticalSeparator2() {
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
        final DecoratorAppearance appearance2 = appearance.getParentOfType(Widget.class).getAppearance();
        appearance2.setElementMap(elementMap);
        ((ImageAppearance)appearance2).setScaled(true);
        appearance2.onAttributesInitialized();
        final PixmapElement checkOut = PixmapElement.checkOut();
        checkOut.setElementMap(elementMap);
        checkOut.setHeight(6);
        checkOut.setPosition(Alignment17.CENTER);
        checkOut.setRotation(GeometrySprite.SpriteRotation.QUARTER_CLOCKWISE);
        checkOut.setTexture(this.doc.getTexture("default_1.tga"));
        checkOut.setWidth(13);
        checkOut.setX(360);
        checkOut.setY(331);
        appearance2.addBasicElement(checkOut);
        checkOut.onAttributesInitialized();
        checkOut.onChildrenAdded();
        appearance2.onChildrenAdded();
        appearance.onChildrenAdded();
    }
}
