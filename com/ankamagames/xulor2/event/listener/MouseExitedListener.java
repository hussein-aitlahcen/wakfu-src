package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class MouseExitedListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.MOUSE_EXITED;
    }
}
