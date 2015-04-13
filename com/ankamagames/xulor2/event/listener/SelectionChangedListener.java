package com.ankamagames.xulor2.event.listener;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;

public class SelectionChangedListener extends AbstractEventListener
{
    private static Logger m_logger;
    
    @Override
    public Events getType() {
        return Events.SELECTION_CHANGED;
    }
    
    static {
        SelectionChangedListener.m_logger = Logger.getLogger((Class)SelectionChangedListener.class);
    }
}
