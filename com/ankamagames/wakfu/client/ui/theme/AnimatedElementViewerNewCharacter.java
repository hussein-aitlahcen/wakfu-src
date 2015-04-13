package com.ankamagames.wakfu.client.ui.theme;

import java.util.*;
import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.appearance.*;

public class AnimatedElementViewerNewCharacter implements StyleSetter
{
    private DocumentParser doc;
    private Stack<ElementMap> elementMaps;
    
    public AnimatedElementViewerNewCharacter() {
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
        ((AnimatedElementViewerAppearance)appearance).setAnimName("AnimStatique");
        ((AnimatedElementViewerAppearance)appearance).setDirection(3);
        ((AnimatedElementViewerAppearance)appearance).setFilePath("newCharacterAnim.anm");
        widget.addBasicElement(appearance);
        appearance.onAttributesInitialized();
        appearance.onChildrenAdded();
    }
}
