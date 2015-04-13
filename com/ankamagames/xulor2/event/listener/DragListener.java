package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class DragListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.DRAG;
    }
}
