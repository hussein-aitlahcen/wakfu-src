package com.ankamagames.xulor2.decorator;

import com.ankamagames.xulor2.core.event.*;

public interface Trigger extends AppearanceElement
{
    void setTriggerAction(Events p0);
    
    Events getTriggerAction();
    
    void run();
}
