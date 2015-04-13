package com.ankamagames.framework.graphics.engine;

import com.ankamagames.framework.graphics.engine.entity.*;
import java.util.*;

public interface RenderTreeInterface
{
    void clear();
    
    void push(Entity p0, int p1);
    
    void pushAtEnd(Entity p0);
    
    void render(Renderer p0);
    
    void getAddEntities(HashSet<Entity> p0);
}
