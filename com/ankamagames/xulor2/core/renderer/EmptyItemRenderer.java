package com.ankamagames.xulor2.core.renderer;

import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.property.*;
import com.ankamagames.xulor2.component.*;

public class EmptyItemRenderer extends ItemRenderer
{
    private static final EventDispatcher[] EMPTY;
    
    @Override
    public void add(final EventDispatcher element) {
        element.destroySelfFromParent();
    }
    
    @Override
    public void applyItemValue(final EventDispatcher[] elements, final Item item) {
    }
    
    @Override
    public void render(final RenderableContainer renderable) {
        renderable.setRenderableChildren(EmptyItemRenderer.EMPTY);
    }
    
    @Override
    public boolean isRenderableCompatible(final RenderableContainer renderable) {
        return true;
    }
    
    @Override
    public void addListeners(final RenderableContainer m) {
    }
    
    @Override
    public void removeListeners(final RenderableContainer r) {
    }
    
    static {
        EMPTY = new EventDispatcher[0];
    }
}
