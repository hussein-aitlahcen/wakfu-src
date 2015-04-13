package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class KeyTypedListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.KEY_TYPED;
    }
}
