package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class MouseDraggedListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.MOUSE_DRAGGED;
    }
}
