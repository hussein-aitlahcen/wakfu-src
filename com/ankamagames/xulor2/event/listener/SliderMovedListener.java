package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class SliderMovedListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.SLIDER_MOVED;
    }
}
