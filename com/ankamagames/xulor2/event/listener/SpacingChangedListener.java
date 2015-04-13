package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class SpacingChangedListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.SPACING_CHANGED;
    }
}
