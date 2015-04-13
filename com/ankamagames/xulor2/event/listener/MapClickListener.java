package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class MapClickListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.MAP_CLICK;
    }
}
