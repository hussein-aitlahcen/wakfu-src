package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class DropListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.DROP;
    }
}
