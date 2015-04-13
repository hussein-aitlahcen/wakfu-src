package com.ankamagames.framework.graphics.engine.fx;

import com.ankamagames.framework.graphics.engine.*;
import com.ankamagames.framework.graphics.engine.entity.*;

public abstract class Pass
{
    public abstract void render(final Renderer p0, final Entity p1);
    
    public abstract void reset();
}
