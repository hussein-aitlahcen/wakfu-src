package com.ankamagames.xulor2.core.event;

import org.apache.log4j.*;
import com.ankamagames.xulor2.core.*;
import java.util.*;

public abstract class AbstractEventListener extends CallBack implements EventListener
{
    private static final Logger m_logger;
    private Event m_event;
    
    public abstract Events getType();
    
    public Object invokeCallBack(final Event event) {
        this.m_event = event;
        final EventDispatcher element = event.getCurrentTarget();
        if (element != null) {
            super.setElementMap(element.getElementMap());
        }
        return super.invokeCallBack();
    }
    
    @Override
    protected void fillParameters(final String[] parameters, final List<Class<?>> parameterTypes, final List<Object> args) {
        parameterTypes.add(this.m_event.getClass());
        args.add(this.m_event);
        super.fillParameters(parameters, parameterTypes, args);
    }
    
    @Override
    public boolean run(final Event event) {
        final Object obj = this.invokeCallBack(event);
        this.m_event = null;
        return obj instanceof Boolean && (boolean)obj;
    }
    
    static {
        m_logger = Logger.getLogger((Class)AbstractEventListener.class);
    }
}
