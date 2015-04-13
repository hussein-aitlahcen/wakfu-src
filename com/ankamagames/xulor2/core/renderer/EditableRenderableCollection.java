package com.ankamagames.xulor2.core.renderer;

import com.ankamagames.xulor2.component.*;
import java.util.*;

public interface EditableRenderableCollection extends RenderableCollection
{
    void removeValue(Object p0);
    
    void addValue(Object p0);
    
    boolean addValue(int p0, Object p1);
    
    void addValue(Object p0, Object p1);
    
    boolean replaceValue(Object p0, Object p1);
    
    Object getValue(int p0);
    
    RenderableContainer getSelected();
    
    int getTableIndex(RenderableContainer p0);
    
    int getItemIndex(Object p0);
    
    ArrayList<RenderableContainer> getRenderables();
    
    int size();
    
    public interface CollectionOffsetListener
    {
        void onOffsetChanged(float p0);
    }
    
    public interface CollectionContentLoadedListener
    {
        void onContentLoaded();
    }
}
