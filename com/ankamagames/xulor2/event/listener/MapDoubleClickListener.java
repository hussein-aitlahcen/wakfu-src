package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class MapDoubleClickListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.MAP_DOUBLE_CLICK;
    }
}
