package com.ankamagames.xulor2.core.renderer.condition;

import com.ankamagames.xulor2.component.*;
import com.ankamagames.xulor2.core.*;
import com.ankamagames.xulor2.core.renderer.*;

public class ListCondition extends UnaryConditionOperator
{
    public static final String TAG = "ListCondition";
    public static final String EVEN_INDEX_KEY = "evenIndex";
    public static final String ODD_INDEX_KEY = "oddIndex";
    public static final String INDEX_KEY = "index";
    public static final String TABLE_INDEX_KEY = "tableIndex";
    
    @Override
    public String getTag() {
        return "ListCondition";
    }
    
    @Override
    public boolean isValid(Object object) {
        if (this.m_comparedValueInit) {
            object = this.m_comparedValue;
        }
        RenderableContainer renderable;
        if (object instanceof RenderableContainer) {
            renderable = (RenderableContainer)object;
        }
        else if (object instanceof EventDispatcher) {
            final EventDispatcher eventDispatcher = (EventDispatcher)object;
            renderable = eventDispatcher.getRenderableParent();
        }
        else {
            renderable = this.getParentOfType(RenderableContainer.class);
        }
        if (renderable == null) {
            return false;
        }
        final EditableRenderableCollection collection = renderable.getRenderableCollection();
        if (collection == null) {
            return false;
        }
        if (this.m_key != null) {
            if (this.m_key.equalsIgnoreCase("evenIndex")) {
                final int index = collection.getTableIndex(renderable);
                return this.m_condition.isValid(index % 2 == 0);
            }
            if (this.m_key.equalsIgnoreCase("oddIndex")) {
                final int index = collection.getTableIndex(renderable);
                return this.m_condition.isValid(index % 2 != 0);
            }
            if (this.m_key.equalsIgnoreCase("index")) {
                final int index = collection.getItemIndex(renderable.getItemValue());
                return this.m_condition.isValid(index);
            }
            if (this.m_key.equalsIgnoreCase("tableIndex")) {
                final int index = collection.getTableIndex(renderable);
                return this.m_condition.isValid(index);
            }
        }
        return false;
    }
}
