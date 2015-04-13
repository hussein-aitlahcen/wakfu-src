package com.ankamagames.baseImpl.graphics.ui;

import com.ankamagames.framework.graphics.opengl.base.render.*;
import com.ankamagames.framework.kernel.core.controllers.*;

public interface UIScene extends GLRenderable, MouseController, KeyboardController, FocusController
{
    void addEventListener(UISceneEventListener p0);
    
    void removeEventListener(UISceneEventListener p0);
}
