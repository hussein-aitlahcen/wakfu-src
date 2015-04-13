package com.ankamagames.baseImpl.graphics.alea.mobile;

import com.ankamagames.baseImpl.graphics.alea.animatedElement.*;

public interface InteractiveElementSelectionChangeListener<IE extends AnimatedInteractiveElement>
{
    void selectionChanged(IE p0, boolean p1);
}
