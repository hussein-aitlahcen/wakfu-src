package com.ankamagames.xulor2.event.listener;

import com.ankamagames.xulor2.core.event.*;

public class ItemDoubleClickListener extends AbstractEventListener
{
    @Override
    public Events getType() {
        return Events.ITEM_DOUBLE_CLICK;
    }
}
