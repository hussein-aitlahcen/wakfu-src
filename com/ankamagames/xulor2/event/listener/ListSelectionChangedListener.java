package com.ankamagames.xulor2.event.listener;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.event.*;

public class ListSelectionChangedListener extends AbstractEventListener
{
    private static Logger m_logger;
    
    @Override
    public Events getType() {
        return Events.LIST_SELECTION_CHANGED;
    }
    
    static {
        ListSelectionChangedListener.m_logger = Logger.getLogger((Class)ListSelectionChangedListener.class);
    }
}
