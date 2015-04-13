package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class ActivationChangedListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.ACTIVATION_CHANGED;
    }
}
