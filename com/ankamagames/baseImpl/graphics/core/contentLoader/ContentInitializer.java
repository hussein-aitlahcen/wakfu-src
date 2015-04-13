package com.ankamagames.baseImpl.graphics.core.contentLoader;

import com.ankamagames.baseImpl.graphics.*;

public interface ContentInitializer
{
    void init(AbstractGameClientInstance p0) throws Exception;
    
    String getName();
}
