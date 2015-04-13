package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.appearance.*;

public class AnimatedElementViewerLoginAnimLeft implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public AnimatedElementViewerLoginAnimLeft() {
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
        ((AnimatedElementViewerAppearance)appearance).setAnimName("AnimLoginLeft");
        ((AnimatedElementViewerAppearance)appearance).setDirection(0);
        ((AnimatedElementViewerAppearance)appearance).setFilePath("loginAnim.anm");
        ((AnimatedElementViewerAppearance)appearance).setFlipVertical(true);
        ((AnimatedElementViewerAppearance)appearance).setOffsetX(0.0f);
        ((AnimatedElementViewerAppearance)appearance).setOffsetY(0.0f);
        ((AnimatedElementViewerAppearance)appearance).setScale(0.75f);
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        appearance.onChildrenAdded();
    }
}
